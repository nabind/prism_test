package bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "zrow")
public class PortalTO {

	private String ows_ID;
	private String ows_Attachments;
	private String ows_LinkTitle;
	private String ows__Status;
	private String ows_Date_x0020_Requested;
	private String ows_Requestor;
	private String ows_Breif_x0020_Description;
	private String ows_Priority;
	private String ows_CAMIS;
	private String ows_Hours;
	private String ows_Requested_x0020_Completion_x0020;
	private String ows_Customer;
	private String ows_Tier_x0020_II_x0020_Resource_x00;
	private String ows_Completion_x0020_Date;
	private String ows_Comments;

	public String getOws_ID() {
		return ows_ID;
	}

	@XmlAttribute(name = "ows_ID")
	public void setOws_ID(String ows_ID) {
		this.ows_ID = ows_ID;
	}

	public String getOws_Attachments() {
		return ows_Attachments;
	}

	@XmlAttribute(name = "ows_Attachments")
	public void setOws_Attachments(String ows_Attachments) {
		this.ows_Attachments = ows_Attachments;
	}

	public String getOws_LinkTitle() {
		return ows_LinkTitle;
	}

	@XmlAttribute(name = "ows_LinkTitle")
	public void setOws_LinkTitle(String ows_LinkTitle) {
		this.ows_LinkTitle = ows_LinkTitle;
	}

	public String getOws__Status() {
		return ows__Status;
	}

	@XmlAttribute(name = "ows__Status")
	public void setOws__Status(String ows__Status) {
		this.ows__Status = ows__Status;
	}

	public String getOws_Date_x0020_Requested() {
		return ows_Date_x0020_Requested;
	}

	@XmlAttribute(name = "ows_Date_x0020_Requested")
	public void setOws_Date_x0020_Requested(String ows_Date_x0020_Requested) {
		this.ows_Date_x0020_Requested = ows_Date_x0020_Requested;
	}

	public String getOws_Requestor() {
		return ows_Requestor;
	}

	@XmlAttribute(name = "ows_Requestor")
	public void setOws_Requestor(String ows_Requestor) {
		this.ows_Requestor = ows_Requestor;
	}

	public String getOws_Breif_x0020_Description() {
		return ows_Breif_x0020_Description;
	}

	@XmlAttribute(name = "ows_Breif_x0020_Description")
	public void setOws_Breif_x0020_Description(
			String ows_Breif_x0020_Description) {
		this.ows_Breif_x0020_Description = ows_Breif_x0020_Description;
	}

	public String getOws_Priority() {
		return ows_Priority;
	}

	@XmlAttribute(name = "ows_Priority")
	public void setOws_Priority(String ows_Priority) {
		this.ows_Priority = ows_Priority;
	}

	public String getOws_CAMIS() {
		return ows_CAMIS;
	}

	@XmlAttribute(name = "ows_CAMIS")
	public void setOws_CAMIS(String ows_CAMIS) {
		this.ows_CAMIS = ows_CAMIS;
	}

	public String getOws_Hours() {
		return ows_Hours;
	}

	@XmlAttribute(name = "ows_Hours")
	public void setOws_Hours(String ows_Hours) {
		this.ows_Hours = ows_Hours;
	}

	public String getOws_Requested_x0020_Completion_x0020() {
		return ows_Requested_x0020_Completion_x0020;
	}

	@XmlAttribute(name = "ows_Requested_x0020_Completion_x0020")
	public void setOws_Requested_x0020_Completion_x0020(
			String ows_Requested_x0020_Completion_x0020) {
		this.ows_Requested_x0020_Completion_x0020 = ows_Requested_x0020_Completion_x0020;
	}

	public String getOws_Customer() {
		return ows_Customer;
	}

	@XmlAttribute(name = "ows_Customer")
	public void setOws_Customer(String ows_Customer) {
		this.ows_Customer = ows_Customer;
	}

	public String getOws_Tier_x0020_II_x0020_Resource_x00() {
		return ows_Tier_x0020_II_x0020_Resource_x00;
	}

	@XmlAttribute(name = "ows_Tier_x0020_II_x0020_Resource_x00")
	public void setOws_Tier_x0020_II_x0020_Resource_x00(
			String ows_Tier_x0020_II_x0020_Resource_x00) {
		this.ows_Tier_x0020_II_x0020_Resource_x00 = ows_Tier_x0020_II_x0020_Resource_x00;
	}

	public String getOws_Completion_x0020_Date() {
		return ows_Completion_x0020_Date;
	}

	@XmlAttribute(name = "ows_Completion_x0020_Date")
	public void setOws_Completion_x0020_Date(String ows_Completion_x0020_Date) {
		this.ows_Completion_x0020_Date = ows_Completion_x0020_Date;
	}

	public String getOws_Comments() {
		return ows_Comments;
	}

	@XmlAttribute(name = "ows_Comments")
	public void setOws_Comments(String ows_Comments) {
		this.ows_Comments = ows_Comments;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CTBPortalTO [ows_ID=").append(ows_ID)
				.append(", ows_Attachments=").append(ows_Attachments)
				.append(", ows_LinkTitle=").append(ows_LinkTitle)
				.append(", ows__Status=").append(ows__Status)
				.append(", ows_Date_x0020_Requested=")
				.append(ows_Date_x0020_Requested).append(", ows_Requestor=")
				.append(ows_Requestor).append(", ows_Breif_x0020_Description=")
				.append(ows_Breif_x0020_Description).append(", ows_Priority=")
				.append(ows_Priority).append(", ows_CAMIS=").append(ows_CAMIS)
				.append(", ows_Hours=").append(ows_Hours)
				.append(", ows_Requested_x0020_Completion_x0020=")
				.append(ows_Requested_x0020_Completion_x0020)
				.append(", ows_Customer=").append(ows_Customer)
				.append(", ows_Tier_x0020_II_x0020_Resource_x00=")
				.append(ows_Tier_x0020_II_x0020_Resource_x00)
				.append(", ows_Completion_x0020_Date=")
				.append(ows_Completion_x0020_Date).append(", ows_Comments=")
				.append(ows_Comments).append("]");
		return builder.toString();
	}

}
