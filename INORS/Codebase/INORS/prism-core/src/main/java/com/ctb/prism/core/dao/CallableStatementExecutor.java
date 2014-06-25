package com.ctb.prism.core.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.util.CustomStringUtil;

public class CallableStatementExecutor {
	private static final IAppLogger logger = LogFactory.getLoggerInstance(CallableStatementExecutor.class.getName());
	
	@SuppressWarnings("unchecked")
	public List<ArrayList<String>> execute(JdbcTemplate jdbcTemplate, String stotedProcedure, List<PlaceHolder> placeHolderList, Integer paramNumber, String[] aliases) {
		long start = System.currentTimeMillis();
		List<ArrayList<String>> resultList = (List<ArrayList<String>>) jdbcTemplate.execute(getCallableStatementCreator(stotedProcedure, placeHolderList), getCallableStatementCallback(paramNumber, aliases));
		long end = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "execute(" + stotedProcedure + ") : " + CustomStringUtil.getHMSTimeFormat(end - start));
		return resultList;
	}

	private CallableStatementCreator getCallableStatementCreator(final String name, final List<PlaceHolder> placeHolderList){
		return new CallableStatementCreator(){
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(name);
				for (PlaceHolder placeHolder : placeHolderList) {
					if (placeHolder.getParamType().equals("IN")) {
						if (placeHolder.getValueType().equals(oracle.jdbc.OracleTypes.NUMBER)) {
							cs.setLong(placeHolder.getParamNumber(), (Long) placeHolder.getValue());
						} else if (placeHolder.getValueType().equals(oracle.jdbc.OracleTypes.VARCHAR)) {
							cs.setString(placeHolder.getParamNumber(), (String) placeHolder.getValue());
						}
					} else if (placeHolder.getParamType().equals("OUT")) {
						cs.registerOutParameter(placeHolder.getParamNumber(), placeHolder.getValueType());
					}
				}
				return cs;
			}
		};
	}
	
	private CallableStatementCallback<Object> getCallableStatementCallback(final Integer paramNumber, final String[] aliases) {
		return new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet rs = null;
				List<ArrayList<String>> resultList = null;
				try {
					cs.execute();
					String err = cs.getString(paramNumber + 1);
					if (err != null && !err.isEmpty()) {
						logger.log(IAppLogger.ERROR, err);
					}
					rs = (ResultSet) cs.getObject(paramNumber);
					resultList = getTabularDataFromResultSet(rs, aliases);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return resultList;
			}

		};
	}
	
	private List<ArrayList<String>> getTabularDataFromResultSet(ResultSet rs, String[] aliases) throws SQLException {
		List<ArrayList<String>> tabularData = new ArrayList<ArrayList<String>>();
		// Add Rows
		while (rs.next()) { // Rows
			ArrayList<String> rowData = new ArrayList<String>();
			for (String columnAlias : aliases) { // Columns
				rowData.add(rs.getString(columnAlias));
			}
			tabularData.add(rowData);
		}
		return tabularData;
	}
	
}