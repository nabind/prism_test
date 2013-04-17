package com.ctb.prism.core.util;

import java.text.MessageFormat;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;

/**
 * This is the manager class for performing any operation on LDAP server.
 */
@Component("ldapManager")
public class LdapManager {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(LdapManager.class.getName());

	@Autowired
	private LdapTemplate ldapTemplate;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	private String getLdapDn(String cn, String sn, String uid) {
		String dn = CustomStringUtil.appendString(
				MessageFormat.format((String) propertyLookup.get("app.ldap.dn"), cn, sn, uid),
				propertyLookup.get("app.ldap.userdn")
				);
		logger.log(IAppLogger.DEBUG, dn);
		return dn;
	}

	/**
	 * Adds a new user in LDAP server
	 * 
	 * @param cn
	 *            Common Name
	 * @param sn
	 *            Surname
	 * @param uid
	 *            User ID
	 * @param pwd
	 *            Password
	 * @return
	 * @throws Exception 
	 */
	public boolean addUser(String cn, String sn, String uid, String pwd) throws BusinessException {
		try {
			logger.log(IAppLogger.INFO, "Adding new user in LDAP. User ID - " + uid);
			String dn = getLdapDn(cn, sn, uid); //"cn=" + cn + "+sn=" + sn + "+uid=" + uid + ",ou=users,ou=system";
			DirContextAdapter context = new DirContextAdapter(dn);
			context.setAttributeValues("objectclass", new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
			context.setAttributeValue("userPassword", pwd);
			ldapTemplate.bind(context);
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Successfully added new user in LDAP. User ID - ", uid));
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, CustomStringUtil.appendString("Could not add new user in LDAP. User ID - ", uid), e);
			String msg = e.getMessage();
			if(msg != null && msg.lastIndexOf(":") != -1 && msg.lastIndexOf("]") != -1) {
				msg = msg.substring(msg.lastIndexOf(":")+2, msg.lastIndexOf("]"));
				throw new BusinessException(msg);
			}
			return false;
		}
		return true;
	}

	/**
	 * Search a user in LDAP server
	 * 
	 * @param cn
	 *            Common Name
	 * @param sn
	 *            Surname
	 * @param uid
	 *            User ID
	 * @param pwd
	 *            Password
	 * @return
	 */
	public boolean searchUser(String uid) throws Exception {
		try {
			logger.log(IAppLogger.INFO, "Searching for a user in LDAP. User ID - " + uid);
			final String BASE_DN = propertyLookup.get("app.ldap.userdn");
			AndFilter filter = new AndFilter();
			filter.and(new EqualsFilter("uid", uid));
			AttributesMapper mapper = new AttributesMapper() {
				public Object mapFromAttributes(Attributes attrs) throws NamingException {
					return attrs.get("uid").get();
				}
			};
			List userList = ldapTemplate.search(BASE_DN, filter.encode(), mapper);
			if (userList != null) {
				if (userList.size() > 0) {
					return true;
				}
			}
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Successfully Searched for a user in LDAP. User ID - " + uid));
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, CustomStringUtil.appendString("Could not Searched for a user in LDAP. User ID - " + uid), e);
			return false;
		}
		return false;
	}

	/**
	 * Update a user in LDAP server
	 * 
	 * @param cn
	 *            Common Name
	 * @param sn
	 *            Surname
	 * @param uid
	 *            User ID
	 * @param pwd
	 *            Password
	 * @return
	 */
	public boolean updateUser(String cn, String sn, String uid, String pwd) throws BusinessException {
		try {
			logger.log(IAppLogger.INFO, "Updating a user in LDAP. User ID - " + uid);
			String dn = getLdapDn(cn, sn, uid); //"cn=" + cn + "+sn=" + sn + "+uid=" + uid + ",ou=users,ou=system";
			DirContextAdapter context = new DirContextAdapter(dn);
			context.setAttributeValues("objectclass", new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
			context.setAttributeValue("userPassword", pwd);
			ldapTemplate.rebind(context);
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Successfully Updated a user in LDAP. User ID user in LDAP. User ID - ", uid));
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, CustomStringUtil.appendString("Could not Update a user in LDAP. User ID - ", uid), e);
			String msg = e.getMessage();
			if(msg != null && msg.lastIndexOf(":") != -1 && msg.lastIndexOf("]") != -1) {
				msg = msg.substring(msg.lastIndexOf(":")+2, msg.lastIndexOf("]"));
				throw new BusinessException(msg);
			}
			return false;
		}
		return true;
	}

	/**
	 * Delete a user in LDAP server
	 * 
	 * @param cn
	 *            Common Name
	 * @param sn
	 *            Surname
	 * @param uid
	 *            User ID
	 * @param pwd
	 *            Password
	 * @return
	 */
	public boolean deleteUser(String cn, String sn, String uid) throws Exception {
		try {
			logger.log(IAppLogger.INFO, "Updating a user in LDAP. User ID - " + uid);
			String dn = getLdapDn(cn, sn, uid); //"cn=" + cn + "+sn=" + sn + "+uid=" + uid + ",ou=users,ou=system";
			ldapTemplate.unbind(dn, true);
			logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Successfully deleted user in LDAP. User ID user in LDAP. User ID - ", uid));
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, CustomStringUtil.appendString("Could not delete a user in LDAP. User ID - ", uid), e);
			return false;
		}
		return true;
	}

	private Attributes buildAttributes() {
		Attributes attrs = new BasicAttributes();
		BasicAttribute ocattr = new BasicAttribute("objectclass");
		ocattr.add("person");
		ocattr.add("inetOrgPerson");
		attrs.put(ocattr);
		attrs.put("cn", "");
		attrs.put("sn", "");
		attrs.put("userPassword", "{SHA}" + "");
		attrs.put("mail", "");

		return attrs;
	}
}
