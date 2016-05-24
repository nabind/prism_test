package org.one2team.highcharts.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.one2team.highcharts.shared.PlotOptions;

@XmlAccessorType(XmlAccessType.NONE)
public class JSMPlotOptions extends JSMBaseObject implements PlotOptions {

	public JSMPlotOptions () {
    this.area = new JSMSeries ();
    this.areaspline = new JSMSeries ();
    this.bar = new JSMSeries ();
    this.column = new JSMSeries ();
    this.line = new JSMSeries ();
    this.pie = new JSMSeries ();
    this.series = new JSMSeries ();
    this.spline = new JSMSeries ();
  }

  
  public JSMSeries getArea () {
    return area;
  }

  
  public JSMSeries getAreaspline () {
    return areaspline;
  }

  
  public JSMSeries getBar () {
    return bar;
  }

  
  public JSMSeries getColumn () {
    return column;
  }

  
  public JSMSeries getLine () {
    return line;
  }

  
  public JSMSeries getPie () {
    return pie;
  }

  
  public JSMSeries getSeries () {
    return this.series;
  }

  
  public JSMSeries getSpline () {
    return this.spline;
  }
  
  @XmlType(namespace="plotoptions")
  @XmlAccessorType(XmlAccessType.NONE)
  public static class JSMSeries extends JSMBaseObject implements Series {
  
    public JSMSeries () {
    }
  	
  	@SuppressWarnings("unchecked")
		protected <T> T cast () {
  		return (T) this;
  	}
    
    
    public String getStacking () {
      return this.stacking;
    }
  
    
    public boolean isAllowPointSelect () {
      return this.allowPointSelect;
    }
  
    
    public JSMSeries setAllowPointSelect (boolean allowPointSelect) {
      this.allowPointSelect = allowPointSelect;
			return this;
    }
  
    
    @XmlElement
    public JSMSeries setStacking (String stacking) {
      this.stacking = stacking;
			return this;
    }
    
    
    public Marker getMarker () {
    	if (this.marker == null)
    		this.marker = new JSMMarker ();
      return marker;
    }
    
    
    public double getFillOpacity () {
      return fillOpacity;
    }
    
    
    public JSMSeries setFillOpacity (double fillOpacity) {
      this.fillOpacity = fillOpacity;
			return this;
    }
    
    
    public JSMDataLabels getDataLabels () {
    	if (this.dataLabels == null)
    		this.dataLabels = new JSMDataLabels ();
      return dataLabels;
    }
  
    
    public JSMSeries setStates (States states) {
      this.states = states;
			return this;
    }
    
    
    public States getStates () {
    	if (this.states == null)
        this.states = new JSMStates ();
      return this.states;
    }

		
		public boolean isShadow() {
			// TODO Auto-generated method stub
			return shadow;
		}

		
		public int getLineWidth() {
			return lineWidth;
		}

		
		public JSMSeries setShadow(boolean shadow) {
			this.shadow = shadow;
			return this;
		}

		
		public JSMSeries setLineWidth(int lineWidth) {
			this.lineWidth = lineWidth;
			return this;
		}

		
		public JSMSeries setBorderWidth(int borderWidth) {
			this.borderWidth = borderWidth;
			return this;
		}

		
		public int getBorderWidth() {
			return borderWidth;
		}
  
    @XmlElement(type = JSMStates.class)
    public States states;
    
    public JSMDataLabels dataLabels;
  
    public String stacking;
  
    public Marker marker; 
    
    @XmlElement
    public Boolean allowPointSelect;
    
    @XmlElement
    public Boolean shadow;
    
    @XmlElement
    public Integer lineWidth;
    
    @XmlElement
    public Double fillOpacity;
	
		public int borderWidth;
    
    public static class JSMStates extends JSMBaseObject implements States {
  
      public JSMStates () {
      }
      
      
      public JSMSelect getSelect () {
      	if (this.select == null)
      		this.select = new JSMSelect ();
        return select;
      }
      
      @XmlElement(type=JSMSelect.class)
      public JSMSelect select;
  
      @XmlAccessorType(XmlAccessType.NONE)
      public static class JSMSelect extends JSMBaseObject implements Select{
        
