package org.one2team.highcharts.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.one2team.highcharts.shared.Tooltip;


@XmlAccessorType(XmlAccessType.NONE)
public class JSMTooltip extends JSMBaseObject implements Tooltip {

  
  public Tooltip setFormatter (Object formatter) {
    this.formatter = formatter;
    return this;
  }
  
  
  public Object getFormatter () {
    return formatter;
  }

  
  public Tooltip setShared (boolean shared) {
    this.shared = shared;
    return this;
  }

  
  public Tooltip setCrosshairs (boolean b) {
    this.crosshairs = b;
    return this;
  }
  
  
  public boolean isCrosshairs () {
    return crosshairs;
  }
  
  
  public boolean isShared () {
    return shared;
  }

  @XmlElement
  private Boolean crosshairs;

  @XmlElement
  private Boolean shared;
  
  private Object formatter;

}
