import java.io.UnsupportedEncodingException;


public class ACSIIConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  String s = "&#40;831)920-0434";
		 try {
			byte[] bytes = s.getBytes("US-ASCII");
			System.out.println(bytes.length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
