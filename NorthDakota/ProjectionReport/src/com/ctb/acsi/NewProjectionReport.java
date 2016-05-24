package com.ctb.acsi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;


public class NewProjectionReport implements JRChartCustomizer {
	private final float LINE_WIDTH = 3.5f;

	//Shape circle = new Ellipse2D.Float(3.0f, -3.8f, 6.5f, 6.5f);
	Shape circle = new Ellipse2D.Float(3.0f, 3.0f, 4.0f, 4.0f);
	Shape square = new Rectangle2D.Double(100, 100,100, 100);
	Color legendBorderColor = new Color(144,144,144);
	GeneralPath p0 = new GeneralPath();
	Stroke strokeGridLine = new BasicStroke( 0.5f);// for grid lines
	Font legendFont = new Font("Calibri",0,9);
		
	
	  private Shape createRightTriangle(final float s) {
		      GeneralPath p0 = new GeneralPath();
		       p0.moveTo(s,0.0f);
		       p0.lineTo(-s, -s);
		       p0.lineTo(-s, s);
		       p0.closePath();
		      return p0;
		    }
	
	@Override
	public void customize(JFreeChart chart, JRChart jasperChart) {
        CategoryPlot plot = chart.getCategoryPlot();
        chart.getLegend().setFrame(new BlockBorder(.05,.05,.05,.05,legendBorderColor));//setting the legend color
        //chart.getLegend().setItemFont(new Font("Calibri",Font.PLAIN,8));
        chart.getLegend().setItemFont(legendFont.deriveFont(Font.PLAIN, 8.5f));
        chart.getLegend().setItemLabelPadding(new RectangleInsets());//setting the padding of the legend items to zero
        plot.setBackgroundPaint(new Color(238,236,225));//setting color of the plot area
		//XYPlot plot = chart.getXYPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
       
        // 0 -->PP , 1 --> P ,2 --->A , 3 ---> SS , 4---->3YR	
        // change line color 
        renderer.setSeriesPaint(2, new Color(190,76,73));//for advanced cut score
        renderer.setSeriesPaint(1, new Color(155, 187, 89));//proficient cut score
        renderer.setSeriesPaint(0, new Color(79, 129, 189));//for partially proficient cut score
        renderer.setSeriesPaint(3, new Color(128, 100, 175));//student score
        renderer.setSeriesPaint(4, new Color(70, 170, 197));//for 3-year path
        
        
        // 0 -->PP , 1 --> P ,2 --->A , 3 ---> SS , 4---->3YR
        // change default series shape
        renderer.setSeriesShape(2, ShapeUtilities.createUpTriangle(2.0f));
        //renderer.setSeriesShape(0, CustomShape.createArrow(2.0f, 3.0f));
        renderer.setSeriesShape(0, ShapeUtilities.createDownTriangle(2.0f));
        //renderer.setSeriesShape(1, circle);//ShapeUtilities.createDiamond(4.0f)
        renderer.setSeriesShape(1, ShapeUtilities.rotateShape(circle, 4.0d, 2.0f, 3.0f));
        //renderer.setSeriesShape(3, ShapeUtilities.rotateShape(circle, 4.0d, 2.0f, 3.0f));


        // customize line width for student score
        renderer.setSeriesStroke(3, (Stroke) new BasicStroke(LINE_WIDTH) );
        // customize line width and line style for 3 year path
        /*renderer.setSeriesStroke(
                4, (Stroke) new BasicStroke(
                	LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,
                	1.0f, new float[] {6.0f, 6.0f}, 0.0f
                )
            );*/
        // customize line width and line style for 3 year path
        //renderer.setSeriesShape(4, ShapeUtilities.createDiamond(4.0f));
        
        //renderer.setSeriesShape(4, ShapeUtilities.rotateShape(createRightTriangle(4.0f), 30.0d, 2.0f, 3.0f));
        renderer.setSeriesShape(4, createRightTriangle(4.0f));
        renderer.setSeriesStroke(4,(Stroke) new BasicStroke(LINE_WIDTH,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,1.0f, new float[] {10.0f, 4.5f}, 0.0f));
        
        
        
        // 0 -->PP , 1 --> P ,2 --->A , 3 ---> SS , 4---->3YR
        // add item label and item position
        //for advance cut score
        renderer.setSeriesItemLabelGenerator(2, new StandardCategoryItemLabelGenerator());
        renderer.setSeriesPositiveItemLabelPosition(2, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6,TextAnchor.TOP_CENTER));
        renderer.setSeriesItemLabelsVisible(2, Boolean.TRUE);
        renderer.setSeriesItemLabelFont(2,new Font("Calibri",0,8));
        
        // for proficient cut score
        renderer.setSeriesItemLabelGenerator(1, new StandardCategoryItemLabelGenerator());
        //renderer.setSeriesPositiveItemLabelPosition(1, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE8,TextAnchor.CENTER_RIGHT));
        renderer.setSeriesPositiveItemLabelPosition(1, new ItemLabelPosition(ItemLabelAnchor.CENTER,TextAnchor.CENTER));
        renderer.setSeriesItemLabelsVisible(1, Boolean.TRUE);
        renderer.setSeriesItemLabelFont(1,new Font("Calibri",0,8));
        
        // for partially proficient cut score 
        renderer.setSeriesItemLabelGenerator(0, new StandardCategoryItemLabelGenerator());
        renderer.setSeriesItemLabelsVisible(0, Boolean.TRUE);
        renderer.setSeriesItemLabelFont(0,new Font("Calibri",0,8));
        
        // for student score
        renderer.setSeriesItemLabelGenerator(3, new StandardCategoryItemLabelGenerator());
        renderer.setSeriesPositiveItemLabelPosition(3, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.CENTER_LEFT));
        renderer.setSeriesItemLabelsVisible(3, Boolean.TRUE);
        renderer.setSeriesItemLabelFont(3,new Font("Calibri",1,10));

        plot.setRenderer(renderer);


        NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
       // rangeAxis.setStandardTickUnits(TickUnits.createIntegerTickUnits());
        rangeAxis.setLabelFont(new Font("Arial",Font.BOLD,11));
		rangeAxis.setTickUnit(new NumberTickUnit(50));
		rangeAxis.setTickLabelFont(new Font("Calibri",Font.PLAIN,10));
		rangeAxis.setTickMarksVisible(true);//RangeAboutValue(675, 12);//LabelFont(new Font("Calibri",1,12));
		plot.setRangeAxis(rangeAxis);
		plot.setRangeGridlineStroke(strokeGridLine);
		/*ValueAxis yValue=null;
		yValue.setUpperMargin(2);*/
		CategoryAxis domainAxis = (CategoryAxis)plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("Arial",Font.BOLD,11));
		domainAxis.setVisible(true);
		domainAxis.setAxisLineVisible(true);
		domainAxis.setTickLabelFont(new Font("Calibri",Font.PLAIN,10));
		domainAxis.setTickMarksVisible(true);
		
		//domainAxis.setAxisLinePaint(new Color(190,76,73));
		plot.setDomainAxis(domainAxis);

    }


}

