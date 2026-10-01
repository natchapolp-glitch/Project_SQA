#!/usr/bin/env python3
"""Render saved PDF pages and create private contact sheets for human review."""
import argparse
import json
from pathlib import Path
import pypdfium2 as pdfium
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[2]


def contacts(images, output, prefix):
    pages = []
    for start in range(0,len(images),6):
        sheet = Image.new('RGB',(1110,1080),'#DDE3E9')
        draw = ImageDraw.Draw(sheet)
        for index,path in enumerate(images[start:start+6]):
            item = Image.open(path).convert('RGB')
            item.thumbnail((350,490))
            x=(index%3)*370+10; y=(index//3)*540+32
            draw.text((x,y-22),f'{prefix} {start+index+1}',fill='black')
            sheet.paste(item,(x,y))
        target=output/f'{prefix}-contact-{start//6+1}.png'
        sheet.save(target); pages.append(str(target))
    return pages


def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output',type=Path,required=True)
    parser.add_argument('--presentation-previews',type=Path)
    args=parser.parse_args()
    args.output.mkdir(parents=True,exist_ok=True)
    pdf=pdfium.PdfDocument(ROOT/'output/submission/SQA_Round2_Report.pdf')
    images=[]
    for index in range(len(pdf)):
        target=args.output/f'page-{index+1:02}.png'
        page=pdf[index]; bitmap=page.render(scale=1.6)
        bitmap.to_pil().save(target)
        bitmap.close(); page.close(); images.append(target)
    pdf.close()
    result={'pdf_pages':len(images),'pdf_contacts':contacts(images,args.output,'pdf')}
    if args.presentation_previews:
        slides=sorted(args.presentation_previews.glob('slide-*.png'),key=lambda p:int(p.stem.split('-')[1]))
        result.update(slides=len(slides),slide_contacts=contacts(slides,args.output,'slide'))
    (args.output/'rendered.json').write_text(json.dumps(result,indent=2),encoding='utf-8')
    print(json.dumps(result))


if __name__ == '__main__':
    main()
