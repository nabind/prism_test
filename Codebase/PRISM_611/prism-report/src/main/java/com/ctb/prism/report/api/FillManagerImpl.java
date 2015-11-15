/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package com.ctb.prism.report.api;

//import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.context.ApplicationContext;

import com.ctb.prism.core.util.ApplicationContextProvider;
import com.ctb.prism.report.dao.IReportDAO;

public class FillManagerImpl implements IFillManager
{
	
	/**
	 * @throws SQLException 
	 * @see #fill(JasperReport, Map, Connection)
	 */
	public JasperPrint fillReport(
		JasperReport jasperReport, 
		Map<String,Object> parameters
		) throws JRException, SQLException {
		//Connection conn = null;
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		IReportDAO reportDao = (IReportDAO) appContext.getBean("reportDAO");
		try {
			return reportDao.getFilledReportNoCache(jasperReport, parameters);
			//return reportDao.getFilledReport(jasperReport, parameters);
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}/* finally {
			if(conn != null) try {conn.close();} catch (SQLException e) {}
		}*/
		
	}

	
}
