
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;



public class SvgToPdf {
	
	

	 public void paint(Graphics2D g2d) {
		    g2d.setPaint(Color.red);
		    g2d.fill(new Rectangle(10, 10, 100, 100));
		  }
	 
	
	
	 public static void main(String[] argv) throws TranscoderException, FileNotFoundException, UnsupportedEncodingException, SVGGraphics2DIOException {
	        Transcoder transcoder = new PDFTranscoder();
	        TranscoderInput transcoderInput = new TranscoderInput(new FileInputStream(new File("C:\\Users\\233208.INDIA\\Downloads\\test.svg")));
	        TranscoderOutput transcoderOutput = new TranscoderOutput(new FileOutputStream(new File("C:\\Users\\233208.INDIA\\Downloads\\HtmlToSvg.pdf")));
	        transcoder.transcode(transcoderInput, transcoderOutput);
	        
	        
	       /* // Get a DOMImplementation.
	        DOMImplementation domImpl =
	          GenericDOMImplementation.getDOMImplementation();

	        // Create an instance of org.w3c.dom.Document.
	        String svgNS = "http://www.w3.org/2000/svg";
	        Document document = domImpl.createDocument(svgNS, "svg", null);

	        // Create an instance of the SVG Generator.
	        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

	        // Ask the test to render into the SVG Graphics2D implementation.
	        SvgToPdf test = new SvgToPdf();
	        test.paint(svgGenerator);

	        // Finally, stream out SVG to the standard output using
	        // UTF-8 encoding.
	        boolean useCSS = true; // we want to use CSS style attributes
	        Writer out = new OutputStreamWriter(System.out, "UTF-8");
	        svgGenerator.stream(out, useCSS);*/
	    }
}
