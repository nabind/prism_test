package com.ctb.prism.admin.transferobject;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;

/**
 * @author TCS
 * 
 */
public class EduCenterTOMapper implements RowMapper<EduCenterTO> {
	private static final IAppLogger logger = LogFactory.getLoggerInstance(EduCenterTOMapper.class.getName());
	private final JdbcTemplate jdbcTemplate;

	/**
	 * @param jdbcTemplate
	 */
	public EduCenterTOMapper(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public EduCenterTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		EduCenterTO eduCenterTO = new EduCenterTO();
		try {
			eduCenterTO.setEduCenterId(rs.getLong("EDU_CENTERID"));
			eduCenterTO.setEduCenterCode(rs.getString("EDU_CENTERID"));
			eduCenterTO.setEduCenterName(rs.getString("EDU_CENTER_NAME"));
			eduCenterTO.setUserId(rs.getLong("USERID"));
			eduCenterTO.setUserName(rs.getString("USERNAME"));
			eduCenterTO.setFullName(rs.getString("FULLNAME"));
			eduCenterTO.setStatus(rs.getString("STATUS"));

			// fetching role for each users
			if (eduCenterTO.getUserId() != 0) {
				List<Map<String, Object>> roleListDb = jdbcTemplate.queryForList(IQueryConstants.GET_USER_ROLE, eduCenterTO.getUserId());
				if (roleListDb.size() > 0) {
					com.ctb.prism.core.transferobject.ObjectValueTO roleTO = null;
					for (Map<String, Object> roleDetails : roleListDb) {
						roleTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
						roleTO.setValue(((BigDecimal) roleDetails.get("ROLEID")).toString());
						roleTO.setName((String) roleDetails.get("ROLE_NAME"));
						eduCenterTO.getRoleList().add(roleTO);
					}
				}
			}
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred in EduCenterTOMapper: " + e);
			e.printStackTrace();
		}
		return eduCenterTO;
	}
}
