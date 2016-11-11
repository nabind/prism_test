package bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rsdata")
public class RootTO {

	private List<PortalTO> portalList;

	public List<PortalTO> getPortalList() {
		return portalList;
	}

	@XmlElement(name = "zrow")
	public void setPortalList(List<PortalTO> portalList) {
		this.portalList = portalList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RootTO [portalList=").append(portalList).append("]");
		return builder.toString();
	}

}
