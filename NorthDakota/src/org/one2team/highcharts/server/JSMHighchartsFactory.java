package org.one2team.highcharts.server;

import org.one2team.highcharts.shared.ChartOptions;
import org.one2team.highcharts.shared.HighchartsFactory;
import org.one2team.highcharts.shared.Point;
import org.one2team.highcharts.shared.Series;

public class JSMHighchartsFactory implements HighchartsFactory {

	public JSMHighchartsFactory() {
		super ();
	}

	
	public ChartOptions createChartOptions () {
	  return new JSMChartOptions ();
	}

  
  public Series createSeries () {
    return new JSMSeries ();
  }

  
  public JSMPoint createPoint () {
    return new JSMPoint ();
  }

}