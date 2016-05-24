package org.one2team.highcharts.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.one2team.highcharts.shared.Axis;
import org.one2team.highcharts.shared.DateTimeLabelFormats;
import org.one2team.highcharts.shared.Style;
import org.one2team.highcharts.shared.Title;
import org.one2team.utils.JSMArrayString;

import com.google.gwt.shared.Array;

@XmlAccessorType(XmlAccessType.NONE)
public class JSMAxis extends JSMBaseObject implements Axis {

	public JSMAxis () {
    this.labels = null;
    this.dateTimeLabelFormats = null;
    this.categories = null;
    this.title = null;
  }

  @XmlTransient
  public JSMArrayString getCategories () {
  	if (categories == null)
  		categories = new JSMArrayString ();
    return (JSMArrayString) this.categories;
  }

	public int getCategoriesLength () {
		return (categories != null) ? ((JSMArrayString)categories).size () : 0;
	}

  public Title getTitle () {
  	if (title == null)
  		title = new JSMTitle ();
    return title;
  }

  
  public Labels getLabels () {
  	if (this.labels == null)
  		this.labels = new JSMLabels ();
    return this.labels;
  }

  
  public Axis setMin (double min) {
    this.min = min;
    return this;
  }
  
  
  public Axis setMax (double max) {
    this.max = max;
    return this;
  }


  
  public Axis setType (String type) {
    this.type = type;
    return this;
  }

  
  public Axis setTickInterval (double tickInterval) {
    this.tickInterval = tickInterval;
    return this;
  }

  
  public Axis setStartOnTick (boolean startOnTick) {
    this.startOnTick = startOnTick;
    return this;
  }

  
  public Axis setShowFirstLabel (boolean showFirstLabel) {
    this.showFirstLabel = showFirstLabel;
    return this;
  }
  
  
  public Axis setPlotLines (Array<PlotLines> plotLines) {
    this.plotLines = plotLines;
    return this;
  }
  
  @SuppressWarnings("unchecked")
  
  public Array<PlotLines> getPlotLines () {
    return (Array<PlotLines>) plotLines;
  }

  
  public DateTimeLabelFormats getDateTimeLabelFormats () {
  	if (dateTimeLabelFormats == null)
  		this.dateTimeLabelFormats = new JSMDateTimeLabelFormats ();
    return dateTimeLabelFormats;
  }

  
  public Axis setMaxZoom (int maxZoom) {
    this.maxZoom = maxZoom;
    return this;
  }

  
  public boolean isShowFirstLabel () {
    return showFirstLabel;
  }

  
  public boolean isStartOnTick () {
    return startOnTick;
  }

  
  public int getMaxZoom () {
    return maxZoom;
  }

  
  public double getMin () {
    return min;
  }
  
  
  public double getMax () {
    return max;
  }

  
  public double getTickInterval () {
    return tickInterval;
  }

  
  public String getType () {
    return type;
  }

  @XmlElement
  public Integer maxZoom;

  @XmlElement
  public Boolean startOnTick;

  @XmlElement
  public Double tickInterval;

  @XmlElement
  public Boolean showFirstLabel;

  @XmlElement
  public Double min;
  
  @XmlElement
  public Double max;

  @XmlElement
  public String type;

  @XmlElement(type = JSMLabels.class)
  public Labels labels;

  public DateTimeLabelFormats dateTimeLabelFormats;

  @XmlTransient
  public Object categories;

  public  Object plotLines;

  public JSMTitle title;
  
  @XmlAccessorType(XmlAccessType.NONE)
  @XmlType(namespace="axis")
  public static class JSMLabels extends JSMBaseObject implements Labels {

    
    public Labels setAlign (String align) {
      this.align = align;
      return this;
    }

    
    public Labels setRotation (double rotation) {
      this.rotation = rotation;
      return this;
    }

		public Double getRotation () {
			return rotation;
		}

		public String getAlign () {
			return align;
		}

		@XmlElement
    public Double rotation;
    @XmlElement
    public String align;

  }
  
  public static class JSMPlotLines extends JSMBaseObject implements PlotLines {
    
    public JSMPlotLines () {
      this.label = new JSMLabel ();
    }
    
    
    public PlotLines setLabel (Label label) {
      this.label = label;
      return this;
    }

    
    public Label getLabel () {
      return label;
    }

    
    public PlotLines setColor (String color) {
      this.color = color;
      return this;
    }

    
    public String getColor () {
      return color;
    }

    
    public PlotLines setDashStyle (String dashStyle) {
      this.dashStyle = dashStyle;
      return this;
    }

    
    public String getDashStyle () {
      return dashStyle;
    }

    
    public PlotLines setId (String id) {
      this.id = id;
      return this;
    }

    
    public String getId () {
      return id;
    }

    
    public PlotLines setValue (double value) {
      this.value = value;
      return this;
    }

    
    public double getValue () {
      return value;
    }

    
    public PlotLines setWidth (int width) {
      this.width = width;
      return this;
    }

    
    public int getWidth () {
      return width;
    }

    
    public PlotLines setZIndex (int zindex) {
      this.zindex = zindex;
      return this;
    }

    
    public int getZIndex () {
      return zindex;
    }

    public int zindex;
    public int width;
    public double value;
    public String id;
    public String color;
    public String dashStyle;
    public Label label;
    
    public static class JSMLabel extends JSMBaseObject implements Label {
      
      public JSMLabel () {
        style = new JSMStyle ();
      }
      
      public Label setAlign (String align) {
        this.align = align;
        return this;
      }

      
      public String getAlign () {
        return align;
      }

      
      public Label setVerticalAlign (String verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
      }

      
      public String getVerticalAlign () {
        return verticalAlign;
      }

      
      public Label setRotation (double rotation) {
        this.rotation = rotation;
        return this;
      }

      
      public double getRotation () {
        return rotation;
      }

      
      public Label setText (String text) {
        this.text = text;
        return this;
      }

      
      public String getText () {
        return text;
      }

      
      public Label setTextAlign (String textAlign) {
        this.textAlign = textAlign;
        return this;
      }

      
      public String getTextAlign () {
        return textAlign;
      }

      
      public Label setX (double x) {
        this.x = x;
        return this;
      }

      
      public double getX () {
        return x;
      }

      
      public Label setY (double y) {
        this.y = y;
        return this;
      }

      
      public double getY () {
        return y;
      }
      
      
      
      public Label setStyle (Style style) {
        this.style = style;
        return this;
      }
      
      
      public Style getStyle () {
        return style;
      }

      
      public String align;
      public String verticalAlign;
      public double rotation;
      public String text;
      public String textAlign;
      public double x;
      public double y;
      public Style style;

    }
  }

}
