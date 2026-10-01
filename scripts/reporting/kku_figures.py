"""Native vector workflow and coverage plot from measured KKU-only counters."""
from reportlab.graphics.shapes import Drawing, Rect, String, Line, Polygon
from reportlab.graphics.charts.barcharts import VerticalBarChart
from reportlab.lib import colors

def workflow():
    d=Drawing(507,270)
    labels=[('Original prompt and fixed API','No evaluation feedback sent to providers'),
            ('KKU Claude or Gemini','Explicit agent selection, original response preserved'),
            ('Versioned local processing','Complete prefixes, source-order cap, fixed-only pruning'),
            ('Fixed twice, buggy, fixed coverage','Assertion failure separated from harness errors'),
            ('Evidence audit and linked manifest','Hashes, archives, logs, counters and actual model labels')]
    for i,(title,body) in enumerate(labels):
        y=223-i*51
        d.add(Rect(12,y,483,41,fillColor=colors.HexColor('#EDF3F8'),strokeColor=colors.HexColor('#426784')))
        d.add(String(253.5,y+25,title,textAnchor='middle',fontName='Helvetica-Bold',fontSize=10))
        d.add(String(253.5,y+10,body,textAnchor='middle',fontName='Helvetica',fontSize=8))
        if i<4:
            d.add(Line(253.5,y,253.5,y-9,strokeColor=colors.HexColor('#426784')))
            d.add(Polygon([250,y-6,257,y-6,253.5,y-10],fillColor=colors.HexColor('#426784'),strokeColor=None))
    return d

def coverage(methods):
    names={'cmaes':'CMA-ES','fscs-art':'FSCS-ART','kku-claude':'KKU Claude','kku-gemini':'KKU Gemini'}
    d=Drawing(507,290)
    d.add(String(253.5,277,'Coverage of completed subsets',textAnchor='middle',fontName='Helvetica-Bold',fontSize=12))
    d.add(String(253.5,263,'Different denominators; descriptive comparison only',textAnchor='middle',fontName='Helvetica',fontSize=9))
    chart=VerticalBarChart(); chart.x=38;chart.y=46;chart.width=448;chart.height=185
    chart.data=[[100*m['line_coverage_macro'] for m in methods],[100*m['branch_coverage_macro'] for m in methods]]
    chart.categoryAxis.categoryNames=[names[m['approach']]+'\n(n='+str(m['completed_runs'])+')' for m in methods]
    chart.categoryAxis.labels.fontName='Helvetica';chart.categoryAxis.labels.fontSize=9
    chart.valueAxis.valueMin=0;chart.valueAxis.valueMax=100;chart.valueAxis.valueStep=25
    chart.valueAxis.labels.fontName='Helvetica';chart.valueAxis.labels.fontSize=8
    chart.bars[0].fillColor=colors.HexColor('#245B82');chart.bars[1].fillColor=colors.HexColor('#709A9D')
    chart.barLabelFormat='%.1f';chart.barLabels.fontName='Helvetica';chart.barLabels.fontSize=8
    chart.barLabels.nudge=5
    chart.groupSpacing=18;chart.barSpacing=3;d.add(chart)
    for x,title,color in [(115,'Line macro (%)','#245B82'),(285,'Condition macro (%)','#709A9D')]:
        d.add(Rect(x,242,10,10,fillColor=colors.HexColor(color),strokeColor=None))
        d.add(String(x+15,243,title,fontName='Helvetica',fontSize=9))
    return d
