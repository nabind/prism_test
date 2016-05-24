package org.one2team.highcharts.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.one2team.highcharts.shared.Point;


@XmlAccessorType(XmlAccessType.NONE)
public class JSMPoint extends JSMBaseObject implements Point {
  
  public JSMPoint () {
  }

  
  public double getY () {
    return this.y;
  }

  
  public double getX () {
    return this.x;
  }
  
  public boolean getSelected () {
    return this.selected;
  }
  
  
  public String getColor () {
    return color;
  };

  
  public String getName () {
    return name;
  }
  
  
  public Point setX (double x) {
    this.x = x;
    return this;
  }

  
  public Point setY (double y) {
    this.y = y;
    return this;
  }

  
  public Point setSelected (boolean selected) {
    this.selected = selected;
    return this;
  }
  
  
  public Point setSliced (boolean sliced) {
    this.sliced = sliced;
    return this;
  }
  
  
  public Point setColor (String color) {
    this.color = color;
    return this;
  }
  
  
  public Point setName (String name) {
    this.name = name;
    return this;
  }
  
  
  public boolean isSliced () {
    return sliced;
  }
  
  @XmlElement
  public Double x;
  @XmlElement
  public Double y;
  public Boolean selected, sliced;
  public String color;
  public String name;

}