# Run with Windows Administrator rights. Never restarts the user's machine.
[CmdletBinding()]
param([string]$EvidenceDirectory)
$ErrorActionPreference = 'Stop'
if (-not $EvidenceDirectory) {
    $beamScriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path
    $EvidenceDirectory = Join-Path $beamScriptDirectory '../../output/api854-beam/setup'
}
$EvidenceDirectory = [IO.Path]::GetFullPath($EvidenceDirectory)
New-Item -ItemType Directory -Force -Path $EvidenceDirectory | Out-Null
Start-Transcript -Path (Join-Path $EvidenceDirectory ('wsl-install-' + (Get-Date -Format 'yyyyMMdd-HHmmss') + '.txt'))
try {
    $principal = [Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()
    if (-not $principal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
        throw 'Run this script as Administrator. Windows optional features require OS elevation.'
    }
    $needsRestart = $false
    foreach ($feature in @('Microsoft-Windows-Subsystem-Linux', 'VirtualMachinePlatform')) {
        $current = Get-WindowsOptionalFeature -Online -FeatureName $feature
        if ($current.State -in @('EnablePending', 'DisablePending')) {
            $needsRestart = $true
            continue
        }
        if ($current.State -ne 'Enabled') {
            $enabled = Enable-WindowsOptionalFeature -Online -FeatureName $feature -All -NoRestart
            $needsRestart = $needsRestart -or $enabled.RestartNeeded
        }
    }
    if ($needsRestart) {
        Write-Output 'RESTART_REQUIRED: Save your work and restart Windows, then rerun this script.'
        exit 3010
    }
    & wsl.exe --install -d Ubuntu-24.04 --no-launch
    if ($LASTEXITCODE -ne 0) { throw "WSL install failed: exit $LASTEXITCODE" }
    & wsl.exe --list --verbose
    Write-Output 'Initialize Ubuntu by launching wsl -d Ubuntu-24.04 and choosing a Linux user/password.'
} catch {
    Write-Output ('INSTALL_BLOCKED: ' + $_.Exception.Message)
    throw
} finally {
    Stop-Transcript
}
