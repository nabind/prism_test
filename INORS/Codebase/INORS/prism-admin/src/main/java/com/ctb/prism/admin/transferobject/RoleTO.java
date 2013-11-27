package com.ctb.prism.admin.transferobject;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS version 1.1
 */
public class RoleTO extends BaseTO {

	private static final long serialVersionUID = 1L;
	private long roleId;
	private String roleName;
	private String roleDescription;
	private String label;
	private String defaultSelection;
	private boolean enabled;
	private List<UserTO> userList = new ArrayList<UserTO>();

	public List<UserTO> getUserList() {
		return userList;
	}

	public void setUserList(List<UserTO> userList) {
		this.userList = userList;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getDefaultSelection() {
		return defaultSelection;
	}

	public void setDefaultSelection(String defaultSelection) {
		this.defaultSelection = defaultSelection;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
