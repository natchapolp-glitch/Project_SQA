#!/usr/bin/env python3
"""Render the evidence report as PDF using the bundled ReportLab runtime."""
from __future__ import annotations
import argparse
import html
import json
from pathlib import Path
import re

from reportlab.lib import colors
from reportlab.lib.enums import TA_LEFT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, Image, XPreformatted
import textwrap
from build_figures import flow_drawing

ROOT = Path(__file__).resolve().parents[2]


def clean(text):
    return html.escape(re.sub(r'[`*]', '', text))


def build(source, output, font, bold):
    pdfmetrics.registerFont(TTFont('Thai', str(font)))
    pdfmetrics.registerFont(TTFont('ThaiBold', str(bold)))
    pdfmetrics.registerFontFamily('Thai', normal='Thai', bold='ThaiBold', italic='Thai', boldItalic='ThaiBold')
    body = ParagraphStyle('Body', fontName='Thai', fontSize=10.5, leading=17,
                          spaceAfter=7, wordWrap='CJK', textColor=colors.HexColor('#20252B'))
    heading = ParagraphStyle('Heading', parent=body, fontName='ThaiBold', fontSize=15,
                             leading=22, spaceBefore=13, spaceAfter=8, keepWithNext=True)
    title = ParagraphStyle('Title', parent=heading, fontSize=21, leading=31, spaceAfter=16)
    small = ParagraphStyle('Small', parent=body, fontSize=8, leading=12, spaceAfter=2)
    code_style = ParagraphStyle('Code', parent=small, fontName='Courier', fontSize=7.2, leading=10,
                                backColor=colors.HexColor('#F3F5F7'), borderPadding=6, spaceAfter=8)
    story, paragraph, table = [], [], []
    code, in_code = [], False
    next_paragraph_keeps_table = False
    table_intro = ParagraphStyle('TableIntro', parent=body, keepWithNext=True)

    def flush_paragraph():
        nonlocal next_paragraph_keeps_table
        if paragraph:
            story.append(Paragraph(clean(' '.join(paragraph)), table_intro if next_paragraph_keeps_table else body))
            next_paragraph_keeps_table = False
            paragraph.clear()

    def flush_table():
        if not table:
            return
        rows = [[Paragraph(clean(cell), small) for cell in row] for row in table]
        width = A4[0] - 88
        columns = len(rows[0])
        view = Table(rows, colWidths=[width / columns] * columns, repeatRows=1, hAlign='LEFT')
        view.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, 0), colors.HexColor('#E7EDF3')),
            ('ROWBACKGROUNDS', (0, 1), (-1, -1), [colors.white, colors.HexColor('#F7F9FC')]),
            ('GRID', (0, 0), (-1, -1), 0.4, colors.HexColor('#D9D9D9')),
            ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
            ('LEFTPADDING', (0, 0), (-1, -1), 6), ('RIGHTPADDING', (0, 0), (-1, -1), 6),
            ('TOPPADDING', (0, 0), (-1, -1), 5), ('BOTTOMPADDING', (0, 0), (-1, -1), 5),
        ]))
        story.extend([view, Spacer(1, 10)])
        table.clear()

    for line in source.read_text(encoding='utf-8').splitlines():
        if line.startswith('```'):
            flush_paragraph()
            flush_table()
            if in_code:
                wrapped = [part for raw in code for part in (textwrap.wrap(raw, width=107,
                    replace_whitespace=False, drop_whitespace=False) if raw else [''])]
                story.append(XPreformatted(html.escape('\n'.join(wrapped)), code_style))
                code.clear()
            in_code = not in_code
            continue
        if in_code:
            code.append(line)
            continue
        picture = re.fullmatch(r'!\[.*\]\(([^)]+)\)', line)
        if picture:
            flush_paragraph()
            flush_table()
            path = source.parent / picture.group(1)
            if path.name == 'experiment-flow.svg':
                story.append(flow_drawing())
            else:
                item = Image(str(path))
                scale = min((A4[0] - 88) / item.imageWidth, 660 / item.imageHeight)
                item.drawWidth, item.drawHeight = item.imageWidth * scale, item.imageHeight * scale
                story.append(item)
            story.append(Spacer(1, 10))
            continue
        if line.startswith('|'):
            flush_paragraph()
            cells = [cell.strip() for cell in line.strip('|').split('|')]
            if not all(re.fullmatch(r'[:\- ]+', cell) for cell in cells):
                table.append(cells)
            continue
        flush_table()
        if line.startswith('#'):
            flush_paragraph()
            if line.startswith(('## ภาคผนวก', '## การซ่อมและต้นทุน', '## อุปสรรคและบทเรียน', '## แหล่งข้อมูลและการทำซ้ำ')):
                story.append(PageBreak())
            next_paragraph_keeps_table = line.startswith('## เปรียบเทียบสี่วิธี')
            level = len(line) - len(line.lstrip('#'))
            story.append(Paragraph(clean(line.lstrip('# ')), title if level == 1 else heading))
        elif not line.strip():
            flush_paragraph()
        elif line.startswith('- '):
            flush_paragraph()
            story.append(Paragraph(clean(line), body))
        else:
            paragraph.append(line)
    flush_paragraph()
    flush_table()
    story.append(PageBreak())
    story.append(Paragraph('วิธีทดลองและข้อจำกัดของตัวสร้างชุดทดสอบ', heading))
    for text in [
        'ใช้ Defects4J 3.0.1 และ Java 11 ใน timezone America/Los_Angeles เลือก active bug ที่มีหมายเลขน้อยที่สุดของแต่ละ Java project รวม 17 projects ข้อสรุปของผลทดลองจำกัดอยู่ในหนึ่ง bug ต่อโปรเจกต์ตาม sample ที่ระบุ',
        'FSCS-ART กระจายเวกเตอร์ด้วยระยะห่างจากข้อมูลก่อนหน้า ส่วน CMA-ES ใช้ความถี่ของผลสังเกตบน fixed revision เป็น objective เพื่อเพิ่มความหลากหลายของพฤติกรรม โดยไม่ได้ใช้ coverage เป็น fitness หรือใช้ buggy outcome เลือก assertion',
        'ตัวสร้างใช้ declaration signatures ที่มีทั้ง fixed และ buggy รวมถึงสมาชิก non-public เมื่อ reflection เข้าถึงได้ สร้างอินพุตจาก scalar, enum, array, collection interface และ constructor fixture แบบจำกัด ทดสอบ fixed behavior สองครั้งก่อนสร้าง JUnit และเก็บกรณีที่ตรวจไม่พบ fault ไว้ output ของ object ที่ไม่ใช่ scalar ยืนยันได้เพียง runtime type ส่วน serialized output ที่ยาวเกิน 16000 ตัวอักษรใช้ SHA-256 และ byte length จึงยังไม่ครอบคลุม object graph หรือ stateful call sequences',
        'ค่าความครอบคลุมมาจากคลาสที่แก้ไขเพื่อซ่อม bug ไม่ใช่ทุกคลาสในโปรเจกต์ จำนวน input ที่ให้ผลต่างหลายรายการยังอาจเป็นการตรวจพบ bug เดียวกัน และต้องนับ bug เดียวใน fault detection rate',
        'เวลาของ record.duration_seconds เป็นเวลาประเมินชุดทดสอบ ส่วน generation_seconds เป็นเวลาสร้าง และ total_seconds รวมสองส่วน ต้องแยกต้นทุน AI token และ prompt iterations ออกจากจำนวน program evaluations',
        'คำสั่งและขั้นตอนทำซ้ำอยู่ใน README.md, docs/study-protocol.md และ presentation/demo-guide.md ส่วนต้นทางที่ใช้สร้างรายงานคือ output/submission/summary.json และ record.json ของแต่ละรอบ',
    ]:
        story.append(Paragraph(clean(text), body))

    def footer(canvas, doc):
        canvas.setFont('Thai', 8)
        canvas.setFillColor(colors.HexColor('#606C78'))
        canvas.drawString(44, 25, 'CP353201 Software Quality Assurance')
        canvas.drawRightString(A4[0] - 44, 25, str(doc.page))

    output.parent.mkdir(parents=True, exist_ok=True)
    SimpleDocTemplate(str(output), pagesize=A4, rightMargin=44, leftMargin=44,
                      topMargin=42, bottomMargin=43,
                      title='รายงานผลการทดลอง SQA รอบที่ 2', author='SQA Project Team').build(
                          story, onFirstPage=footer, onLaterPages=footer)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--source', type=Path, default=ROOT / 'output/submission/report.md')
    parser.add_argument('--output', type=Path, default=ROOT / 'output/submission/SQA_Round2_Report.pdf')
    parser.add_argument('--font', type=Path, default=Path('C:/Windows/Fonts/tahoma.ttf'))
    parser.add_argument('--bold-font', type=Path, default=Path('C:/Windows/Fonts/tahomabd.ttf'))
    args = parser.parse_args()
    build(args.source, args.output, args.font, args.bold_font)
    print(args.output)


if __name__ == '__main__':
    main()
