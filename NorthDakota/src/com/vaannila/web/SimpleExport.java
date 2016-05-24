package com.vaannila.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.one2team.highcharts.server.export.ExportType;
import org.one2team.highcharts.server.export.HighchartsExporter;
import org.one2team.highcharts.shared.ChartOptions;
import org.one2team.highcharts.shared.Jsonify;

public class SimpleExport {

	/*public static void main(String[] args) {
        try 
        {
        	
        	

        	// ...
	        try {
	            URL url = new URL("http://127.0.0.1:8080/NorthDakota/process/welcome.htm");
	            URLConnection conn = url.openConnection();
	            System.out.println(conn);
	            conn.connect();
	            System.out.println("connected");
	            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	            String line;

	            while ((line = reader.readLine()) != null) {
	                System.out.println(line);
	            }
	            reader.close();

	        } catch (MalformedURLException e) {
	        	e.printStackTrace();
	            // ...
	        } catch (IOException e) {
	        	e.printStackTrace();
	            // ...
	        }
        	System.out.println("starting");
        	URL url = new URL("http://127.0.0.1:8080/NorthDakota/process/welcome.htm").toURI().toURL();
        	WebClient webClient = new WebClient(); 
        	HtmlPage page = webClient.getPage(url);

        	OutputStream os = null;
        	try{
        	   os = new FileOutputStream("c:/test.pdf");

        	   ITextRenderer renderer = new ITextRenderer();
        	   renderer.setDocument(page,url.toString());
        	   renderer.layout();
        	   renderer.createPDF(os);
        	} finally{
        	   if(os != null) os.close();
        	}
        }
        catch(Exception exc) {
        	exc.printStackTrace();
        }
    }*/
	
	private static BigDecimal rounded(BigDecimal aNumber){
	    return aNumber.setScale(0, BigDecimal.ROUND_HALF_EVEN);
	  }
	private static BigDecimal getPercentageChange(BigDecimal current, BigDecimal total){
	    BigDecimal fractionalChange = current.divide(
	    		total, 2, BigDecimal.ROUND_HALF_EVEN
	    );
	    return rounded( fractionalChange.multiply(new BigDecimal("100")) );
	  }
	
	public static void main (String[] args) {
		
		
		
		
		int total = 2900;
		int current = 280;
		
		float ptg = 6/29*100;
		
		System.out.println("% age -> " + getPercentageChange(new BigDecimal(current), new BigDecimal(total)));
		
		
		
		
		// This executable expects an export directory as input
		File exportDirectory = new File ("c:/temp");
		
		final SamplesFactory highchartsSamples = SamplesFactory.getSingleton ();
		/*
		// ====================================================================
		// ChartOptions creation
		// ----------------------
		//  The createHighchartsDemoColumnBasic method describes the creation of 
		//   a java chartOption. It is a java equivalent to javascript Highcharts sample
		//   (see http://highcharts.com/demo/column-basic)
		ChartOptions chartOptions1 = highchartsSamples.createColumnBasic ();

		// ====================================================================
		// Chart export
		// ----------------
		// Inputs :
		//    1. chartOptions : the java ChartOptions to be exported,
		//    2. exportFile  : file to export to.
		HighchartsExporter<ChartOptions> pngExporter = ExportType.png.createExporter ();
		pngExporter.export (chartOptions1, null, new File (exportDirectory, "column-basic.png"));
		
//		// ====================================================================
//		// Another example using the same exporter
//		// ---------------------------------------
		ChartOptions chartOptions2 = highchartsSamples.createPieChart ();
		pngExporter.export (chartOptions2, null, new File (exportDirectory, "pie-chart.png"));
//		
//		// ====================================================================
//		// An example exporting to JPEG
//		// ---------------------------------------
		ChartOptions chartOptions3 = highchartsSamples.createTimeDataWithIrregularIntervals ();
		final HighchartsExporter<ChartOptions> jpegExporter = ExportType.jpeg.createExporter ();
		jpegExporter.export (chartOptions3, null, new File (exportDirectory, "time-data-with-irregular-intervals.jpeg"));
		*/
		// ====================================================================
		// Chart export with a json input (instead of a java one)
		// ----------------
		// Inputs :
		//    1. chartOptions : the json ChartOptions to be exported,
		//    2. exportFile  : file to export to.
		
		
		String chartOption = highchartsSamples.createJsonColumnBasicOrg ();
		HighchartsExporter<String> pngFromJsonExporter = ExportType.svg.createJsonExporter ();
		pngFromJsonExporter.export (chartOption, null, new File (exportDirectory, "_column-basic-from-json.svg"));
		
		/*
		ChartOptions chartOptions3 = highchartsSamples.createTimeDataWithIrregularIntervals ();
		final HighchartsExporter<ChartOptions> jpegExporter = ExportType.jpeg.createExporter ();
		jpegExporter.export (chartOptions3, null, new File (exportDirectory, "time-data-with-irregular-intervals.jpeg"));*/
		
		// ====================================================================
		// json export
		// ----------------
		// Inputs :
		//    1. chartOptions : chartOptions1
		/*Jsonify jsonify = (Jsonify) highchartsSamples.createColumnBasic ();
		String json = jsonify.toJson ();
		System.out.println("json "+json);
		pngFromJsonExporter.export (json, null, new File (exportDirectory, "column-basic-from-jsonified-java.png"));*/
	}


}
