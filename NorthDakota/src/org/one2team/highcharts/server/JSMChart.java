package org.one2team.highcharts.server;

import org.one2team.highcharts.shared.Chart;
import org.one2team.highcharts.shared.SeriesType;


public class JSMChart extends JSMBaseObject implements Chart {
	
  public Chart setMarginBottom (int marginBottom) {
    this.marginBottom = marginBottom;
    return this;
  }

  
  public Chart setMarginRight (int marginRight) {
    this.marginRight = marginRight;
    return this;
  }

  
  public Chart setZoomType (String zoomType) {
    this.zoomType = zoomType;
    return this;
  }

  
  public String getZoomType () {
    return zoomType;
  }

  
  public int getMarginRight () {
    return marginRight;
  }

  
  public int getMarginBottom () {
    return marginBottom;
  }

  
  public Object getRenderTo () {
    return renderTo;
  }

  public void setRenderTo (Object renderTo) {
		this.renderTo = renderTo;
	}
  
  
  public Chart setBackgroundColor (String backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }
  
  
  public String getBackgroundColor () {
    return backgroundColor;
  }

  
  public Chart setDefaultSeriesType (SeriesType type) {
    defaultSeriesType = type.name ();
    return this;
  }
  
  
  public String getDefaultSeriesType () {
  	System.out.println("defaultSeriesType "+defaultSeriesType);
    return defaultSeriesType;
  }
  
	
	public JSMChart setWidth(int width) {
		this.width = width;
		return this;
	}

	
	public int getWidth() {
		return this.width;
	}
	
	public JSMChart setHeight(int height) {
		this.height = height;
		return this;
	}

	
	public int getHeight() {
		return this.height;
	}

  
  public Chart setMarginLeft (int marginLeft) {
    this.marginLeft = marginLeft;
    return this;
  }

  
  public int getMarginLeft () {
    return marginLeft;
  }

  
  public Chart setMarginTop (int marginTop) {
    this.marginTop = marginTop;
    return this;
  }

  
  public int getMarginTop () {
    return marginTop;
  }

	public void setRenderer (Object renderer) {
		this.renderer = renderer;
	}

	public Object getRenderer () {
		return renderer;
	}

	public String zoomType;
  public String backgroundColor;
  public Integer marginRight;
  public Integer marginBottom;
  public Integer width;
  public Integer height;
  public String defaultSeriesType;
  public int marginLeft;
  public int marginTop;
  public Object renderTo;
  public Object renderer;

}
