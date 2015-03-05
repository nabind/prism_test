package com.ctb.prism.report.transferobject;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;

public class ObjectValueTOMapper implements RowMapper<ObjectValueTO> {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(ObjectValueTOMapper.class.getName());
 
	public ObjectValueTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ObjectValueTO objectValueTO = new ObjectValueTO();
		try{
			objectValueTO.setValue(rs.getString(1));
			objectValueTO.setName(rs.getString(2));
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "Error occurred in Report ObjectValueTOMapper: " + e.getMessage());
			e.printStackTrace();
		}
		return objectValueTO;
	}

}
