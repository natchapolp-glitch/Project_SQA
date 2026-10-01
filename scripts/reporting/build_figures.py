#!/usr/bin/env python3
"""Generate native vector protocol and scientific plots from recorded evidence."""
import argparse
import json
from pathlib import Path

from reportlab.graphics.shapes import Drawing, Rect, String, Line, Polygon
from reportlab.graphics import renderSVG
from reportlab.lib import colors

ROOT = Path(__file__).resolve().parents[2]


def flow_drawing():
    drawing = Drawing(507, 290)
    labels = [
        ('API eligibility and oracle', 'Shared declarations; fixed source and fixed behavior'),
        ('Generate JUnit source suites', 'CMA-ES / FSCS-ART / actual Claude / actual IntelliSphere'),
        ('Evaluate each supplied test suite', 'Fixed twice -> buggy -> fixed Cobertura coverage'),
        ('Preserve and aggregate evidence', 'Source archives, hashes, command logs, record.json, CSV'),
    ]
    for index, (title, body) in enumerate(labels):
        y = 230 - index * 66
        drawing.add(Rect(12, y, 483, 49, rx=5, ry=5,
                         fillColor=colors.HexColor('#EDF3F8'), strokeColor=colors.HexColor('#426784')))
        drawing.add(String(253.5, y + 30, title, textAnchor='middle',
                           fontName='Helvetica-Bold', fontSize=11, fillColor=colors.HexColor('#16334A')))
        drawing.add(String(253.5, y + 12, body, textAnchor='middle',
                           fontName='Helvetica', fontSize=9, fillColor=colors.HexColor('#243746')))
        if index < 3:
            drawing.add(Line(253.5, y - 2, 253.5, y - 13, strokeColor=colors.HexColor('#426784')))
            drawing.add(Polygon([250, y - 9, 257, y - 9, 253.5, y - 15],
                                fillColor=colors.HexColor('#426784'), strokeColor=None))
    drawing.add(String(253.5, 10, 'Failures, timeouts and missing results remain explicit.',
                       textAnchor='middle', fontName='Helvetica-Oblique', fontSize=9))
    return drawing


def coverage_figure(data, output, methods, cohort):
    import matplotlib
    matplotlib.use('Agg')
    import matplotlib.pyplot as plt
    import numpy as np
    plans = data.get('planned_runs') or [*data['runs'], *data['missing_runs']]
    projects = sorted({row['project'] for row in plans})
    palette = {'cmaes': '#255D82', 'fscs-art': '#789E9F', 'claude': '#BC793C', 'intellisphere': '#775D9A'}
    titles = {'cmaes': 'CMA-ES', 'fscs-art': 'FSCS-ART', 'claude': 'Claude', 'intellisphere': 'IntelliSphere'}
    stats = {(row['project'], row['generator']): row for row in data['project_method_summary']}
    if len({row['budget'] for row in data['project_method_summary']}) > 1:
        raise ValueError('Select one declared budget before plotting project means')
    fig, axes = plt.subplots(1, 2, figsize=(10.8, max(6, len(projects) * .47 + 2.1)), sharey=True)
    height = .72 / len(methods)
    y = np.arange(len(projects))
    for ax, metric, title in zip(axes, ['line', 'branch'], ['Line coverage', 'Condition coverage (Cobertura)']):
        for index, method in enumerate(methods):
            positions = y + (index - (len(methods) - 1) / 2) * height
            for project_index, project in enumerate(projects):
                row = stats.get((project, method), {})
                value = row.get(metric + '_coverage_macro')
                n = row.get(metric + '_coverage_observations', 0)
                if value is None:
                    ax.text(1, positions[project_index], 'n/a', va='center', fontsize=10, color=palette[method])
                else:
                    ax.barh(positions[project_index], 100 * value, height=height * .88,
                            color=palette[method], label=titles[method] if project_index == 0 else None)
                    ax.text(100 * value + 1, positions[project_index], f'n={n}',
                            va='center', fontsize=10, color='#263746')
        ax.set_title(title, fontsize=14, fontweight='bold')
        ax.set_xlim(0, 110)
        ax.set_xticks([0, 25, 50, 75, 100])
        ax.set_xlabel('Covered / total (%)', fontsize=12)
        ax.tick_params(labelsize=12)
        ax.grid(axis='x', alpha=.18)
        ax.set_axisbelow(True)
        ax.spines[['top', 'right']].set_visible(False)
    axes[0].set_yticks(y, projects)
    axes[0].invert_yaxis()
    from matplotlib.patches import Patch
    fig.legend(handles=[Patch(color=palette[method], label=titles[method]) for method in methods],
               loc='lower center', ncol=len(methods), bbox_to_anchor=(.5, .035), frameon=False, fontsize=12)
    fig.suptitle(f'{cohort}: coverage on modified classes for selected bugs', fontsize=16, fontweight='bold')
    fig.text(.5, .015, 'Completed runs only; n = measurements per bar. n/a = no measurement. Not whole-project union coverage.',
             ha='center', fontsize=10)
    fig.tight_layout(rect=(0, .08, 1, .96))
    fig.savefig(output, dpi=180, facecolor='white')
    plt.close(fig)


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument('--summary', type=Path, default=ROOT / 'output/submission/summary.json')
    cli.add_argument('--output', type=Path, default=ROOT / 'output/submission')
    args = cli.parse_args()
    args.output.mkdir(parents=True, exist_ok=True)
    renderSVG.drawToFile(flow_drawing(), str(args.output / 'experiment-flow.svg'))
    data = json.loads(args.summary.read_text(encoding='utf-8'))
    coverage_figure(data, args.output / 'coverage-by-project.png', ['cmaes','fscs-art'], 'Algorithms')
    if any(row['generator'] in ('claude','intellisphere') and row['completed_runs'] for row in data['method_summary']):
        coverage_figure(data, args.output / 'coverage-ai-by-project.png', ['claude','intellisphere'], 'AI-assisted tests')
    print(args.output)


if __name__ == '__main__':
    main()
