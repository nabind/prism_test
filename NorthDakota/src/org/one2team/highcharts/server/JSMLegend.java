package org.one2team.highcharts.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.one2team.highcharts.shared.Legend;


@XmlAccessorType(XmlAccessType.NONE)
public class JSMLegend extends JSMBaseObject implements Legend {

  
  @XmlElement
  public Legend setAlign (String align) {
    this.align = align;
    return this;
  }

  
  @XmlElement
  public Legend setLayout (String layout) {
    this.layout = layout;
    return this;
  }

  
  @XmlElement
  public Legend setVerticalAlign (String verticalAlign) {
    this.verticalAlign = verticalAlign;
    return this;
  }

  
  public Legend setX (int x) {
    this.x = x;
    return this;
  }

  
  public Legend setY (int y) {
    this.y = y;
    return this;
  }

  
  public String getAlign () {
    return align;
  }

  
  public String getLayout () {
    return layout;
  }

  
  public String getVerticalAlign () {
    return verticalAlign;
  }

  
  public int getX () {
    return x;
  }

  
  public int getY () {
    return y;
  }
  
  
  public boolean getEnabled () {
    return enabled;
  }

  
  public Legend setEnabled (boolean enabled) {
    this.enabled = enabled;
    return this;
  }
  
  
  public Legend setReversed (boolean reversed) {
    this.reversed = reversed;
    return this;
  }
  
  
  public boolean isReversed () {
    return reversed;
  }

  public Boolean reversed;
  public Boolean enabled;
  public String align;
  public String layout;
  public String verticalAlign;
  @XmlElement
  public Integer x;
  @XmlElement
  public Integer y;

}
