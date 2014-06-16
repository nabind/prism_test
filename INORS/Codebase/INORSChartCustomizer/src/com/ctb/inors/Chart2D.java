/**
 * 
 */
package com.ctb.inors;

import java.text.DecimalFormat;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

/**
 * @author 353639
 *
 */
public class Chart2D implements JRChartCustomizer
{

	@Override
	public void customize(JFreeChart chart, JRChart jasperChart) {
		
		
		CategoryPlot plot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer)plot.getRenderer();
	    //DefaultCategoryDataset chrtDataset = (DefaultCategoryDataset)plot.getDataset();
	    //Integer score = (Integer)chrtDataset.getValue("Score", (String)chrtDataset.getColumnKey(0));
	    
		//To show score with % sign in the center above the bar
	    //NumberFormat percentageFormat = NumberFormat.getPercentInstance();
	    DecimalFormat percentageFormat = new DecimalFormat("#'%'");
	    renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",percentageFormat));
	    renderer.setBaseItemLabelsVisible(true);
	    renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
	    
	    //to remove the grid lines
	    plot.setRangeGridlinesVisible(false);
	    
	    //to remove extra space around the plot area
	    plot.setAxisOffset(new RectangleInsets(-5, -10, -8,  0));
	  
	    System.out.println("Chart printed!!!!");
	}
	
}
