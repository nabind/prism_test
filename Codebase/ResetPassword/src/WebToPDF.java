import java.text.DecimalFormat;


public class WebToPDF {

	/**
	 * @param args
	 */
	/*public static void main(String[] args) throws IOException, DocumentException{
		String inputFile = "test.htm";
		String url = new File(inputFile).toURI().toURL().toString();
        String outputFile = "firstdoc.pdf";
        OutputStream os = new FileOutputStream(outputFile);
        
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);
        renderer.layout();
        renderer.createPDF(os);
        
        os.close();

	}*/
	
	public static  void main (String args[]) {
		double size = 0.00d;
		DecimalFormat f = new DecimalFormat("##.##");
		size = (double)(9410 / 1024) / 1024;
		if (size < 1) {
			size = (double) 9410 / 1024;
			System.out.println((f.format(size) + " K"));
		} else {
			System.out.println((f.format(size) + " M"));
		}
	}
	

}
