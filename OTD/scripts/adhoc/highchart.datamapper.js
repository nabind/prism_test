////////////////////////////////////////////////////////
//
//  HighChartDataMapper
//
//  Works on the dataset that is wrapped in an AdhocDataProcessor
//
//  Set up to recognize 3 styles of AdhocState data:
//
//    Style 0:
//
//       Measure Axis only.  All grouping on the Measure Axis, no groups on the non-Measure Axis.
//
//
//    Style 1:
//
//       Measure on one Axis.  All groups on the non-Measure Axis.
//
//
//    Style 2:
//
//      'Crosstab Style'  Groups on Measure Axis and groups on non-Measure Axis.
//
//
//
//   The chart renderers for various chart types (column, pie, etc), recognize these Styles
//   and handle them each in chart specific manner.
//
//
//
//   2012-08-01  thorick    created:  this thing actually works !
//
//
///////////////////////////////////////////////////////////

var HighchartsDataMapper = {
    debug: false,

    chartType: null,
    fullGroupHierarchyNames: true,
    //containerWidth: 0,

    ////////////////////
    //  pie constants
    //

    defaultPiesPerRow: 8,
    maxPieRows: 4,


    categories: [],
    categoryNames: {},
    groupedCategories: false,
    highchartsCategories: [],


    //
    // the highcharts default color palette
    //
    colors: [
                          '#4572A7',
                          '#AA4643',
                          '#89A54E',
                          '#80699B',
                          '#3D96AE',
                          '#DB843D',
                          '#92A8CD',
                          '#A47D7C',
                          '#B5CA92'
                        ],
    //
    //  track measure boundaries
    //
    measureMin: null,
    measureMax: null,


    getSeries: function(rowSlider, columnSlider) {
        var me = HighchartsDataMapper;

        if (AdhocDataProcessor.fn.isOLAP()) {
            var rowAxisLeafArray    = AdhocDataProcessor.fn.getNodeListForDimLevelRadio(0, rowSlider);
            var columnAxisLeafArray = AdhocDataProcessor.fn.getNodeListForDimLevelRadio(1, columnSlider);
            return me.getSeries_common(rowAxisLeafArray, columnAxisLeafArray);

        }
        else {
            var rowAxisLeafArray    = AdhocDataProcessor.fn.getNodeListForSliderLevel(0, rowSlider);
            var columnAxisLeafArray = AdhocDataProcessor.fn.getNodeListForSliderLevel(1, columnSlider);
            return me.getSeries_common(rowAxisLeafArray, columnAxisLeafArray);
        }
    },

    getColor: function(num) {
        var me = HighchartsDataMapper;

        return me.colors[num % me.colors.length];
    },

    getSeries_common: function(rowAxisLeafArray, columnAxisLeafArray) {
        var me = HighchartsDataMapper;
        var dataStyle = AdhocDataProcessor.fn.getDataStyle();
        var label;

        me.groupedCategories = false;
        me.highchartsCategories = [];
        me.measureMin = null;
        me.measureMax = null;

        if (HighchartsDataMapper.debug) {
            console.info('Data Style: ' + dataStyle);
        }

        var result = {
            xAxis: {
                categories: []
            },
            yAxis: {
                title: { text: '' }      // force empty y-Axis label
            },
            plotOptions: {},
            series: []
        };

        // single axis ONLY.   march along axis and create chart
        // each measure is a bar
        // as one descends to more detail, the individual measure bars are grouped by the lowest
        // level of grouping
        if (dataStyle == 0)  {
            //
            //  for now treat single axis only data the same whether it's on column or row
            //
            //  data on rows only
            //
            //  in all cases there is a single pie with slices = row groups
            //
            var dataOnColumns = true;
            if (AdhocDataProcessor.metadata.axes[0].length > 0)  dataOnColumns = false;

             // since there's only 1 'live' axis the non-live axis should have only one node and this is it
            var emptyNode = dataOnColumns ? rowAxisLeafArray[0] : columnAxisLeafArray[0] ;

            if (dataOnColumns)  {
                //
                // there is only a single xAxis category
                // each column group is it's own series
                //
                result.xAxis.categories.push('Totals');

                for (var i=0; i < columnAxisLeafArray.length; i++) {
                    var leafNode = columnAxisLeafArray[i];
                    var value = AdhocDataProcessor.fn.getDataFromRowColumn(emptyNode, leafNode);
                    if (value === null)  continue;   // SKIP this if no value
                    me.measureMinMax(value);

                    label = '';
                    if (me.fullGroupHierarchyNames)  {
                        label = me.assembleFullGroupLinearName(1, leafNode);
                    }
                    else {
                        alert("oienw f   implement name hierarchy display");
                    }
                    var dataArray = [];
                    dataArray.push(value);
                    var seriesItem = {
                        name: label,
                        data: dataArray
                    };
                    result.series.push(seriesItem);
                }
            }
            else {
                //
                // no legend, the x-axis shows the set of grouped data one category per datum
                // a single un-named series
                //
                var seriesData = [];
                var seriesItem = {
                    data: seriesData
                };

                for (var i=0; i < rowAxisLeafArray.length; i++) {
                    var leafNode = rowAxisLeafArray[i];
                    var value = AdhocDataProcessor.fn.getDataFromRowColumn(leafNode, emptyNode);
                    //
                    // 2012-08-21 thorick:  highcharts handles NULL data in seriesData
                    //
                    me.measureMinMax(value);
                    label = '';
                    if (me.fullGroupHierarchyNames)  {
                        label = me.assembleFullGroupHierarchyName(0, leafNode);
                    }
                    else {
                        alert("oienw f   implement name hierarchy display");
                    }
                    result.xAxis.categories.push(label);

                    seriesData.push(value);
                }

                result.series.push(seriesItem);
            }
        }
    
        // 2 axes:   1 with measures ONLY   the other with groups
        //
        // each measure is a bar  in a series  consisting of one series array member per measure
        //
        // the measure sets are grouped by the leaf slider level of the other axis
        //   which also provides the x-axis labels
        //

        if (dataStyle == 1 || dataStyle == 2)  {

            // column groups are legend items, one legend per seriesItem
            // row groups are categories across the x-axis
            var seriesSetSize = columnAxisLeafArray.length;
            var categoriesSetSize = rowAxisLeafArray.length;

            // set up series array
            for (var i=0; i < columnAxisLeafArray.length; i++)  {
                label = '';
                if (me.fullGroupHierarchyNames)  {
                    label = me.assembleFullGroupLinearName(1, columnAxisLeafArray[i]);
                }
                else {
                    alert("oiene5b987nf   implement name hierarchy display");
                }
                result.series.push({
                    name: label,
                    data: []
                });
            }


            // go through each measure leaf (set on the x axis) and generate the series for each
            for (var i=0; i < rowAxisLeafArray.length; i++) {
                var rowAxisLeafNode = rowAxisLeafArray[i];

                for (var j=0; j < columnAxisLeafArray.length; j++) {
                    var columnAxisLeafNode = columnAxisLeafArray[j];
                    var currSeries = result.series[j];

                    var value = AdhocDataProcessor.fn.getDataFromRowColumn(rowAxisLeafNode, columnAxisLeafNode);

                    //
                    // 2012-08-21 thorick:
                    //            highcharts handles NULL data in seriesData OK so set whatever we get back
                    //
                    me.measureMinMax(value);
                    currSeries.data.push(value);
                }

                label = '';
                if(me.fullGroupHierarchyNames)  {
                    label = me.assembleFullGroupHierarchyName(0, rowAxisLeafNode);
                }
                else {
                    alert("oiene5bhfff5w f   implement name hierarchy display");
                }

                result.xAxis.categories.push(label);
            }
        }
        /*
        if(me.groupedCategories && AIC.state.chartState.chartType.indexOf('column') >= 0) {
            result.xAxis.categories = HighchartsDataMapper.highchartsCategories;
            //result.xAxis.categories = HighchartsDataMapper.categories;
        }
        */

        if (me.groupedCategories) {
            if (!me.chartType) {
                alert("! fatal charting error highchart.datamapper.js  chartType is NOT set");
            }
            if (me.chartType.indexOf('column') >= 0 ||
                me.chartType.indexOf('area') >= 0  ||
                me.chartType.indexOf('line') >= 0  ||
                me.chartType.indexOf('spline') >= 0)
            {
                result.xAxis.categories = HighchartsDataMapper.highchartsCategories;
                    //result.xAxis.categories = HighchartsDataMapper.categories;
            }
        }

        return result;
    },
    //
    //  The Pie chart is completely axis based
    //
    //  The general principle is that there is 1 pie per Column Axis group
    //  and an individual pie's slices correspond to the Row Axis groups
    //
    //
    getPieSeries: function(rowSlider, columnSlider) {
        var me = HighchartsDataMapper;

        if (AdhocDataProcessor.fn.isOLAP()) {
            var rowAxisLeafArray    = AdhocDataProcessor.fn.getNodeListForDimLevelRadio(0, rowSlider);
            var columnAxisLeafArray = AdhocDataProcessor.fn.getNodeListForDimLevelRadio(1, columnSlider);
            return me.getPieSeries_common(rowAxisLeafArray, columnAxisLeafArray);
        }
        else {
            var rowAxisLeafArray    = AdhocDataProcessor.fn.getNodeListForSliderLevel(0, rowSlider);
            var columnAxisLeafArray = AdhocDataProcessor.fn.getNodeListForSliderLevel(1, columnSlider);
            return me.getPieSeries_common(rowAxisLeafArray, columnAxisLeafArray);
        }
    },

    getPieSeries_common: function(rowAxisLeafArray, columnAxisLeafArray) {
        var me = HighchartsDataMapper;
        me.measureMin = null;
        me.measureMax = null;

        /*
        //
        //   DO NOT ERASE
        //
        //manual test code for high pie count testing
        //
        //var columnCount = 24;   // 3 row limit
        //var columnCount = 32;   // normal 32 pies in 4 rows
        //var columnCount = 36;   //  9 pies per row
        //var columnCount = 40;   // 10
        //var columnCount = 44;   // 11
        //var columnCount = 48;   // 12
        //var columnCount = 52;   // 13
        //var columnCount = 80;   // 20

        //var columnCount = 100;
        //var columnCount = 120;  // 30
        var columnCount = 160;    // 40

        if (columnCount) {
            alert("for test only, cutting column size to "+columnCount);

            var newLeaves = [];
            for (var i=0; i < columnCount; i++) {
                if (i >= columnAxisLeafArray.length)  break;
                newLeaves.push(columnAxisLeafArray[i]);
            }
            columnAxisLeafArray = newLeaves;
        }
        */


        // Highcharts 2.3 patch to override color reset for each series
        // Override to reset color counter for each series (the wrap function requieres Highcharts 2.3)
        Highcharts.wrap(Highcharts.Point.prototype, 'init', function (proceed, series, options, x) {

            if (series.options.colorByPoint && series.data.length == 0) {
                series.chart.counters.color = 0;
            }

            return proceed.call(this, series, options, x);
        });

        var containerWidth = 1000;
        var containerHeight = 500;

        // TODO: temp fix. Refactor to make width and height to be passed in as parameters.
        if (jQuery && jQuery('#chartContainer')) {
            containerWidth = jQuery('#chartContainer').width();
            containerHeight = jQuery('#chartContainer').height();
        }

        var dataStyle = AdhocDataProcessor.fn.getDataStyle();
        var label;

        if (HighchartsDataMapper.debug) {
            console.info('Pie Series Data Style: ' + dataStyle);
        }

        var result = {
            xAxis: {
                categories: []
            },
            plotOptions: {},
            series: [],
            labels: {
                items: []
            }
        };

        //
        // single axis ONLY.
        //
        //  1 cases:
        //      0.  data on rows only
        //      1.  data on columns only
        //
        //
        //
        //
        if (dataStyle == 0)  {
            //
            //  data on rows only
            //
            //  in all cases there is a single pie with slices = row groups
            //
            if (AdhocDataProcessor.metadata.axes[0].length > 0)  {

                // since there's only 1 'live' axis the non-live axis should have only one node and this is it
                var columnNode = columnAxisLeafArray[0];
                var pieSetSize = 1;
                var pieMaxPositionCount = pieSetSize + 1;
                var xAxisPositionIncrement = 100 / pieMaxPositionCount;
                var xAxisPosition = xAxisPositionIncrement;
                var yAxisPosition = 50;

                //  nonMeasure groups contain measure subgroups.
                //  the label is for nonMeasure groups so it spans all of its measure pies
                var labelMaxPositionCount = pieSetSize + 1;
                var labelAbsolutePositionIncrement = containerWidth / labelMaxPositionCount;
                var labelAbsolutePosition = labelAbsolutePositionIncrement - (labelAbsolutePositionIncrement/pieMaxPositionCount);

                var centerArray = [];
                var xAxis = xAxisPosition + "%";
                var yAxis = yAxisPosition + "%";
                centerArray.push(xAxis);
                centerArray.push(yAxis);

                var name = "Totals";
                if (AdhocDataProcessor.metadata.measures[0].name) {
                    name = AdhocDataProcessor.metadata.measures[0].name;
                }
                result.series.push({
                    type: 'pie',
                    name: name,
                    data: [],
                    center: centerArray,
                    size: (xAxisPositionIncrement *2) + "%",
                    showInLegend: true,
                    dataLabels: { enabled: false }
                });

                for (var i=0; i < rowAxisLeafArray.length; i++) {
                    var rowAxisLeafNode = rowAxisLeafArray[i];
                    var value =
                        AdhocDataProcessor.fn.getDataFromRowColumn(rowAxisLeafNode, columnNode);
                    me.measureMinMax(value);

                    label = '';
                    if (me.fullGroupHierarchyNames)  {
                        label = me.assembleFullGroupLinearName(0, rowAxisLeafNode);
                    }
                    else {
                        alert("oienw f   implement name hierarchy display");
                    }
                    var valueArray = [];
                    valueArray.push(label);
                    valueArray.push(value);
                    result.series[0].data.push(valueArray);
                }
            }

            //
            // data on columns only
            //
            //  if it's measures only , then there is 1 pie with measure slices
            //
            //  for groups it's 1 pie per group with measure slices
            //
            //  we distinguish 2 cases:
            //      0.  measures in axis leaves:
            //              In this case the number of pies is the (number of axis leaves) / (number of measures)
            //              The slices are the measures
            //
            //
            //      1.  measures NOT in axis leaves:
            //              In this case tne number of pies is the number of axis leaves
            //
            //
            else {
                var rowAxisNode      = rowAxisLeafArray[0];
                var isMeasureOnly    = (AdhocDataProcessor.metadata.axes[1].length <= 1 ? true : false);
                var measureIsLast    = AdhocDataProcessor.fn.isMeasuresLastOnAxis(1);
                var numberOfMeasures = AdhocDataProcessor.metadata.measures.length;

                // determine how many pies there are
                var pieSetSize = 1;     // measure only case
                if (!isMeasureOnly)  {
                    if (measureIsLast) {
                         pieSetSize = columnAxisLeafArray.length / numberOfMeasures;
                    }
                    else {
                        // for measures not at the leaf level
                        // the number of pies is the number of leaf level members
                        pieSetSize = columnAxisLeafArray.length;

                    }
                }

                for (var m=0; m < pieSetSize; m++) {
                    var pieInfo = me.computePieParams(m+1, pieSetSize, containerWidth, containerHeight);
                    var index = m;

                    label = "All";
                    if (!isMeasureOnly) {
                        if (measureIsLast) {
                            index = m * numberOfMeasures;             // 1 pie per group of measures
                            var node = columnAxisLeafArray[index];    // this label is measure name
                            label = node.parent.label;                // parent of measure
                        }
                        else {
                            label = columnAxisLeafArray[m].label;
                        }
                    }
                    var labelItem = {
                        html: label,
                        style: {
                            left: pieInfo.labelLeftAbsolutePosition,
                            top: pieInfo.labelTopSetting + "%",
                            position: "relative"
                        }
                    }
                    result.labels.items.push(labelItem);

                    var centerArray = [];
                    var xAxis = pieInfo.xAxisPositionPercent + "%";
                    var yAxis = pieInfo.yAxisPositionPercent + "%";
                    centerArray.push(xAxis);
                    centerArray.push(yAxis);
                    var index = m;
                    if (measureIsLast)  {
                        if (!isMeasureOnly) {
                            index = m * numberOfMeasures;
                        }
                    }
                    // only show the legend once
                    var showInLegendValue = m > 0 ? false : true;
                    result.series.push({
                        type: 'pie',
                        name: columnAxisLeafArray[index].label,
                        data: [],
                        center: centerArray,
                        size: pieInfo.pieSizePercent + "%",
                        showInLegend: showInLegendValue,
                        dataLabels: { enabled: false }
                    });
                }


                //
                //  case:  measures only (no groups)
                //      single pie with measures as slices
                //
                //  case:  measures are leaves:
                //      all leaves are grouped by common lowest non-measure group
                //      we have to cycle through the labels
                //      e.g.  Canada-Sales, Canada-Cost, Mexico-Sales, Mexico-Cost, etc..
                //
                //      so we take advantage of this ordering to know when to switch pies
                //      switch on a change in the non-measure group  'Country'
                //
                //  case:  measures are NOT leaves:
                //      one pie per column leaf group
                //
                if (isMeasureOnly) {     // single pie only
                    for (var i=0; i < columnAxisLeafArray.length; i++) {
                        var columnAxisLeafNode = columnAxisLeafArray[i];

                        var value =
                            AdhocDataProcessor.fn.getDataFromRowColumn(rowAxisNode, columnAxisLeafNode);
                        me.measureMinMax(value);

                        label = '';
                        if (me.fullGroupHierarchyNames)  {
                            label = me.assembleFullGroupLinearName(1, columnAxisLeafNode);
                        }
                        else {
                            alert("oienw f   implement name hierarchy display");
                        }
                        var valueArray = [];
                        valueArray.push(label);
                        valueArray.push(value);
                        result.series[0].data.push(valueArray);
                    }
                }
                else if (measureIsLast) {   // pie per non-measure group
                    var currLeafLabel = columnAxisLeafArray[0].label;
                    var pieIndex = 0;
                    var measureCounter = 0;
                    for (var i=0; i < columnAxisLeafArray.length; i++) {
                        var columnAxisLeafNode = columnAxisLeafArray[i];
                        measureCounter++;

                        if (measureCounter > numberOfMeasures) {
                            pieIndex++;
                            if (pieIndex >= pieSetSize)  throw "highchart.datamapper getPieSeries_common: exceeded numberOfPies="+numberOfPies;
                            measureCounter = 1;
                        }
                        var value =
                            AdhocDataProcessor.fn.getDataFromRowColumn(rowAxisNode, columnAxisLeafNode);
                        me.measureMinMax(value);

                        label = '';
                        if (me.fullGroupHierarchyNames)  {
                            label = columnAxisLeafNode.label;    // measure name only
                        }
                        else {
                            alert("oippenw f   implement name hierarchy display");
                        }

                        var valueArray = [];
                        valueArray.push(label);
                        valueArray.push(value);
                        result.series[pieIndex].data.push(valueArray);
                    }
                }
                else {           // pie per leaf node
                    for (var i=0; i < columnAxisLeafArray.length; i++) {
                        var columnAxisLeafNode = columnAxisLeafArray[i];
                        var value =
                            AdhocDataProcessor.fn.getDataFromRowColumn(rowAxisNode, columnAxisLeafNode);
                        me.measureMinMax(value);

                        label = '';
                        if (me.fullGroupHierarchyNames)  {
                            label = me.assembleFullGroupHierarchyName(1, columnAxisLeafNode);
                        }
                        else {
                            alert("oippenw f   implement name hierarchy display");
                        }
                        var valueArray = [];
                        valueArray.push(label);
                        valueArray.push(value);
                        result.series[i].data.push(valueArray);
                    }
                }
            }
        }
        //
        // full on crosstab
        //
        // one pie per column group
        //
        // 1 slice per row group
        //
        //
        if (dataStyle == 2 || dataStyle == 1)  {

            // setup each individual pie  in the highcharts  series
            //
            // There is a single pie for each column group
            //   so we iterate on the column axis
            //
            for (var m=0; m < columnAxisLeafArray.length; m++) {
                var pieInfo = me.computePieParams(m+1, columnAxisLeafArray.length, containerWidth, containerHeight);

                var columnGroupName = columnAxisLeafArray[m].label;
                //
                // column axis is the measure axis so we want the label to
                //  be  previous-level-label + measure name
                //
                var label = me.assembleFullGroupHierarchyName(1, columnAxisLeafArray[m]);


                var labelItem = {
                    html: label,
                    style: {
                        left: pieInfo.labelLeftAbsolutePosition,
                        top: pieInfo.labelTopSetting + "%",
                        position: "relative"
                    }
                }
                result.labels.items.push(labelItem);

                var pieName = columnGroupName;
                var centerArray = [];
                var xAxis = pieInfo.xAxisPositionPercent + "%";
                var yAxis = pieInfo.yAxisPositionPercent + "%";
                centerArray.push(xAxis);
                centerArray.push(yAxis);

                // only show the legend once
                var showInLegendValue = m > 0 ? false : true;
                result.series.push({
                        type: 'pie',
                        name: pieName,
                        data: [],
                        center: centerArray,
                        size: pieInfo.pieSizePercent + "%",
                        showInLegend: showInLegendValue,
                        dataLabels: { enabled: false }
                });
            }


            // for each columnGroup's pie:
            //  go through the row groups to fill in the slices

            var pieSeriesIndex = 0;      // be really careful with the use of this.  It MUST match the series initialization above !
            for (var i=0; i < columnAxisLeafArray.length; i++) {
                var columnAxisLeafNode = columnAxisLeafArray[i];      // for this nonMeasure axis group a set of pies for each measure

                for (var j=0; j < rowAxisLeafArray.length; j++) {
                    var rowAxisLeafNode = rowAxisLeafArray[j];
                    var value = AdhocDataProcessor.fn.getDataFromRowColumn(rowAxisLeafNode, columnAxisLeafNode);
                    me.measureMinMax(value);

                    label = '';
                    if(me.fullGroupHierarchyNames)  {
                        var label1 = me.assembleFullGroupLinearName(0, rowAxisLeafNode);
                        label = label1;
                    }
                    else {
                        alert("oiene5bhiunfff5w f   implement name hierarchy display");
                    }
                    var valueArray = [];
                    valueArray.push(label);
                    valueArray.push(value);
                    var currSeries = result.series[i];
                    currSeries.data.push(valueArray);
                }
            }
        }
        return result;
    },

    computePieParams: function(pieNumber, numberOfPies, containerWidth, containerHeight) {
        var me = HighchartsDataMapper;

        var piesPerRow = me.defaultPiesPerRow;
        var maxPies = piesPerRow * me.maxPieRows;
        if (numberOfPies > (maxPies))  {
            piesPerRow = Math.ceil(numberOfPies / me.maxPieRows);
        }

        ///////////////////////////////
        //
        if (!me.yAxisPositions)  {
            me.yAxisPositions = [];
            me.yAxisPositions.push({});

            // 1 row  1 position
            var yAxisPosition = [];
            yAxisPosition.push(0);
            yAxisPosition.push(50);
            me.yAxisPositions.push(yAxisPosition);

            // 2 rows  2 positions
            yAxisPosition = [];
            yAxisPosition.push(0);
            yAxisPosition.push(33);
            yAxisPosition.push(75);
            me.yAxisPositions.push(yAxisPosition);

            // 3 rows 3 positions
            yAxisPosition = [];
            yAxisPosition.push(0);
            yAxisPosition.push(20);
            yAxisPosition.push(57);
            yAxisPosition.push(90);
            me.yAxisPositions.push(yAxisPosition);

            // 4 rows 4 positions
            yAxisPosition = [];
            yAxisPosition.push(0);
            yAxisPosition.push(18);
            yAxisPosition.push(42);
            yAxisPosition.push(68);
            yAxisPosition.push(94);
            me.yAxisPositions.push(yAxisPosition);
        }
        //
        ///////////////////////////////////////////////


        /////////////////
        //
        if (!me.pieSizes) {
            me.pieSizes = [];
            me.pieSizes.push(0);
            me.pieSizes.push(0);
            me.pieSizes.push(22.2222222);    // 2 rows
            me.pieSizes.push(15);            // 3
            me.pieSizes.push(15);            // 4
        };

        // amount to add to the calculated Label Y position
        if (!me.labelYaxisAddFractions) {
            me.labelYaxisAddFractions = [];
            me.labelYaxisAddFractions.push(labelYaxisAddFraction);


            //  single row
            var labelYaxisAddFraction = [];
            labelYaxisAddFraction.push(0);
            labelYaxisAddFraction.push(0.05);
            me.labelYaxisAddFractions.push(labelYaxisAddFraction);

            // 2 rows
            labelYaxisAddFraction = [];
            labelYaxisAddFraction.push(0);
            labelYaxisAddFraction.push(0.10);
            labelYaxisAddFraction.push(-0.12);
            me.labelYaxisAddFractions.push(labelYaxisAddFraction);

            // 3 rows
            labelYaxisAddFraction = [];
            labelYaxisAddFraction.push(0);
            labelYaxisAddFraction.push(0);
            labelYaxisAddFraction.push(-0.12);
            labelYaxisAddFraction.push(-0.12);
            me.labelYaxisAddFractions.push(labelYaxisAddFraction);


            // 4 rows
            labelYaxisAddFraction = [];
            labelYaxisAddFraction.push(0);
            labelYaxisAddFraction.push(0);
            labelYaxisAddFraction.push(-0.12);
            labelYaxisAddFraction.push(-0.22);
            labelYaxisAddFraction.push(-0.26);
            me.labelYaxisAddFractions.push(labelYaxisAddFraction);

        }
        //
        //////////////////////////



        var numFullRows = Math.floor(numberOfPies / piesPerRow);
        var numNonFullRowPies = numberOfPies % piesPerRow;
        var numRows = numFullRows + (numNonFullRowPies == 0 ? 0 : 1);
        var pieXaxisPositionNumber = pieNumber % piesPerRow == 0 ? piesPerRow : (pieNumber % piesPerRow);


        var pieMaxRowPositionCount = numberOfPies + 1;
        if (numFullRows > 0)  pieMaxRowPositionCount = piesPerRow + 1;

        var xAxisPositionIncrement = 100 / pieMaxRowPositionCount;
        var labelAbsolutePositionIncrement = containerWidth / pieMaxRowPositionCount;

        if (piesPerRow > 8) {
            labelAbsolutePositionIncrement = labelAbsolutePositionIncrement - (labelAbsolutePositionIncrement * 0.02);
        }

        var xAxisPositionPercent = xAxisPositionIncrement * pieXaxisPositionNumber;

        var yAxisPosArray = me.yAxisPositions[numRows];
        var pieYaxisPositionNumber = Math.ceil(pieNumber / piesPerRow);

        var yAxisPositionIncrement = containerHeight / numRows;
        var yAxisCalcLabelPosition = (pieYaxisPositionNumber-1) * yAxisPositionIncrement;

        var labelYaxisAddFractArray = me.labelYaxisAddFractions[numRows];
        var fraction =  labelYaxisAddFractArray[pieYaxisPositionNumber];
        yAxisCalcLabelPosition = yAxisCalcLabelPosition + (yAxisPositionIncrement * fraction);


        var yAxisPositionPercent = yAxisPosArray[pieYaxisPositionNumber];

        var labelLeftAbsolutePosition =
            (labelAbsolutePositionIncrement - (labelAbsolutePositionIncrement/2)) +
            (labelAbsolutePositionIncrement*(pieXaxisPositionNumber-1));

        //
        // the size of the pie is adjusted down as the number of pies per row increases
        //
        var pieSizePercent = xAxisPositionIncrement * 2;
        if (numRows > 2)  pieSizePercent = me.pieSizes[numRows];

        if (piesPerRow > 30) {
            pieSizePercent = pieSizePercent - (pieSizePercent * 0.50);
        }
        else if (piesPerRow > 20) {
            pieSizePercent = pieSizePercent - (pieSizePercent * 0.30);
        }
        else if (piesPerRow > 14)  {
            pieSizePercent = pieSizePercent - (pieSizePercent * 0.10);
        }


        return {
            xAxisPositionPercent: xAxisPositionPercent,
            yAxisPositionPercent: yAxisPositionPercent,
            labelLeftAbsolutePosition: labelLeftAbsolutePosition,
            labelTopSetting: yAxisCalcLabelPosition,
            pieSizePercent: pieSizePercent

        };

    },

    //
    //  keep track of the min and max measure values that we charted
    //
    measureMinMax: function(currVal) {
        if (currVal === null)  return;
        var me = HighchartsDataMapper;
        if (me.measureMin === null) {
            me.measureMin = currVal;
            me.measureMax = currVal;
            return;
        }
        if (currVal < me.measureMin) me.measureMin = currVal;
        if (currVal > me.measureMax) me.measureMax = currVal;
        return;
    },

    //
    // http://bugzilla.jaspersoft.com/show_bug.cgi?id=30095
    //
    //  2012-11-19  thorick
    //
    //              Post Processing of the generated yAxis.
    //              For line or spline charts, highcharts can default to
    //              showing a negative y-axis tick even when all the
    //              charted measures are greater than zero.
    //              We get around this by setting the y-axis 'min' property
    //              if there are no negative measures.
    //
    yAxisLineChartAdjust: function(result) {
        var me = HighchartsDataMapper;
        if (me.measureMin < 0)   return;
        if (result.yAxis) {
            // do NOT overwrite any pre-existing 'min' value
            if (!result.yAxis.min) {
                result.yAxis.min = me.measureMin;
                result.yAxis.startOnTick = true;
            }
        }
    },

    assembleFullGroupName: function(axisIndex, leafNode, groupLineBreaks)  {
        var label = '';
        var nameArray = AdhocDataProcessor.fn.getLabelNameArray(axisIndex, leafNode);
        var len = nameArray.length;

        for (var j=0; j < len; j++) {
            label = label + nameArray[j];
            if (j < (len - 1))  {
                if (groupLineBreaks) {
                    label = label + '<br>';
                }
                else {
                    label = label + ' ';
                }
            }
        }

        //  grouped hierarchy labels are for x axis only

        if(axisIndex == 0 && len > 1) {
            HighchartsDataMapper.groupedCategories = true;
            HighchartsDataMapper.addLeaf2HighchartsCategory(nameArray);
        }

        /*
        if(axisIndex == 0 && len > 1) {
            HighchartsDataMapper.groupedCategories = true;
            var leaf = nameArray.shift();
            var categories = HighchartsDataMapper.categories;
            var categoryNames = HighchartsDataMapper.categoryNames;
            for(j=len-2;j>=0;j--){
                if(categoryNames[nameArray[j]] && categoryNames[nameArray[j]].level == j) {
                    categories = categories[categoryNames[nameArray[j]].index].categories;
                } else {
                    categories.push({
                        name: nameArray[j],
                        categories: []
                    });
                    categoryNames[nameArray[j]] = {level: j, index: categories.length-1};
                    categories = categories[categories.length-1].categories;
                }
            }
            categories.push(leaf);
        }
        */
        return label;
    },

    assembleFullGroupHierarchyName: function(axisIndex, leafNode)  {
        var me = HighchartsDataMapper;
        return me.assembleFullGroupName(axisIndex, leafNode, true);
    },

    assembleFullGroupLinearName: function(axisIndex, leafNode)  {
        var me = HighchartsDataMapper;
        return me.assembleFullGroupName(axisIndex, leafNode, false);
    },


    addLeaf2HighchartsCategory: function(nameArray) {
        var me = HighchartsDataMapper;

        if (me.highchartsCategories == null ) me.highchartsCategories = [];

        if (nameArray.length <= 1) {
            me.highchartsCategories.push(nameArray[0]);
            return;
        }
        var currCategory = me.highchartsCategories;

        for (var i=(nameArray.length-1); i>=0; i--) {
            var name = nameArray[i];
            if (i==0) {
                currCategory.push(name);
                return;
            }
            var theCategory = null;

            for (var j=0; j<currCategory.length; j++) {
                if (currCategory[j].name === nameArray[i]) {
                    theCategory = currCategory[j];
                }
            }
            if (theCategory == null) {
                theCategory =
                    {
                        name: nameArray[i],
                        categories: []
                    };
                currCategory.push(theCategory);
            }
            currCategory = theCategory.categories;
        }
    },

    getMeasureOnlyNodeFromArray: function(measureName, leafArray)  {
        for (var i=0; i < leafArray.length; i++) {
            if (leafArray[i].measureName == measureName) {
                return leafArray[i];
            }
        }
        alert("498nhg97  ERROR !  could not find leaf NodeList Measure for measureName='"+measureName+"'");
    }
};

