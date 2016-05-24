package com.vaannila.web;

import java.util.Calendar;

import org.one2team.highcharts.server.JSMHighchartsFactory;
import org.one2team.highcharts.shared.ChartOptions;
import org.one2team.highcharts.shared.HighchartsFactory;
import org.one2team.highcharts.shared.Point;
import org.one2team.highcharts.shared.Series;
import org.one2team.highcharts.shared.SeriesType;
import org.one2team.highcharts.shared.DateTimeLabelFormats.TimeUnit;

import com.google.gwt.shared.Array;

public class SamplesFactory {

	public static SamplesFactory getSingleton () {
		return SINGLETON;
	}

	public ChartOptions createTimeDataWithIrregularIntervals () {
		// http://highcharts.com/demo/spline-irregular-time
		ChartOptions chartOptions = factory.createChartOptions ();
		chartOptions.getChart ().setWidth (800).setHeight (600)
				.setDefaultSeriesType (SeriesType.spline).setMarginLeft (70)
				.setMarginTop (80);

		// titles
		chartOptions.getTitle ().setText (
				"Snow depth in the Vikjafjellet mountain, Norway");
		chartOptions.getSubtitle ().setText (
				"An example of irregular time data in Highcharts JS");

		// axis
		chartOptions.getXAxis ().setType ("datetime").getDateTimeLabelFormats ()
				.set (TimeUnit.month, "%e. %b").set (TimeUnit.year, "%b");
		chartOptions.getYAxis ().setMin (0).getTitle ().setText ("Snow depth (m)");

		// plotOptions
		chartOptions
				.getPlotOptions ()
				.getPie ()
				.setAllowPointSelect (true)
				.getDataLabels ()
				.setEnabled (true)
				.setColor ("#000000")
				.setFormatter (
						"function() {return '<b>'+ this.point.name +'</b>: '+ this.y +' %';}");

		Series newSeries = factory.createSeries ().setName ("Winter 2007-2008");
		chartOptions.getSeries ().pushElement (newSeries);
		newSeries
				.getData ()
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 9, 27)).setY (0))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 10, 10)).setY (0.6))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 10, 18)).setY (0.7))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 11, 2)).setY (0.8))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 11, 9)).setY (0.6))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 11, 16)).setY (0.6))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 11, 28)).setY (0.67))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 0, 1)).setY (0.81))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 0, 8)).setY (0.78))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 0, 12)).setY (0.98))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 0, 27)).setY (1.84))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 1, 10)).setY (1.8))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 1, 18)).setY (1.8))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 1, 24)).setY (1.92))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 2, 4)).setY (2.49))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 2, 11)).setY (2.79))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 2, 15)).setY (2.73))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 2, 25)).setY (2.61))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 2)).setY (2.76))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 6)).setY (2.82))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 13)).setY (2.8))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 4, 3)).setY (2.1))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 4, 26)).setY (1.1))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 5, 9)).setY (0.25))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 5, 12)).setY (0));

		newSeries = factory.createSeries ().setName ("Winter 2008-2009");
		chartOptions.getSeries ().pushElement (newSeries);
		newSeries
				.getData ()
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 9, 18)).setY (0))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 9, 26)).setY (0.2))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 11, 1)).setY (0.47))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 11, 11)).setY (0.55))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 11, 25)).setY (1.38))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 0, 8)).setY (1.38))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 0, 15)).setY (1.38))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 1, 1)).setY (1.38))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 1, 8)).setY (1.48))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 1, 21)).setY (1.5))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 2, 12)).setY (1.89))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 2, 25)).setY (2.0))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 4)).setY (1.94))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 9)).setY (1.91))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 13)).setY (1.75))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 19)).setY (1.6))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 4, 25)).setY (0.6))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 4, 31)).setY (0.35))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 5, 7)).setY (0));

		newSeries = factory.createSeries ().setName ("Winter 2009-2010");
		chartOptions.getSeries ().pushElement (newSeries);
		newSeries
				.getData ()
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 9, 9)).setY (0))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 9, 14)).setY (0.15))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 10, 28)).setY (0.35))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1970, 11, 12)).setY (0.46))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 0, 1)).setY (0.59))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 0, 24)).setY (0.58))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 1, 1)).setY (0.62))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 1, 7)).setY (0.65))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 1, 23)).setY (0.77))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 2, 8)).setY (0.77))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 2, 14)).setY (0.79))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 2, 24)).setY (0.86))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 4)).setY (0.8))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 18)).setY (0.94))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 3, 24)).setY (0.9))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 4, 16)).setY (0.39))
				.pushElement (
						factory.createPoint ().setX (getDateUTC (1971, 4, 21)).setY (0));
		return chartOptions;
	}

	public ChartOptions createPieChart () {
		// http://highcharts.com/demo/pie-basic
		ChartOptions chartOptions = factory.createChartOptions ();
		chartOptions.getChart ().setWidth (800).setHeight (600).setMarginLeft (70)
				.setMarginTop (80);
		// title
		chartOptions.getTitle ().setText (
				"Browser market shares at a specific website, 2010");

		// plotOptions
		chartOptions
				.getPlotOptions ()
				.getPie ()
				.setAllowPointSelect (true)
				.getDataLabels ()
				.setEnabled (true)
				.setColor ("#000000")
				.setFormatter (
						"function() {return '<b>'+ this.point.name +'</b>: '+ this.y +' %';}");

		Series newSeries = factory.createSeries ().setName ("Browser share")
				.setType ("pie");
		chartOptions.getSeries ().pushElement (newSeries);
		newSeries
				.getData ()
				.pushElement (factory.createPoint ().setName ("Firefox").setY (45))
				.pushElement (factory.createPoint ().setName ("IE").setY (26.8))
				.pushElement (
						factory.createPoint ().setName ("Chrome").setY (12.8)
								.setSliced (true).setSelected (true))
				.pushElement (factory.createPoint ().setName ("Safari").setY (8.5))
				.pushElement (factory.createPoint ().setName ("Opera").setY (6.2))
				.pushElement (factory.createPoint ().setName ("Others").setY (0.7));

		return chartOptions;
	}

	public ChartOptions createColumnBasic () {
		// http://highcharts.com/demo/column-basic
		ChartOptions chartOptions = factory.createChartOptions ();

		chartOptions.getChart ().setDefaultSeriesType (SeriesType.column)
				.setWidth (800).setHeight (400).setMarginLeft (70).setMarginTop (80);

		// titles
		chartOptions.getTitle ().setText ("Monthly Average Rainfall");
		chartOptions.getSubtitle ().setText ("Source: WorldClimate.com");

		// xAxis
		chartOptions.getXAxis ().getCategories ().pushString ("Jan").pushString ("Feb")
				.pushString ("Mar").pushString ("Apr").pushString ("May").pushString ("Jun")
				.pushString ("Jul").pushString ("Aug").pushString ("Sep").pushString ("Oct")
				.pushString ("Nov").pushString ("Dec");
		// yAxis
		chartOptions.getYAxis ().setMin (0).getTitle ().setText ("Rainfall (mm)");

		// Legend
		chartOptions.getLegend ().setLayout ("vertical").setAlign ("left")
				.setVerticalAlign ("top").setX (100).setY (70);

		// PlotOptions
		chartOptions.getPlotOptions ().getColumn ().setBorderWidth (0);

		// Several series
		addSeries (chartOptions, "Tokyo", new double[] { 49.9, 71.5, 106.4, 129.2,
				144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4 });

		addSeries (chartOptions, "New York", new double[] { 83.6, 78.8, 98.5, 93.4,
				106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3 });

		addSeries (chartOptions, "London", new double[] { 48.9, 38.8, 39.3, 41.4,
				47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2 });

		addSeries (chartOptions, "Berlin", new double[] { 42.4, 33.2, 34.5, 39.7,
				52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1 });

		return chartOptions;
	}
	
	public String createJsonColumnBasicOrg () {
		return "{chart: {"
		+"			defaultSeriesType: 'line',"
		+"			width: 800,"
		+" 			height: 400,"
		+" 			plotBackgroundColor:'#fffddd',"
		+"			borderColor: '#999999',"
		+"			plotBorderColor: '#DDD',"
		+"			plotBorderWidth: 1,"
		+"			marginRight: 20,"
		+"			spacingTop: 0"
		+"		},"
		+"		credits: {"
		+"			enabled: false"
		+"		},"
		+"		title: {"
		+"			text: ' '"
		+"		},"
		+"		xAxis: {"
		+"			title: {"
		+"				text: 'Grade Level'"
		+"			},"
		+"			categories: [0,1,2,3,4,5,6,7,8,9,10,11]"
		+"		},"
		+"		yAxis: {"
		+"			title: {"
		+"				text: 'Scale Score'"
		+"			},"
		+"			plotLines: [{"
		+"				value: 0,"
		+"				width: 1,"
		+"				color: '#808080'"
		+"			}]"
		+"		},"
		+"		legend: {"
		+"			layout: 'horizontal',"
		+"			align: 'bottom',"
		+"			verticalAlign: 'bottom',"
		+"			x: 0,"
		+"			y: 0,"
		+"			borderWidth: 1,"
		+"			backgroundColor: '#fffddd'"
		+"		},"
		+"		plotOptions: {"
		+"			line: {"
		+"				dataLabels: {"
		+"					enabled: true"
		+"				},"
		+"				enableMouseTracking: false,"
		+"				shadow: false,"
		+"				animation: false"
		+"			}"
		+"		},"
		+"		series: [{"
		+"			name: 'Advanced Cut Score',"
		+"			color: '#9bbb59',"
		+"			marker: {symbol: 'diamond'}, dataLabels: { y: -15},"
		+"			data: [[3,650], [4,670], [5,690], [6,696], [7,707], [8,714], [9,722], [10,730], [11,738]]"
		+"		}, {"
		+"			name: 'Proficient Cut Score',"
		+"			color: '#be4c49',"
		+"			marker: {symbol: 'circle'},"
		+"			data: [[3,610], [4,630], [5,645], [6,655], [7,666], [8,670], [9,680], [10,690], [11,700]]"
		+"		}, {"
		+"			name: 'Partially Proficient Cut Score',"
		+"			color: '#4f81bd',"
		+"			marker: {symbol: 'triangle'}, dataLabels: { y: 20},"
		+"			data: [[3,579], [4,606], [5,619], [6,632], [7,644], [8,648], [9,658], [10,668], [11,679]]"
		+"		}, {"
		+"			name: 'Student Score',"
		+"			color: '#8064a2',"
		+"			marker: {symbol: 'square', radius: 5 },"
		+"			lineWidth: 4,"
		+"			dataLabels: { "
		+"				backgroundColor: 'rgba(252, 255, 197, 0.7)',"
		+"				borderWidth: 1,"
		+"				borderColor: '#AAA',"
		+"				style: {fontWeight: 'bold'} "
		+"			},"
		+"			data: [[5,638], [6,660], [7,614]]"
		+"		}, {"
		+"			name: '3-Year Path',"
		+"			color: '#46aac5',"
		+"			marker: {symbol: 'triangle-down', radius: 6 },"
		+"			lineWidth: 4,"
		+"			dataLabels: { enabled: false },"
		+"			dashStyle: 'longdash',"
		+"			data: [[7,614], [8,640], [9,665], [10,690]]"
		+"		}]}"; 
	}
	
	public String createJsonColumnBasic() {
		return "{chart: {"
		+"			defaultSeriesType: 'line',"
		+"			width: 850,"
		+" 			height: 610,"
		+" 			plotBackgroundColor:'#EEECE1',"
		+"			borderColor: '#999999',"
		+"			plotBorderColor: '#DDD',"
		+"			plotBorderWidth: 1,"
		+"			marginRight: 20,"
		+"			spacingTop: 0"
		+"		},"
		+"		credits: {"
		+"			enabled: false"
		+"		},"
		+"		title: {"
		+"			text: ' '"
		+"		},"
		+"		xAxis: {"
		+"			title: {"
		+"				text: 'Grade Level', style: {fontSize:14}"
		+"			},"
		+"			labels: {"
		+"				style: {fontSize:13}"
		+"			},"
		+"			categories: [0,1,2,3,4,5,6,7,8,9,10,11]"
		+"		},"
		+"		yAxis: {"
		+"			title: {"
		+"				text: 'Scale Score', style: {fontSize:14}"
		+"			},"
		+"			labels: {"
		+"				style: {fontSize:13}"
		+"			},"
		+"			min: scale_min,"
		+"			max: 900,"
		+"			tickInterval: 50,"
		+"			plotLines: [{"
		+"				value: 0,"
		+"				width: 1,"
		+"				color: '#808080'"
		+"			}]"
		+"		},"
		+"		legend: {"
		+"			layout: 'horizontal',"
		+"			align: 'bottom',"
		+"			verticalAlign: 'bottom',"
		+"			x: 55,"
		+"			y: 0,"
		+"			width: 742,"
		+"			itemWidth: 210,"
		+"			borderWidth: 1,"
		+"			backgroundColor: '#EEECE1',itemStyle: {fontSize:14}"
		+"		},"
		+"		plotOptions: {"
		+"			line: {"
		+"				dataLabels: {"
		+"					enabled: true, color: '#000000'"
		+"				},"
		+"				enableMouseTracking: false,"
		+"				shadow: false,"
		+"				animation: false"
		+"			}"
		+"		},"
		+"		series: [{"
		+"			name: 'Advanced Cut Score',"
		+"			color: '#4f81bd',"
		+"			marker: {symbol: 'triangle-down'}, dataLabels: { y: -15, style: {fontSize:12}},"
		+"			data: line1"
		+"		}, {"
		+"			name: 'Proficient Cut Score',"
		+"			color: '#9bbb59',"
		+"			marker: {symbol: 'circle'}, dataLabels: { y: 3, style: {fontSize:12}},"
		+"			data: line2"
		+"		}, {"
		+"			name: 'Partially Proficient Cut Score',"
		+"			color: '#be4c49',"
		+"			marker: {symbol: 'triangle'}, dataLabels: { y: 20, style: {fontSize:12}},"
		+"			data: line3"
		+"		}, {"
		+"			name: 'Student Score',"
		+"			color: '#8064aF',"
		+"			marker: {symbol: 'square', radius: 6 },"
		+"			lineWidth: 5,"
		+"			dataLabels: { "
		+"				backgroundColor: 'rgba(252, 255, 197, 0.7)',"
		+"				borderWidth: 1,"
		+"				borderColor: '#AAA',"
		+"				style: {fontWeight: 'bold', fontSize:16, fontFamily: 'Vardana'}, y: 5, x:25 "
		+"			},"
		+"			data: line4"
		+"		}, {"
		+"			name: '3-Year Path',"
		+"			color: '#46aac5',"
		+"			marker: {symbol: 'diamond', radius: 7 },"
		+"			lineWidth: 5,"
		+"			dataLabels: { enabled: false },"
		+"			dashStyle: 'longdash',"
		+"			data: line5"
		+"		}]}"; 
	}


	private static long getDateUTC (int year, int month, int day) {
		Calendar cal = Calendar.getInstance ();
		cal.set (Calendar.YEAR, year);
		cal.set (Calendar.MONTH, month);
		cal.set (Calendar.DAY_OF_MONTH, day);
		return cal.getTimeInMillis ();
	}

	private void addSeries (ChartOptions chartOptions, String seriesName,
			double[] datas) {

		Series newSeries = factory.createSeries ().setName (seriesName);
		final Array<Point> seriesDatas = newSeries.getData ();
		for (double d : datas) {
			seriesDatas.pushElement (factory.createPoint ().setY (d));
		}
		chartOptions.getSeries ().pushElement (newSeries);
	}

	private SamplesFactory() {
		factory = new JSMHighchartsFactory ();
	}

	private static SamplesFactory SINGLETON = new SamplesFactory ();
	
	private final HighchartsFactory factory;

}
