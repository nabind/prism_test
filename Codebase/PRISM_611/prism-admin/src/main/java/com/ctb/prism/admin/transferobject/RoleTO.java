package com.ctb.prism.admin.transferobject;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author TCS
 * @version 1.1
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

	/**
	 * @return
	 */
	public List<UserTO> getUserList() {
		return userList;
	}

	/**
	 * @param userList
	 */
	public void setUserList(List<UserTO> userList) {
		this.userList = userList;
	}

	/**
	 * @return
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return
	 */
	public String getRoleDescription() {
		return roleDescription;
	}

	/**
	 * @param roleDescription
	 */
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	/**
	 * @return
	 */
	public String getDefaultSelection() {
		return defaultSelection;
	}

	/**
	 * @param defaultSelection
	 */
	public void setDefaultSelection(String defaultSelection) {
		this.defaultSelection = defaultSelection;
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

}