        public String getBorderColor () {
          return borderColor;
        }
        
        
        public int getBorderWidth () {
          return borderWidth;
        }
        
        
        public String getColor () {
          return color;
        }
        
        
        public Select setBorderColor (String borderColor) {
          this.borderColor=borderColor;
          return this;
        }
        
        
        public Select setBorderWidth (int borderWidth) {
          this.borderWidth=borderWidth;
          return this;
        }
        
        
        public Select setColor (String color) {
          this.color=color;
          return this;
        }
        
        public String color;
        public String borderColor;
        @XmlElement
        public Integer borderWidth;
        
      }
    }
    
    public static class JSMDataLabels extends JSMBaseObject implements DataLabels {
  
      
      public JSMDataLabels setColor (String color) {
        this.color = color;
        return this;
      }
      
      
      public DataLabels setDistance (int distance) {
        this.distance = distance;
        return this;
      }
      
      
      public DataLabels setEnabled (boolean enabled) {
        this.enabled = enabled;
        return this;
      }
  
      
      public DataLabels setFormatter (Object formatter) {
        this.formatter = formatter;
        return this;
      }
  
      
      public DataLabels setAlign (String align) {
        this.align = align;
        return this;
      }

      
      public DataLabels setRotation (double rotation) {
        this.rotation = rotation;
        return this;
      }

      
      public String getColor () {
        return color;
      }
      
      
      public int getDistance () {
        return distance;
      }
      
      
      public boolean isEnabled () {
        return enabled;
      }
      
      
      public Object getFormatter () {
        return formatter;
      }

			
			public DataLabels setY (int y) {
				this.y = y;
				return this;
			}

			
			public int getY () {
				return y;
			}

			
			public DataLabels setX (int x) {
				this.x = x;
				return this;
			}

			
			public int getX () {
				return x;
			}
      
      
      public String getAlign () {
        return align;
      }

      
      public double getRotation () {
        return rotation;
      }

      public String color;
      
      public boolean enabled;
  
      public Object formatter;
  
      public Integer x, y, distance;
      
      public String align = "center";
      
      public double rotation;
  
    }
    
    public static class JSMMarker extends JSMBaseObject implements Marker {
  
      public JSMMarker () {
      }
      
      
      public Marker setEnabled (boolean enabled) {
        this.enabled = enabled;
        return this;
      }
  
      
      public JSMStates getStates () {
      	if (this.states == null)
          this.states = new JSMStates ();
        return states;
      }
      
      
      public boolean isEnabled () {
        return enabled;
      }
      
      
      public String getSymbol () {
        return symbol;
      }
      
      
      public Marker setSymbol (String symbol) {
        this.symbol = symbol;
        return this;
      }
      
      public String symbol;
      
      public boolean enabled;
      
      public JSMStates states;
      
      public static class JSMStates extends JSMBaseObject implements States {
  
        public JSMStates () {
          select = new JSMSelect ();
          hover = new JSMHover ();
        }
        
        
        public JSMSelect getSelect () {
          return select;
        }
        
        
        public JSMHover getHover () {
          return hover;
        }
        
        public JSMSelect select;
        
        public JSMHover hover;
        
        public static class JSMHover extends JSMBaseObject implements Hover {
  
          
          public Hover setEnabled (boolean enabled) {
            this.enabled = enabled;
            return this;
          }
          
          public boolean isEnabled () {
            return enabled;
          }
          
          public boolean enabled;
        }
        
        public static class JSMSelect extends JSMBaseObject implements Select {
          
          
          public Select setFillColor (String fillColor) {
            this.fillColor = fillColor;
            return this;
          }
          
          
          public Select setLineWidth (int lineWidth) {
            this.lineWidth = lineWidth;
            return this;
          }
          
          
          public Select setLineColor (String lineColor) {
            this.lineColor = lineColor;
            return this;
          }
          
          
          public String getFillColor () {
            return fillColor;
          }
          
          
          public String getLineColor () {
            return lineColor;
          }
          
          
          public int getLineWidth () {
            return lineWidth;
          }
          
          public String fillColor, lineColor;
          
          public int lineWidth;
          
        }
      }
    }
  }

  @XmlElement
  public final JSMSeries area, areaspline, line, pie, series, spline;
  
  public JSMSeries column, bar;
}
