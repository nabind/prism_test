import java.util.Scanner;


public class GenerateERLink {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter student_bio_id for which you want to generate URL: ");
		String studentBioId = reader.nextLine();
		if(studentBioId.isEmpty()) {
			System.out.println("student_bio_id was not provided ... exiting!!");
			System.exit(0);
		}
		
		System.out.print("Enter Student UUID for which you want to generate URL: ");
		String uuid = reader.nextLine();
		if(uuid.isEmpty()) {
			System.out.println("Student UUID was not provided ... exiting!!");
			System.exit(0);
		}
		
		System.out.print("Enter STATE CODE for which you want to generate URL: ");
		String stateCode = reader.nextLine();
		if(stateCode.isEmpty()) {
			System.out.println("STATE CODE was not provided ... exiting!!");
			System.exit(0);
		}
		
		try {
			 System.out.println("-------------------------------------------------");
			 String param = "ctbstudentid="+studentBioId+"&application_name=ERESOURCE&time_stamp=2015-11-26T00%3A42%3A45Z&uuid="+uuid+"&org_node_code="+stateCode;
			 String signature = DigitalMeasuresHMACQueryStringBuilder.getSignature(param,"ER");
			 String qaUrl = "https://prism-qa-274458748.us-east-1.elb.amazonaws.com";
			 String devUrl = "http://10.160.23.11:8080";
			 String generatedUrl = qaUrl+"/tasc/candidateReport.do?" + param + "&signature=" + signature + "&clienttype=R";
			 System.out.println(generatedUrl);
			 System.out.println("-------------------------------------------------");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR .......!......!......... ");
		} 
		
	}

}
