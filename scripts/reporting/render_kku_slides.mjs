import fs from 'node:fs/promises';
import path from 'node:path';
import {fileURLToPath,pathToFileURL} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'../..');
const runtime=process.env.SQA_RUNTIME_ROOT;
if(!runtime)throw new Error('Set SQA_RUNTIME_ROOT to bundled dependencies');
const {PresentationFile,FileBlob}=await import(pathToFileURL(path.join(runtime,'node/node_modules/@oai/artifact-tool/dist/artifact_tool.mjs')).href);
const source=path.join(root,'output/kku-only-20261001/SQA_Round2_KKU_Only_v2.pptx');
const p=await PresentationFile.importPptx(await FileBlob.load(source));
const out=path.join(root,'tmp/kku-presentation/final-previews');await fs.mkdir(out,{recursive:true});
for(let i=0;i<16;i++){
  const preview=await p.export({slide:p.slides.getItem(i),format:'png',scale:1});
  await fs.writeFile(path.join(out,`slide-${i+1}.png`),new Uint8Array(await preview.arrayBuffer()));
}
console.log(JSON.stringify({source,slides:16,renderer:'Artifact Tool imported final PPTX; native PowerPoint not opened'}));
