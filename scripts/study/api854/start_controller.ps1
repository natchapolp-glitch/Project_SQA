param([string]$Python = 'python', [int]$Port = 8765)
$ErrorActionPreference = 'Stop'
$repo = (Resolve-Path (Join-Path $PSScriptRoot '../../..')).Path
$runtime = Join-Path $repo '.local/api854'
$binary = Join-Path $runtime 'bin/cloudflared.exe'
if (!(Test-Path $binary)) { throw 'Install official cloudflared-windows-amd64.exe in .local/api854/bin first.' }
if (Get-NetTCPConnection -State Listen -LocalPort $Port -ErrorAction SilentlyContinue) { throw 'Controller port already occupied; do not start a second controller.' }
$controller = Start-Process -FilePath $Python -ArgumentList @('scripts/study/api854/queue_server.py','--root',('"'+$runtime+'"'),'--port',$Port) -WorkingDirectory $repo -WindowStyle Hidden -RedirectStandardOutput "$runtime/controller.out.log" -RedirectStandardError "$runtime/controller.err.log" -PassThru
$tunnel = Start-Process -FilePath $binary -ArgumentList @('tunnel','--url',"http://127.0.0.1:$Port",'--no-autoupdate') -WorkingDirectory $repo -WindowStyle Hidden -RedirectStandardOutput "$runtime/tunnel.out.log" -RedirectStandardError "$runtime/tunnel.err.log" -PassThru
@{controller_pid=$controller.Id;tunnel_pid=$tunnel.Id} | ConvertTo-Json | Set-Content "$runtime/processes.json"
$url = ''
for($i=0;$i -lt 30;$i++) {
    if ($tunnel.HasExited -or $controller.HasExited) { throw 'Background process stopped; inspect private logs.' }
    if (Test-Path "$runtime/tunnel.err.log") {
        $url=[regex]::Match((Get-Content "$runtime/tunnel.err.log" -Raw),'https://[a-z0-9-]+\.trycloudflare\.com').Value
    }
    if($url){break}
    Start-Sleep -Seconds 1
}
if(!$url){throw 'Tunnel URL not ready; inspect private logs. Processes may still be running.'}
foreach($role in @('champ','beam','aom')) {
    $file = "$runtime/$role-access.private.json"
    $access = Get-Content $file -Raw | ConvertFrom-Json
    $access | Add-Member base_url $url -Force
    $access | ConvertTo-Json | Set-Content $file
}
@{base_url=$url;port=$Port;created_at_utc=[DateTime]::UtcNow.ToString('o')} | ConvertTo-Json | Set-Content "$runtime/connection.json"
Write-Output "Queue URL: $url"
Write-Output 'Private access files updated. Verify queue_client.py check before sharing.'
