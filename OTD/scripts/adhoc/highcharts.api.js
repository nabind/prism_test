/**
 * This adapter makes adoption of Ad Hoc data, metadata and chart state to Highcharts options.
 */
var AdhocHighchartsAdapter = {
    /**
     * Generates Highcharts options based on query data and chart state. This method does not do rendering. It just
     * prepare the options for rendering.
     *
     * @param queryData the query data object.
     * @param chartState the chart state.
     * @return {Object} the options object to be passed to Highcharts.Chart constructor.
     */
    generateOptions: function(queryData, chartState) {
        AdhocDataProcessor.fn.load(queryData);

        HighchartsDataMapper.chartType = chartState.chartType;

        var highchartsOptions = {
            chart: {
                renderTo: 'chartContainer',
                zoomType: 'xy',
                jrstype: chartState.chartType,
                type: HighchartsDataMapper.cast[chartState.chartType]
            },

            credits: {
                enabled: false
            },

            tooltip: {
                valueDecimals: 2
            },

            title: {
                // Skip internal Highcharts title.
                text: null
            }
        };

        var dataProcessorRow = AdhocDataProcessor.fn.levelsToLevelNumbers(chartState.rowsSelectedLevels, 0);
        var dataProcessorCol = AdhocDataProcessor.fn.levelsToLevelNumbers(chartState.columnsSelectedLevels, 1);

        _.extend(highchartsOptions, HighchartsDataMapper.get[chartState.chartType](dataProcessorRow, dataProcessorCol));

        return highchartsOptions;
    }
};
