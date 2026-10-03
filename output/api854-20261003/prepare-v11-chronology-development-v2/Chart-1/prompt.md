Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: Chart; fixed revision: 1f.
Modified target classes:
org.jfree.chart.renderer.category.AbstractCategoryItemRenderer

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "org.jfree.chart.renderer.category.AreaRenderer",
    "constructor_types": "",
    "method": "findRangeBounds",
    "parameter_types": "org.jfree.data.category.CategoryDataset"
  },
  {
    "class": "org.jfree.chart.renderer.category.AreaRenderer",
    "constructor_types": "",
    "method": "findRangeBounds",
    "parameter_types": "org.jfree.data.category.CategoryDataset,boolean"
  },
  {
    "class": "org.jfree.chart.renderer.category.AreaRenderer",
    "constructor_types": "",
    "method": "getColumnCount",
    "parameter_types": ""
  },
  {
    "class": "org.jfree.chart.renderer.category.AreaRenderer",
    "constructor_types": "",
    "method": "getItemMiddle",
    "parameter_types": "java.lang.Comparable,java.lang.Comparable,org.jfree.data.category.CategoryDataset,org.jfree.chart.axis.CategoryAxis,java.awt.geom.Rectangle2D,org.jfree.chart.util.RectangleEdge"
  },
  {
    "class": "org.jfree.chart.renderer.category.AreaRenderer",
    "constructor_types": "",
    "method": "getLegendItem",
    "parameter_types": "int,int"
  },
  {
    "class": "org.jfree.chart.renderer.category.AreaRenderer",
    "constructor_types": "",
    "method": "getLegendItems",
    "parameter_types": ""
  },
  {
    "class": "org.jfree.chart.renderer.category.AreaRenderer",
    "constructor_types": "",
    "method": "getPassCount",
    "parameter_types": ""
  },
  {
    "class": "org.jfree.chart.renderer.category.AreaRenderer",
    "constructor_types": "",
    "method": "getRowCount",
    "parameter_types": ""
  }
]
```

Common compiled production fixture classes (eligibility only, not oracle approval):
```json
[
  "org.jfree.chart.BufferedImageRenderingSource",
  "org.jfree.chart.ChartColor",
  "org.jfree.chart.ChartFactory",
  "org.jfree.chart.ChartFrame",
  "org.jfree.chart.ChartMouseEvent",
  "org.jfree.chart.ChartMouseListener",
  "org.jfree.chart.ChartPanel",
  "org.jfree.chart.ChartRenderingInfo",
  "org.jfree.chart.ChartTheme",
  "org.jfree.chart.ChartTransferable",
  "org.jfree.chart.ChartUtilities",
  "org.jfree.chart.Drawable",
  "org.jfree.chart.Effect3D",
  "org.jfree.chart.JFreeChart",
  "org.jfree.chart.JFreeChartInfo",
  "org.jfree.chart.LegendItem",
  "org.jfree.chart.LegendItemCollection",
  "org.jfree.chart.LegendItemSource",
  "org.jfree.chart.LegendRenderingOrder",
  "org.jfree.chart.MouseWheelHandler",
  "org.jfree.chart.PolarChartPanel",
  "org.jfree.chart.RenderingSource",
  "org.jfree.chart.StandardChartTheme",
  "org.jfree.chart.annotations.AbstractAnnotation",
  "org.jfree.chart.annotations.AbstractXYAnnotation",
  "org.jfree.chart.annotations.Annotation",
  "org.jfree.chart.annotations.CategoryAnnotation",
  "org.jfree.chart.annotations.CategoryLineAnnotation",
  "org.jfree.chart.annotations.CategoryPointerAnnotation",
  "org.jfree.chart.annotations.CategoryTextAnnotation",
  "org.jfree.chart.annotations.TextAnnotation",
  "org.jfree.chart.annotations.XYAnnotation",
  "org.jfree.chart.annotations.XYAnnotationBoundsInfo",
  "org.jfree.chart.annotations.XYBoxAnnotation",
  "org.jfree.chart.annotations.XYDataImageAnnotation",
  "org.jfree.chart.annotations.XYDrawableAnnotation",
  "org.jfree.chart.annotations.XYImageAnnotation",
  "org.jfree.chart.annotations.XYLineAnnotation",
  "org.jfree.chart.annotations.XYPointerAnnotation",
  "org.jfree.chart.annotations.XYPolygonAnnotation",
  "org.jfree.chart.annotations.XYShapeAnnotation",
  "org.jfree.chart.annotations.XYTextAnnotation",
  "org.jfree.chart.annotations.XYTitleAnnotation",
  "org.jfree.chart.axis.Axis",
  "org.jfree.chart.axis.AxisCollection",
  "org.jfree.chart.axis.AxisLocation",
  "org.jfree.chart.axis.AxisSpace",
  "org.jfree.chart.axis.AxisState",
  "org.jfree.chart.axis.CategoryAnchor",
  "org.jfree.chart.axis.CategoryAxis",
  "org.jfree.chart.axis.CategoryAxis3D",
  "org.jfree.chart.axis.CategoryLabelPosition",
  "org.jfree.chart.axis.CategoryLabelPositions",
  "org.jfree.chart.axis.CategoryLabelWidthType",
  "org.jfree.chart.axis.CategoryTick",
  "org.jfree.chart.axis.CompassFormat",
  "org.jfree.chart.axis.CyclicNumberAxis",
  "org.jfree.chart.axis.DateAxis",
  "org.jfree.chart.axis.DateTick",
  "org.jfree.chart.axis.DateTickMarkPosition",
  "org.jfree.chart.axis.DateTickUnit",
  "org.jfree.chart.axis.DateTickUnitType",
  "org.jfree.chart.axis.ExtendedCategoryAxis",
  "org.jfree.chart.axis.LogAxis",
  "org.jfree.chart.axis.LogarithmicAxis",
  "org.jfree.chart.axis.MarkerAxisBand",
  "org.jfree.chart.axis.ModuloAxis",
  "org.jfree.chart.axis.MonthDateFormat",
  "org.jfree.chart.axis.NumberAxis",
  "org.jfree.chart.axis.NumberAxis3D",
  "org.jfree.chart.axis.NumberTick",
  "org.jfree.chart.axis.NumberTickUnit",
  "org.jfree.chart.axis.PeriodAxis",
  "org.jfree.chart.axis.PeriodAxisLabelInfo",
  "org.jfree.chart.axis.QuarterDateFormat",
  "org.jfree.chart.axis.SegmentedTimeline",
  "org.jfree.chart.axis.StandardTickUnitSource",
  "org.jfree.chart.axis.SubCategoryAxis",
  "org.jfree.chart.axis.SymbolAxis",
  "org.jfree.chart.axis.Tick",
  "org.jfree.chart.axis.TickType",
  "org.jfree.chart.axis.TickUnit",
  "org.jfree.chart.axis.TickUnitSource",
  "org.jfree.chart.axis.TickUnits",
  "org.jfree.chart.axis.Timeline",
  "org.jfree.chart.axis.ValueAxis",
  "org.jfree.chart.axis.ValueTick",
  "org.jfree.chart.block.AbstractBlock",
  "org.jfree.chart.block.Arrangement",
  "org.jfree.chart.block.Block",
  "org.jfree.chart.block.BlockBorder",
  "org.jfree.chart.block.BlockContainer",
  "org.jfree.chart.block.BlockFrame",
  "org.jfree.chart.block.BlockParams",
  "org.jfree.chart.block.BlockResult",
  "org.jfree.chart.block.BorderArrangement",
  "org.jfree.chart.block.CenterArrangement",
  "org.jfree.chart.block.ColorBlock",
  "org.jfree.chart.block.ColumnArrangement",
  "org.jfree.chart.block.EmptyBlock",
  "org.jfree.chart.block.EntityBlockParams",
  "org.jfree.chart.block.EntityBlockResult",
  "org.jfree.chart.block.FlowArrangement",
  "org.jfree.chart.block.GridArrangement",
  "org.jfree.chart.block.LabelBlock",
  "org.jfree.chart.block.LengthConstraintType",
  "org.jfree.chart.block.LineBorder",
  "org.jfree.chart.block.RectangleConstraint",
  "org.jfree.chart.demo.BarChartDemo1",
  "org.jfree.chart.demo.PieChartDemo1",
  "org.jfree.chart.demo.TimeSeriesChartDemo1",
  "org.jfree.chart.editor.ChartEditor",
  "org.jfree.chart.editor.ChartEditorFactory",
  "org.jfree.chart.editor.ChartEditorManager",
  "org.jfree.chart.editor.DefaultAxisEditor",
  "org.jfree.chart.editor.DefaultChartEditor",
  "org.jfree.chart.editor.DefaultChartEditorFactory",
  "org.jfree.chart.editor.DefaultNumberAxisEditor",
  "org.jfree.chart.editor.DefaultPlotEditor",
  "org.jfree.chart.editor.DefaultTitleEditor",
  "org.jfree.chart.encoders.EncoderUtil",
  "org.jfree.chart.encoders.ImageEncoder",
  "org.jfree.chart.encoders.ImageEncoderFactory",
  "org.jfree.chart.encoders.ImageFormat",
  "org.jfree.chart.encoders.SunJPEGEncoderAdapter",
  "org.jfree.chart.encoders.SunPNGEncoderAdapter",
  "org.jfree.chart.entity.AxisEntity",
  "org.jfree.chart.entity.AxisLabelEntity",
  "org.jfree.chart.entity.CategoryItemEntity",
  "org.jfree.chart.entity.CategoryLabelEntity",
  "org.jfree.chart.entity.ChartEntity",
  "org.jfree.chart.entity.EntityCollection",
  "org.jfree.chart.entity.JFreeChartEntity",
  "org.jfree.chart.entity.LegendItemEntity",
  "org.jfree.chart.entity.PieSectionEntity",
  "org.jfree.chart.entity.PlotEntity",
  "org.jfree.chart.entity.StandardEntityCollection",
  "org.jfree.chart.entity.TickLabelEntity",
  "org.jfree.chart.entity.TitleEntity",
  "org.jfree.chart.entity.XYAnnotationEntity",
  "org.jfree.chart.entity.XYItemEntity",
  "org.jfree.chart.event.AnnotationChangeEvent",
  "org.jfree.chart.event.AnnotationChangeListener",
  "org.jfree.chart.event.AxisChangeEvent",
  "org.jfree.chart.event.AxisChangeListener",
  "org.jfree.chart.event.ChartChangeEvent",
  "org.jfree.chart.event.ChartChangeEventType",
  "org.jfree.chart.event.ChartChangeListener",
  "org.jfree.chart.event.ChartProgressEvent",
  "org.jfree.chart.event.ChartProgressListener",
  "org.jfree.chart.event.DatasetChangeInfo",
  "org.jfree.chart.event.MarkerChangeEvent",
  "org.jfree.chart.event.MarkerChangeListener",
  "org.jfree.chart.event.OverlayChangeEvent",
  "org.jfree.chart.event.OverlayChangeListener",
  "org.jfree.chart.event.PlotChangeEvent",
  "org.jfree.chart.event.PlotChangeListener",
  "org.jfree.chart.event.RendererChangeEvent",
  "org.jfree.chart.event.RendererChangeListener",
  "org.jfree.chart.event.TitleChangeEvent",
  "org.jfree.chart.event.TitleChangeListener",
  "org.jfree.chart.imagemap.DynamicDriveToolTipTagFragmentGenerator",
  "org.jfree.chart.imagemap.ImageMapUtilities",
  "org.jfree.chart.imagemap.OverLIBToolTipTagFragmentGenerator",
  "org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator",
  "org.jfree.chart.imagemap.StandardURLTagFragmentGenerator",
  "org.jfree.chart.imagemap.ToolTipTagFragmentGenerator",
  "org.jfree.chart.imagemap.URLTagFragmentGenerator",
  "org.jfree.chart.labels.AbstractCategoryItemLabelGenerator",
  "org.jfree.chart.labels.AbstractPieItemLabelGenerator",
  "org.jfree.chart.labels.AbstractXYItemLabelGenerator",
  "org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator",
  "org.jfree.chart.labels.BoxAndWhiskerXYToolTipGenerator",
  "org.jfree.chart.labels.BubbleXYItemLabelGenerator",
  "org.jfree.chart.labels.CategoryItemLabelGenerator",
  "org.jfree.chart.labels.CategorySeriesLabelGenerator",
  "org.jfree.chart.labels.CategoryToolTipGenerator",
  "org.jfree.chart.labels.CrosshairLabelGenerator",
  "org.jfree.chart.labels.CustomXYToolTipGenerator",
  "org.jfree.chart.labels.HighLowItemLabelGenerator",
  "org.jfree.chart.labels.IntervalCategoryItemLabelGenerator",
  "org.jfree.chart.labels.IntervalCategoryToolTipGenerator",
  "org.jfree.chart.labels.IntervalXYItemLabelGenerator",
  "org.jfree.chart.labels.ItemLabelAnchor",
  "org.jfree.chart.labels.ItemLabelPosition",
  "org.jfree.chart.labels.MultipleXYSeriesLabelGenerator",
  "org.jfree.chart.labels.PieSectionLabelGenerator",
  "org.jfree.chart.labels.PieToolTipGenerator",
  "org.jfree.chart.labels.StandardCategoryItemLabelGenerator",
  "org.jfree.chart.labels.StandardCategorySeriesLabelGenerator",
  "org.jfree.chart.labels.StandardCategoryToolTipGenerator",
  "org.jfree.chart.labels.StandardCrosshairLabelGenerator",
  "org.jfree.chart.labels.StandardPieSectionLabelGenerator",
  "org.jfree.chart.labels.StandardPieToolTipGenerator",
  "org.jfree.chart.labels.StandardXYItemLabelGenerator",
  "org.jfree.chart.labels.StandardXYSeriesLabelGenerator",
  "org.jfree.chart.labels.StandardXYToolTipGenerator",
  "org.jfree.chart.labels.StandardXYZToolTipGenerator",
  "org.jfree.chart.labels.SymbolicXYItemLabelGenerator",
  "org.jfree.chart.labels.XYItemLabelGenerator",
  "org.jfree.chart.labels.XYSeriesLabelGenerator",
  "org.jfree.chart.labels.XYToolTipGenerator",
  "org.jfree.chart.labels.XYZToolTipGenerator",
  "org.jfree.chart.needle.ArrowNeedle",
  "org.jfree.chart.needle.LineNeedle",
  "org.jfree.chart.needle.LongNeedle",
  "org.jfree.chart.needle.MeterNeedle",
  "org.jfree.chart.needle.MiddlePinNeedle",
  "org.jfree.chart.needle.PinNeedle",
  "org.jfree.chart.needle.PlumNeedle",
  "org.jfree.chart.needle.PointerNeedle",
  "org.jfree.chart.needle.ShipNeedle",
  "org.jfree.chart.needle.WindNeedle",
  "org.jfree.chart.panel.AbstractMouseHandler",
  "org.jfree.chart.panel.AbstractOverlay",
  "org.jfree.chart.panel.CrosshairOverlay",
  "org.jfree.chart.panel.Overlay",
  "org.jfree.chart.panel.PanHandler",
  "org.jfree.chart.panel.RegionSelectionHandler",
  "org.jfree.chart.panel.ZoomHandler",
  "org.jfree.chart.plot.AbstractPieLabelDistributor",
  "org.jfree.chart.plot.CategoryCrosshairState",
  "org.jfree.chart.plot.CategoryMarker",
  "org.jfree.chart.plot.CategoryPlot",
  "org.jfree.chart.plot.CombinedDomainCategoryPlot",
  "org.jfree.chart.plot.CombinedDomainXYPlot",
  "org.jfree.chart.plot.CombinedRangeCategoryPlot",
  "org.jfree.chart.plot.CombinedRangeXYPlot",
  "org.jfree.chart.plot.CompassPlot",
  "org.jfree.chart.plot.Crosshair",
  "org.jfree.chart.plot.CrosshairState",
  "org.jfree.chart.plot.DatasetRenderingOrder",
  "org.jfree.chart.plot.DefaultDrawingSupplier",
  "org.jfree.chart.plot.DialShape",
  "org.jfree.chart.plot.DrawingSupplier",
  "org.jfree.chart.plot.FastScatterPlot",
  "org.jfree.chart.plot.IntervalMarker",
  "org.jfree.chart.plot.Marker",
  "org.jfree.chart.plot.MeterInterval",
  "org.jfree.chart.plot.MeterPlot",
  "org.jfree.chart.plot.MultiplePiePlot",
  "org.jfree.chart.plot.Pannable",
  "org.jfree.chart.plot.PieLabelDistributor",
  "org.jfree.chart.plot.PieLabelLinkStyle",
  "org.jfree.chart.plot.PieLabelRecord",
  "org.jfree.chart.plot.PiePlot",
  "org.jfree.chart.plot.PiePlot3D",
  "org.jfree.chart.plot.PiePlotState",
  "org.jfree.chart.plot.PieSelectionAttributes",
  "org.jfree.chart.plot.Plot",
  "org.jfree.chart.plot.PlotOrientation",
  "org.jfree.chart.plot.PlotRenderingInfo",
  "org.jfree.chart.plot.PlotState",
  "org.jfree.chart.plot.PlotUtilities",
  "org.jfree.chart.plot.PolarPlot",
  "org.jfree.chart.plot.RingPlot",
  "org.jfree.chart.plot.Selectable",
  "org.jfree.chart.plot.SeriesRenderingOrder",
  "org.jfree.chart.plot.SpiderWebPlot",
  "org.jfree.chart.plot.ThermometerPlot",
  "org.jfree.chart.plot.ValueAxisPlot",
  "org.jfree.chart.plot.ValueMarker",
  "org.jfree.chart.plot.WaferMapPlot",
  "org.jfree.chart.plot.XYCrosshairState",
  "org.jfree.chart.plot.XYPlot",
  "org.jfree.chart.plot.Zoomable",
  "org.jfree.chart.plot.dial.AbstractDialLayer",
  "org.jfree.chart.plot.dial.ArcDialFrame",
  "org.jfree.chart.plot.dial.DialBackground",
  "org.jfree.chart.plot.dial.DialCap",
  "org.jfree.chart.plot.dial.DialFrame",
  "org.jfree.chart.plot.dial.DialLayer",
  "org.jfree.chart.plot.dial.DialLayerChangeEvent",
  "org.jfree.chart.plot.dial.DialLayerChangeListener",
  "org.jfree.chart.plot.dial.DialPlot",
  "org.jfree.chart.plot.dial.DialPointer",
  "org.jfree.chart.plot.dial.DialScale",
  "org.jfree.chart.plot.dial.DialTextAnnotation",
  "org.jfree.chart.plot.dial.DialValueIndicator",
  "org.jfree.chart.plot.dial.StandardDialFrame",
  "org.jfree.chart.plot.dial.StandardDialRange",
  "org.jfree.chart.plot.dial.StandardDialScale",
  "org.jfree.chart.renderer.AbstractRenderer",
  "org.jfree.chart.renderer.AreaRendererEndType",
  "org.jfree.chart.renderer.DefaultPolarItemRenderer",
  "org.jfree.chart.renderer.GrayPaintScale",
  "org.jfree.chart.renderer.LookupPaintScale",
  "org.jfree.chart.renderer.NotOutlierException",
  "org.jfree.chart.renderer.Outlier",
  "org.jfree.chart.renderer.OutlierList",
  "org.jfree.chart.renderer.OutlierListCollection",
  "org.jfree.chart.renderer.PaintScale",
  "org.jfree.chart.renderer.PolarItemRenderer",
  "org.jfree.chart.renderer.RenderAttributes",
  "org.jfree.chart.renderer.RendererState",
  "org.jfree.chart.renderer.RendererUtilities",
  "org.jfree.chart.renderer.WaferMapRenderer",
  "org.jfree.chart.renderer.category.AbstractCategoryItemRenderer",
  "org.jfree.chart.renderer.category.AreaRenderer",
  "org.jfree.chart.renderer.category.BarPainter",
  "org.jfree.chart.renderer.category.BarRenderer",
  "org.jfree.chart.renderer.category.BarRenderer3D",
  "org.jfree.chart.renderer.category.BoxAndWhiskerRenderer",
  "org.jfree.chart.renderer.category.CategoryItemRenderer",
  "org.jfree.chart.renderer.category.CategoryItemRendererState",
  "org.jfree.chart.renderer.category.CategoryStepRenderer",
  "org.jfree.chart.renderer.category.DefaultCategoryItemRenderer",
  "org.jfree.chart.renderer.category.GanttRenderer",
  "org.jfree.chart.renderer.category.GradientBarPainter",
  "org.jfree.chart.renderer.category.GroupedStackedBarRenderer",
  "org.jfree.chart.renderer.category.IntervalBarRenderer",
  "org.jfree.chart.renderer.category.LayeredBarRenderer",
  "org.jfree.chart.renderer.category.LevelRenderer",
  "org.jfree.chart.renderer.category.LineAndShapeRenderer",
  "org.jfree.chart.renderer.category.LineRenderer3D",
  "org.jfree.chart.renderer.category.MinMaxCategoryRenderer",
  "org.jfree.chart.renderer.category.ScatterRenderer",
  "org.jfree.chart.renderer.category.StackedAreaRenderer",
  "org.jfree.chart.renderer.category.StackedBarRenderer",
  "org.jfree.chart.renderer.category.StackedBarRenderer3D",
  "org.jfree.chart.renderer.category.StandardBarPainter",
  "org.jfree.chart.renderer.category.StatisticalBarRenderer",
  "org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer",
  "org.jfree.chart.renderer.category.WaterfallBarRenderer",
  "org.jfree.chart.renderer.xy.AbstractXYItemRenderer",
  "org.jfree.chart.renderer.xy.CandlestickRenderer",
  "org.jfree.chart.renderer.xy.ClusteredXYBarRenderer",
  "org.jfree.chart.renderer.xy.CyclicXYItemRenderer",
  "org.jfree.chart.renderer.xy.DefaultXYItemRenderer",
  "org.jfree.chart.renderer.xy.DeviationRenderer",
  "org.jfree.chart.renderer.xy.GradientXYBarPainter",
  "org.jfree.chart.renderer.xy.HighLowRenderer",
  "org.jfree.chart.renderer.xy.SamplingXYLineRenderer",
  "org.jfree.chart.renderer.xy.StackedXYAreaRenderer",
  "org.jfree.chart.renderer.xy.StackedXYAreaRenderer2",
  "org.jfree.chart.renderer.xy.StackedXYBarRenderer",
  "org.jfree.chart.renderer.xy.StandardXYBarPainter",
  "org.jfree.chart.renderer.xy.StandardXYItemRenderer",
  "org.jfree.chart.renderer.xy.VectorRenderer",
  "org.jfree.chart.renderer.xy.WindItemRenderer",
  "org.jfree.chart.renderer.xy.XYAreaRenderer",
  "org.jfree.chart.renderer.xy.XYAreaRenderer2",
  "org.jfree.chart.renderer.xy.XYBarPainter",
  "org.jfree.chart.renderer.xy.XYBarRenderer",
  "org.jfree.chart.renderer.xy.XYBlockRenderer",
  "org.jfree.chart.renderer.xy.XYBoxAndWhiskerRenderer",
  "org.jfree.chart.renderer.xy.XYBubbleRenderer",
  "org.jfree.chart.renderer.xy.XYDifferenceRenderer",
  "org.jfree.chart.renderer.xy.XYDotRenderer",
  "org.jfree.chart.renderer.xy.XYErrorRenderer",
  "org.jfree.chart.renderer.xy.XYItemRenderer",
  "org.jfree.chart.renderer.xy.XYItemRendererState",
  "org.jfree.chart.renderer.xy.XYLine3DRenderer",
  "org.jfree.chart.renderer.xy.XYLineAndShapeRenderer",
  "org.jfree.chart.renderer.xy.XYShapeRenderer",
  "org.jfree.chart.renderer.xy.XYSplineRenderer",
  "org.jfree.chart.renderer.xy.XYStepAreaRenderer",
  "org.jfree.chart.renderer.xy.XYStepRenderer",
  "org.jfree.chart.renderer.xy.YIntervalRenderer",
  "org.jfree.chart.resources.JFreeChartResources",
  "org.jfree.chart.servlet.ChartDeleter",
  "org.jfree.chart.servlet.DisplayChart",
  "org.jfree.chart.servlet.ServletUtilities",
  "org.jfree.chart.text.G2TextMeasurer",
  "org.jfree.chart.text.TextAnchor",
  "org.jfree.chart.text.TextBlock",
  "org.jfree.chart.text.TextBlockAnchor",
  "org.jfree.chart.text.TextBox",
  "org.jfree.chart.text.TextFragment",
  "org.jfree.chart.text.TextLine",
  "org.jfree.chart.text.TextMeasurer",
  "org.jfree.chart.text.TextUtilities",
  "org.jfree.chart.title.CompositeTitle",
  "org.jfree.chart.title.DateTitle",
  "org.jfree.chart.title.ImageTitle",
  "org.jfree.chart.title.LegendGraphic",
  "org.jfree.chart.title.LegendItemBlockContainer",
  "org.jfree.chart.title.LegendTitle",
  "org.jfree.chart.title.PaintScaleLegend",
  "org.jfree.chart.title.ShortTextTitle",
  "org.jfree.chart.title.TextTitle",
  "org.jfree.chart.title.Title",
  "org.jfree.chart.ui.BasicProjectInfo",
  "org.jfree.chart.ui.Contributor",
  "org.jfree.chart.ui.ExtensionFileFilter",
  "org.jfree.chart.ui.FontChooserPanel",
  "org.jfree.chart.ui.FontDisplayField",
  "org.jfree.chart.ui.LCBLayout",
  "org.jfree.chart.ui.Library",
  "org.jfree.chart.ui.Licences",
  "org.jfree.chart.ui.PaintSample",
  "org.jfree.chart.ui.ProjectInfo",
  "org.jfree.chart.ui.StrokeChooserPanel",
  "org.jfree.chart.ui.StrokeSample",
  "org.jfree.chart.urls.CategoryURLGenerator",
  "org.jfree.chart.urls.CustomCategoryURLGenerator",
  "org.jfree.chart.urls.CustomPieURLGenerator",
  "org.jfree.chart.urls.CustomXYURLGenerator",
  "org.jfree.chart.urls.PieURLGenerator",
  "org.jfree.chart.urls.StandardCategoryURLGenerator",
  "org.jfree.chart.urls.StandardPieURLGenerator",
  "org.jfree.chart.urls.StandardXYURLGenerator",
  "org.jfree.chart.urls.StandardXYZURLGenerator",
  "org.jfree.chart.urls.TimeSeriesURLGenerator",
  "org.jfree.chart.urls.XYURLGenerator",
  "org.jfree.chart.urls.XYZURLGenerator",
  "org.jfree.chart.util.AbstractObjectList",
  "org.jfree.chart.util.Align",
  "org.jfree.chart.util.ApplicationFrame",
  "org.jfree.chart.util.ArrayUtilities",
  "org.jfree.chart.util.AttributedStringUtilities",
  "org.jfree.chart.util.BooleanList",
  "org.jfree.chart.util.DefaultShadowGenerator",
  "org.jfree.chart.util.GradientPaintTransformType",
  "org.jfree.chart.util.GradientPaintTransformer",
  "org.jfree.chart.util.HashUtilities",
  "org.jfree.chart.util.HexNumberFormat",
  "org.jfree.chart.util.HorizontalAlignment",
  "org.jfree.chart.util.Layer",
  "org.jfree.chart.util.LengthAdjustmentType",
  "org.jfree.chart.util.LineUtilities",
  "org.jfree.chart.util.LogFormat",
  "org.jfree.chart.util.NumberCellRenderer",
  "org.jfree.chart.util.ObjectList",
  "org.jfree.chart.util.ObjectUtilities",
  "org.jfree.chart.util.PaintList",
  "org.jfree.chart.util.PaintMap",
  "org.jfree.chart.util.PaintUtilities",
  "org.jfree.chart.util.PublicCloneable",
  "org.jfree.chart.util.RectangleAnchor",
  "org.jfree.chart.util.RectangleEdge",
  "org.jfree.chart.util.RectangleInsets",
  "org.jfree.chart.util.RefineryUtilities",
  "org.jfree.chart.util.RelativeDateFormat",
  "org.jfree.chart.util.ResourceBundleWrapper",
  "org.jfree.chart.util.Rotation",
  "org.jfree.chart.util.SerialUtilities",
  "org.jfree.chart.util.ShadowGenerator",
  "org.jfree.chart.util.ShapeList",
  "org.jfree.chart.util.ShapeUtilities",
  "org.jfree.chart.util.Size2D",
  "org.jfree.chart.util.SortOrder",
  "org.jfree.chart.util.StandardGradientPaintTransformer",
  "org.jfree.chart.util.StringUtilities",
  "org.jfree.chart.util.StrokeList",
  "org.jfree.chart.util.StrokeMap",
  "org.jfree.chart.util.TableOrder",
  "org.jfree.chart.util.UnitType",
  "org.jfree.chart.util.VerticalAlignment",
  "org.jfree.chart.util.XYCoordinateType",
  "org.jfree.data.ComparableObjectItem",
  "org.jfree.data.ComparableObjectSeries",
  "org.jfree.data.DataUtilities",
  "org.jfree.data.DefaultKeyedValue",
  "org.jfree.data.DefaultKeyedValues",
  "org.jfree.data.DefaultKeyedValues2D",
  "org.jfree.data.DomainInfo",
  "org.jfree.data.DomainOrder",
  "org.jfree.data.KeyToGroupMap",
  "org.jfree.data.KeyedObject",
  "org.jfree.data.KeyedObjectComparator",
  "org.jfree.data.KeyedObjectComparatorType",
  "org.jfree.data.KeyedObjects",
  "org.jfree.data.KeyedObjects2D",
  "org.jfree.data.KeyedValue",
  "org.jfree.data.KeyedValueComparator",
  "org.jfree.data.KeyedValueComparatorType",
  "org.jfree.data.KeyedValues",
  "org.jfree.data.KeyedValues2D",
  "org.jfree.data.Range",
  "org.jfree.data.RangeInfo",
  "org.jfree.data.RangeType",
  "org.jfree.data.SelectableValue",
  "org.jfree.data.UnknownKeyException",
  "org.jfree.data.Value",
  "org.jfree.data.Values",
  "org.jfree.data.Values2D",
  "org.jfree.data.category.AbstractCategoryDataset",
  "org.jfree.data.category.CategoryDataset",
  "org.jfree.data.category.CategoryDatasetSelectionState",
  "org.jfree.data.category.CategoryRangeInfo",
  "org.jfree.data.category.CategoryToPieDataset",
  "org.jfree.data.category.DefaultCategoryDataset",
  "org.jfree.data.category.DefaultIntervalCategoryDataset",
  "org.jfree.data.category.IntervalCategoryDataset",
  "org.jfree.data.category.SelectableCategoryDataset",
  "org.jfree.data.category.SlidingCategoryDataset",
  "org.jfree.data.event.DatasetChangeEvent",
  "org.jfree.data.event.DatasetChangeListener",
  "org.jfree.data.event.SeriesChangeEvent",
  "org.jfree.data.event.SeriesChangeListener",
  "org.jfree.data.function.Function2D",
  "org.jfree.data.function.LineFunction2D",
  "org.jfree.data.function.NormalDistributionFunction2D",
  "org.jfree.data.function.PolynomialFunction2D",
  "org.jfree.data.function.PowerFunction2D",
  "org.jfree.data.gantt.GanttCategoryDataset",
  "org.jfree.data.gantt.SlidingGanttCategoryDataset",
  "org.jfree.data.gantt.Task",
  "org.jfree.data.gantt.TaskSeries",
  "org.jfree.data.gantt.TaskSeriesCollection",
  "org.jfree.data.gantt.XYTaskDataset",
  "org.jfree.data.general.AbstractDataset",
  "org.jfree.data.general.AbstractSeriesDataset",
  "org.jfree.data.general.Dataset",
  "org.jfree.data.general.DatasetAndSelection",
  "org.jfree.data.general.DatasetGroup",
  "org.jfree.data.general.DatasetSelectionState",
  "org.jfree.data.general.DatasetUtilities",
  "org.jfree.data.general.DefaultHeatMapDataset",
  "org.jfree.data.general.DefaultKeyedValueDataset",
  "org.jfree.data.general.DefaultKeyedValues2DDataset",
  "org.jfree.data.general.DefaultKeyedValuesDataset",
  "org.jfree.data.general.DefaultValueDataset",
  "org.jfree.data.general.HeatMapDataset",
  "org.jfree.data.general.HeatMapUtilities",
  "org.jfree.data.general.KeyedValueDataset",
  "org.jfree.data.general.KeyedValues2DDataset",
  "org.jfree.data.general.KeyedValuesDataset",
  "org.jfree.data.general.Series",
  "org.jfree.data.general.SeriesChangeInfo",
  "org.jfree.data.general.SeriesChangeType",
  "org.jfree.data.general.SeriesDataset",
  "org.jfree.data.general.SeriesException",
  "org.jfree.data.general.ValueDataset",
  "org.jfree.data.general.WaferMapDataset",
  "org.jfree.data.io.CSV",
  "org.jfree.data.jdbc.JDBCCategoryDataset",
  "org.jfree.data.jdbc.JDBCPieDataset",
  "org.jfree.data.jdbc.JDBCXYDataset",
  "org.jfree.data.pie.AbstractPieDataset",
  "org.jfree.data.pie.DefaultPieDataset",
  "org.jfree.data.pie.PieDataset",
  "org.jfree.data.pie.PieDatasetChangeInfo",
  "org.jfree.data.pie.PieDatasetChangeType",
  "org.jfree.data.pie.PieDatasetSelectionState",
  "org.jfree.data.pie.SelectablePieDataset",
  "org.jfree.data.resources.DataPackageResources",
  "org.jfree.data.resources.DataPackageResources_de",
  "org.jfree.data.resources.DataPackageResources_es",
  "org.jfree.data.resources.DataPackageResources_fr",
  "org.jfree.data.resources.DataPackageResources_pl",
  "org.jfree.data.resources.DataPackageResources_ru",
  "org.jfree.data.statistics.BoxAndWhiskerCalculator",
  "org.jfree.data.statistics.BoxAndWhiskerCategoryDataset",
  "org.jfree.data.statistics.BoxAndWhiskerItem",
  "org.jfree.data.statistics.BoxAndWhiskerXYDataset",
  "org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset",
  "org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset",
  "org.jfree.data.statistics.DefaultMultiValueCategoryDataset",
  "org.jfree.data.statistics.DefaultStatisticalCategoryDataset",
  "org.jfree.data.statistics.HistogramBin",
  "org.jfree.data.statistics.HistogramDataset",
  "org.jfree.data.statistics.HistogramType",
  "org.jfree.data.statistics.MeanAndStandardDeviation",
  "org.jfree.data.statistics.MultiValueCategoryDataset",
  "org.jfree.data.statistics.Regression",
  "org.jfree.data.statistics.SimpleHistogramBin",
  "org.jfree.data.statistics.SimpleHistogramDataset",
  "org.jfree.data.statistics.StatisticalCategoryDataset",
  "org.jfree.data.statistics.Statistics",
  "org.jfree.data.time.DateRange",
  "org.jfree.data.time.Day",
  "org.jfree.data.time.DynamicTimeSeriesCollection",
  "org.jfree.data.time.FixedMillisecond",
  "org.jfree.data.time.Hour",
  "org.jfree.data.time.Millisecond",
  "org.jfree.data.time.Minute",
  "org.jfree.data.time.Month",
  "org.jfree.data.time.MonthConstants",
  "org.jfree.data.time.MovingAverage",
  "org.jfree.data.time.Quarter",
  "org.jfree.data.time.RegularTimePeriod",
  "org.jfree.data.time.Second",
  "org.jfree.data.time.SerialDate",
  "org.jfree.data.time.SimpleTimePeriod",
  "org.jfree.data.time.SpreadsheetDate",
  "org.jfree.data.time.TimePeriod",
  "org.jfree.data.time.TimePeriodAnchor",
  "org.jfree.data.time.TimePeriodFormatException",
  "org.jfree.data.time.TimePeriodValue",
  "org.jfree.data.time.TimePeriodValues",
  "org.jfree.data.time.TimePeriodValuesCollection",
  "org.jfree.data.time.TimeSeries",
  "org.jfree.data.time.TimeSeriesCollection",
  "org.jfree.data.time.TimeSeriesDataItem",
  "org.jfree.data.time.TimeSeriesTableModel",
  "org.jfree.data.time.TimeTableXYDataset",
  "org.jfree.data.time.Week",
  "org.jfree.data.time.Year",
  "org.jfree.data.time.ohlc.OHLC",
  "org.jfree.data.time.ohlc.OHLCItem",
  "org.jfree.data.time.ohlc.OHLCSeries",
  "org.jfree.data.time.ohlc.OHLCSeriesCollection",
  "org.jfree.data.xml.CategoryDatasetHandler",
  "org.jfree.data.xml.CategorySeriesHandler",
  "org.jfree.data.xml.DatasetReader",
  "org.jfree.data.xml.DatasetTags",
  "org.jfree.data.xml.ItemHandler",
  "org.jfree.data.xml.KeyHandler",
  "org.jfree.data.xml.PieDatasetHandler",
  "org.jfree.data.xml.RootHandler",
  "org.jfree.data.xml.ValueHandler",
  "org.jfree.data.xy.AbstractIntervalXYDataset",
  "org.jfree.data.xy.AbstractXYDataset",
  "org.jfree.data.xy.AbstractXYZDataset",
  "org.jfree.data.xy.CategoryTableXYDataset",
  "org.jfree.data.xy.DefaultHighLowDataset",
  "org.jfree.data.xy.DefaultIntervalXYDataset",
  "org.jfree.data.xy.DefaultOHLCDataset",
  "org.jfree.data.xy.DefaultTableXYDataset",
  "org.jfree.data.xy.DefaultWindDataset",
  "org.jfree.data.xy.DefaultXYDataset",
  "org.jfree.data.xy.DefaultXYZDataset",
  "org.jfree.data.xy.IntervalXYDataset",
  "org.jfree.data.xy.IntervalXYDelegate",
  "org.jfree.data.xy.IntervalXYZDataset",
  "org.jfree.data.xy.MatrixSeries",
  "org.jfree.data.xy.MatrixSeriesCollection",
  "org.jfree.data.xy.NormalizedMatrixSeries",
  "org.jfree.data.xy.OHLCDataItem",
  "org.jfree.data.xy.OHLCDataset",
  "org.jfree.data.xy.SelectableXYDataset",
  "org.jfree.data.xy.TableXYDataset",
  "org.jfree.data.xy.Vector",
  "org.jfree.data.xy.VectorDataItem",
  "org.jfree.data.xy.VectorSeries",
  "org.jfree.data.xy.VectorSeriesCollection",
  "org.jfree.data.xy.VectorXYDataset",
  "org.jfree.data.xy.WindDataItem",
  "org.jfree.data.xy.WindDataset",
  "org.jfree.data.xy.XIntervalDataItem",
  "org.jfree.data.xy.XIntervalSeries",
  "org.jfree.data.xy.XIntervalSeriesCollection",
  "org.jfree.data.xy.XYBarDataset",
  "org.jfree.data.xy.XYCoordinate",
  "org.jfree.data.xy.XYDataItem",
  "org.jfree.data.xy.XYDataset",
  "org.jfree.data.xy.XYDatasetSelectionState",
  "org.jfree.data.xy.XYDatasetTableModel",
  "org.jfree.data.xy.XYDomainInfo",
  "org.jfree.data.xy.XYInterval",
  "org.jfree.data.xy.XYIntervalDataItem",
  "org.jfree.data.xy.XYIntervalSeries",
  "org.jfree.data.xy.XYIntervalSeriesCollection",
  "org.jfree.data.xy.XYRangeInfo",
  "org.jfree.data.xy.XYSeries",
  "org.jfree.data.xy.XYSeriesCollection",
  "org.jfree.data.xy.XYZDataset",
  "org.jfree.data.xy.XisSymbolic",
  "org.jfree.data.xy.YInterval",
  "org.jfree.data.xy.YIntervalDataItem",
  "org.jfree.data.xy.YIntervalSeries",
  "org.jfree.data.xy.YIntervalSeriesCollection",
  "org.jfree.data.xy.YWithXInterval",
  "org.jfree.data.xy.YisSymbolic",
  "org.jfree.experimental.chart.renderer.xy.XYSmoothLineAndShapeRenderer"
]
```

Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.

Explicit fixture policy: aom-beam-champ-chronology-fixtures-v11-development. Use the reviewed capability recipes below instead of legacy recursive/null construction.
## source/org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java

```
/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2010, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ---------------------------------
 * AbstractCategoryItemRenderer.java
 * ---------------------------------
 * (C) Copyright 2002-2010, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Richard Atkinson;
 *                   Peter Kolb (patch 2497611);
 *
 * Changes:
 * --------
 * 29-May-2002 : Version 1 (DG);
 * 06-Jun-2002 : Added accessor methods for the tool tip generator (DG);
 * 11-Jun-2002 : Made constructors protected (DG);
 * 26-Jun-2002 : Added axis to initialise method (DG);
 * 05-Aug-2002 : Added urlGenerator member variable plus accessors (RA);
 * 22-Aug-2002 : Added categoriesPaint attribute, based on code submitted by
 *               Janet Banks.  This can be used when there is only one series,
 *               and you want each category item to have a different color (DG);
 * 01-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 29-Oct-2002 : Fixed bug where background image for plot was not being
 *               drawn (DG);
 * 05-Nov-2002 : Replaced references to CategoryDataset with TableDataset (DG);
 * 26-Nov 2002 : Replaced the isStacked() method with getRangeType() (DG);
 * 09-Jan-2003 : Renamed grid-line methods (DG);
 * 17-Jan-2003 : Moved plot classes into separate package (DG);
 * 25-Mar-2003 : Implemented Serializable (DG);
 * 12-May-2003 : Modified to take into account the plot orientation (DG);
 * 12-Aug-2003 : Very minor javadoc corrections (DB)
 * 13-Aug-2003 : Implemented Cloneable (DG);
 * 16-Sep-2003 : Changed ChartRenderingInfo --> PlotRenderingInfo (DG);
 * 05-Nov-2003 : Fixed marker rendering bug (833623) (DG);
 * 21-Jan-2004 : Update for renamed method in ValueAxis (DG);
 * 11-Feb-2004 : Modified labelling for markers (DG);
 * 12-Feb-2004 : Updated clone() method (DG);
 * 15-Apr-2004 : Created a new CategoryToolTipGenerator interface (DG);
 * 05-May-2004 : Fixed bug (948310) where interval markers extend outside axis
 *               range (DG);
 * 14-Jun-2004 : Fixed bug in drawRangeMarker() method - now uses 'paint' and
 *               'stroke' rather than 'outlinePaint' and 'outlineStroke' (DG);
 * 15-Jun-2004 : Interval markers can now use GradientPaint (DG);
 * 30-Sep-2004 : Moved drawRotatedString() from RefineryUtilities
 *               --> TextUtilities (DG);
 * 01-Oct-2004 : Fixed bug 1029697, problem with label alignment in
 *               drawRangeMarker() method (DG);
 * 07-Jan-2005 : Renamed getRangeExtent() --> findRangeBounds() (DG);
 * 21-Jan-2005 : Modified return type of calculateRangeMarkerTextAnchorPoint()
 *               method (DG);
 * 08-Mar-2005 : Fixed positioning of marker labels (DG);
 * 20-Apr-2005 : Added legend label, tooltip and URL generators (DG);
 * 01-Jun-2005 : Handle one dimension of the marker label adjustment
 *               automatically (DG);
 * 09-Jun-2005 : Added utility method for adding an item entity (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 01-Mar-2006 : Updated getLegendItems() to check seriesVisibleInLegend
 *               flags (DG);
 * 20-Jul-2006 : Set dataset and series indices in LegendItem (DG);
 * 23-Oct-2006 : Draw outlines for interval markers (DG);
 * 24-Oct-2006 : Respect alpha setting in markers, as suggested by Sergei
 *               Ivanov in patch 1567843 (DG);
 * 30-Nov-2006 : Added a check for series visibility in the getLegendItem()
 *               method (DG);
 * 07-Dec-2006 : Fix for equals() method (DG);
 * 22-Feb-2007 : Added createState() method (DG);
 * 01-Mar-2007 : Fixed interval marker drawing (patch 1670686 thanks to
 *               Sergei Ivanov) (DG);
 * 20-Apr-2007 : Updated getLegendItem() for renderer change, and deprecated
 *               itemLabelGenerator, toolTipGenerator and itemURLGenerator
 *               override fields (DG);
 * 18-May-2007 : Set dataset and seriesKey for LegendItem (DG);
 * 20-Jun-2007 : Removed deprecated code and removed JCommon dependencies (DG);
 * 27-Jun-2007 : Added some new methods with 'notify' argument, renamed
 *               methods containing 'ItemURL' to just 'URL' (DG);
 * 06-Jul-2007 : Added annotation support (DG);
 * 17-Jun-2008 : Apply legend shape, font and paint attributes (DG);
 * 26-Jun-2008 : Added crosshair support (DG);
 * 25-Nov-2008 : Fixed bug in findRangeBounds() method (DG);
 * 14-Jan-2009 : Update initialise() to store visible series indices (PK);
 * 21-Jan-2009 : Added drawRangeLine() method (DG);
 * 28-Jan-2009 : Updated for changes to CategoryItemRenderer interface (DG);
 * 27-Mar-2009 : Added new findRangeBounds() method to account for hidden
 *               series (DG);
 * 01-Apr-2009 : Added new addEntity() method (DG);
 * 09-Feb-2010 : Fixed bug 2947660 (DG);
 *
 */

package org.jfree.chart.renderer.category;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.RenderingSource;
import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryCrosshairState;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.text.TextUtilities;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.Layer;
import org.jfree.chart.util.LengthAdjustmentType;
import org.jfree.chart.util.ObjectList;
import org.jfree.chart.util.ObjectUtilities;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleAnchor;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.RectangleInsets;
import org.jfree.chart.util.SortOrder;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryDatasetSelectionState;
import org.jfree.data.category.SelectableCategoryDataset;
import org.jfree.data.general.DatasetUtilities;

/**
 * An abstract base class that you can use to implement a new
 * {@link CategoryItemRenderer}.  When you create a new
 * {@link CategoryItemRenderer} you are not required to extend this class,
 * but it makes the job easier.
 */
public abstract class AbstractCategoryItemRenderer extends AbstractRenderer
        implements CategoryItemRenderer, Cloneable, PublicCloneable,
        Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 1247553218442497391L;

    /** The plot that the renderer is assigned to. */
    private CategoryPlot plot;

    /** A list of item label generators (one per series). */
    private ObjectList itemLabelGeneratorList;

    /** The base item label generator. */
    private CategoryItemLabelGenerator baseItemLabelGenerator;

    /** A list of tool tip generators (one per series). */
    private ObjectList toolTipGeneratorList;

    /** The base tool tip generator. */
    private CategoryToolTipGenerator baseToolTipGenerator;

    /** A list of label generators (one per series). */
    private ObjectList urlGeneratorList;

    /** The base label generator. */
    private CategoryURLGenerator baseURLGenerator;

    /** The legend item label generator. */
    private CategorySeriesLabelGenerator legendItemLabelGenerator;

    /** The legend item tool tip generator. */
    private CategorySeriesLabelGenerator legendItemToolTipGenerator;

    /** The legend item URL generator. */
    private CategorySeriesLabelGenerator legendItemURLGenerator;

    /**
     * Annotations to be drawn in the background layer ('underneath' the data
     * items).
     *
     * @since 1.2.0
     */
    private List backgroundAnnotations;

    /**
     * Annotations to be drawn in the foreground layer ('on top' of the data
     * items).
     *
     * @since 1.2.0
     */
    private List foregroundAnnotations;

    /** The number of rows in the dataset (temporary record). */
    private transient int rowCount;

    /** The number of columns in the dataset (temporary record). */
    private transient int columnCount;

    /**
     * Creates a new renderer with no tool tip generator and no URL generator.
     * The defaults (no tool tip or URL generators) have been chosen to
     * minimise the processing required to generate a default chart.  If you
     * require tool tips or URLs, then you can easily add the required
     * generators.
     */
    protected AbstractCategoryItemRenderer() {
        this.itemLabelGeneratorList = new ObjectList();
        this.toolTipGeneratorList = new ObjectList();
        this.urlGeneratorList = new ObjectList();
        this.legendItemLabelGenerator
                = new StandardCategorySeriesLabelGenerator();
        this.backgroundAnnotations = new ArrayList();
        this.foregroundAnnotations = new ArrayList();
    }

    /**
     * Returns the number of passes through the dataset required by the
     * renderer.  This method returns <code>1</code>, subclasses should
     * override if they need more passes.
     *
     * @return The pass count.
     */
    public int getPassCount() {
        return 1;
    }

    /**
     * Returns the plot that the renderer has been assigned to (where
     * <code>null</code> indicates that the renderer is not currently assigned
     * to a plot).
     *
     * @return The plot (possibly <code>null</code>).
     *
     * @see #setPlot(CategoryPlot)
     */
    public CategoryPlot getPlot() {
        return this.plot;
    }

    /**
     * Sets the plot that the renderer has been assigned to.  This method is
     * usually called by the {@link CategoryPlot}, in normal usage you
     * shouldn't need to call this method directly.
     *
     * @param plot  the plot (<code>null</code> not permitted).
     *
     * @see #getPlot()
     */
    public void setPlot(CategoryPlot plot) {
        if (plot == null) {
            throw new IllegalArgumentException("Null 'plot' argument.");
        }
        this.plot = plot;
    }

    // ITEM LABEL GENERATOR

    /**
     * Returns the item label generator for a data item.  This implementation
     * returns the series item label generator if one is defined, otherwise
     * it returns the default item label generator (which may be
     * <code>null</code>).
     *
     * @param row  the row index (zero based).
     * @param column  the column index (zero based).
     * @param selected  is the item selected?
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @since 1.2.0
     */
    public CategoryItemLabelGenerator getItemLabelGenerator(int row,
            int column, boolean selected) {
        CategoryItemLabelGenerator generator = (CategoryItemLabelGenerator)
                this.itemLabelGeneratorList.get(row);
        if (generator == null) {
            generator = this.baseItemLabelGenerator;
        }
        return generator;
    }

    /**
     * Returns the item label generator for a series.
     *
     * @param series  the series index (zero based).
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @see #setSeriesItemLabelGenerator(int, CategoryItemLabelGenerator)
     */
    public CategoryItemLabelGenerator getSeriesItemLabelGenerator(int series) {
        return (CategoryItemLabelGenerator) this.itemLabelGeneratorList.get(
                series);
    }

    /**
     * Sets the item label generator for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero based).
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getSeriesItemLabelGenerator(int)
     */
    public void setSeriesItemLabelGenerator(int series,
            CategoryItemLabelGenerator generator) {
        setSeriesItemLabelGenerator(series, generator, true);
    }

    /**
     * Sets the item label generator for a series and, if requested, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero based).
     * @param generator  the generator (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @since 1.2.0
     *
     * @see #getSeriesItemLabelGenerator(int)
     */
    public void setSeriesItemLabelGenerator(int series,
            CategoryItemLabelGenerator generator, boolean notify) {
        this.itemLabelGeneratorList.set(series, generator);
        if (notify) {
            notifyListeners(new RendererChangeEvent(this));
        }
    }

    /**
     * Returns the base item label generator.
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @see #setBaseItemLabelGenerator(CategoryItemLabelGenerator)
     */
    public CategoryItemLabelGenerator getBaseItemLabelGenerator() {
        return this.baseItemLabelGenerator;
    }

    /**
     * Sets the base item label generator and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getBaseItemLabelGenerator()
     */
    public void setBaseItemLabelGenerator(
            CategoryItemLabelGenerator generator) {
        setBaseItemLabelGenerator(generator, true);
    }

    /**
     * Sets the base item label generator and, if requested, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @since 1.2.0
     *
     * @see #getBaseItemLabelGenerator()
     */
    public void setBaseItemLabelGenerator(
            CategoryItemLabelGenerator generator, boolean notify) {
        this.baseItemLabelGenerator = generator;
        if (notify) {
            notifyListeners(new RendererChangeEvent(this));
        }
    }

    // TOOL TIP GENERATOR

    /**
     * Returns the tool tip generator that should be used for the specified
     * item.  You can override this method if you want to return a different
     * generator per item.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     * @param selected  is the item selected?
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @since 1.2.0
     */
    public CategoryToolTipGenerator getToolTipGenerator(int row, int column,
            boolean selected) {

        CategoryToolTipGenerator result = null;
        result = getSeriesToolTipGenerator(row);
        if (result == null) {
            result = this.baseToolTipGenerator;
        }
        return result;
    }

    /**
     * Returns the tool tip generator for the specified series (a "layer 1"
     * generator).
     *
     * @param series  the series index (zero-based).
     *
     * @return The tool tip generator (possibly <code>null</code>).
     *
     * @see #setSeriesToolTipGenerator(int, CategoryToolTipGenerator)
     */
    public CategoryToolTipGenerator getSeriesToolTipGenerator(int series) {
        return (CategoryToolTipGenerator) this.toolTipGeneratorList.get(series);
    }

    /**
     * Sets the tool tip generator for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getSeriesToolTipGenerator(int)
     */
    public void setSeriesToolTipGenerator(int series,
            CategoryToolTipGenerator generator) {
        setSeriesToolTipGenerator(series, generator, true);
    }

    /**
     * Sets the tool tip generator for a series and sends a
     * {@link org.jfree.chart.event.RendererChangeEvent} to all registered
     * listeners.
     *
     * @param series  the series index (zero-based).
     * @param generator  the generator (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @since 1.2.0
     *
     * @see #getSeriesToolTipGenerator(int)
     */
    public void setSeriesToolTipGenerator(int series,
            CategoryToolTipGenerator generator, boolean notify) {
        this.toolTipGeneratorList.set(series, generator);
        if (notify) {
            notifyListeners(new RendererChangeEvent(this));
        }
    }

    /**
     * Returns the base tool tip generator (the "layer 2" generator).
     *
     * @return The tool tip generator (possibly <code>null</code>).
     *
     * @see #setBaseToolTipGenerator(CategoryToolTipGenerator)
     */
    public CategoryToolTipGenerator getBaseToolTipGenerator() {
        return this.baseToolTipGenerator;
    }

    /**
     * Sets the base tool tip generator and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getBaseToolTipGenerator()
     */
    public void setBaseToolTipGenerator(CategoryToolTipGenerator generator) {
        setBaseToolTipGenerator(generator, true);
    }

    /**
     * Sets the base tool tip generator and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @since 1.2.0
     *
     * @see #getBaseToolTipGenerator()
     */
    public void setBaseToolTipGenerator(CategoryToolTipGenerator generator,
            boolean notify) {
        this.baseToolTipGenerator = generator;
        if (notify) {
            notifyListeners(new RendererChangeEvent(this));
        }
    }

    // URL GENERATOR

    /**
     * Returns the URL generator for a data item.
     *
     * @param row  the row index (zero based).
     * @param column  the column index (zero based).
     * @param selected  is the item selected?
     *
     * @return The URL generator.
     *
     * @since 1.2.0
     */
    public CategoryURLGenerator getURLGenerator(int row, int column, boolean
            selected) {
        CategoryURLGenerator generator
                = (CategoryURLGenerator) this.urlGeneratorList.get(row);
        if (generator == null) {
            generator = this.baseURLGenerator;
        }
        return generator;
    }

    /**
     * Returns the URL generator for a series.
     *
     * @param series  the series index (zero based).
     *
     * @return The URL generator for the series.
     *
     * @see #setSeriesURLGenerator(int, CategoryURLGenerator)
     */
    public CategoryURLGenerator getSeriesURLGenerator(int series) {
        return (CategoryURLGenerator) this.urlGeneratorList.get(series);
    }

    /**
     * Sets the URL generator for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero based).
     * @param generator  the generator.
     *
     * @see #getSeriesURLGenerator(int)
     */
    public void setSeriesURLGenerator(int series,
            CategoryURLGenerator generator) {
        setSeriesURLGenerator(series, generator, true);
    }

    /**
     * Sets the URL generator for a series and, if requested, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero based).
     * @param generator  the generator (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @since 1.2.0
     *
     * @see #getSeriesURLGenerator(int)
     */
    public void setSeriesURLGenerator(int series,
            CategoryURLGenerator generator, boolean notify) {
        this.urlGeneratorList.set(series, generator);
        if (notify) {
            notifyListeners(new RendererChangeEvent(this));
        }
    }

    /**
     * Returns the base item URL generator.
     *
     * @return The item URL generator.
     *
     * @see #setBaseURLGenerator(CategoryURLGenerator)
     */
    public CategoryURLGenerator getBaseURLGenerator() {
        return this.baseURLGenerator;
    }

    /**
     * Sets the base item URL generator.
     *
     * @param generator  the item URL generator.
     *
     * @see #getBaseURLGenerator()
     */
    public void setBaseURLGenerator(CategoryURLGenerator generator) {
        setBaseURLGenerator(generator, true);
    }

    /**
     * Sets the base item URL generator.
     *
     * @param generator  the item URL generator (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @see #getBaseURLGenerator()
     *
     * @since 1.2.0
     */
    public void setBaseURLGenerator(CategoryURLGenerator generator,
            boolean notify) {
        this.baseURLGenerator = generator;
        if (notify) {
            notifyListeners(new RendererChangeEvent(this));
        }
    }

    // ANNOTATIONS

    /**
     * Adds an annotation and sends a {@link RendererChangeEvent} to all
     * registered listeners.  The annotation is added to the foreground
     * layer.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     *
     * @since 1.2.0
     */
    public void addAnnotation(CategoryAnnotation annotation) {
        // defer argument checking
        addAnnotation(annotation, Layer.FOREGROUND);
    }

    /**
     * Adds an annotation to the specified layer.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     * @param layer  the layer (<code>null</code> not permitted).
     *
     * @since 1.2.0
     */
    public void addAnnotation(CategoryAnnotation annotation, Layer layer) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        if (layer.equals(Layer.FOREGROUND)) {
            this.foregroundAnnotations.add(annotation);
            notifyListeners(new RendererChangeEvent(this));
        }
        else if (layer.equals(Layer.BACKGROUND)) {
            this.backgroundAnnotations.add(annotation);
            notifyListeners(new RendererChangeEvent(this));
        }
        else {
            // should never get here
            throw new RuntimeException("Unknown layer.");
        }
    }
    /**
     * Removes the specified annotation and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param annotation  the annotation to remove (<code>null</code> not
     *                    permitted).
     *
     * @return A boolean to indicate whether or not the annotation was
     *         successfully removed.
     *
     * @since 1.2.0
     */
    public boolean removeAnnotation(CategoryAnnotation annotation) {
        boolean removed = this.foregroundAnnotations.remove(annotation);
        removed = removed & this.backgroundAnnotations.remove(annotation);
        notifyListeners(new RendererChangeEvent(this));
        return removed;
    }

    /**
     * Removes all annotations and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @since 1.2.0
     */
    public void removeAnnotations() {
        this.foregroundAnnotations.clear();
        this.backgroundAnnotations.clear();
        notifyListeners(new RendererChangeEvent(this));
    }

    /**
     * Returns the legend item label generator.
     *
     * @return The label generator (never <code>null</code>).
     *
     * @see #setLegendItemLabelGenerator(CategorySeriesLabelGenerator)
     */
    public CategorySeriesLabelGenerator getLegendItemLabelGenerator() {
        return this.legendItemLabelGenerator;
    }

    /**
     * Sets the legend item label generator and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> not permitted).
     *
     * @see #getLegendItemLabelGenerator()
     */
    public void setLegendItemLabelGenerator(
            CategorySeriesLabelGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("Null 'generator' argument.");
        }
        this.legendItemLabelGenerator = generator;
        fireChangeEvent();
    }

    /**
     * Returns the legend item tool tip generator.
     *
     * @return The tool tip generator (possibly <code>null</code>).
     *
     * @see #setLegendItemToolTipGenerator(CategorySeriesLabelGenerator)
     */
    public CategorySeriesLabelGenerator getLegendItemToolTipGenerator() {
        return this.legendItemToolTipGenerator;
    }

    /**
     * Sets the legend item tool tip generator and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #setLegendItemToolTipGenerator(CategorySeriesLabelGenerator)
     */
    public void setLegendItemToolTipGenerator(
            CategorySeriesLabelGenerator generator) {
        this.legendItemToolTipGenerator = generator;
        fireChangeEvent();
    }

    /**
     * Returns the legend item URL generator.
     *
     * @return The URL generator (possibly <code>null</code>).
     *
     * @see #setLegendItemURLGenerator(CategorySeriesLabelGenerator)
     */
    public CategorySeriesLabelGenerator getLegendItemURLGenerator() {
        return this.legendItemURLGenerator;
    }

    /**
     * Sets the legend item URL generator and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getLegendItemURLGenerator()
     */
    public void setLegendItemURLGenerator(
            CategorySeriesLabelGenerator generator) {
        this.legendItemURLGenerator = generator;
        fireChangeEvent();
    }

    /**
     * Returns the number of rows in the dataset.  This value is updated in the
     * {@link AbstractCategoryItemRenderer#initialise} method.
     *
     * @return The row count.
     */
    public int getRowCount() {
        return this.rowCount;
    }

    /**
     * Returns the number of columns in the dataset.  This value is updated in
     * the {@link AbstractCategoryItemRenderer#initialise} method.
     *
     * @return The column count.
     */
    public int getColumnCount() {
        return this.columnCount;
    }

    /**
     * Creates a new state instance---this method is called from the
     * {@link #initialise(Graphics2D, Rectangle2D, CategoryPlot, int,
     * PlotRenderingInfo)} method.  Subclasses can override this method if
     * they need to use a subclass of {@link CategoryItemRendererState}.
     *
     * @param info  collects plot rendering info (<code>null</code> permitted).
     *
     * @return The new state instance (never <code>null</code>).
     *
     * @since 1.0.5
     */
    protected CategoryItemRendererState createState(PlotRenderingInfo info) {
        CategoryItemRendererState state = new CategoryItemRendererState(info);
        int[] visibleSeriesTemp = new int[this.rowCount];
        int visibleSeriesCount = 0;
        for (int row = 0; row < this.rowCount; row++) {
            if (isSeriesVisible(row)) {
                visibleSeriesTemp[visibleSeriesCount] = row;
                visibleSeriesCount++;
            }
        }
        int[] visibleSeries = new int[visibleSeriesCount];
        System.arraycopy(visibleSeriesTemp, 0, visibleSeries, 0,
                visibleSeriesCount);
        state.setVisibleSeriesArray(visibleSeries);
        return state;
    }

    /**
     * Initialises the renderer and returns a state object that will be used
     * for the remainder of the drawing process for a single chart.  The state
     * object allows for the fact that the renderer may be used simultaneously
     * by multiple threads (each thread will work with a separate state object).
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param plot  the plot.
     * @param info  an object for returning information about the structure of
     *              the plot (<code>null</code> permitted).
     *
     * @return The renderer state.
     */
    public CategoryItemRendererState initialise(Graphics2D g2,
            Rectangle2D dataArea, CategoryPlot plot, CategoryDataset dataset,
            PlotRenderingInfo info) {

        setPlot(plot);
        if (dataset != null) {
            this.rowCount = dataset.getRowCount();
            this.columnCount = dataset.getColumnCount();
        }
        else {
            this.rowCount = 0;
            this.columnCount = 0;
        }
        CategoryItemRendererState state = createState(info);

        // determine if there is any selection state for the dataset
        CategoryDatasetSelectionState selectionState = null;
        if (dataset instanceof SelectableCategoryDataset) {
            SelectableCategoryDataset scd = (SelectableCategoryDataset) dataset;
            selectionState = scd.getSelectionState();
        }
        // if the selection state is still null, go to the selection source
        // and ask if it has state...
        if (selectionState == null && info != null) {
            ChartRenderingInfo cri = info.getOwner();
            if (cri != null) {
                RenderingSource rs = cri.getRenderingSource();
                selectionState = (CategoryDatasetSelectionState)
                        rs.getSelectionState(dataset);
            }
        }
        state.setSelectionState(selectionState);

        return state;
    }

    /**
     * Returns the range of values the renderer requires to display all the
     * items from the specified dataset.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The range (or <code>null</code> if the dataset is
     *         <code>null</code> or empty).
     */
    public Range findRangeBounds(CategoryDataset dataset) {
        return findRangeBounds(dataset, false);
    }

    /**
     * Returns the range of values the renderer requires to display all the
     * items from the specified dataset.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     * @param includeInterval  include the y-interval if the dataset has one.
     *
     * @return The range (<code>null</code> if the dataset is <code>null</code>
     *         or empty).
     *
     * @since 1.0.13
     */
    protected Range findRangeBounds(CategoryDataset dataset,
            boolean includeInterval) {
        if (dataset == null) {
            return null;
        }
        if (getDataBoundsIncludesVisibleSeriesOnly()) {
            List visibleSeriesKeys = new ArrayList();
            int seriesCount = dataset.getRowCount();
            for (int s = 0; s < seriesCount; s++) {
                if (isSeriesVisible(s)) {
                    visibleSeriesKeys.add(dataset.getRowKey(s));
                }
            }
            return DatasetUtilities.findRangeBounds(dataset,
                    visibleSeriesKeys, includeInterval);
        }
        else {
            return DatasetUtilities.findRangeBounds(dataset, includeInterval);
        }
    }

    /**
     * Returns the Java2D coordinate for the middle of the specified data item.
     *
     * @param rowKey  the row key.
     * @param columnKey  the column key.
     * @param dataset  the dataset.
     * @param axis  the axis.
     * @param area  the data area.
     * @param edge  the edge along which the axis lies.
     *
     * @return The Java2D coordinate for the middle of the item.
     *
     * @since 1.0.11
     */
    public double getItemMiddle(Comparable rowKey, Comparable columnKey,
            CategoryDataset dataset, CategoryAxis axis, Rectangle2D area,
            RectangleEdge edge) {
        return axis.getCategoryMiddle(columnKey, dataset.getColumnKeys(), area,
                edge);
    }

    /**
     * Draws a background for the data area.  The default implementation just
     * gets the plot to draw the background, but some renderers will override
     * this behaviour.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param dataArea  the data area.
     */
    public void drawBackground(Graphics2D g2,
                               CategoryPlot plot,
                               Rectangle2D dataArea) {

        plot.drawBackground(g2, dataArea);

    }

    /**
     * Draws an outline for the data area.  The default implementation just
     * gets the plot to draw the outline, but some renderers will override this
     * behaviour.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param dataArea  the data area.
     */
    public void drawOutline(Graphics2D g2,
                            CategoryPlot plot,
                            Rectangle2D dataArea) {

        plot.drawOutline(g2, dataArea);

    }

    /**
     * Draws a grid line against the domain axis.
     * <P>
     * Note that this default implementation assumes that the horizontal axis
     * is the domain axis. If this is not the case, you will need to override
     * this method.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param dataArea  the area for plotting data (not yet adjusted for any
     *                  3D effect).
     * @param value  the Java2D value at which the grid line should be drawn.
     * @param paint  the paint (<code>null</code> not permitted).
     * @param stroke  the stroke (<code>null</code> not permitted).
     *
     * @see #drawRangeGridline(Graphics2D, CategoryPlot, ValueAxis,
     *     Rectangle2D, double)
     *
     * @since 1.2.0
     */
    public void drawDomainLine(Graphics2D g2, CategoryPlot plot,
            Rectangle2D dataArea, double value, Paint paint, Stroke stroke) {

        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        Line2D line = null;
        PlotOrientation orientation = plot.getOrientation();

        if (orientation == PlotOrientation.HORIZONTAL) {
            line = new Line2D.Double(dataArea.getMinX(), value,
                    dataArea.getMaxX(), value);
        }
        else if (orientation == PlotOrientation.VERTICAL) {
            line = new Line2D.Double(value, dataArea.getMinY(), value,
                    dataArea.getMaxY());
        }

        g2.setPaint(paint);
        g2.setStroke(stroke);
        g2.draw(line);

    }

    /**
     * Draws a line perpendicular to the range axis.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param axis  the value axis.
     * @param dataArea  the area for plotting data (not yet adjusted for any 3D
     *                  effect).
     * @param value  the value at which the grid line should be drawn.
     * @param paint  the paint (<code>null</code> not permitted).
     * @param stroke  the stroke (<code>null</code> not permitted).
     *
     * @see #drawRangeGridline
     *
     * @since 1.0.13
     */
    public void drawRangeLine(Graphics2D g2, CategoryPlot plot, ValueAxis axis,
            Rectangle2D dataArea, double value, Paint paint, Stroke stroke) {

        Range range = axis.getRange();
        if (!range.contains(value)) {
            return;
        }

        PlotOrientation orientation = plot.getOrientation();
        Line2D line = null;
        double v = axis.valueToJava2D(value, dataArea, plot.getRangeAxisEdge());
        if (orientation == PlotOrientation.HORIZONTAL) {
            line = new Line2D.Double(v, dataArea.getMinY(), v,
                    dataArea.getMaxY());
        }
        else if (orientation == PlotOrientation.VERTICAL) {
            line = new Line2D.Double(dataArea.getMinX(), v,
                    dataArea.getMaxX(), v);
        }

        g2.setPaint(paint);
        g2.setStroke(stroke);
        g2.draw(line);

    }

    /**
     * Draws a marker for the domain axis.
     *
     * @param g2  the graphics device (not <code>null</code>).
     * @param plot  the plot (not <code>null</code>).
     * @param axis  the range axis (not <code>null</code>).
     * @param marker  the marker to be drawn (not <code>null</code>).
     * @param dataArea  the area inside the axes (not <code>null</code>).
     *
     * @see #drawRangeMarker(Graphics2D, CategoryPlot, ValueAxis, Marker,
     *     Rectangle2D)
     */
    public void drawDomainMarker(Graphics2D g2,
                                 CategoryPlot plot,
                                 CategoryAxis axis,
                                 CategoryMarker marker,
                                 Rectangle2D dataArea) {

        Comparable category = marker.getKey();
        CategoryDataset dataset = plot.getDataset(plot.getIndexOf(this));
        int columnIndex = dataset.getColumnIndex(category);
        if (columnIndex < 0) {
            return;
        }

        final Composite savedComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, marker.getAlpha()));

        PlotOrientation orientation = plot.getOrientation();
        Rectangle2D bounds = null;
        if (marker.getDrawAsLine()) {
            double v = axis.getCategoryMiddle(columnIndex,
                    dataset.getColumnCount(), dataArea,
                    plot.getDomainAxisEdge());
            Line2D line = null;
            if (orientation == PlotOrientation.HORIZONTAL) {
                line = new Line2D.Double(dataArea.getMinX(), v,
                        dataArea.getMaxX(), v);
            }
            else if (orientation == PlotOrientation.VERTICAL) {
                line = new Line2D.Double(v, dataArea.getMinY(), v,
                        dataArea.getMaxY());
            }
            g2.setPaint(marker.getPaint());
            g2.setStroke(marker.getStroke());
            g2.draw(line);
            bounds = line.getBounds2D();
        }
        else {
            double v0 = axis.getCategoryStart(columnIndex,
                    dataset.getColumnCount(), dataArea,
                    plot.getDomainAxisEdge());
            double v1 = axis.getCategoryEnd(columnIndex,
                    dataset.getColumnCount(), dataArea,
                    plot.getDomainAxisEdge());
            Rectangle2D area = null;
            if (orientation == PlotOrientation.HORIZONTAL) {
                area = new Rectangle2D.Double(dataArea.getMinX(), v0,
                        dataArea.getWidth(), (v1 - v0));
            }
            else if (orientation == PlotOrientation.VERTICAL) {
                area = new Rectangle2D.Double(v0, dataArea.getMinY(),
                        (v1 - v0), dataArea.getHeight());
            }
            g2.setPaint(marker.getPaint());
            g2.fill(area);
            bounds = area;
        }

        String label = marker.getLabel();
        RectangleAnchor anchor = marker.getLabelAnchor();
        if (label != null) {
            Font labelFont = marker.getLabelFont();
            g2.setFont(labelFont);
            g2.setPaint(marker.getLabelPaint());
            Point2D coordinates = calculateDomainMarkerTextAnchorPoint(
                    g2, orientation, dataArea, bounds, marker.getLabelOffset(),
                    marker.getLabelOffsetType(), anchor);
            TextUtilities.drawAlignedString(label, g2,
                    (float) coordinates.getX(), (float) coordinates.getY(),
                    marker.getLabelTextAnchor());
        }
        g2.setComposite(savedComposite);
    }

    /**
     * Draws a marker for the range axis.
     *
     * @param g2  the graphics device (not <code>null</code>).
     * @param plot  the plot (not <code>null</code>).
     * @param axis  the range axis (not <code>null</code>).
     * @param marker  the marker to be drawn (not <code>null</code>).
     * @param dataArea  the area inside the axes (not <code>null</code>).
     *
     * @see #drawDomainMarker(Graphics2D, CategoryPlot, CategoryAxis,
     *     CategoryMarker, Rectangle2D)
     */
    public void drawRangeMarker(Graphics2D g2,
                                CategoryPlot plot,
                                ValueAxis axis,
                                Marker marker,
                                Rectangle2D dataArea) {

        if (marker instanceof ValueMarker) {
            ValueMarker vm = (ValueMarker) marker;
            double value = vm.getValue();
            Range range = axis.getRange();

            if (!range.contains(value)) {
                return;
            }

            final Composite savedComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, marker.getAlpha()));

            PlotOrientation orientation = plot.getOrientation();
            double v = axis.valueToJava2D(value, dataArea,
                    plot.getRangeAxisEdge());
            Line2D line = null;
            if (orientation == PlotOrientation.HORIZONTAL) {
                line = new Line2D.Double(v, dataArea.getMinY(), v,
                        dataArea.getMaxY());
            }
            else if (orientation == PlotOrientation.VERTICAL) {
                line = new Line2D.Double(dataArea.getMinX(), v,
                        dataArea.getMaxX(), v);
            }

            g2.setPaint(marker.getPaint());
            g2.setStroke(marker.getStroke());
            g2.draw(line);

            String label = marker.getLabel();
            RectangleAnchor anchor = marker.getLabelAnchor();
            if (label != null) {
                Font labelFont = marker.getLabelFont();
                g2.setFont(labelFont);
                g2.setPaint(marker.getLabelPaint());
                Point2D coordinates = calculateRangeMarkerTextAnchorPoint(
                        g2, orientation, dataArea, line.getBounds2D(),
                        marker.getLabelOffset(), LengthAdjustmentType.EXPAND,
                        anchor);
                TextUtilities.drawAlignedString(label, g2,
                        (float) coordinates.getX(), (float) coordinates.getY(),
                        marker.getLabelTextAnchor());
            }
            g2.setComposite(savedComposite);
        }
        else if (marker instanceof IntervalMarker) {
            IntervalMarker im = (IntervalMarker) marker;
            double start = im.getStartValue();
            double end = im.getEndValue();
            Range range = axis.getRange();
            if (!(range.intersects(start, end))) {
                return;
            }

            final Composite savedComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, marker.getAlpha()));

            double start2d = axis.valueToJava2D(start, dataArea,
                    plot.getRangeAxisEdge());
            double end2d = axis.valueToJava2D(end, dataArea,
                    plot.getRangeAxisEdge());
            double low = Math.min(start2d, end2d);
            double high = Math.max(start2d, end2d);

            PlotOrientation orientation = plot.getOrientation();
            Rectangle2D rect = null;
            if (orientation == PlotOrientation.HORIZONTAL) {
                // clip left and right bounds to data area
                low = Math.max(low, dataArea.getMinX());
                high = Math.min(high, dataArea.getMaxX());
                rect = new Rectangle2D.Double(low,
                        dataArea.getMinY(), high - low,
                        dataArea.getHeight());
            }
            else if (orientation == PlotOrientation.VERTICAL) {
                // clip top and bottom bounds to data area
                low = Math.max(low, dataArea.getMinY());
                high = Math.min(high, dataArea.getMaxY());
                rect = new Rectangle2D.Double(dataArea.getMinX(),
                        low, dataArea.getWidth(),
                        high - low);
            }
            Paint p = marker.getPaint();
            if (p instanceof GradientPaint) {
                GradientPaint gp = (GradientPaint) p;
                GradientPaintTransformer t = im.getGradientPaintTransformer();
                if (t != null) {
                    gp = t.transform(gp, rect);
                }
                g2.setPaint(gp);
            }
            else {
                g2.setPaint(p);
            }
            g2.fill(rect);

            // now draw the outlines, if visible...
            if (im.getOutlinePaint() != null && im.getOutlineStroke() != null) {
                if (orientation == PlotOrientation.VERTICAL) {
                    Line2D line = new Line2D.Double();
                    double x0 = dataArea.getMinX();
                    double x1 = dataArea.getMaxX();
                    g2.setPaint(im.getOutlinePaint());
                    g2.setStroke(im.getOutlineStroke());
                    if (range.contains(start)) {
                        line.setLine(x0, start2d, x1, start2d);
                        g2.draw(line);
                    }
                    if (range.contains(end)) {
                        line.setLine(x0, end2d, x1, end2d);
                        g2.draw(line);
                    }
                }
                else { // PlotOrientation.HORIZONTAL
                    Line2D line = new Line2D.Double();
                    double y0 = dataArea.getMinY();
                    double y1 = dataArea.getMaxY();
                    g2.setPaint(im.getOutlinePaint());
                    g2.setStroke(im.getOutlineStroke());
                    if (range.contains(start)) {
                        line.setLine(start2d, y0, start2d, y1);
                        g2.draw(line);
                    }
                    if (range.contains(end)) {
                        line.setLine(end2d, y0, end2d, y1);
                        g2.draw(line);
                    }
                }
            }

            String label = marker.getLabel();
            RectangleAnchor anchor = marker.getLabelAnchor();
            if (label != null) {
                Font labelFont = marker.getLabelFont();
                g2.setFont(labelFont);
                g2.setPaint(marker.getLabelPaint());
                Point2D coordinates = calculateRangeMarkerTextAnchorPoint(
                        g2, orientation, dataArea, rect,
                        marker.getLabelOffset(), marker.getLabelOffsetType(),
                        anchor);
                TextUtilities.drawAlignedString(label, g2,
                        (float) coordinates.getX(), (float) coordinates.getY(),
                        marker.getLabelTextAnchor());
            }
            g2.setComposite(savedComposite);
        }
    }

    /**
     * Calculates the (x, y) coordinates for drawing the label for a marker on
     * the range axis.
     *
     * @param g2  the graphics device.
     * @param orientation  the plot orientation.
     * @param dataArea  the data area.
     * @param markerArea  the rectangle surrounding the marker.
     * @param markerOffset  the marker offset.
     * @param labelOffsetType  the label offset type.
     * @param anchor  the label anchor.
     *
     * @return The coordinates for drawing the marker label.
     */
    protected Point2D calculateDomainMarkerTextAnchorPoint(Graphics2D g2,
                                      PlotOrientation orientation,
                                      Rectangle2D dataArea,
                                      Rectangle2D markerArea,
                                      RectangleInsets markerOffset,
                                      LengthAdjustmentType labelOffsetType,
                                      RectangleAnchor anchor) {

        Rectangle2D anchorRect = null;
        if (orientation == PlotOrientation.HORIZONTAL) {
            anchorRect = markerOffset.createAdjustedRectangle(markerArea,
                    LengthAdjustmentType.CONTRACT, labelOffsetType);
        }
        else if (orientation == PlotOrientation.VERTICAL) {
            anchorRect = markerOffset.createAdjustedRectangle(markerArea,
                    labelOffsetType, LengthAdjustmentType.CONTRACT);
        }
        return RectangleAnchor.coordinates(anchorRect, anchor);

    }

    /**
     * Calculates the (x, y) coordinates for drawing a marker label.
     *
     * @param g2  the graphics device.
     * @param orientation  the plot orientation.
     * @param dataArea  the data area.
     * @param markerArea  the rectangle surrounding the marker.
     * @param markerOffset  the marker offset.
     * @param labelOffsetType  the label offset type.
     * @param anchor  the label anchor.
     *
     * @return The coordinates for drawing the marker label.
     */
    protected Point2D calculateRangeMarkerTextAnchorPoint(Graphics2D g2,
                                      PlotOrientation orientation,
                                      Rectangle2D dataArea,
                                      Rectangle2D markerArea,
                                      RectangleInsets markerOffset,
                                      LengthAdjustmentType labelOffsetType,
                                      RectangleAnchor anchor) {

        Rectangle2D anchorRect = null;
        if (orientation == PlotOrientation.HORIZONTAL) {
            anchorRect = markerOffset.createAdjustedRectangle(markerArea,
                    labelOffsetType, LengthAdjustmentType.CONTRACT);
        }
        else if (orientation == PlotOrientation.VERTICAL) {
            anchorRect = markerOffset.createAdjustedRectangle(markerArea,
                    LengthAdjustmentType.CONTRACT, labelOffsetType);
        }
        return RectangleAnchor.coordinates(anchorRect, anchor);

    }

    /**
     * Returns a legend item for a series.  This default implementation will
     * return <code>null</code> if {@link #isSeriesVisible(int)} or
     * {@link #isSeriesVisibleInLegend(int)} returns <code>false</code>.
     *
     * @param datasetIndex  the dataset index (zero-based).
     * @param series  the series index (zero-based).
     *
     * @return The legend item (possibly <code>null</code>).
     *
     * @see #getLegendItems()
     */
    public LegendItem getLegendItem(int datasetIndex, int series) {

        CategoryPlot p = getPlot();
        if (p == null) {
            return null;
        }

        // check that a legend item needs to be displayed...
        if (!isSeriesVisible(series) || !isSeriesVisibleInLegend(series)) {
            return null;
        }

        CategoryDataset dataset = p.getDataset(datasetIndex);
        String label = this.legendItemLabelGenerator.generateLabel(dataset,
                series);
        String description = label;
        String toolTipText = null;
        if (this.legendItemToolTipGenerator != null) {
            toolTipText = this.legendItemToolTipGenerator.generateLabel(
                    dataset, series);
        }
        String urlText = null;
        if (this.legendItemURLGenerator != null) {
            urlText = this.legendItemURLGenerator.generateLabel(dataset,
                    series);
        }
        Shape shape = lookupLegendShape(series);
        Paint paint = lookupSeriesPaint(series);
        Paint outlinePaint = lookupSeriesOutlinePaint(series);
        Stroke outlineStroke = lookupSeriesOutlineStroke(series);

        LegendItem item = new LegendItem(label, description, toolTipText,
                urlText, shape, paint, outlineStroke, outlinePaint);
        item.setLabelFont(lookupLegendTextFont(series));
        Paint labelPaint = lookupLegendTextPaint(series);
        if (labelPaint != null) {
            item.setLabelPaint(labelPaint);
        }
        item.setSeriesKey(dataset.getRowKey(series));
        item.setSeriesIndex(series);
        item.setDataset(dataset);
        item.setDatasetIndex(datasetIndex);
        return item;
    }

    /**
     * Tests this renderer for equality with another object.
     *
     * @param obj  the object.
     *
     * @return <code>true</code> or <code>false</code>.
     */
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AbstractCategoryItemRenderer)) {
            return false;
        }
        AbstractCategoryItemRenderer that = (AbstractCategoryItemRenderer) obj;

        if (!ObjectUtilities.equal(this.itemLabelGeneratorList,
                that.itemLabelGeneratorList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseItemLabelGenerator,
                that.baseItemLabelGenerator)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.toolTipGeneratorList,
                that.toolTipGeneratorList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseToolTipGenerator,
                that.baseToolTipGenerator)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.urlGeneratorList,
                that.urlGeneratorList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseURLGenerator,
                that.baseURLGenerator)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.legendItemLabelGenerator,
                that.legendItemLabelGenerator)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.legendItemToolTipGenerator,
                that.legendItemToolTipGenerator)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.legendItemURLGenerator,
                that.legendItemURLGenerator)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.backgroundAnnotations,
                that.backgroundAnnotations)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.foregroundAnnotations,
                that.foregroundAnnotations)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Returns a hash code for the renderer.
     *
     * @return The hash code.
     */
    public int hashCode() {
        int result = super.hashCode();
        return result;
    }

    /**
     * Returns the drawing supplier from the plot.
     *
     * @return The drawing supplier (possibly <code>null</code>).
     */
    public DrawingSupplier getDrawingSupplier() {
        DrawingSupplier result = null;
        CategoryPlot cp = getPlot();
        if (cp != null) {
            result = cp.getDrawingSupplier();
        }
        return result;
    }

    /**
     * Considers the current (x, y) coordinate and updates the crosshair point
     * if it meets the criteria (usually means the (x, y) coordinate is the
     * closest to the anchor point so far).
     *
     * @param crosshairState  the crosshair state (<code>null</code> permitted,
     *                        but the method does nothing in that case).
     * @param rowKey  the row key.
     * @param columnKey  the column key.
     * @param value  the data value.
     * @param datasetIndex  the dataset index.
     * @param transX  the x-value translated to Java2D space.
     * @param transY  the y-value translated to Java2D space.
     * @param orientation  the plot orientation (<code>null</code> not
     *                     permitted).
     *
     * @since 1.0.11
     */
    protected void updateCrosshairValues(CategoryCrosshairState crosshairState,
            Comparable rowKey, Comparable columnKey, double value,
            int datasetIndex,
            double transX, double transY, PlotOrientation orientation) {

        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }

        if (crosshairState != null) {
            if (this.plot.isRangeCrosshairLockedOnData()) {
                // both axes
                crosshairState.updateCrosshairPoint(rowKey, columnKey, value,
                        datasetIndex, transX, transY, orientation);
            }
            else {
                crosshairState.updateCrosshairX(rowKey, columnKey,
                        datasetIndex, transX, orientation);
            }
        }
    }

    /**
     * Draws an item label.
     *
     * @param g2  the graphics device.
     * @param orientation  the orientation.
     * @param dataset  the dataset.
     * @param row  the row.
     * @param column  the column.
     * @param selected  is the item selected?
     * @param x  the x coordinate (in Java2D space).
     * @param y  the y coordinate (in Java2D space).
     * @param negative  indicates a negative value (which affects the item
     *                  label position).
     *
     * @since 1.2.0
     */
    protected void drawItemLabel(Graphics2D g2, PlotOrientation orientation,
            CategoryDataset dataset, int row, int column, boolean selected,
            double x, double y, boolean negative) {

        CategoryItemLabelGenerator generator = getItemLabelGenerator(row,
                column, selected);
        if (generator != null) {
            Font labelFont = getItemLabelFont(row, column, selected);
            Paint paint = getItemLabelPaint(row, column, selected);
            g2.setFont(labelFont);
            g2.setPaint(paint);
            String label = generator.generateLabel(dataset, row, column);
            ItemLabelPosition position = null;
            if (!negative) {
                position = getPositiveItemLabelPosition(row, column, selected);
            }
            else {
                position = getNegativeItemLabelPosition(row, column, selected);
            }
            Point2D anchorPoint = calculateLabelAnchorPoint(
                    position.getItemLabelAnchor(), x, y, orientation);
            TextUtilities.drawRotatedString(label, g2,
                    (float) anchorPoint.getX(), (float) anchorPoint.getY(),
                    position.getTextAnchor(),
                    position.getAngle(), position.getRotationAnchor());
        }

    }

    /**
     * Draws all the annotations for the specified layer.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param layer  the layer.
     * @param info  the plot rendering info.
     *
     * @since 1.2.0
     */
    public void drawAnnotations(Graphics2D g2, Rectangle2D dataArea,
            CategoryAxis domainAxis, ValueAxis rangeAxis, Layer layer,
            PlotRenderingInfo info) {

        Iterator iterator = null;
        if (layer.equals(Layer.FOREGROUND)) {
            iterator = this.foregroundAnnotations.iterator();
        }
        else if (layer.equals(Layer.BACKGROUND)) {
            iterator = this.backgroundAnnotations.iterator();
        }
        else {
            // should not get here
            throw new RuntimeException("Unknown layer.");
        }
        while (iterator.hasNext()) {
            CategoryAnnotation annotation = (CategoryAnnotation) iterator.next();
            annotation.draw(g2, this.plot, dataArea, domainAxis, rangeAxis,
                    0, info);
        }

    }

    /**
     * Returns an independent copy of the renderer.  The <code>plot</code>
     * reference is shallow copied.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException  can be thrown if one of the objects
     *         belonging to the renderer does not support cloning (for example,
     *         an item label generator).
     */
    public Object clone() throws CloneNotSupportedException {

        AbstractCategoryItemRenderer clone
                = (AbstractCategoryItemRenderer) super.clone();


        if (this.itemLabelGeneratorList != null) {
            clone.itemLabelGeneratorList
                    = (ObjectList) this.itemLabelGeneratorList.clone();
        }

        if (this.baseItemLabelGenerator != null) {
            if (this.baseItemLabelGenerator instanceof PublicCloneable) {
                PublicCloneable pc
                        = (PublicCloneable) this.baseItemLabelGenerator;
                clone.baseItemLabelGenerator
                        = (CategoryItemLabelGenerator) pc.clone();
            }
            else {
                throw new CloneNotSupportedException(
                        "ItemLabelGenerator not cloneable.");
            }
        }

        if (this.toolTipGeneratorList != null) {
            clone.toolTipGeneratorList
                    = (ObjectList) this.toolTipGeneratorList.clone();
        }

        if (this.baseToolTipGenerator != null) {
            if (this.baseToolTipGenerator instanceof PublicCloneable) {
                PublicCloneable pc
                        = (PublicCloneable) this.baseToolTipGenerator;
                clone.baseToolTipGenerator
                        = (CategoryToolTipGenerator) pc.clone();
            }
            else {
                throw new CloneNotSupportedException(
                        "Base tool tip generator not cloneable.");
            }
        }

        if (this.urlGeneratorList != null) {
            clone.urlGeneratorList = (ObjectList) this.urlGeneratorList.clone();
        }

        if (this.baseURLGenerator != null) {
            if (this.baseURLGenerator instanceof PublicCloneable) {
                PublicCloneable pc = (PublicCloneable) this.baseURLGenerator;
                clone.baseURLGenerator = (CategoryURLGenerator) pc.clone();
            }
            else {
                throw new CloneNotSupportedException(
                        "Base item URL generator not cloneable.");
            }
        }

        if (this.legendItemLabelGenerator instanceof PublicCloneable) {
            clone.legendItemLabelGenerator = (CategorySeriesLabelGenerator)
                    ObjectUtilities.clone(this.legendItemLabelGenerator);
        }
        if (this.legendItemToolTipGenerator instanceof PublicCloneable) {
            clone.legendItemToolTipGenerator = (CategorySeriesLabelGenerator)
                    ObjectUtilities.clone(this.legendItemToolTipGenerator);
        }
        if (this.legendItemURLGenerator instanceof PublicCloneable) {
            clone.legendItemURLGenerator = (CategorySeriesLabelGenerator)
                    ObjectUtilities.clone(this.legendItemURLGenerator);
        }
        return clone;
    }

    /**
     * Returns the domain axis that is used for the specified dataset.
     *
     * @param plot  the plot (<code>null</code> not permitted).
     * @param dataset  the dataset (<code>null</code> not permitted).
     *
     * @return A domain axis.
     */
    protected CategoryAxis getDomainAxis(CategoryPlot plot, 
            CategoryDataset dataset) {
        int datasetIndex = plot.indexOf(dataset);
        return plot.getDomainAxisForDataset(datasetIndex);
    }

    /**
     * Returns a range axis for a plot.
     *
     * @param plot  the plot.
     * @param index  the axis index.
     *
     * @return A range axis.
     */
    protected ValueAxis getRangeAxis(CategoryPlot plot, int index) {
        ValueAxis result = plot.getRangeAxis(index);
        if (result == null) {
            result = plot.getRangeAxis();
        }
        return result;
    }

    /**
     * Returns a (possibly empty) collection of legend items for the series
     * that this renderer is responsible for drawing.
     *
     * @return The legend item collection (never <code>null</code>).
     *
     * @see #getLegendItem(int, int)
     */
    public LegendItemCollection getLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        if (this.plot == null) {
            return result;
        }
        int index = this.plot.getIndexOf(this);
        CategoryDataset dataset = this.plot.getDataset(index);
        if (dataset == null) {
            return result;
        }
        int seriesCount = dataset.getRowCount();
        if (plot.getRowRenderingOrder().equals(SortOrder.ASCENDING)) {
            for (int i = 0; i < seriesCount; i++) {
                if (isSeriesVisibleInLegend(i)) {
                    LegendItem item = getLegendItem(index, i);
                    if (item != null) {
                        result.add(item);
                    }
                }
            }
        }
        else {
            for (int i = seriesCount - 1; i >= 0; i--) {
                if (isSeriesVisibleInLegend(i)) {
                    LegendItem item = getLegendItem(index, i);
                    if (item != null) {
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Adds an entity with the specified hotspot.
     *
     * @param entities  the entity collection.
     * @param hotspot  the hotspot (<code>null</code> not permitted).
     * @param dataset  the dataset.
     * @param row  the row index.
     * @param column  the column index.
     * @param selected  is the item selected?
     *
     * @since 1.2.0
     */
    protected void addEntity(EntityCollection entities, Shape hotspot,
            CategoryDataset dataset, int row, int column, boolean selected) {

        if (hotspot == null) {
            throw new IllegalArgumentException("Null 'hotspot' argument.");
        }
        addEntity(entities, hotspot, dataset, row, column, selected, 0.0, 0.0);
    }

    /**
     * Adds an entity to the collection.
     *
     * @param entities  the entity collection being populated.
     * @param hotspot  the entity area (if <code>null</code> a default will be
     *              used).
     * @param dataset  the dataset.
     * @param row  the series.
     * @param column  the item.
     * @param selected  is the item selected?
     * @param entityX  the entity's center x-coordinate in user space (only
     *                 used if <code>area</code> is <code>null</code>).
     * @param entityY  the entity's center y-coordinate in user space (only
     *                 used if <code>area</code> is <code>null</code>).
     *
     * @since 1.2.0
     */
    protected void addEntity(EntityCollection entities, Shape hotspot,
            CategoryDataset dataset, int row, int column, boolean selected,
            double entityX, double entityY) {
        if (!getItemCreateEntity(row, column, selected)) {
            return;
        }
        Shape s = hotspot;
        if (hotspot == null) {
            double r = getDefaultEntityRadius();
            double w = r * 2;
            if (getPlot().getOrientation() == PlotOrientation.VERTICAL) {
                s = new Ellipse2D.Double(entityX - r, entityY - r, w, w);
            }
            else {
                s = new Ellipse2D.Double(entityY - r, entityX - r, w, w);
            }
        }
        String tip = null;
        CategoryToolTipGenerator generator = getToolTipGenerator(row, column,
                selected);
        if (generator != null) {
            tip = generator.generateToolTip(dataset, row, column);
        }
        String url = null;
        CategoryURLGenerator urlster = getURLGenerator(row, column, selected);
        if (urlster != null) {
            url = urlster.generateURL(dataset, row, column);
        }
        CategoryItemEntity entity = new CategoryItemEntity(s, tip, url,
                dataset, dataset.getRowKey(row), dataset.getColumnKey(column));
        entities.add(entity);
    }

        /**
     * Returns a shape that can be used for hit testing on a data item drawn
     * by the renderer.
     *
     * @param g2  the graphics device.
     * @param dataArea  the area within which the data is being rendered.
     * @param plot  the plot (can be used to obtain standard color
     *              information etc).
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset  the dataset.
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     * @param selected  is the item selected?
     *
     * @return A shape equal to the hot spot for a data item.
     */
    public Shape createHotSpotShape(Graphics2D g2, Rectangle2D dataArea,
            CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis,
            CategoryDataset dataset, int row, int column, boolean selected,
            CategoryItemRendererState state) {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Returns the rectangular bounds for the hot spot for an item drawn by
     * this renderer.  This is intended to provide a quick test for
     * eliminating data points before more accurate testing against the
     * shape returned by createHotSpotShape().
     *
     * @param g2
     * @param dataArea
     * @param plot
     * @param domainAxis
     * @param rangeAxis
     * @param dataset
     * @param row
     * @param column
     * @param selected
     * @param result
     * @return
     */
    public Rectangle2D createHotSpotBounds(Graphics2D g2, Rectangle2D dataArea,
            CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis,
            CategoryDataset dataset, int row, int column, boolean selected,
            CategoryItemRendererState state, Rectangle2D result) {
        if (result == null) {
            result = new Rectangle();
        }
        Comparable key = dataset.getColumnKey(column);
        Number y = dataset.getValue(row, column);
        if (y == null) {
            return null;
        }
        double xx = domainAxis.getCategoryMiddle(key,
                plot.getCategoriesForAxis(domainAxis),
                dataArea, plot.getDomainAxisEdge());
        double yy = rangeAxis.valueToJava2D(y.doubleValue(), dataArea,
                plot.getRangeAxisEdge());
        result.setRect(xx - 2, yy - 2, 4, 4);
        return result;
    }

    /**
     * Returns <code>true</code> if the specified point (xx, yy) in Java2D
     * space falls within the "hot spot" for the specified data item, and
     * <code>false</code> otherwise.
     *
     * @param xx
     * @param yy
     * @param g2
     * @param dataArea
     * @param plot
     * @param domainAxis
     * @param rangeAxis
     * @param dataset
     * @param row
     * @param column
     * @param selected
     *
     * @return
     *
     * @since 1.2.0
     */
    public boolean hitTest(double xx, double yy, Graphics2D g2,
            Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
            ValueAxis rangeAxis, CategoryDataset dataset, int row, int column,
            boolean selected, CategoryItemRendererState state) {
        Rectangle2D bounds = createHotSpotBounds(g2, dataArea, plot,
                domainAxis, rangeAxis, dataset, row, column, selected,
                state, null);
        if (bounds == null) {
            return false;
        }
        // FIXME:  if the following test passes, we should then do the more
        // expensive test against the hotSpotShape
        return bounds.contains(xx, yy);
    }
    
}

```

## source/org/jfree/chart/renderer/category/AreaRenderer.java

```
/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2009, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * -----------------
 * AreaRenderer.java
 * -----------------
 * (C) Copyright 2002-2009, by Jon Iles and Contributors.
 *
 * Original Author:  Jon Iles;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *                   Christian W. Zuckschwerdt;
 *
 * Changes:
 * --------
 * 21-May-2002 : Version 1, contributed by John Iles (DG);
 * 29-May-2002 : Now extends AbstractCategoryItemRenderer (DG);
 * 11-Jun-2002 : Updated Javadoc comments (DG);
 * 25-Jun-2002 : Removed unnecessary imports (DG);
 * 01-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 10-Oct-2002 : Added constructors and basic entity support (DG);
 * 24-Oct-2002 : Amendments for changes in CategoryDataset interface and
 *               CategoryToolTipGenerator interface (DG);
 * 05-Nov-2002 : Replaced references to CategoryDataset with TableDataset (DG);
 * 06-Nov-2002 : Renamed drawCategoryItem() --> drawItem() and now using axis
 *               for category spacing.  Renamed AreaCategoryItemRenderer
 *               --> AreaRenderer (DG);
 * 17-Jan-2003 : Moved plot classes into a separate package (DG);
 * 25-Mar-2003 : Implemented Serializable (DG);
 * 10-Apr-2003 : Changed CategoryDataset to KeyedValues2DDataset in
 *               drawItem() method (DG);
 * 12-May-2003 : Modified to take into account the plot orientation (DG);
 * 30-Jul-2003 : Modified entity constructor (CZ);
 * 13-Aug-2003 : Implemented Cloneable (DG);
 * 07-Oct-2003 : Added renderer state (DG);
 * 05-Nov-2004 : Modified drawItem() signature (DG);
 * 20-Apr-2005 : Apply tooltips and URLs to legend items (DG);
 * 09-Jun-2005 : Use addItemEntity() method from superclass (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 11-Oct-2006 : Fixed bug in equals() method (DG);
 * 30-Nov-2006 : Added checks for series visibility (DG);
 * 20-Apr-2007 : Updated getLegendItem() for renderer change (DG);
 * 17-May-2007 : Set datasetIndex and seriesIndex in getLegendItem() (DG);
 * 18-May-2007 : Set dataset and seriesKey for LegendItem (DG);
 * 20-Jun-2007 : Removed JCommon dependencies (DG);
 * 17-Jun-2008 : Apply legend shape, font and paint attributes (DG);
 * 26-Jun-2008 : Added crosshair support (DG);
 *
 */

package org.jfree.chart.renderer.category;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AreaRendererEndType;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.data.category.CategoryDataset;

/**
 * A category item renderer that draws area charts.  You can use this renderer
 * with the {@link CategoryPlot} class.  The example shown here is generated
 * by the <code>AreaChartDemo1.java</code> program included in the JFreeChart
 * Demo Collection:
 * <br><br>
 * <img src="../../../../../images/AreaRendererSample.png"
 * alt="AreaRendererSample.png" />
 */
public class AreaRenderer extends AbstractCategoryItemRenderer
        implements Cloneable, PublicCloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -4231878281385812757L;

    /** A flag that controls how the ends of the areas are drawn. */
    private AreaRendererEndType endType;

    /**
     * Creates a new renderer.
     */
    public AreaRenderer() {
        super();
        this.endType = AreaRendererEndType.TAPER;
        setBaseLegendShape(new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
    }

    /**
     * Returns a token that controls how the renderer draws the end points.
     * The default value is {@link AreaRendererEndType#TAPER}.
     *
     * @return The end type (never <code>null</code>).
     *
     * @see #setEndType
     */
    public AreaRendererEndType getEndType() {
        return this.endType;
    }

    /**
     * Sets a token that controls how the renderer draws the end points, and
     * sends a {@link RendererChangeEvent} to all registered listeners.
     *
     * @param type  the end type (<code>null</code> not permitted).
     *
     * @see #getEndType()
     */
    public void setEndType(AreaRendererEndType type) {
        if (type == null) {
            throw new IllegalArgumentException("Null 'type' argument.");
        }
        this.endType = type;
        fireChangeEvent();
    }

    /**
     * Returns a legend item for a series.
     *
     * @param datasetIndex  the dataset index (zero-based).
     * @param series  the series index (zero-based).
     *
     * @return The legend item.
     */
    public LegendItem getLegendItem(int datasetIndex, int series) {

        // if there is no plot, there is no dataset to access...
        CategoryPlot cp = getPlot();
        if (cp == null) {
            return null;
        }

        // check that a legend item needs to be displayed...
        if (!isSeriesVisible(series) || !isSeriesVisibleInLegend(series)) {
            return null;
        }

        CategoryDataset dataset = cp.getDataset(datasetIndex);
        String label = getLegendItemLabelGenerator().generateLabel(dataset,
                series);
        String description = label;
        String toolTipText = null;
        if (getLegendItemToolTipGenerator() != null) {
            toolTipText = getLegendItemToolTipGenerator().generateLabel(
                    dataset, series);
        }
        String urlText = null;
        if (getLegendItemURLGenerator() != null) {
            urlText = getLegendItemURLGenerator().generateLabel(dataset,
                    series);
        }
        Shape shape = lookupLegendShape(series);
        Paint paint = lookupSeriesPaint(series);
        Paint outlinePaint = lookupSeriesOutlinePaint(series);
        Stroke outlineStroke = lookupSeriesOutlineStroke(series);

        LegendItem result = new LegendItem(label, description, toolTipText,
                urlText, shape, paint, outlineStroke, outlinePaint);
        result.setLabelFont(lookupLegendTextFont(series));
        Paint labelPaint = lookupLegendTextPaint(series);
        if (labelPaint != null) {
            result.setLabelPaint(labelPaint);
        }
        result.setDataset(dataset);
        result.setDatasetIndex(datasetIndex);
        result.setSeriesKey(dataset.getRowKey(series));
        result.setSeriesIndex(series);
        return result;

    }

    /**
     * Draw a single data item.
     *
     * @param g2  the graphics device.
     * @param state  the renderer state.
     * @param dataArea  the data plot area.
     * @param plot  the plot.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset  the dataset.
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     * @param selected  is the item selected?
     * @param pass  the pass index.
     *
     * @since 1.2.0
     */
    public void drawItem(Graphics2D g2, CategoryItemRendererState state,
            Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
            ValueAxis rangeAxis, CategoryDataset dataset, int row, int column,
            boolean selected, int pass) {

        // do nothing if item is not visible or null
        if (!getItemVisible(row, column)) {
            return;
        }
        Number value = dataset.getValue(row, column);
        if (value == null) {
            return;
        }
        PlotOrientation orientation = plot.getOrientation();
        RectangleEdge axisEdge = plot.getDomainAxisEdge();
        int count = dataset.getColumnCount();
        float x0 = (float) domainAxis.getCategoryStart(column, count, dataArea,
                axisEdge);
        float x1 = (float) domainAxis.getCategoryMiddle(column, count,
                dataArea, axisEdge);
        float x2 = (float) domainAxis.getCategoryEnd(column, count, dataArea,
                axisEdge);

        x0 = Math.round(x0);
        x1 = Math.round(x1);
        x2 = Math.round(x2);

        if (this.endType == AreaRendererEndType.TRUNCATE) {
            if (column == 0) {
                x0 = x1;
            }
            else if (column == getColumnCount() - 1) {
                x2 = x1;
            }
        }

        double yy1 = value.doubleValue();

        double yy0 = 0.0;
        if (this.endType == AreaRendererEndType.LEVEL) {
            yy0 = yy1;
        }
        if (column > 0) {
            Number n0 = dataset.getValue(row, column - 1);
            if (n0 != null) {
                yy0 = (n0.doubleValue() + yy1) / 2.0;
            }
        }

        double yy2 = 0.0;
        if (column < dataset.getColumnCount() - 1) {
            Number n2 = dataset.getValue(row, column + 1);
            if (n2 != null) {
                yy2 = (n2.doubleValue() + yy1) / 2.0;
            }
        }
        else if (this.endType == AreaRendererEndType.LEVEL) {
            yy2 = yy1;
        }

        RectangleEdge edge = plot.getRangeAxisEdge();
        float y0 = (float) rangeAxis.valueToJava2D(yy0, dataArea, edge);
        float y1 = (float) rangeAxis.valueToJava2D(yy1, dataArea, edge);
        float y2 = (float) rangeAxis.valueToJava2D(yy2, dataArea, edge);
        float yz = (float) rangeAxis.valueToJava2D(0.0, dataArea, edge);
        double labelXX = x1;
        double labelYY = y1;
        g2.setPaint(getItemPaint(row, column, selected));
        g2.setStroke(getItemStroke(row, column, selected));

        GeneralPath area = new GeneralPath();

        if (orientation == PlotOrientation.VERTICAL) {
            area.moveTo(x0, yz);
            area.lineTo(x0, y0);
            area.lineTo(x1, y1);
            area.lineTo(x2, y2);
            area.lineTo(x2, yz);
        }
        else if (orientation == PlotOrientation.HORIZONTAL) {
            area.moveTo(yz, x0);
            area.lineTo(y0, x0);
            area.lineTo(y1, x1);
            area.lineTo(y2, x2);
            area.lineTo(yz, x2);
            double temp = labelXX;
            labelXX = labelYY;
            labelYY = temp;
        }
        area.closePath();

        g2.setPaint(getItemPaint(row, column, selected));
        g2.fill(area);

        // draw the item labels if there are any...
        if (isItemLabelVisible(row, column, selected)) {
            drawItemLabel(g2, orientation, dataset, row, column, selected, 
                    labelXX, labelYY, (value.doubleValue() < 0.0));
        }

        // submit the current data point as a crosshair candidate
        int datasetIndex = plot.indexOf(dataset);
        updateCrosshairValues(state.getCrosshairState(),
                dataset.getRowKey(row), dataset.getColumnKey(column), yy1,
                datasetIndex, x1, y1, orientation);

        // add an item entity, if this information is being collected
        EntityCollection entities = state.getEntityCollection();
        if (entities != null) {
            addEntity(entities, area, dataset, row, column, selected);
        }

    }

    /**
     * Tests this instance for equality with an arbitrary object.
     *
     * @param obj  the object to test (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AreaRenderer)) {
            return false;
        }
        AreaRenderer that = (AreaRenderer) obj;
        if (!this.endType.equals(that.endType)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Returns an independent copy of the renderer.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException  should not happen.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}

```


Explicit fixture recipe definitions (generation support, separate from production source):
Use the same construction/projection knowledge across all four approaches. Setup failures are not target observations. Use valid receiver/dependency graphs and node kinds.
```json
{
  "fixture_policy_id": "aom-beam-champ-chronology-fixtures-v11-development",
  "schema_version": 1,
  "scope": "Same fixture construction/projection knowledge for all four approaches; no execution feedback",
  "source_sha256": {
    "algorithms/java/SqaProbe.java": "b1a0d0aa614a2ccc407682b40988b7f5b78111fd7b81b0c58eeedbf42f59561a",
    "scripts/study/api854/fixture_policy.py": "9fcd0fb056b00c60eeec5fe783307f63cf2bf319d79e8d113b270c2445edf5ca"
  },
  "sources": {
    "algorithms/java/SqaProbe.java": "import java.lang.reflect.Array;\nimport java.lang.reflect.Constructor;\nimport java.lang.reflect.InvocationTargetException;\nimport java.lang.reflect.Method;\nimport java.lang.reflect.Modifier;\nimport java.nio.charset.StandardCharsets;\nimport java.nio.file.Files;\nimport java.nio.file.Paths;\nimport java.security.MessageDigest;\nimport java.security.NoSuchAlgorithmException;\nimport java.util.ArrayList;\nimport java.util.Arrays;\nimport java.util.Base64;\nimport java.util.Comparator;\nimport java.util.List;\n\n/** Fixed-revision observations for explicitly supported, deterministic Java APIs.\n * No buggy source, patch, or triggering test is used during input generation.\n * The same source is packaged with the generated JUnit suite.\n */\npublic final class SqaProbe {\n    private static final String[] STRINGS = {\n        \"\", \"0\", \"1\", \"-1\", \"null\", \"true\", \"false\", \"abc\", \"ABC\", \" \",\n        \"0x0\", \"0x1\", \"0xFFFFFFFF\", \"1.0\", \"1e3\", \"NaN\", \"Infinity\",\n        \"{}\", \"[]\", \"[1]\", \"{\\\"a\\\":1}\", \"a=b\", \"--help\", \"-x\", \"a,b\",\n        \"1970-01-01\", \"a\\\\nb\", \"a\\nb\", \"a\\tb\", \"\\u0e17\\u0e14\\u0e2a\\u0e2d\\u0e1a\"\n    };\n    private static final long[] NUMBERS = {0, 1, -1, 2, -2, 10, -10, 127, 128,\n        255, 256, 32767, -32768, Integer.MAX_VALUE, Integer.MIN_VALUE};\n\n    private SqaProbe() { }\n\n    /** Schema scaffolding carried in the suite; no benchmark test classes. */\n    public static class GenericFixture<T> { public T value; public T[] array; public List<T> items; }\n    public static class StringBinding extends GenericFixture<String> { }\n    public static class IntegerBinding extends GenericFixture<Integer> { }\n    public static class FixtureBean { public String value = \"fixture-value\"; }\n    public interface FixtureMock { String accept(String value); }\n\n    public static final String EXPLICIT_FIXTURES = \"beam-explicit-fixtures-v3-proposal\";\n    public static final String SCALAR_FIXTURES = \"beam-explicit-fixtures-v4-proposal\";\n    public static final String PILOT_FIXTURES = \"beam-explicit-fixtures-v5-proposal\";\n    public static final String BUFFER_FIXTURES = \"beam-explicit-fixtures-v6-buffer-proposal\";\n    public static final String FRACTION_FIELD_FIXTURES = \"aom-beam-fraction-field-v6-development\";\n    public static final String LANG_HELPER_FIXTURES = \"beam-explicit-fixtures-v9-buffer-lang-development\";\n    public static final String JOINT_FIXTURES = \"aom-beam-champ-joint-fixtures-v10-development\";\n    public static final String CHRONOLOGY_FIXTURES = \"aom-beam-champ-chronology-fixtures-v11-development\";\n    // Diagnostic scope only, serialized by observeChronology. Empty during all\n    // receiver setup/projection calls, so JDI cannot count setup as target entry.\n    public static String chronologyActiveCase = \"\";\n    private static final ThreadLocal<FixtureSession> FIXTURES = new ThreadLocal<FixtureSession>();\n    private static final ThreadLocal<Boolean> INVOKED = new ThreadLocal<Boolean>();\n\n    /** A setup failure is never an observation of an uncalled target method. */\n    private static final class FixtureFailure extends RuntimeException {\n        FixtureFailure(String message, Throwable cause) { super(message, cause); }\n    }\n\n    // Production factories only: no dataset test classes, patches or buggy results.\n    // Reflection keeps the helper compilable without project-specific dependencies.\n    private static Object call(Object receiver, String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Class<?> declaring = receiver instanceof Class ? (Class<?>)receiver : receiver.getClass();\n        while (declaring != null) {\n            try {\n                Method method = declaring.getDeclaredMethod(name, parameterTypes);\n                method.setAccessible(true);\n                return method.invoke(receiver instanceof Class ? null : receiver, values);\n            } catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n        }\n        throw new NoSuchMethodException(name);\n    }\n\n    private static Object construct(String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Constructor<?> ctor = Class.forName(name).getDeclaredConstructor(parameterTypes);\n        ctor.setAccessible(true);\n        return ctor.newInstance(values);\n    }\n\n    private static final class FixtureSession {\n        final String targetClass;\n        final String method;\n        final boolean pilot;\n        final boolean bufferSlices;\n        char[] outputBuffer;\n        final boolean fractionField;\n        final boolean langHelpers;\n        final boolean reviewed;\n        Object validationInput;\n        boolean constructing;\n        Object compiler, registry, scope, cfg, reverse, flow, closureNode, receiver;\n        org.w3c.dom.Element domRoot;\n        org.w3c.dom.Node domChild;\n        Object jdomRoot, jdomChild;\n        java.io.ByteArrayOutputStream archiveBytes;\n        Object mapper, parser, context, collectionType, collectionDeserializer;\n        Object mock, baseInvocation, actualInvocation;\n        Object chartDataset, chartPlot, chartAxis, cleanupScript, cleanupExterns;\n        int cleanupNodeIndex;\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void unusedClosure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> ac = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            cleanupExterns = call(compiler, \"parseTestCode\", new Class<?>[]{String.class}, \"\");\n            cleanupScript = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                \"var unused = 1; function fixture(x) { var local = \" + (a < 0 ? \"2\" : \"3\") + \"; return x; } fixture(1);\");\n            // Normalize traverses sibling roots and requires their common parent.\n            int block = Class.forName(\"com.google.javascript.rhino.Token\").getField(\"BLOCK\").getInt(null);\n            Object roots = construct(node.getName(), new Class<?>[]{int.class}, block);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupExterns);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupScript);\n            Object normalize = construct(\"com.google.javascript.jscomp.Normalize\", new Class<?>[]{ac, boolean.class}, compiler, false);\n            call(normalize, \"process\", new Class<?>[]{node, node}, cleanupExterns, cleanupScript);\n            Class<?> lifecycle = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler$LifeCycleStage\");\n            call(compiler, \"setLifeCycleStage\", new Class<?>[]{lifecycle}, Enum.valueOf((Class)lifecycle, \"NORMALIZED\"));\n            closureNode = cleanupScript;\n        }\n\n        void chart(double a) throws ReflectiveOperationException {\n            if (chartDataset != null) return;\n            Class<?> dataset = Class.forName(\"org.jfree.data.category.CategoryDataset\");\n            Class<?> axis = Class.forName(\"org.jfree.chart.axis.CategoryAxis\");\n            Class<?> valueAxis = Class.forName(\"org.jfree.chart.axis.ValueAxis\");\n            Class<?> renderer = Class.forName(\"org.jfree.chart.renderer.category.CategoryItemRenderer\");\n            chartDataset = construct(\"org.jfree.data.category.DefaultCategoryDataset\", new Class<?>[]{});\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, a < 0 ? -2.0 : 2.0, \"row-a\", \"column-a\");\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, 5.0, \"row-b\", \"column-a\");\n            chartAxis = construct(axis.getName(), new Class<?>[]{String.class}, \"Domain\");\n            Object rangeAxis = construct(\"org.jfree.chart.axis.NumberAxis\", new Class<?>[]{String.class}, \"Range\");\n            chartPlot = construct(\"org.jfree.chart.plot.CategoryPlot\", new Class<?>[]{dataset, axis, valueAxis, renderer},\n                chartDataset, chartAxis, rangeAxis, receiver);\n            java.awt.Graphics2D graphics = new java.awt.image.BufferedImage(16,16,java.awt.image.BufferedImage.TYPE_INT_RGB).createGraphics();\n            try {\n                call(receiver, \"initialise\", new Class<?>[]{java.awt.Graphics2D.class, java.awt.geom.Rectangle2D.class,\n                    chartPlot.getClass(), dataset, Class.forName(\"org.jfree.chart.plot.PlotRenderingInfo\")},\n                    graphics, new java.awt.geom.Rectangle2D.Double(0,0,16,16), chartPlot, chartDataset, null);\n            } finally { graphics.dispose(); }\n        }\n\n        Object beanWriter() throws ReflectiveOperationException {\n            Object objectMapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Object provider = call(objectMapper, \"getSerializerProvider\", new Class<?>[]{});\n            provider = call(provider, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.SerializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.databind.ser.SerializerFactory\")},\n                call(objectMapper, \"getSerializationConfig\", new Class<?>[]{}), call(objectMapper, \"getSerializerFactory\", new Class<?>[]{}));\n            Object serializer = call(provider, \"findValueSerializer\", new Class<?>[]{Class.class, Class.forName(\"com.fasterxml.jackson.databind.BeanProperty\")}, FixtureBean.class, null);\n            return Array.get(field(serializer, \"_props\"), 0);\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void jacksonCollection(double a) throws ReflectiveOperationException {\n            if (mapper != null) return;\n            mapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Class<?> feature = Class.forName(\"com.fasterxml.jackson.databind.DeserializationFeature\");\n            call(mapper, \"configure\", new Class<?>[]{feature, boolean.class}, Enum.valueOf((Class)feature, \"ACCEPT_SINGLE_VALUE_AS_ARRAY\"), true);\n            Object typeFactory = call(mapper, \"getTypeFactory\", new Class<?>[]{});\n            collectionType = call(typeFactory, \"constructCollectionType\", new Class<?>[]{Class.class, Class.class}, java.util.ArrayList.class, String.class);\n            Object factory = call(mapper, \"getFactory\", new Class<?>[]{});\n            String input = method.equals(\"handleNonArray\") ? a < 0 ? \"\\\"alpha\\\"\" : \"\\\"beta\\\"\"\n                : a < 0 ? \"[\\\"alpha\\\",\\\"beta\\\"]\" : \"[\\\"left\\\",\\\"right\\\"]\";\n            parser = call(factory, \"createParser\", new Class<?>[]{String.class}, input);\n            call(parser, \"nextToken\", new Class<?>[]{});\n            Object blueprint = call(mapper, \"getDeserializationContext\", new Class<?>[]{});\n            context = call(blueprint, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.DeserializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.core.JsonParser\"), Class.forName(\"com.fasterxml.jackson.databind.InjectableValues\")},\n                call(mapper, \"getDeserializationConfig\", new Class<?>[]{}), parser, null);\n            collectionDeserializer = call(context, \"findRootValueDeserializer\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.JavaType\")}, collectionType);\n        }\n\n        void mockito(double a) throws ReflectiveOperationException {\n            if (mock != null) return;\n            mock = call(Class.forName(\"org.mockito.Mockito\"), \"mock\", new Class<?>[]{Class.class}, FixtureMock.class);\n            call(mock, \"accept\", new Class<?>[]{String.class}, \"alpha\");\n            call(mock, \"accept\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            Object util = construct(\"org.mockito.internal.util.MockUtil\", new Class<?>[]{});\n            Object handler = call(util, \"getMockHandler\", new Class<?>[]{Object.class}, mock);\n            Object container = call(handler, \"getInvocationContainer\", new Class<?>[]{});\n            List<?> invocations = (List<?>)call(container, \"getInvocations\", new Class<?>[]{});\n            baseInvocation = invocations.get(0);\n            actualInvocation = invocations.get(1);\n        }\n\n        FixtureSession(String targetClass, String method, String policy) {\n            this.targetClass = targetClass;\n            this.method = method;\n            this.reviewed = JOINT_FIXTURES.equals(policy) || CHRONOLOGY_FIXTURES.equals(policy);\n            this.langHelpers = LANG_HELPER_FIXTURES.equals(policy) || reviewed;\n            this.bufferSlices = BUFFER_FIXTURES.equals(policy) || langHelpers;\n            this.fractionField = FRACTION_FIELD_FIXTURES.equals(policy) || langHelpers;\n            this.pilot = PILOT_FIXTURES.equals(policy) || bufferSlices || fractionField;\n        }\n\n        Object[] langHelperArguments(Class<?>[] types, double[] vector) {\n            if (!langHelpers || constructing || !targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    || types.length != 1) return null;\n            double a = vector[0];\n            if (method.equals(\"isAllZeros\") && types[0] == String.class)\n                return new Object[]{new String[]{null, \"\", \"0\", \"000\", \"001\", \"12\", \"00 0\", \"-0\"}[bucket(a, 8)]};\n            if (method.equals(\"validateArray\") && types[0] == Object.class) {\n                Object[] arrays = {null, new int[0], new int[]{0}, new int[]{-1, 0, 7}};\n                validationInput = arrays[bucket(a, arrays.length)];\n                return new Object[]{validationInput};\n            }\n            return null;\n        }\n\n        Object[] boundedBufferArguments(Class<?>[] types, double[] vector) {\n            if (!bufferSlices || constructing || types.length == 0) return null;\n            double a = vector[0], b = vector[1 % vector.length];\n            if (targetClass.equals(\"com.fasterxml.jackson.core.io.NumberInput\") && types[0] == char[].class) {\n                String text;\n                if (method.equals(\"parseLong\"))\n                    text = new String[]{\"1000000000\", \"1234567890123\", \"123456789012345678\"}[bucket(a, 3)];\n                else if (method.equals(\"parseInt\"))\n                    text = new String[]{\"0\", \"7\", \"12345\", \"999999999\"}[bucket(a, 4)];\n                else if (method.equals(\"inLongRange\"))\n                    text = new String[]{\"0\", \"9223372036854775807\", \"9223372036854775808\", \"9223372036854775809\"}[bucket(a, 4)];\n                else if (method.equals(\"parseBigDecimal\"))\n                    text = new String[]{\"0\", \"12.50\", \"-0.125\"}[bucket(a, 3)];\n                else return null;\n                if (types.length == 1) return new Object[]{text.toCharArray()};\n                char[] chars = (\"##\" + text + \"?\").toCharArray();\n                if (types.length == 4) return new Object[]{chars, 2, text.length(), b < 0};\n                return new Object[]{chars, 2, text.length()};\n            }\n            if (targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\") && method.equals(\"append\")\n                    && types.length == 3 && (types[0] == char[].class || types[0] == String.class)) {\n                String text = a < 0 ? \"xABCDy\" : \"p12345q\";\n                int offset = a < 0 ? 1 : 2;\n                int length = 1 + bucket(b, text.length() - offset - 1);\n                return new Object[]{types[0] == char[].class ? text.toCharArray() : text, offset, length};\n            }\n            if (targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\") && method.equals(\"read\")\n                    && types.length == 3 && types[0] == char[].class) {\n                outputBuffer = new char[8];\n                Arrays.fill(outputBuffer, '~');\n                int offset = a < 0 ? 1 : 2;\n                int length = 1 + bucket(b, outputBuffer.length - offset - 1);\n                return new Object[]{outputBuffer, offset, length};\n            }\n            return null;\n        }\n\n        Object option(String name, String text) throws ReflectiveOperationException {\n            Object option = construct(\"org.apache.commons.cli.Option\",\n                    new Class<?>[]{String.class, boolean.class, String.class}, name, true, \"fixture\");\n            call(option, \"setType\", new Class<?>[]{Object.class}, String.class);\n            call(option, \"addValue\", new Class<?>[]{String.class}, text);\n            return option;\n        }\n\n        Object archiveEntry(String name, long size) throws ReflectiveOperationException {\n            Object entry = construct(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\",\n                    new Class<?>[]{String.class}, name);\n            call(entry, \"setSize\", new Class<?>[]{long.class}, size);\n            call(entry, \"setTime\", new Class<?>[]{long.class}, 0L);\n            call(entry, \"setMode\", new Class<?>[]{long.class}, 0100644L);\n            return entry;\n        }\n\n        Object prepareReceiver(Object value, double a) throws ReflectiveOperationException {\n            if (!pilot) return value;\n            if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                call(value, \"addOption\", new Class<?>[]{Class.forName(\"org.apache.commons.cli.Option\")}, option(\"x\", a < 0 ? \"alpha\" : \"beta\"));\n                call(value, \"addArg\", new Class<?>[]{String.class}, \"positional\");\n            } else if (targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\")) {\n                char[] content = (a < 0 ? \"123\" : \"45.5\").toCharArray();\n                call(value, \"resetWithCopy\", new Class<?>[]{char[].class, int.class, int.class}, content, 0, content.length);\n            } else if (targetClass.equals(\"org.jsoup.nodes.Document\")) {\n                Object html = call(value, \"appendElement\", new Class<?>[]{String.class}, \"html\");\n                call(html, \"appendElement\", new Class<?>[]{String.class}, \"head\");\n                Object body = call(html, \"appendElement\", new Class<?>[]{String.class}, \"body\");\n                call(body, \"text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n                call(value, \"title\", new Class<?>[]{String.class}, \"Fixture\");\n            } else if (targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                call(value, \"putNextEntry\", new Class<?>[]{Class.forName(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\")},\n                        archiveEntry(\"fixture.txt\", method.equals(\"write\") ? 1 : 0));\n            } else if (targetClass.equals(\"org.joda.time.Partial\")) {\n                return call(value, \"with\", new Class<?>[]{Class.forName(\"org.joda.time.DateTimeFieldType\"), int.class},\n                        call(Class.forName(\"org.joda.time.DateTimeFieldType\"), \"hourOfDay\", new Class<?>[]{}), 10);\n            } else if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                receiver = value;\n                chart(a);\n            } else if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                // Real StAX input; getters start on a named leaf VALUE_STRING.\n                for (int i = 0; i < 8; i++) {\n                    Object token = call(value, \"nextToken\", new Class<?>[]{});\n                    if (token != null && token.toString().equals(\"VALUE_STRING\")) break;\n                }\n            }\n            return value;\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        Object nativeType(String name, boolean object) throws ReflectiveOperationException {\n            Class<?> nativeClass = Class.forName(\"com.google.javascript.rhino.jstype.JSTypeNative\");\n            Object key = Enum.valueOf((Class)nativeClass, name);\n            return call(registry, object ? \"getNativeObjectType\" : \"getNativeType\", new Class<?>[]{nativeClass}, key);\n        }\n\n        void closure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> scopeClass = Class.forName(\"com.google.javascript.jscomp.Scope\");\n            Class<?> abstractCompiler = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            registry = call(compiler, \"getTypeRegistry\", new Class<?>[]{});\n            String expression = a < 0 ? \"x + 1\" : \"x + 's'\";\n            if (method.contains(\"And\") || method.contains(\"ShortCircuit\")) expression = \"x && true\";\n            if (method.contains(\"Or\")) expression = \"x || false\";\n            if (method.equals(\"traverseArrayLiteral\")) expression = \"[x, 1]\";\n            if (method.equals(\"traverseObjectLiteral\")) expression = \"({p:x})\";\n            if (method.equals(\"traverseHook\")) expression = \"x ? 1 : 2\";\n            if (method.equals(\"traverseAssign\")) expression = \"x = 2\";\n            if (method.equals(\"traverseGetElem\")) expression = \"x['p']\";\n            if (method.equals(\"traverseGetProp\") || method.contains(\"Property\")) expression = \"x.p\";\n            if (method.equals(\"traverseName\") || method.equals(\"redeclareSimpleVar\")\n                    || method.equals(\"narrowScope\") || method.equals(\"updateScopeForTypeChange\")) expression = \"x\";\n            Object script = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                    \"function fixture(x) { return \" + expression + \"; }\");\n            Object function = call(script, \"getFirstChild\", new Class<?>[]{});\n            Object global = call(scopeClass, \"createGlobalScope\", new Class<?>[]{node}, script);\n            scope = construct(scopeClass.getName(), new Class<?>[]{scopeClass, node}, global, function);\n            Object astParameters = call(call(function, \"getFirstChild\", new Class<?>[]{}), \"getNext\", new Class<?>[]{});\n            Object name = call(astParameters, \"getFirstChild\", new Class<?>[]{});\n            call(scope, \"declare\", new Class<?>[]{String.class, node,\n                    Class.forName(\"com.google.javascript.rhino.jstype.JSType\"),\n                    Class.forName(\"com.google.javascript.jscomp.CompilerInput\")}, \"x\", name, nativeType(\"UNKNOWN_TYPE\", false), null);\n            Object body = call(function, \"getLastChild\", new Class<?>[]{});\n            Object returnNode = call(body, \"getFirstChild\", new Class<?>[]{});\n            closureNode = method.equals(\"traverseReturn\") || method.equals(\"branchedFlowThrough\")\n                    ? returnNode : call(returnNode, \"getFirstChild\", new Class<?>[]{});\n            if (method.equals(\"traverseObjectLiteral\"))\n                call(closureNode, \"setJSType\", new Class<?>[]{Class.forName(\"com.google.javascript.rhino.jstype.JSType\")}, nativeType(\"OBJECT_TYPE\", true));\n            Object analysis = construct(\"com.google.javascript.jscomp.ControlFlowAnalysis\",\n                    new Class<?>[]{abstractCompiler, boolean.class, boolean.class}, compiler, false, true);\n            call(analysis, \"process\", new Class<?>[]{node, node}, null, function);\n            cfg = call(analysis, \"getCfg\", new Class<?>[]{});\n            Object convention = call(compiler, \"getCodingConvention\", new Class<?>[]{});\n            reverse = construct(\"com.google.javascript.jscomp.type.SemanticReverseAbstractInterpreter\",\n                    new Class<?>[]{Class.forName(\"com.google.javascript.jscomp.CodingConvention\"), registry.getClass()}, convention, registry);\n            flow = call(Class.forName(\"com.google.javascript.jscomp.LinkedFlowScope\"), \"createEntryLattice\",\n                    new Class<?>[]{scopeClass}, scope);\n            call(flow, \"inferSlotType\", new Class<?>[]{String.class, Class.forName(\"com.google.javascript.rhino.jstype.JSType\")},\n                    \"x\", nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false));\n        }\n\n        void dom(double a) throws Exception {\n            if (domRoot != null) return;\n            javax.xml.parsers.DocumentBuilderFactory factory = pilot\n                ? javax.xml.parsers.DocumentBuilderFactory.newInstance(\"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl\", SqaProbe.class.getClassLoader())\n                : javax.xml.parsers.DocumentBuilderFactory.newInstance();\n            factory.setNamespaceAware(true);\n            org.w3c.dom.Document document = factory.newDocumentBuilder().newDocument();\n            domRoot = document.createElementNS(\"urn:sqa:root\", \"r:root\");\n            document.appendChild(domRoot);\n            domRoot.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:r\", \"urn:sqa:root\");\n            domRoot.setAttributeNS(\"http://www.w3.org/XML/1998/namespace\", \"xml:lang\", \"en\");\n            org.w3c.dom.Element element = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            domChild = element;\n            element.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:i\", \"urn:sqa:item\");\n            element.setAttribute(\"id\", a < 0 ? \"left\" : \"right\");\n            domChild.appendChild(document.createTextNode(a < 0 ? \"alpha\" : \"beta\"));\n            org.w3c.dom.Element grandchild = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            grandchild.appendChild(document.createTextNode(\"nested\"));\n            domChild.appendChild(grandchild);\n            org.w3c.dom.Element last = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            last.appendChild(document.createTextNode(\"nested-last\"));\n            domChild.appendChild(last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                domRoot.appendChild(document.createProcessingInstruction(\"fixture\", \"before\"));\n                domChild = document.createProcessingInstruction(\"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                domRoot.appendChild(document.createCDATASection(\"before\"));\n                domChild = document.createTextNode(a < 0 ? \"alpha\" : \"beta\");\n            }\n            domRoot.appendChild(domChild);\n        }\n\n        void jdom(double a) throws ReflectiveOperationException {\n            if (jdomRoot != null) return;\n            Class<?> element = Class.forName(\"org.jdom.Element\");\n            jdomRoot = construct(element.getName(), new Class<?>[]{String.class}, \"root\");\n            jdomChild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(jdomChild, \"setText\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            call(jdomChild, \"setAttribute\", new Class<?>[]{String.class, String.class}, \"id\", a < 0 ? \"left\" : \"right\");\n            Object grandchild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(grandchild, \"setText\", new Class<?>[]{String.class}, \"nested\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, grandchild);\n            Object last = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(last, \"setText\", new Class<?>[]{String.class}, \"nested-last\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                Object before = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                Object before = construct(\"org.jdom.CDATA\", new Class<?>[]{String.class}, \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.Text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            }\n            call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, jdomChild);\n        }\n\n        void configurePointer(Object pointer) throws ReflectiveOperationException {\n            Class<?> resolverClass = Class.forName(\"org.apache.commons.jxpath.ri.NamespaceResolver\");\n            Object resolver = construct(resolverClass.getName(), new Class<?>[]{resolverClass}, new Object[]{null});\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"i\", \"urn:sqa:item\");\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"r\", \"urn:sqa:root\");\n            call(resolver, \"setNamespaceContextPointer\", new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\")}, pointer);\n            call(pointer, \"setNamespaceResolver\", new Class<?>[]{resolverClass}, resolver);\n        }\n\n        Object argument(Class<?> type, double a, double b, double c, int depth) {\n            try {\n                if (depth > 2) throw new FixtureFailure(\"Fixture recursion limit: \" + type.getName(), null);\n                String name = type.getName();\n                if (reviewed && !constructing && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                        && method.equals(\"setMaxCodeLen\") && type == int.class)\n                    return a < -8 ? 0 : a < 0 ? 1 : a < 8 ? 4 : 8;\n                if (pilot) {\n                    if (targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                        java.lang.reflect.Field value = GenericFixture.class.getField(a < 0 ? \"value\" : \"items\");\n                        if (type == java.lang.reflect.TypeVariable.class) return GenericFixture.class.getTypeParameters()[0];\n                        if (type == java.lang.reflect.Field.class) return value;\n                        if (type == Class.class) return GenericFixture.class;\n                        if (type == java.lang.reflect.Type.class) {\n                            if (method.equals(\"getTypeInfoForArray\")) return a < 0 ? String[].class : Integer[].class;\n                            return a < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                        }\n                    }\n                    if (targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\")) {\n                        unusedClosure(a);\n                        if (type == boolean.class) return false; // No call-site optimizer prerequisite.\n                        if (name.equals(\"com.google.javascript.jscomp.AbstractCompiler\")) return compiler;\n                        if (name.equals(\"com.google.javascript.rhino.Node\")) {\n                            if (method.equals(\"process\")) return cleanupNodeIndex++ == 0 ? cleanupExterns : cleanupScript;\n                            if (method.equals(\"getFunctionArgList\")) {\n                                Object child = call(cleanupScript, \"getFirstChild\", new Class<?>[]{});\n                                while (child != null && !(Boolean)call(child, \"isFunction\", new Class<?>[]{}))\n                                    child = call(child, \"getNext\", new Class<?>[]{});\n                                if (child == null) throw new FixtureFailure(\"Missing parsed function\", null);\n                                return child;\n                            }\n                            return cleanupScript;\n                        }\n                    }\n                    if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                        chart(a);\n                        if (name.equals(\"org.jfree.data.category.CategoryDataset\")) return chartDataset;\n                        if (name.equals(\"org.jfree.chart.axis.CategoryAxis\")) return chartAxis;\n                        if (type == Comparable.class) return a < 0 ? \"row-a\" : \"column-a\";\n                        if (type == java.awt.geom.Rectangle2D.class) return new java.awt.geom.Rectangle2D.Double(0,0,16,16);\n                        if (name.equals(\"org.jfree.chart.util.RectangleEdge\")) return type.getField(\"BOTTOM\").get(null);\n                        if (type == int.class) return 0;\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\")) {\n                        if (name.equals(targetClass)) return beanWriter();\n                        if (name.equals(\"com.fasterxml.jackson.databind.util.NameTransformer\"))\n                            return call(type, \"simpleTransformer\", new Class<?>[]{String.class, String.class}, a < 0 ? \"left_\" : \"right_\", \"_suffix\");\n                        if (type == Object.class) return method.equals(\"get\") ? new FixtureBean() : a < 0 ? \"fixture-key\" : \"fixture-value\";\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\")) {\n                        jacksonCollection(a);\n                        if (name.equals(\"com.fasterxml.jackson.databind.JavaType\")) return collectionType;\n                        if (name.equals(\"com.fasterxml.jackson.core.JsonParser\")) return parser;\n                        if (name.equals(\"com.fasterxml.jackson.databind.DeserializationContext\")) return context;\n                        if (name.equals(\"com.fasterxml.jackson.databind.deser.ValueInstantiator\"))\n                            return call(collectionDeserializer, \"getValueInstantiator\", new Class<?>[]{});\n                        if (name.equals(\"com.fasterxml.jackson.databind.JsonDeserializer\"))\n                            return Class.forName(\"com.fasterxml.jackson.databind.deser.std.StringDeserializer\").getField(\"instance\").get(null);\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                        String xml = a < 0 ? \"<root><item>123</item><other>alpha</other></root>\" : \"<root><item>45</item><other>beta</other></root>\";\n                        if (type == int.class && constructing) return 0;\n                        if (name.equals(\"com.fasterxml.jackson.core.io.IOContext\"))\n                            return construct(name, new Class<?>[]{Class.forName(\"com.fasterxml.jackson.core.util.BufferRecycler\"), Object.class, boolean.class},\n                                construct(\"com.fasterxml.jackson.core.util.BufferRecycler\", new Class<?>[]{}), xml, false);\n                        if (name.equals(\"com.fasterxml.jackson.core.ObjectCodec\")) return construct(\"com.fasterxml.jackson.dataformat.xml.XmlMapper\", new Class<?>[]{});\n                        if (type == javax.xml.stream.XMLStreamReader.class) {\n                            javax.xml.stream.XMLStreamReader reader = javax.xml.stream.XMLInputFactory.newInstance().createXMLStreamReader(new java.io.StringReader(xml));\n                            while (reader.hasNext() && reader.getEventType() != javax.xml.stream.XMLStreamConstants.START_ELEMENT) reader.next();\n                            return reader;\n                        }\n                    }\n                    if (targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")) {\n                        mockito(a);\n                        if (name.equals(\"org.mockito.invocation.Invocation\")) return constructing ? baseInvocation : actualInvocation;\n                    }\n                    if (targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) {\n                        int number = 1 + bucket(a, 8);\n                        if (type == double.class) return (a < 0 ? -1 : 1) * number / 4.0;\n                        if (type == int.class) return number;\n                        if (type == long.class) return (long)number;\n                        if (type == java.math.BigInteger.class) return java.math.BigInteger.valueOf(number);\n                        if (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\"))\n                            return construct(name, new Class<?>[]{int.class, int.class}, number, 3);\n                    }\n                    if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                        if (type == String.class) return constructing ? \"fixture\" : a < -0.33 ? \"x\" : a < 0.33 ? \"missing\" : \"extra\";\n                        if (type == char.class) return a < 0 ? 'x' : 'z';\n                        if (name.equals(\"org.apache.commons.cli.Option\")) return option(\"extra\", a < 0 ? \"left\" : \"right\");\n                    }\n                    if (targetClass.equals(\"org.jsoup.nodes.Document\") && type == String.class)\n                        return constructing ? \"https://fixture.invalid/\" : method.equals(\"createElement\") ? a < 0 ? \"span\" : \"section\"\n                            : STRINGS[bucket(a, STRINGS.length)];\n                    if (targetClass.equals(\"org.joda.time.Partial\")) {\n                        if (type == int.class) return bucket(a, 24);\n                        if (name.equals(\"org.joda.time.DateTimeFieldType\"))\n                            return call(type, \"hourOfDay\", new Class<?>[]{});\n                    }\n                    if (name.equals(\"org.joda.time.DurationFieldType\")) return call(type, a < 0 ? \"hours\" : \"days\", new Class<?>[]{});\n                    if (name.equals(\"org.joda.time.DurationField\")) return call(Class.forName(\"org.joda.time.field.UnsupportedDurationField\"),\n                        \"getInstance\", new Class<?>[]{Class.forName(\"org.joda.time.DurationFieldType\")},\n                        call(Class.forName(\"org.joda.time.DurationFieldType\"), \"hours\", new Class<?>[]{}));\n                    if (name.equals(\"com.fasterxml.jackson.core.util.BufferRecycler\")) return construct(name, new Class<?>[]{});\n                    if (type == java.io.OutputStream.class && targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                        archiveBytes = new java.io.ByteArrayOutputStream();\n                        return archiveBytes;\n                    }\n                    if (name.equals(\"org.apache.commons.compress.archivers.ArchiveEntry\") || name.equals(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\"))\n                        return archiveEntry(a < 0 ? \"next-left.txt\" : \"next-right.txt\", 0);\n                    if (targetClass.equals(\"com.fasterxml.jackson.core.io.NumberInput\") && type == String.class)\n                        return new String[]{\"0\", \"1\", \"12\", \"2147483647\"}[bucket(a, 4)];\n                }\n                if (scalar(type)) {\n                    if (type == String.class && method.equals(\"getRelativePositionOfPI\")) return a < 0 ? \"fixture\" : \"other\";\n                    if (type == String.class && (method.equals(\"namespacePointer\") || method.equals(\"getNamespaceURI\")))\n                        return a < 0 ? \"r\" : \"i\";\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                }\n                if (type.isArray()) {\n                    Object array = Array.newInstance(type.getComponentType(), pilot && (targetClass.endsWith(\"NumberUtils\") || targetClass.endsWith(\"TypeInfoFactory\")) ? 1 + bucket(c, 4) : bucket(c, 5));\n                    for (int i = 0; i < Array.getLength(array); i++)\n                        Array.set(array, i, argument(type.getComponentType(), a, b, c, depth + 1));\n                    return array;\n                }\n                if (type == java.io.Reader.class && targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                    return new java.io.StringReader(bufferSlices ? (a < 0 ? \"A\\nBC\\nDE\" : \"12\\n345\\n\") : STRINGS[bucket(a, STRINGS.length)]);\n                if (name.startsWith(\"com.google.javascript.\")) {\n                    closure(a);\n                    if (name.endsWith(\".AbstractCompiler\")) return compiler;\n                    if (name.endsWith(\".ControlFlowGraph\")) return cfg;\n                    if (name.endsWith(\".ReverseAbstractInterpreter\")) return reverse;\n                    if (name.endsWith(\".Scope\")) return scope;\n                    if (name.endsWith(\".Scope$Var\")) return call(scope, \"getVar\", new Class<?>[]{String.class}, \"x\");\n                    if (name.endsWith(\".FlowScope\")) return flow;\n                    if (name.endsWith(\".Node\")) return closureNode;\n                    if (name.endsWith(\".JSType\")) return nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false);\n                    if (name.endsWith(\".ObjectType\")) return nativeType(\"OBJECT_TYPE\", true);\n                }\n                if (name.startsWith(\"org.w3c.dom.\")) {\n                    dom(a);\n                    if (type.isInstance(domChild)) return domChild;\n                    if (type.isInstance(domChild.getOwnerDocument())) return domChild.getOwnerDocument();\n                }\n                if (type == java.util.Locale.class) return java.util.Locale.ROOT;\n                if (name.equals(\"org.apache.commons.jxpath.ri.QName\"))\n                    return construct(name, new Class<?>[]{String.class}, method.equals(\"attributeIterator\") ? \"id\" : \"item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.compiler.NodeTest\"))\n                    return construct(\"org.apache.commons.jxpath.ri.compiler.NodeNameTest\",\n                            new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.QName\"), String.class},\n                            targetClass.contains(\".jdom.\")\n                                ? construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class}, \"item\")\n                                : construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class, String.class}, \"i\", \"item\"),\n                            targetClass.contains(\".jdom.\") ? null : \"urn:sqa:item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.model.NodePointer\")) {\n                    if (targetClass.contains(\".jdom.\")) {\n                        jdom(a);\n                        if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                            List<?> children = (List<?>)call(jdomChild, \"getContent\", new Class<?>[]{});\n                            Object anchor = children.get(a < 0 ? 0 : children.size() - 1);\n                            Object pointer = construct(targetClass, new Class<?>[]{type, Object.class}, receiver, anchor);\n                            configurePointer(pointer);\n                            return pointer;\n                        }\n                        Object pointer = construct(\"org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer\",\n                                new Class<?>[]{Object.class, java.util.Locale.class}, jdomRoot, java.util.Locale.ROOT);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    dom(a);\n                    if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                        org.w3c.dom.Node anchor = a < 0 ? domChild.getFirstChild() : domChild.getLastChild();\n                        Object pointer = construct(targetClass, new Class<?>[]{type, org.w3c.dom.Node.class}, receiver, anchor);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    Object pointer = construct(\"org.apache.commons.jxpath.ri.model.dom.DOMNodePointer\",\n                            new Class<?>[]{org.w3c.dom.Node.class, java.util.Locale.class}, domRoot, java.util.Locale.ROOT);\n                    configurePointer(pointer);\n                    return pointer;\n                }\n                if (type == Object.class && targetClass.contains(\".jdom.\")\n                        && (constructing || !method.equals(\"setValue\"))) { jdom(a); return jdomChild; }\n                if (type == java.util.Iterator.class) return new ArrayList<Object>().iterator();\n                if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n                    return new ArrayList<Object>();\n                if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n                if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n                if (type == Object.class || type == Number.class || type == java.util.Date.class)\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                throw new FixtureFailure(\"No explicit recipe: \" + name, null);\n            } catch (FixtureFailure failure) { throw failure; }\n            catch (Exception failure) { throw new FixtureFailure(\"Fixture recipe failed: \" + type.getName()\n                    + \":\" + failure.getClass().getName() + \":\" + failure.getMessage(), failure); }\n        }\n\n        String nodeSnapshot(org.w3c.dom.Node node, int depth) {\n            if (depth > 8) return \"depth-limit\";\n            StringBuilder out = new StringBuilder(\"node:\").append(node.getNodeType()).append(':')\n                    .append(quote(node.getNodeName())).append(':').append(quote(String.valueOf(node.getNodeValue())));\n            org.w3c.dom.NamedNodeMap attributes = node.getAttributes();\n            List<String> attrs = new ArrayList<String>();\n            if (attributes != null) for (int i = 0; i < attributes.getLength(); i++)\n                attrs.add(nodeSnapshot(attributes.item(i), depth + 1));\n            java.util.Collections.sort(attrs);\n            out.append(attrs.toString()).append('[');\n            org.w3c.dom.NodeList children = node.getChildNodes();\n            for (int i = 0; i < Math.min(256, children.getLength()); i++) out.append(nodeSnapshot(children.item(i), depth + 1));\n            return out.append(\"]children:\").append(children.getLength()).toString();\n        }\n\n        Object field(Object value, String name) throws ReflectiveOperationException {\n            for (Class<?> type = value.getClass(); type != null; type = type.getSuperclass()) {\n                try {\n                    java.lang.reflect.Field field = type.getDeclaredField(name);\n                    field.setAccessible(true);\n                    return field.get(value);\n                } catch (NoSuchFieldException missing) { }\n            }\n            throw new NoSuchFieldException(name);\n        }\n\n        String projection(Object result, int depth) throws ReflectiveOperationException {\n            if (depth > 8) throw new FixtureFailure(\"Oracle projection depth exceeded\", null);\n            if (result == null) return \"null\";\n            String name = result.getClass().getName();\n            if (fractionField && (name.equals(\"org.apache.commons.math3.fraction.BigFractionField\")\n                    || name.equals(\"org.apache.commons.math3.fraction.FractionField\")))\n                return \"fraction-field:runtime=\" + projection(call(result, \"getRuntimeClass\", new Class<?>[]{}), depth + 1)\n                    + \":zero=\" + projection(call(result, \"getZero\", new Class<?>[]{}), depth + 1)\n                    + \":one=\" + projection(call(result, \"getOne\", new Class<?>[]{}), depth + 1);\n            if (pilot && result instanceof java.lang.reflect.Type) return \"type:\" + nestedTestName(((java.lang.reflect.Type)result).getTypeName());\n            if (pilot && result instanceof Method) return \"method:\" + nestedTestName(((Method)result).toGenericString());\n            if (pilot && name.startsWith(\"com.google.gson.TypeInfo\"))\n                return \"type-info:\" + projection(call(result, \"getActualType\", new Class<?>[]{}), depth + 1);\n            if (pilot && name.equals(\"com.google.javascript.rhino.Node\")) return \"ast:\" + call(result, \"toStringTree\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.jxpath.ri.NamespaceResolver\"))\n                return \"namespaces:r=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"r\")\n                    + \":i=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"i\");\n            if (pilot && name.equals(\"org.jfree.data.Range\"))\n                return \"range:\" + call(result, \"getLowerBound\", new Class<?>[]{}) + ':' + call(result, \"getUpperBound\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItem\")) return \"legend:\" + call(result, \"getLabel\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItemCollection\")) {\n                StringBuilder out = new StringBuilder(\"legends[\");\n                int count = ((Number)call(result, \"getItemCount\", new Class<?>[]{})).intValue();\n                if (count > 256) throw new FixtureFailure(\"Legend limit exceeded\", null);\n                for (int i = 0; i < count; i++) out.append(projection(call(result, \"get\", new Class<?>[]{int.class}, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && name.startsWith(\"com.fasterxml.jackson.databind.type.\")) return \"java-type:\" + call(result, \"toCanonical\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.core.io.SerializedString\")) return \"serialized-name:\" + call(result, \"getValue\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return \"property:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + projection(call(result, \"getType\", new Class<?>[]{}), depth + 1);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")\n                    && Class.forName(\"org.mockito.invocation.Invocation\").isInstance(result))\n                return \"invocation:\" + projection(call(result, \"getMethod\", new Class<?>[]{}), depth + 1)\n                    + ':' + projection(call(result, \"getArguments\", new Class<?>[]{}), depth + 1)\n                    + \":verified=\" + call(result, \"isVerified\", new Class<?>[]{});\n            if (pilot && result.getClass().isArray()) {\n                int length = Array.getLength(result);\n                if (length > 100000) throw new FixtureFailure(\"Oracle array limit exceeded\", null);\n                StringBuilder out = new StringBuilder(\"array[\");\n                for (int i = 0; i < length; i++) out.append(projection(Array.get(result, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.jsoup.nodes.Document\") || name.equals(\"org.jsoup.nodes.Element\")))\n                return \"html:\" + call(result, \"outerHtml\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.cli.Option\"))\n                return \"option:\" + call(result, \"getOpt\", new Class<?>[]{}) + ':' + projection(call(result, \"getValues\", new Class<?>[]{}), depth + 1);\n            if (pilot && result instanceof java.util.Iterator) {\n                StringBuilder out = new StringBuilder(\"iterator[\");\n                java.util.Iterator<?> iterator = (java.util.Iterator<?>)result;\n                int count = 0;\n                while (iterator.hasNext()) {\n                    if (++count > 256) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                    out.append(projection(iterator.next(), depth + 1)).append(';');\n                }\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\")))\n                return \"fraction:\" + call(result, \"getNumerator\", new Class<?>[]{}) + '/' + call(result, \"getDenominator\", new Class<?>[]{});\n            if (pilot && name.startsWith(\"org.joda.time.\")) {\n                if (name.equals(\"org.joda.time.Partial\")) return \"partial:\" + call(result, \"toStringList\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationFieldType\").isInstance(result)) return \"duration-type:\" + call(result, \"getName\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationField\").isInstance(result))\n                    return \"duration:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + call(result, \"isSupported\", new Class<?>[]{});\n            }\n            if (result instanceof org.w3c.dom.Node) return nodeSnapshot((org.w3c.dom.Node)result, 0);\n            if (reviewed && name.equals(\"org.jdom.Attribute\"))\n                return \"jdom-attribute:name=\" + projection(call(result, \"getName\", new Class<?>[]{}), depth + 1)\n                    + \":namespace=\" + projection(call(result, \"getNamespaceURI\", new Class<?>[]{}), depth + 1)\n                    + \":value=\" + projection(call(result, \"getValue\", new Class<?>[]{}), depth + 1);\n            if (name.equals(\"org.jdom.Element\") || name.equals(\"org.jdom.ProcessingInstruction\")\n                    || name.equals(\"org.jdom.Text\") || name.equals(\"org.jdom.CDATA\")) {\n                Object writer = construct(\"org.jdom.output.XMLOutputter\", new Class<?>[]{});\n                return \"xml:\" + call(writer, \"outputString\", new Class<?>[]{result.getClass()}, result);\n            }\n            if (name.equals(\"org.apache.commons.jxpath.ri.QName\")) return \"qname:\" + result.toString();\n            if (name.startsWith(\"com.google.javascript.rhino.jstype.\")) return \"js-type:\" + result.toString();\n            if (name.equals(\"com.google.javascript.jscomp.LinkedFlowScope\")) {\n                Object slot = call(result, \"getSlot\", new Class<?>[]{String.class}, \"x\");\n                return \"flow:x=\" + (slot == null ? \"absent\" : projection(call(slot, \"getType\", new Class<?>[]{}), depth + 1));\n            }\n            if (name.endsWith(\"TypeInference$BooleanOutcomePair\"))\n                return \"boolean-pair:\" + field(result, \"toBooleanOutcomes\") + ':' + field(result, \"booleanValues\")\n                    + \":left=\" + projection(field(result, \"leftScope\"), depth + 1)\n                    + \":right=\" + projection(field(result, \"rightScope\"), depth + 1);\n            if (result instanceof List) {\n                StringBuilder out = new StringBuilder(\"list[\");\n                if (((List<?>)result).size() > 256) throw new FixtureFailure(\"Oracle collection limit exceeded\", null);\n                for (Object item : (List<?>)result) out.append(projection(item, depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (result instanceof java.util.Map) {\n                java.util.Map<?,?> map = (java.util.Map<?,?>)result;\n                if (map.size() > 256) throw new FixtureFailure(\"Oracle map limit exceeded\", null);\n                List<String> entries = new ArrayList<String>();\n                for (java.util.Map.Entry<?,?> entry : map.entrySet())\n                    entries.add(projection(entry.getKey(), depth + 1) + \"=\" + projection(entry.getValue(), depth + 1));\n                java.util.Collections.sort(entries);\n                return \"map:\" + entries.toString();\n            }\n            if (name.startsWith(\"org.apache.commons.jxpath.ri.model.\")) {\n                Class<?> pointer = Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\");\n                if (pointer.isInstance(result))\n                    return \"pointer:\" + projection(call(result, \"getImmediateNode\", new Class<?>[]{}), depth + 1);\n                if (Class.forName(\"org.apache.commons.jxpath.ri.model.NodeIterator\").isInstance(result)) {\n                    StringBuilder out = new StringBuilder(\"iterator[\");\n                    for (int i = 1; i <= 9; i++) {\n                        boolean present = (Boolean)call(result, \"setPosition\", new Class<?>[]{int.class}, i);\n                        if (!present) return out.append(']').toString();\n                        if (i == 9) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                        out.append(projection(call(result, \"getNodePointer\", new Class<?>[]{}), depth + 1)).append(';');\n                    }\n                }\n            }\n            String simple = value(result);\n            if (simple.startsWith(\"object-type:\")) throw new FixtureFailure(\"No structural oracle: \" + name, null);\n            return simple;\n        }\n\n        String state() throws ReflectiveOperationException {\n            if (reviewed && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                    && method.equals(\"setMaxCodeLen\")) {\n                int limit = ((Number)call(receiver, \"getMaxCodeLen\", new Class<?>[]{})).intValue();\n                String encoded = (String)call(receiver, \"metaphone\", new Class<?>[]{String.class}, \"architecture\");\n                return \"metaphone:maxCodeLen=\" + limit + \":encoded=\" + encoded\n                    + \":maxCodeLenAfterEncoding=\" + call(receiver, \"getMaxCodeLen\", new Class<?>[]{});\n            }\n            if (langHelpers && targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    && method.equals(\"validateArray\")) return \"validation-input:\" + value(validationInput);\n            if (pilot && targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\"))\n                return \"cleanup:\" + call(cleanupScript, \"toStringTree\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\"))\n                return \"chart:rows=\" + call(chartDataset, \"getRowCount\", new Class<?>[]{}) + \":columns=\" + call(chartDataset, \"getColumnCount\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return projection(receiver, 0) + \":setting=\" + projection(call(receiver, \"getInternalSetting\", new Class<?>[]{Object.class}, \"fixture-key\"), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\"))\n                return \"json-token:\" + call(parser, \"getCurrentToken\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\"))\n                return \"xml:closed=\" + call(receiver, \"isClosed\", new Class<?>[]{}) + \":token=\" + call(receiver, \"getCurrentToken\", new Class<?>[]{})\n                    + \":text=\" + projection(field(receiver, \"_currText\"), 0);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\"))\n                return projection(baseInvocation, 0) + \":candidate=\" + projection(actualInvocation, 0);\n            if (pilot && targetClass.equals(\"org.apache.commons.cli.CommandLine\"))\n                return \"cli:\" + projection(call(receiver, \"getOptions\", new Class<?>[]{}), 0) + ':' + projection(call(receiver, \"getArgs\", new Class<?>[]{}), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\"))\n                return \"text:\" + call(receiver, \"contentsAsString\", new Class<?>[]{}) + \":size=\" + call(receiver, \"size\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jsoup.nodes.Document\") && receiver != null) return projection(receiver, 0);\n            if (pilot && targetClass.endsWith(\"CpioArchiveOutputStream\")) return \"archive:\" + value(archiveBytes.toByteArray());\n            if (pilot && targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.Partial\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.field.UnsupportedDurationField\") && receiver != null) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.collections.map.Flat3Map\")) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                return \"reader:line=\" + call(receiver, \"getLineNumber\", new Class<?>[]{})\n                    + \":last=\" + call(receiver, \"readAgain\", new Class<?>[]{})\n                    + (bufferSlices && outputBuffer != null ? \":buffer=\" + value(outputBuffer) : \"\");\n            if (compiler != null) {\n                Object jsType = call(closureNode, \"getJSType\", new Class<?>[]{});\n                return \"ast:\" + call(closureNode, \"toStringTree\", new Class<?>[]{})\n                    + \":ast-type=\" + projection(jsType, 0) + ':' + projection(flow, 0);\n            }\n            if (domRoot != null) return nodeSnapshot(domRoot, 0) + \":child=\" + nodeSnapshot(domChild, 0)\n                    + \":attached=\" + (domChild.getParentNode() != null);\n            if (jdomRoot != null) return projection(jdomRoot, 0) + \":child=\" + projection(jdomChild, 0)\n                    + \":attached=\" + (call(jdomChild, \"getParent\", new Class<?>[]{}) != null);\n            return \"stateless-scalars\";\n        }\n    }\n\n    private static String quote(String value) {\n        StringBuilder out = new StringBuilder(\"\\\"\");\n        for (char c : value.toCharArray()) {\n            if (c == '\"' || c == '\\\\') out.append('\\\\').append(c);\n            else if (c < 32) out.append(String.format(\"\\\\u%04x\", (int)c));\n            else out.append(c);\n        }\n        return out.append('\"').toString();\n    }\n\n    private static String typeNames(Class<?>[] types) {\n        List<String> names = new ArrayList<String>();\n        for (Class<?> type : types) names.add(type.getName());\n        return String.join(\",\", names);\n    }\n\n    private static boolean scalar(Class<?> type) {\n        return type.isPrimitive() || type == String.class || type == Boolean.class\n            || type == Character.class || type == Byte.class || type == Short.class\n            || type == Integer.class || type == Long.class || type == Float.class\n            || type == Double.class || type.isEnum();\n    }\n\n    private static boolean supported(Class<?> type) {\n        return scalar(type) || (type.isArray() && scalar(type.getComponentType()));\n    }\n\n    private static boolean supportedParameters(Class<?>[] types) {\n        if (types.length > 6) return false;\n        for (Class<?> type : types) if (type == void.class) return false;\n        return true;\n    }\n\n    private static Class<?> type(String name) throws ClassNotFoundException {\n        if (name.equals(\"boolean\")) return boolean.class;\n        if (name.equals(\"byte\")) return byte.class;\n        if (name.equals(\"short\")) return short.class;\n        if (name.equals(\"int\")) return int.class;\n        if (name.equals(\"long\")) return long.class;\n        if (name.equals(\"float\")) return float.class;\n        if (name.equals(\"double\")) return double.class;\n        if (name.equals(\"char\")) return char.class;\n        return Class.forName(name);\n    }\n\n    private static Class<?>[] types(String names) throws ClassNotFoundException {\n        if (names.length() == 0) return new Class<?>[0];\n        String[] split = names.split(\",\", -1);\n        Class<?>[] result = new Class<?>[split.length];\n        for (int i = 0; i < split.length; i++) result[i] = type(split[i]);\n        return result;\n    }\n\n    private static int bucket(double coordinate, int size) {\n        double unit = Math.max(0, Math.min(1, (coordinate + 1) / 2));\n        return Math.min(size - 1, (int)(unit * size));\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c) {\n        return argument(type, a, b, c, 0);\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c, int depth) {\n        FixtureSession session = FIXTURES.get();\n        return session == null ? legacyArgument(type, a, b, c, depth) : session.argument(type, a, b, c, depth);\n    }\n\n    private static Object legacyArgument(Class<?> type, double a, double b, double c, int depth) {\n        if (depth > 2) return null;\n        if (type.isArray()) {\n            int length = bucket(c, 5);\n            Object array = Array.newInstance(type.getComponentType(), length);\n            for (int i = 0; i < length; i++) {\n                Array.set(array, i, argument(type.getComponentType(),\n                    Math.max(-1, Math.min(1, a + i * 0.17)), b, c, depth + 1));\n            }\n            return array;\n        }\n        if (!type.isPrimitive() && a < -0.96) return null;\n        if (type == String.class) {\n            int selection = bucket(a, STRINGS.length + 4);\n            if (selection < STRINGS.length) return STRINGS[selection];\n            int length = bucket(c, 33);\n            char character = \"0123456789abcdefXYZ +-_.\".charAt(bucket(b, 23));\n            char[] value = new char[length];\n            Arrays.fill(value, character);\n            return new String(value);\n        }\n        if (type == boolean.class || type == Boolean.class) return a >= 0;\n        if (type == char.class || type == Character.class) return (char)bucket(a, 128);\n        if (type.isEnum()) {\n            Object[] values = type.getEnumConstants();\n            return values.length == 0 ? null : values[bucket(a, values.length)];\n        }\n        long integer = b < 0 ? NUMBERS[bucket(a, NUMBERS.length)] : Math.round(a * 10000);\n        if (type == byte.class || type == Byte.class) return (byte)integer;\n        if (type == short.class || type == Short.class) return (short)integer;\n        if (type == int.class || type == Integer.class) return (int)integer;\n        if (type == long.class || type == Long.class) return integer;\n        double real = b < 0 ? integer : a * 1000;\n        if (type == float.class || type == Float.class) return (float)real;\n        if (type == double.class || type == Double.class) return real;\n        if (type == Number.class) return Double.valueOf(real);\n        if (type == Object.class) return b < 0 ? STRINGS[bucket(a, STRINGS.length)] : Long.valueOf(integer);\n        if (type == java.util.Date.class) return new java.util.Date(integer);\n        if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n            return new java.util.ArrayList<Object>();\n        if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n        if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers()) && !type.getName().startsWith(\"java.\")) {\n            Constructor<?>[] constructors = type.getDeclaredConstructors();\n            Arrays.sort(constructors, new Comparator<Constructor<?>>() {\n                public int compare(Constructor<?> left, Constructor<?> right) {\n                    int count = left.getParameterCount() - right.getParameterCount();\n                    return count != 0 ? count : left.toString().compareTo(right.toString());\n                }\n            });\n            for (Constructor<?> constructor : constructors) {\n                if (constructor.getParameterCount() > 3) continue;\n                try {\n                    constructor.setAccessible(true);\n                    Class<?>[] parameters = constructor.getParameterTypes();\n                    Object[] values = new Object[parameters.length];\n                    for (int i = 0; i < values.length; i++) values[i] = argument(parameters[i], a, b, c, depth + 1);\n                    return constructor.newInstance(values);\n                } catch (ReflectiveOperationException error) {\n                    // Failed fixture construction yields an explicit null boundary input.\n                } catch (RuntimeException error) {\n                    // Encapsulated/unconstructible fixture yields the same null boundary.\n                }\n            }\n        }\n        return null;\n    }\n\n    private static Object[] arguments(Class<?>[] types, double[] vector, int offset) {\n        FixtureSession explicitSession = FIXTURES.get();\n        if (explicitSession != null) {\n            Object[] helpers = explicitSession.langHelperArguments(types, vector);\n            if (helpers != null) return helpers;\n            Object[] bounded = explicitSession.boundedBufferArguments(types, vector);\n            if (bounded != null) return bounded;\n        }\n        Object[] values = new Object[types.length];\n        for (int i = 0; i < types.length; i++) {\n            int start = offset + 3 * i;\n            values[i] = argument(types[i], vector[start % vector.length],\n                vector[(start + 1) % vector.length], vector[(start + 2) % vector.length]);\n        }\n        FixtureSession session = FIXTURES.get();\n        if (session != null && session.pilot && !session.constructing) {\n            if (session.targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                java.lang.reflect.Type parent = vector[0] < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                try {\n                    if (session.method.equals(\"getActualType\")) {\n                        values[0] = GenericFixture.class.getField(\"items\").getGenericType();\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    } else if (session.method.equals(\"extractRealTypes\")) {\n                        values[0] = new java.lang.reflect.Type[]{GenericFixture.class.getField(\"value\").getGenericType()};\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    }\n                } catch (NoSuchFieldException failure) { throw new FixtureFailure(\"Generic schema field missing\", failure); }\n            }\n            if (session.targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\") && session.method.equals(\"getItemMiddle\")) {\n                values[0] = \"row-a\";\n                values[1] = \"column-a\";\n            }\n        }\n        return values;\n    }\n\n    private static String value(Object value) {\n        if (value == null) return \"null\";\n        Class<?> type = value.getClass();\n        if (type.isArray()) {\n            StringBuilder out = new StringBuilder(type.getName()).append('[');\n            int length = Array.getLength(value);\n            if (length > 100000) throw new IllegalStateException(\"SQA_HARNESS oversized outcome\");\n            for (int i = 0; i < length; i++) out.append(value(Array.get(value, i))).append(';');\n            return out.append(']').toString();\n        }\n        if (value instanceof Class) return \"class:\" + nestedTestName(((Class<?>)value).getName());\n        if (!scalar(type) && !(value instanceof Number)) return \"object-type:\" + type.getName();\n        String text = value instanceof Enum ? ((Enum<?>) value).name() : String.valueOf(value);\n        return type.getName() + \":\" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));\n    }\n\n    private static String nestedTestName(String text) {\n        // GeneratedStudyTest nests a copy of this helper, so probe-time\n        // \"SqaProbe$FixtureMock\" renders at test runtime as\n        // \"GeneratedStudyTest$SqaProbe$FixtureMock\". Oracles must compare\n        // the probe-time spelling in both phases; never edit old suites.\n        return text.replace(\"GeneratedStudyTest$SqaProbe$\", \"SqaProbe$\");\n    }\n\n    private static String snapshot(String observed) {\n        // JVM string constants are limited to 65,535 encoded bytes. Long exact\n        // observations use a deterministic digest rather than enormous literals.\n        if (observed.length() <= 16000) return observed;\n        byte[] bytes = observed.getBytes(StandardCharsets.UTF_8);\n        try {\n            byte[] digest = MessageDigest.getInstance(\"SHA-256\").digest(bytes);\n            StringBuilder hex = new StringBuilder();\n            for (byte item : digest) hex.append(String.format(\"%02x\", item & 255));\n            return \"sha256:\" + hex + \":bytes:\" + bytes.length;\n        } catch (NoSuchAlgorithmException error) {\n            throw new IllegalStateException(\"SQA_HARNESS SHA-256 unavailable\", error);\n        }\n    }\n\n    public static String observe(String className, String constructorTypes, String methodName,\n                                 String methodTypes, double[] vector) {\n        INVOKED.set(false);\n        if (vector.length == 0) throw new IllegalArgumentException(\"SQA_HARNESS empty vector\");\n        try {\n            Class<?> target = Class.forName(className);\n            Class<?>[] ctorTypes = types(constructorTypes);\n            Class<?>[] parameterTypes = types(methodTypes);\n            Object receiver = null;\n            Method method = null;\n            if (!methodName.equals(\"<init>\")) {\n                Class<?> declaring = target;\n                while (declaring != null) {\n                    try { method = declaring.getDeclaredMethod(methodName, parameterTypes); break; }\n                    catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n                }\n                if (method == null) throw new NoSuchMethodException(methodName);\n                method.setAccessible(true);\n            }\n            if (method == null || !Modifier.isStatic(method.getModifiers())) {\n                Constructor<?> ctor = target.getDeclaredConstructor(ctorTypes);\n                ctor.setAccessible(true);\n                FixtureSession session = FIXTURES.get();\n                if (session != null) session.constructing = true;\n                try {\n                    Object[] values = arguments(ctorTypes, vector, 0);\n                    if (method == null) INVOKED.set(true);\n                    receiver = ctor.newInstance(values);\n                    if (session != null) receiver = session.prepareReceiver(receiver, vector[0]);\n                    if (session != null) session.receiver = receiver;\n                    if (session != null && className.equals(\"org.apache.commons.collections.map.Flat3Map\")) {\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-a\", \"value-a\");\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-b\", \"value-b\");\n                    }\n                    if (session != null && className.startsWith(\"org.apache.commons.jxpath.ri.model.\")) session.configurePointer(receiver);\n                } catch (InvocationTargetException error) {\n                    if (session != null && method != null)\n                        throw new FixtureFailure(\"Receiver constructor failed before method invocation\", error.getCause());\n                    throw error;\n                } finally { if (session != null) session.constructing = false; }\n            }\n            if (method == null) {\n                if (FIXTURES.get() == null) return \"constructed:\" + target.getName();\n                try { return snapshot(\"constructed:\" + target.getName() + \":state=\" + FIXTURES.get().state()); }\n                catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Constructor state oracle failed\", failure); }\n            }\n            Object[] values = arguments(parameterTypes, vector, ctorTypes.length * 3);\n            INVOKED.set(true);\n            Object result = method.invoke(receiver, values);\n            if (FIXTURES.get() != null) {\n                FixtureSession session = FIXTURES.get();\n                try {\n                    return snapshot((method.getReturnType() == void.class ? \"void\" : \"value:\" + session.projection(result, 0))\n                            + \"|state=\" + session.state());\n                } catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Structural oracle failed\", failure); }\n            }\n            return method.getReturnType() == void.class ? \"void\" : snapshot(\"value:\" + value(result));\n        } catch (InvocationTargetException error) {\n            Throwable cause = error.getCause();\n            if (cause instanceof VirtualMachineError || cause instanceof LinkageError || cause instanceof ThreadDeath)\n                throw new IllegalStateException(\"SQA_HARNESS JVM failure\", cause);\n            FixtureSession session = FIXTURES.get();\n            if (session != null && session.langHelpers && session.targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    && session.method.equals(\"validateArray\")) {\n                try { return \"exception:\" + cause.getClass().getName() + \"|message=\" + value(cause.getMessage())\n                        + \"|state=\" + session.state(); }\n                catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Validation boundary oracle failed\", failure); }\n            }\n            return \"exception:\" + cause.getClass().getName();\n        } catch (ReflectiveOperationException error) {\n            throw new IllegalStateException(\"SQA_HARNESS reflection failure\", error);\n        } catch (LinkageError error) {\n            throw new IllegalStateException(\"SQA_HARNESS linkage failure\", error);\n        }\n    }\n\n    public static String observeWithPolicy(String className, String constructorTypes, String methodName,\n            String methodTypes, double[] vector, String policy) {\n        if (!EXPLICIT_FIXTURES.equals(policy) && !SCALAR_FIXTURES.equals(policy)\n                && !PILOT_FIXTURES.equals(policy) && !BUFFER_FIXTURES.equals(policy)\n                && !FRACTION_FIELD_FIXTURES.equals(policy) && !LANG_HELPER_FIXTURES.equals(policy) && !JOINT_FIXTURES.equals(policy)\n                && !CHRONOLOGY_FIXTURES.equals(policy))\n            throw new IllegalArgumentException(\"Unknown explicit fixture policy\");\n        FIXTURES.set(new FixtureSession(className, methodName, policy));\n        try {\n            if (CHRONOLOGY_FIXTURES.equals(policy) && className.equals(\"org.joda.time.Partial\")\n                    && chronologyIdentity(constructorTypes, methodName, methodTypes))\n                return observeChronology(constructorTypes, methodName, methodTypes, vector);\n            return observe(className, constructorTypes, methodName, methodTypes, vector);\n        }\n        finally { FIXTURES.remove(); }\n    }\n\n    private static boolean chronologyIdentity(String ctor, String method, String params) {\n        if (method.equals(\"<init>\") && params.isEmpty()) return ctor.equals(\"org.joda.time.Chronology\")\n            || ctor.equals(\"org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology\")\n            || ctor.equals(\"[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology\")\n            || ctor.equals(\"org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I\");\n        return ctor.isEmpty() && ((method.equals(\"getField\") && params.equals(\"int,org.joda.time.Chronology\"))\n            || (method.equals(\"withChronologyRetainFields\") && params.equals(\"org.joda.time.Chronology\")));\n    }\n\n    private static String partialState(Object partial) throws ReflectiveOperationException {\n        Object chrono = call(partial,\"getChronology\",new Class<?>[]{});\n        Object zone = call(chrono,\"getZone\",new Class<?>[]{});\n        int size = (Integer)call(partial,\"size\",new Class<?>[]{});\n        List<String> names = new ArrayList<String>();\n        List<Integer> values = new ArrayList<Integer>();\n        boolean named = true;\n        for (int i=0; i<size; i++) {\n            Object type = call(partial,\"getFieldType\",new Class<?>[]{int.class},i);\n            names.add((String)call(type,\"getName\",new Class<?>[]{}));\n            Integer indexed = (Integer)call(partial,\"getValue\",new Class<?>[]{int.class},i);\n            values.add(indexed);\n            named &= indexed.equals(call(partial,\"get\",types(\"org.joda.time.DateTimeFieldType\"),type));\n        }\n        return \"partial:\"+chrono.getClass().getName()+\":\"+call(zone,\"getID\",new Class<?>[]{})\n            +\":types=\"+names+\":values=\"+values+\":named=\"+named;\n    }\n\n    /** Six bounded production identities only, separate from all historical policies.\n     * Vector[0] chooses a declared case, not arbitrary legal-domain approval.\n     * Reflection enters the exact protected/internal target on real final Partial.\n     */\n    private static synchronized String observeChronology(String ctor, String method, String params, double[] vector) {\n        INVOKED.set(false);\n        chronologyActiveCase = \"\";\n        try {\n            if (vector.length==0 || !Double.isFinite(vector[0]))\n                throw new IllegalArgumentException(\"Chronology needs a finite vector\");\n            System.setProperty(\"org.joda.time.DateTimeZone.Provider\",\"org.joda.time.tz.UTCProvider\");\n            java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone(\"UTC\"));\n            Class<?> partial = Class.forName(\"org.joda.time.Partial\");\n            Class<?> chronoType = Class.forName(\"org.joda.time.Chronology\");\n            Class<?> fieldType = Class.forName(\"org.joda.time.DateTimeFieldType\");\n            Class<?> zoneType = Class.forName(\"org.joda.time.DateTimeZone\");\n            Object provider = Class.forName(\"org.joda.time.tz.UTCProvider\").getDeclaredConstructor().newInstance();\n            call(zoneType,\"setProvider\",types(\"org.joda.time.tz.Provider\"),provider);\n            Object utc = zoneType.getField(\"UTC\").get(null);\n            call(zoneType,\"setDefault\",new Class<?>[]{zoneType},utc);\n            Object offset = call(zoneType,\"forOffsetHours\",new Class<?>[]{int.class},7);\n            Object iso = call(Class.forName(\"org.joda.time.chrono.ISOChronology\"),\"getInstance\",new Class<?>[]{zoneType},utc);\n            Object isoOffset = call(Class.forName(\"org.joda.time.chrono.ISOChronology\"),\"getInstance\",new Class<?>[]{zoneType},offset);\n            Object buddhist = call(Class.forName(\"org.joda.time.chrono.BuddhistChronology\"),\"getInstance\",new Class<?>[]{zoneType},offset);\n            Object year = call(fieldType,\"year\",new Class<?>[]{});\n            Object month = call(fieldType,\"monthOfYear\",new Class<?>[]{});\n            Object day = call(fieldType,\"dayOfMonth\",new Class<?>[]{});\n            Object hour = call(fieldType,\"hourOfDay\",new Class<?>[]{});\n            Object era = call(fieldType,\"era\",new Class<?>[]{});\n            // All three bounded cases occupy intervals within the generators'\n            // shared [-1,1] domain (including CMA-ES/FSCS-ART proposals).\n            int bucket = Math.min(2, (int)Math.floor((Math.max(-1.0,Math.min(1.0,vector[0]))+1.0)*1.5));\n            String caseName;\n            Object receiver = null, result = null;\n            Object[] args;\n            Object inputTypes = null; int[] inputValues = null;\n            String before = null;\n            Constructor<?> constructor = null;\n            Method targetMethod = null;\n            if (method.equals(\"<init>\")) {\n                constructor = partial.getDeclaredConstructor(types(ctor));\n                constructor.setAccessible(true);\n                if (ctor.equals(\"org.joda.time.Chronology\")) {\n                    caseName = bucket==0 ? \"empty_iso_offset\" : \"empty_null\";\n                    args = new Object[]{bucket==0 ? isoOffset : null};\n                } else if (ctor.equals(\"org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology\")) {\n                    caseName = bucket==0 ? \"single_hour_iso\" : \"single_invalid_hour\";\n                    args = new Object[]{hour,bucket==0 ? 10 : 24,isoOffset};\n                } else {\n                    boolean internal = ctor.startsWith(\"org.joda.time.Chronology,\");\n                    caseName = internal ? \"internal_iso\" : bucket==0 ? \"arrays_leap_iso\"\n                        : bucket==1 ? \"arrays_invalid_date\" : \"arrays_bad_order\";\n                    inputTypes = Array.newInstance(fieldType,3);\n                    Object[] chosen = !internal && bucket==2 ? new Object[]{year,day,era} : new Object[]{year,month,day};\n                    for (int i=0;i<3;i++) Array.set(inputTypes,i,chosen[i]);\n                    inputValues = !internal && bucket==2 ? new int[]{1,1,1} : new int[]{2024,2,!internal && bucket==1 ? 30 : 29};\n                    if (internal) {\n                        Object validated = partial.getDeclaredConstructor(types(\"[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology\"))\n                            .newInstance(inputTypes,inputValues,iso);\n                        inputTypes = call(validated,\"getFieldTypes\",new Class<?>[]{});\n                        inputValues = (int[])call(validated,\"getValues\",new Class<?>[]{});\n                        args = new Object[]{iso,inputTypes,inputValues};\n                    } else args = new Object[]{inputTypes,inputValues,isoOffset};\n                }\n            } else {\n                receiver = partial.getDeclaredConstructor().newInstance();\n                boolean field = method.equals(\"getField\");\n                receiver = call(receiver,\"with\",new Class<?>[]{fieldType,int.class},field ? year : hour,field ? 2024 : 10);\n                if (!field && bucket==2) receiver = call(receiver,\"withChronologyRetainFields\",new Class<?>[]{chronoType},buddhist);\n                before = partialState(receiver);\n                String expected = \"partial:org.joda.time.chrono.\"+(!field && bucket==2 ? \"BuddhistChronology\" : \"ISOChronology\")\n                    +\":UTC:types=[\"+(field ? \"year\" : \"hourOfDay\")+\"]:values=[\"+(field ? 2024 : 10)+\"]:named=true\";\n                if (!before.equals(expected)) throw new IllegalStateException(\"Default receiver seed differs\");\n                targetMethod = partial.getDeclaredMethod(method,types(params));\n                targetMethod.setAccessible(true);\n                if (field) {\n                    caseName = bucket==0 ? \"getfield_buddhist\" : \"getfield_bad_index\";\n                    args = new Object[]{bucket==0 ? 0 : 1,buddhist};\n                } else {\n                    caseName = bucket==0 ? \"withchrono_buddhist\" : bucket==1 ? \"withchrono_same\" : \"withchrono_null\";\n                    args = new Object[]{bucket==0 ? buddhist : bucket==1 ? isoOffset : null};\n                }\n            }\n            Throwable targetException = null;\n            chronologyActiveCase = caseName;\n            INVOKED.set(true);\n            try { result = constructor!=null ? constructor.newInstance(args) : targetMethod.invoke(receiver,args); }\n            catch (InvocationTargetException failure) { targetException = failure.getCause(); }\n            finally { chronologyActiveCase = \"\"; }\n            if (targetException!=null) {\n                if (targetException instanceof VirtualMachineError || targetException instanceof LinkageError || targetException instanceof ThreadDeath)\n                    throw new IllegalStateException(\"SQA_HARNESS JVM failure\",targetException);\n                String out = \"exception:\"+targetException.getClass().getName();\n                if (receiver!=null) out += \"|receiver=\"+partialState(receiver)+\":unchanged=\"+before.equals(partialState(receiver));\n                return out;\n            }\n            if (method.equals(\"getField\")) return \"field:\"+call(result,\"getName\",new Class<?>[]{})\n                +\":epoch=\"+call(result,\"get\",new Class<?>[]{long.class},0L)\n                +\":supplied-identity=\"+(result==call(buddhist,\"year\",new Class<?>[]{}))\n                +\":type-year=\"+(call(result,\"getType\",new Class<?>[]{})==year)\n                +\"|receiver=\"+partialState(receiver)+\":unchanged=\"+before.equals(partialState(receiver));\n            String out = partialState(result);\n            if (method.equals(\"withChronologyRetainFields\"))\n                return out+\":same=\"+(result==receiver)+\"|receiver=\"+partialState(receiver)+\":unchanged=\"+before.equals(partialState(receiver));\n            if (caseName.equals(\"arrays_leap_iso\")) {\n                Array.set(inputTypes,0,hour); inputValues[0]=1900;\n                boolean inputCopy = out.equals(partialState(result));\n                Object getterTypes = call(result,\"getFieldTypes\",new Class<?>[]{});\n                int[] getterValues = (int[])call(result,\"getValues\",new Class<?>[]{});\n                Array.set(getterTypes,0,hour); getterValues[0]=1900;\n                out += \":input-copy=\"+inputCopy+\":output-copy=\"+out.equals(partialState(result));\n            }\n            return out;\n        } catch (ReflectiveOperationException | RuntimeException failure) {\n            throw new FixtureFailure(\"Chronology setup/projection failed\",failure);\n        } finally { chronologyActiveCase = \"\"; }\n    }\n\n    public static boolean targetInvoked() { return Boolean.TRUE.equals(INVOKED.get()); }\n\n    private static String descriptor(String className, String ctor, String method, String params, int count) {\n        return \"{\\\"class\\\":\" + quote(className) + \",\\\"constructor_types\\\":\" + quote(ctor)\n            + \",\\\"method\\\":\" + quote(method) + \",\\\"parameter_types\\\":\" + quote(params)\n            + \",\\\"dimensions\\\":\" + Math.max(3, count * 3) + \"}\";\n    }\n\n    private static void discover(String[] classes, List<String> fixtureClasses) {\n        List<String> targets = new ArrayList<String>();\n        List<String> errors = new ArrayList<String>();\n        for (String className : classes) {\n            try {\n                Class<?> target = Class.forName(className, false, SqaProbe.class.getClassLoader());\n                Class<?> receiverType = target;\n                if (Modifier.isAbstract(target.getModifiers())) {\n                    for (String name : fixtureClasses) {\n                        try {\n                            Class<?> candidate = Class.forName(name, false, SqaProbe.class.getClassLoader());\n                            if (!Modifier.isAbstract(candidate.getModifiers()) && target.isAssignableFrom(candidate)\n                                    && candidate.getDeclaredConstructors().length > 0) {\n                                receiverType = candidate;\n                                break;\n                            }\n                        } catch (ClassNotFoundException ignored) { } catch (LinkageError ignored) { }\n                    }\n                }\n                List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();\n                if (!Modifier.isAbstract(receiverType.getModifiers()) && !receiverType.isEnum()) {\n                    Constructor<?>[] all = receiverType.getDeclaredConstructors();\n                    Arrays.sort(all, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.toString().compareTo(b.toString()); }\n                    });\n                    for (Constructor<?> ctor : all) {\n                        if (supportedParameters(ctor.getParameterTypes())) constructors.add(ctor);\n                    }\n                    // Select a constructor before generating inputs; prefer the simplest fixture.\n                    java.util.Collections.sort(constructors, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.getParameterCount() - b.getParameterCount(); }\n                    });\n                }\n                Method[] methods = target.getDeclaredMethods();\n                Arrays.sort(methods, new Comparator<Method>() {\n                    public int compare(Method a, Method b) { return a.toString().compareTo(b.toString()); }\n                });\n                for (Method method : methods) {\n                    if (method.isSynthetic() || method.getName().equals(\"main\")\n                        || method.isBridge() || !supportedParameters(method.getParameterTypes())\n                        ) continue;\n                    if (Modifier.isStatic(method.getModifiers())) {\n                        targets.add(descriptor(className, \"\", method.getName(),\n                            typeNames(method.getParameterTypes()), method.getParameterCount()));\n                    } else if (!constructors.isEmpty()) {\n                        Constructor<?> ctor = constructors.get(0);\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), method.getName(),\n                            typeNames(method.getParameterTypes()), ctor.getParameterCount() + method.getParameterCount()));\n                    }\n                }\n                for (Constructor<?> ctor : constructors) {\n                    if (ctor.getParameterCount() > 0)\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), \"<init>\", \"\", ctor.getParameterCount()));\n                }\n            } catch (Throwable error) {\n                if (error instanceof VirtualMachineError || error instanceof ThreadDeath) throw (Error)error;\n                errors.add(quote(className + \":\" + error.getClass().getName()));\n            }\n        }\n        System.out.println(\"{\\\"targets\\\":[\" + String.join(\",\", targets) + \"],\\\"errors\\\":[\" + String.join(\",\", errors) + \"]}\");\n    }\n\n    public static void main(String[] args) throws Exception {\n        if (args.length > 0 && args[0].equals(\"discover\")) {\n            int start = 1;\n            List<String> fixtures = new ArrayList<String>();\n            if (args.length > 2 && args[1].equals(\"--fixtures\")) {\n                fixtures = Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8);\n                start = 3;\n            }\n            discover(Arrays.copyOfRange(args, start, args.length), fixtures);\n            return;\n        }\n        if ((args.length != 6 && args.length != 7) || !args[0].equals(\"observe\"))\n            throw new IllegalArgumentException(\"SQA_HARNESS expected discover classes or observe class ctor method types vector\");\n        String[] pieces = args[5].split(\",\");\n        double[] vector = new double[pieces.length];\n        for (int i = 0; i < pieces.length; i++) {\n            vector[i] = Double.parseDouble(pieces[i]);\n            if (!Double.isFinite(vector[i]))\n                throw new IllegalArgumentException(\"SQA_HARNESS nonfinite vector\");\n        }\n        String outcome;\n        try {\n            outcome = args.length == 7 ? observeWithPolicy(args[1], args[2], args[3], args[4], vector, args[6])\n                : observe(args[1], args[2], args[3], args[4], vector);\n        } catch (FixtureFailure failure) {\n            System.out.println(\"SQA_FIXTURE_FAILURE:\" + Base64.getEncoder().encodeToString(failure.getMessage().getBytes(StandardCharsets.UTF_8)));\n            return;\n        }\n        System.out.println(\"SQA_TRACE:{\\\"target_invoked\\\":\" + Boolean.TRUE.equals(INVOKED.get()) + \"}\");\n        System.out.println(\"SQA_RESULT:\" + Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)));\n    }\n}\n",
    "scripts/study/api854/fixture_policy.py": "\"\"\"Predeclared explicit fixture capability filter, never selected by buggy outcomes.\"\"\"\nPOLICY = 'beam-explicit-fixtures-v3-proposal'\nRECIPE_SOURCES = ('algorithms/java/SqaProbe.java', 'scripts/study/api854/fixture_policy.py')\n\n\ndef recipe_document(source_hashes, policy=POLICY):\n    from .common import ROOT, sha256\n    expected = {name: source_hashes[name] for name in RECIPE_SOURCES}\n    if any(sha256(ROOT / name) != value for name, value in expected.items()):\n        raise ValueError('Explicit recipe source differs from protocol')\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10, POLICY_V11}:\n        raise ValueError('Unknown explicit fixture policy')\n    return {'schema_version': 1, 'fixture_policy_id': policy, 'source_sha256': expected,\n        'sources': {name: (ROOT / name).read_bytes().decode('utf-8') for name in RECIPE_SOURCES},\n        'scope': 'Same fixture construction/projection knowledge for all four approaches; no execution feedback'}\n\n\ndef validate_recipe(recipe, source_hashes=None, policy=POLICY):\n    from .preparation import digest\n    if (policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10, POLICY_V11} or not isinstance(recipe, dict) or recipe.get('fixture_policy_id') != policy\n            or not isinstance(recipe.get('sources'), dict) or set(recipe['sources']) != set(RECIPE_SOURCES)\n            or not isinstance(recipe.get('source_sha256'), dict) or set(recipe['source_sha256']) != set(RECIPE_SOURCES)\n            or any(not isinstance(recipe['sources'][name], str)\n                   or digest(recipe['sources'][name].encode('utf-8')) != recipe['source_sha256'][name] for name in RECIPE_SOURCES)):\n        raise ValueError('Explicit recipe source bytes/hash differ')\n    if source_hashes is not None and any(source_hashes.get(name) != recipe['source_sha256'][name] for name in RECIPE_SOURCES):\n        raise ValueError('Explicit recipe source differs from frozen protocol')\n    return True\nPOLICY_V4 = 'beam-explicit-fixtures-v4-proposal'\nPOLICY_V5 = 'beam-explicit-fixtures-v5-proposal'\nPOLICY_V6 = 'aom-beam-fraction-field-v6-development'\nPOLICY_V10 = 'aom-beam-champ-joint-fixtures-v10-development'\nPOLICY_V11 = 'aom-beam-champ-chronology-fixtures-v11-development'\nCHRONOLOGY_SIGNATURES = {\n    ('org.joda.time.Partial', 'org.joda.time.Chronology', '<init>', ''),\n    ('org.joda.time.Partial', 'org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology', '<init>', ''),\n    ('org.joda.time.Partial', '[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology', '<init>', ''),\n    ('org.joda.time.Partial', 'org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I', '<init>', ''),\n    ('org.joda.time.Partial', '', 'getField', 'int,org.joda.time.Chronology'),\n    ('org.joda.time.Partial', '', 'withChronologyRetainFields', 'org.joda.time.Chronology'),\n}\nJOINT_SIGNATURES = {\n    ('org.apache.commons.codec.language.Metaphone', '', 'setMaxCodeLen', 'int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'inLongRange', '[C,int,int,boolean'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseBigDecimal', '[C'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseBigDecimal', '[C,int,int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseInt', '[C,int,int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseLong', '[C,int,int'),\n    ('com.fasterxml.jackson.core.util.TextBuffer', 'com.fasterxml.jackson.core.util.BufferRecycler', 'append', '[C,int,int'),\n    ('com.fasterxml.jackson.core.util.TextBuffer', 'com.fasterxml.jackson.core.util.BufferRecycler', 'append', 'java.lang.String,int,int'),\n    ('org.apache.commons.csv.ExtendedBufferedReader', 'java.io.Reader', 'read', '[C,int,int'),\n    ('org.apache.commons.lang3.math.NumberUtils', '', 'isAllZeros', 'java.lang.String'),\n    ('org.apache.commons.lang3.math.NumberUtils', '', 'validateArray', 'java.lang.Object'),\n}\n\n# Fixed-source recipes, declared before generation/evaluation. This development\n# version deliberately preserves unsupported declarations as explicit exclusions.\nPILOT_METHODS = {\n    'org.apache.commons.lang3.math.NumberUtils': {'isDigits', 'isNumber', 'max', 'min', 'toByte', 'toDouble',\n        'toFloat', 'toInt', 'toLong', 'toShort', 'createDouble', 'createFloat', 'createInteger', 'createLong',\n        'createNumber', 'createBigDecimal', 'createBigInteger'},\n    'com.fasterxml.jackson.core.io.NumberInput': {'parseAsDouble', 'parseDouble', 'parseAsInt', 'parseInt',\n        'parseBigDecimal', 'parseAsLong', 'parseLong', 'inLongRange'},\n    'com.fasterxml.jackson.core.util.TextBuffer': {'hasTextAsCharacters', 'contentsAsArray', 'contentsAsString',\n        'getCurrentSegmentSize', 'getTextOffset', 'size', 'toString', 'append', 'resetWithEmpty', 'resetWithString'},\n    'org.apache.commons.math3.fraction.BigFraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'reduce', 'compareTo'},\n    'org.apache.commons.math3.fraction.Fraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'compareTo'},\n    'org.jsoup.nodes.Document': {'nodeName', 'outerHtml', 'title', 'normalise', 'body', 'head', 'text', 'createElement', 'createShell'},\n    'org.apache.commons.cli.CommandLine': {'hasOption', 'getOptionObject', 'getOptionValue', 'getArgs',\n        'getOptionValues', 'iterator', 'getArgList', 'getOptions', 'addArg', 'addOption'},\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream': {'ensureOpen', 'writeCString',\n        'close', 'closeArchiveEntry', 'finish', 'putArchiveEntry', 'putNextEntry', 'write'},\n    'org.joda.time.field.UnsupportedDurationField': {'equals', 'isPrecise', 'isSupported', 'getType',\n        'getName', 'toString', 'getUnitMillis', 'compareTo', 'getInstance'},\n    'org.joda.time.Partial': {'size', 'getValues', 'toStringList', 'with', 'withField', 'without'},\n    'com.google.gson.TypeInfoFactory': {'getIndex', 'getActualType', 'extractRealTypes', 'getTypeInfoForField', 'getTypeInfoForArray'},\n    'com.google.javascript.jscomp.RemoveUnusedVars': {'process', 'traverseAndRemoveUnusedReferences', 'getFunctionArgList'},\n    'org.jfree.chart.renderer.category.AreaRenderer': {'findRangeBounds', 'getRowCount', 'getColumnCount',\n        'getPassCount', 'getLegendItems', 'getLegendItem', 'getItemMiddle'},\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter': {'getName', 'getSerializedName', 'getType',\n        'getPropertyType', 'getGenericPropertyType', 'isRequired', 'willSuppressNulls', 'hasSerializer',\n        'hasNullSerializer', 'rename', 'get', 'getInternalSetting', 'setInternalSetting', 'removeInternalSetting'},\n    'com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer': {'deserialize', 'deserializeUsingCustom', 'handleNonArray'},\n    'com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser': {'hasTextCharacters', 'isClosed', 'isExpectedStartArrayToken',\n        'requiresCustomCodec', 'getTextCharacters', 'nextToken', 'getText', 'getCurrentName', 'getValueAsString',\n        'nextTextValue', 'close', 'overrideCurrentName', 'setXMLTextElementName'},\n    'org.mockito.internal.invocation.InvocationMatcher': {'matches', 'hasSameMethod', 'hasSimilarMethod',\n        'getMethod', 'getInvocation', 'toString'},\n}\nPILOT_TYPES = {'com.fasterxml.jackson.core.util.BufferRecycler', 'org.apache.commons.math3.fraction.BigFraction',\n    'org.apache.commons.math3.fraction.Fraction', 'java.math.BigInteger', 'org.apache.commons.cli.Option',\n    'java.io.OutputStream', 'org.apache.commons.compress.archivers.ArchiveEntry',\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveEntry', 'org.joda.time.DurationFieldType',\n    'org.joda.time.DurationField', 'org.joda.time.DateTimeFieldType', 'java.lang.reflect.Type',\n    'java.lang.reflect.TypeVariable', '[Ljava.lang.reflect.Type;', '[Ljava.lang.reflect.TypeVariable;',\n    'java.lang.reflect.Field', 'java.lang.Class', 'com.google.javascript.jscomp.AbstractCompiler',\n    'com.google.javascript.rhino.Node', 'org.jfree.data.category.CategoryDataset', 'org.jfree.chart.axis.CategoryAxis',\n    'java.lang.Comparable', 'java.awt.geom.Rectangle2D', 'org.jfree.chart.util.RectangleEdge',\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter', 'com.fasterxml.jackson.databind.util.NameTransformer',\n    'com.fasterxml.jackson.databind.JavaType', 'com.fasterxml.jackson.databind.JsonDeserializer',\n    'com.fasterxml.jackson.databind.deser.ValueInstantiator', 'com.fasterxml.jackson.core.JsonParser',\n    'com.fasterxml.jackson.databind.DeserializationContext', 'com.fasterxml.jackson.core.io.IOContext',\n    'com.fasterxml.jackson.core.ObjectCodec', 'javax.xml.stream.XMLStreamReader', 'org.mockito.invocation.Invocation'}\n\n# Added capability recipes are fixed before any buggy evaluation. Mutators need\n# structural post-state; unsupported helpers/serialization hooks stay excluded.\nADDITIONAL_METHODS = {\n    'org.apache.commons.codec.language.Caverphone': {'isCaverphoneEqual', 'encode', 'caverphone'},\n    'org.apache.commons.codec.language.Metaphone': {'isLastChar', 'isMetaphoneEqual', 'getMaxCodeLen', 'encode', 'metaphone'},\n    'org.apache.commons.codec.language.SoundexUtils': {'differenceEncoded', 'clean'},\n    'org.apache.commons.collections.map.Flat3Map': {'containsKey', 'containsValue', 'equals', 'isEmpty', 'size',\n        'clone', 'get', 'put', 'remove', 'toString', 'clear', 'putAll'},\n    'org.apache.commons.csv.ExtendedBufferedReader': {'getLineNumber', 'lookAhead', 'readAgain', 'read', 'readLine'},\n}\n\nSCALARS = {'boolean', 'byte', 'short', 'int', 'long', 'float', 'double', 'char',\n           'java.lang.String', 'java.lang.Boolean', 'java.lang.Byte', 'java.lang.Short',\n           'java.lang.Integer', 'java.lang.Long', 'java.lang.Float', 'java.lang.Double',\n           'java.lang.Character', 'java.lang.Object', 'java.lang.Number', 'java.util.Date',\n           'java.util.Locale', 'java.util.List', 'java.util.Collection', 'java.lang.Iterable',\n           'java.util.Iterator', 'java.util.Map', 'java.util.Set'}\nCLOSURE = {'com.google.javascript.jscomp.AbstractCompiler', 'com.google.javascript.jscomp.ControlFlowGraph',\n           'com.google.javascript.jscomp.type.ReverseAbstractInterpreter', 'com.google.javascript.jscomp.Scope',\n           'com.google.javascript.jscomp.Scope$Var', 'com.google.javascript.jscomp.type.FlowScope',\n           'com.google.javascript.rhino.Node', 'com.google.javascript.rhino.jstype.JSType',\n           'com.google.javascript.rhino.jstype.ObjectType', 'com.google.javascript.rhino.jstype.JSTypeNative',\n           'com.google.javascript.rhino.jstype.BooleanLiteralSet'}\nJXPATH = {'org.w3c.dom.Node', 'org.w3c.dom.Document', 'org.w3c.dom.Element',\n          'org.apache.commons.jxpath.ri.QName', 'org.apache.commons.jxpath.ri.compiler.NodeTest',\n          'org.apache.commons.jxpath.ri.model.NodePointer'}\n# Methods requiring specialized AST parent/sibling/call metadata have no reviewed\n# recipe yet. This list is a structural restriction, not an outcome-based prune.\nCLOSURE_METHODS = {'createEntryLattice', 'createInitialEstimateLattice', 'flowThrough',\n    'branchedFlowThrough', 'isAddedAsNumber', 'isUnflowable', 'newBooleanOutcomePair',\n    'traverseAnd', 'traverseOr', 'traverseShortCircuitingBinOp', 'traverseWithinShortCircuitingBinOp',\n    'narrowScope', 'traverse', 'traverseAdd', 'traverseArrayLiteral', 'traverseAssign',\n    'traverseChildren', 'traverseGetElem', 'traverseGetProp', 'traverseHook', 'traverseName',\n    'traverseObjectLiteral', 'traverseReturn', 'getJSType', 'getNativeType',\n    'redeclareSimpleVar', 'updateScopeForTypeChange', 'getBooleanOutcomes'}\n\n\ndef select(targets, policy):\n    if policy is None:\n        return targets, []\n    if policy == POLICY_V11:\n        fields = ('class', 'constructor_types', 'method', 'parameter_types')\n        selected, excluded = select(targets, POLICY_V10)\n        chosen = {tuple(t[k] for k in fields) for t in selected} | CHRONOLOGY_SIGNATURES\n        return ([t for t in targets if tuple(t[k] for k in fields) in chosen],\n                [r for r in excluded if tuple(r['target'][k] for k in fields) not in chosen])\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10, POLICY_V11}:\n        raise ValueError('Unknown explicit fixture policy')\n    if policy == POLICY_V10:\n        # Preserve v5+Math and add only exact peer-approved identities. JDOM is a repair.\n        fields = ('class', 'constructor_types', 'method', 'parameter_types')\n        selected, excluded = select(targets, POLICY_V6)\n        chosen = {tuple(t[k] for k in fields) for t in selected} | JOINT_SIGNATURES\n        return ([t for t in targets if tuple(t[k] for k in fields) in chosen],\n                [r for r in excluded if tuple(r['target'][k] for k in fields) not in chosen])\n    if policy == POLICY_V6:\n        # Keep every v5 decision, adding only the exact Champ-accepted signatures.\n        selected, excluded = select(targets, POLICY_V5)\n        accepted = lambda t: (t['class'] in {\n            'org.apache.commons.math3.fraction.BigFraction', 'org.apache.commons.math3.fraction.Fraction'}\n            and t['constructor_types'] == 'double' and t['method'] == 'getField' and t['parameter_types'] == '')\n        chosen = {tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) for t in selected}\n        return ([t for t in targets if accepted(t) or tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) in chosen],\n                [row for row in excluded if not accepted(row['target'])])\n    selected, excluded = [], []\n    for target in targets:\n        name = target['class']\n        family = CLOSURE if name == 'com.google.javascript.jscomp.TypeInference' else JXPATH if name in {\n            'org.apache.commons.jxpath.ri.model.dom.DOMNodePointer',\n            'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer'} else set()\n        extra = policy in {POLICY_V4, POLICY_V5} and name in ADDITIONAL_METHODS\n        pilot = policy == POLICY_V5 and name in PILOT_METHODS\n        if extra:\n            family = {'java.io.Reader'}\n        if pilot:\n            family = PILOT_TYPES | {'[B', '[S', '[I', '[J', '[F', '[D', '[C'}\n        reason = None\n        if not family:\n            reason = 'explicit_project_recipe_not_reviewed'\n        elif target['method'] in {'<init>', 'hashCode'}:\n            reason = 'constructor_or_identity_oracle_not_reviewed'\n        elif family is CLOSURE and target['method'] not in CLOSURE_METHODS:\n            reason = 'specialized_ast_recipe_not_reviewed'\n        elif extra and target['method'] not in ADDITIONAL_METHODS[name]:\n            reason = 'additional_method_preconditions_or_state_not_reviewed'\n        elif extra and name.endswith('ExtendedBufferedReader') and target['parameter_types']:\n            reason = 'reader_buffer_offset_bounds_recipe_not_reviewed'\n        elif pilot and target['method'] not in PILOT_METHODS[name]:\n            reason = 'pilot_method_preconditions_or_oracle_not_reviewed'\n        elif pilot and name.endswith('NumberInput') and '[' in target['parameter_types']:\n            reason = 'numeric_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('TextBuffer') and target['method'] == 'append' and target['parameter_types'] != 'char':\n            reason = 'text_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('Document') and target['parameter_types'] == 'org.jsoup.nodes.Element':\n            reason = 'html_internal_normalise_recipe_not_reviewed'\n        elif pilot and name.endswith('CpioArchiveOutputStream') and target['method'] == 'write' and target['parameter_types'] != 'int':\n            reason = 'archive_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('BeanPropertyWriter') and target['method'] == 'isRequired' and target['parameter_types']:\n            reason = 'annotation_introspector_recipe_not_reviewed'\n        elif pilot and name.endswith('RemoveUnusedVars') and target['method'] == 'process' and target['parameter_types'] != 'com.google.javascript.rhino.Node,com.google.javascript.rhino.Node':\n            reason = 'call_site_definition_finder_recipe_not_reviewed'\n        else:\n            required = set(filter(None, (target['constructor_types'] + ',' + target['parameter_types']).split(',')))\n            missing = required - SCALARS - family\n            if missing:\n                reason = 'explicit_argument_recipe_missing:' + ','.join(sorted(missing))\n        if reason:\n            excluded.append({'target': target, 'reason': reason})\n        else:\n            selected.append(target)\n    return selected, excluded\n"
  }
}
```
