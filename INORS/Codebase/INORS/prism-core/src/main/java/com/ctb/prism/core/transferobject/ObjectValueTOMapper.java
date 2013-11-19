package com.ctb.prism.core.transferobject;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;

public class ObjectValueTOMapper implements RowMapper<ObjectValueTO> {
	
	private static final IAppLogger logger = 
			LogFactory.getLoggerInstance(ObjectValueTOMapper.class.getName());
 
	public ObjectValueTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ObjectValueTO objectValueTO = new ObjectValueTO();
		try{
			objectValueTO.setValue(rs.getString("VALUE"));
			objectValueTO.setName(rs.getString("NAME"));
			try{
				objectValueTO.setOther(rs.getString("OTHER"));
			}catch(Exception e){
				logger.log(IAppLogger.INFO, "No column name is specified for OTHER: "+e);
			}
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "Error occurred in ObjectValueTOMapper: "+e);
			e.printStackTrace();
		}
		return objectValueTO;
	}

}
