package com.ctb.prism.report.transferobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Clob;
import org.springframework.jdbc.core.RowMapper;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.core.constant.IApplicationConstants;

public class ManageMessageTOMapper implements RowMapper<ManageMessageTO> {
	
	private static final IAppLogger logger = 
			LogFactory.getLoggerInstance(ManageMessageTOMapper.class.getName());
 
	public ManageMessageTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		String message = "";
		ManageMessageTO manageMessageTO = new ManageMessageTO();
		try{
			message = Utils.convertClobToString((Clob) rs.getClob("MESSAGE")).trim();
			manageMessageTO.setMessage(("".equals(message)) ? IApplicationConstants.EMPTY_MESSAGE : message);
			manageMessageTO.setMessageTypeDesc(rs.getString("MESSAGE_DESC").trim());
			manageMessageTO.setMessageTypeName(rs.getString("MESSAGE_NAME").trim());
			manageMessageTO.setMessageTypeId(rs.getLong("MESSAGE_TYPEID"));
			manageMessageTO.setMessageType(rs.getString("MESSAGE_TYPE").trim());
			manageMessageTO.setReportId(rs.getLong("REPORTID"));
			manageMessageTO.setActivationStatus(null != rs.getString("ACTIVATION_STATUS") ? rs.getString("ACTIVATION_STATUS").trim() : IApplicationConstants.DEFAULT_VALUE_DRM_CHECKBOX);
			manageMessageTO.setCustProdIdHidden(rs.getLong("CUST_PROD_ID"));
			if((null!=rs.getString("MESSAGE_TYPE").trim()) && (rs.getString("MESSAGE_TYPE").trim().equals("GSCM")))
			{
				manageMessageTO.setReportName(IApplicationConstants.GENERIC_REPORT_NAME);
			}
			if((null!=rs.getString("MESSAGE_TYPE").trim()) && (rs.getString("MESSAGE_TYPE").trim().equals("PSCM")))
			{
				manageMessageTO.setReportName(IApplicationConstants.PRODUCT_SPECIFIC_REPORT_NAME);
			}
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "Error occurred in ManageMessageTOMapper: "+e);
			e.printStackTrace();
		}
		return manageMessageTO;
	}
}
