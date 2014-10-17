package com.ctb.prism.webservice.transferobject;

import java.util.Set;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.transferobject.ObjectValueTO;

/**
 * 
 * @author 796763
 *
 */
public class ReportActionTO extends BaseTO {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String status;
	private Set<ObjectValueTO> productList;
	private Set<ObjectValueTO> roleList;
	private Set<ObjectValueTO> orgLevelList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<ObjectValueTO> getProductList() {
		return productList;
	}

	public void setProductList(Set<ObjectValueTO> productList) {
		this.productList = productList;
	}

	public Set<ObjectValueTO> getRoleList() {
		return roleList;
	}

	public void setRoleList(Set<ObjectValueTO> roleList) {
		this.roleList = roleList;
	}

	public Set<ObjectValueTO> getOrgLevelList() {
		return orgLevelList;
	}

	public void setOrgLevelList(Set<ObjectValueTO> orgLevelList) {
		this.orgLevelList = orgLevelList;
	}

}