/*
 * Chart Type Routing
 */
_.extend(HighchartsDataMapper,{
    get: {
        column: HighchartsDataMapper.getSeries,

        stacked_column: function(rowIndex,columnIndex) {
            var result = HighchartsDataMapper.getSeries(rowIndex,columnIndex);
            result.plotOptions.column =
                {
                    stacking: "normal"
                };
            return result;
        },

        percent_column:  function(rowIndex,columnIndex) {
            var result = HighchartsDataMapper.getSeries(rowIndex,columnIndex);
            result.plotOptions.column =
                {
                    stacking: "percent"
                };
            return result;
        },

        bar:  HighchartsDataMapper.getSeries,

        stacked_bar:  function(rowIndex,columnIndex) {
            var result = HighchartsDataMapper.getSeries(rowIndex,columnIndex);
            result.plotOptions.series =
                {
                    stacking: "normal"
                };
            return result;
        },

        percent_bar:   function(rowIndex,columnIndex) {
            var result = HighchartsDataMapper.getSeries(rowIndex,columnIndex);
            result.plotOptions.series =
                {
                    stacking: "percent"
                };
            return result;
        },

        line:   function(rowIndex,columnIndex) {
            var result = HighchartsDataMapper.getSeries(rowIndex,columnIndex);
            HighchartsDataMapper.yAxisLineChartAdjust(result);
            return result;
        },

        inverted_line: '',

        area: HighchartsDataMapper.getSeries,

        inverted_area: '',

        spline_area: HighchartsDataMapper.getSeries,

        inverted_spline_area: '',

        stacked_area:  function(rowIndex,columnIndex) {
            var result = HighchartsDataMapper.getSeries(rowIndex,columnIndex);
            result.plotOptions.area =
                {
                    stacking: "normal"
                };
            return result;
        },

        inverted_stacked_area: '',

        percent_area:  function(rowIndex,columnIndex) {
            var result = HighchartsDataMapper.getSeries(rowIndex,columnIndex);
            result.plotOptions.area =
                {
                    stacking: "percent"
                };
            return result;
        },

        inverted_percent_area: '',

        pie: function(rowIndex,columnIndex) {
            var result = HighchartsDataMapper.getPieSeries(rowIndex,columnIndex);
            return result;
        },

        scatter: '',
        inverted_scatter: '',

        spline:   function(rowIndex,columnIndex) {
            var result = HighchartsDataMapper.getSeries(rowIndex,columnIndex);
            HighchartsDataMapper.yAxisLineChartAdjust(result);
            return result;
        },

        inverted_spline: '',
        time_series: ''
    },
    cast: {
        column: 'column',
        stacked_column: 'column',
        percent_column: 'column',
        bar: 'bar',
        stacked_bar: 'bar',
        percent_bar: 'bar',
        line: 'line',
        spline: 'spline',
        spline_area: 'areaspline',
        area: 'area',
        stacked_area: 'area',
        percent_area: 'area',
        pie: 'pie'
    }
});