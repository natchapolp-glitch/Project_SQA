from pathlib import Path
import subprocess
from pypdf import PdfReader
from PIL import Image,ImageOps,ImageDraw
root=Path(__file__).resolve().parents[2]
pdf=root/'output/kku-only-20261001/SQA_Round2_KKU_Only_20261002_COMPLETE.pdf'
out=root/'tmp/kku-pdf-preview-20261002_COMPLETE';out.mkdir(parents=True,exist_ok=True)
doc=PdfReader(pdf).pages
subprocess.run(['pdftoppm','-r','115','-png',str(pdf),str(out/'page')],check=True)
for kind,directory,paths in [('pdf',out,sorted(out.glob('page-*.png'))),('slides',root/'tmp/kku-presentation/final-previews-20261002_COMPLETE',[root/f'tmp/kku-presentation/final-previews-20261002_COMPLETE/slide-{i}.png' for i in range(1,17)])]:
    if not all(p.exists() for p in paths):continue
    for i in range(0,len(paths),2):
        images=[Image.open(p).convert('RGB') for p in paths[i:i+2]]
        width=sum(im.width for im in images);height=max(im.height for im in images)+32
        contact=Image.new('RGB',(width,height),'#ddd');draw=ImageDraw.Draw(contact);x=0
        for im,path in zip(images,paths[i:i+2]):
            draw.text((x+8,8),path.name,fill='black');contact.paste(im,(x,32));x+=im.width
        contact.save(directory/f'review-{kind}-{i//2+1:02}.png')
print({'pdf_pages':len(doc)})
