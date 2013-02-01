package com.bcs.codgen.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.bcs.codgen.model.JdbcConfig;
import com.bcs.codgen.service.ColumnHandler;
import com.bcs.codgen.service.DbProvider;
import com.bcs.codgen.util.JdbcUtil;

/**
 * 针对Oracle的数据库信息提供者
 * @author 黄天政
 *
 */
public class OracleProvider extends DbProvider {

	public OracleProvider(Connection conn) {
		super(conn);
	}
	
	public OracleProvider(JdbcConfig jdbcConfig) {
		super(jdbcConfig);
	}
	
	@Override
	protected Map<String, String> doGetColumnComments(String tableName) {
		Map<String, String> colComment = new LinkedHashMap<String, String>();
		String columnName = null, comment = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "select * from USER_COL_COMMENTS where TABLE_NAME='"+tableName.toUpperCase()+"'";
		try{
			stmt = getConn().createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				columnName = rs.getString("COLUMN_NAME").toLowerCase();
				comment = StringUtils.trim(rs.getString("COMMENTS"));
				colComment.put(columnName, comment);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			JdbcUtil.safelyClose(rs, stmt);
		}
		return colComment;
	}

	@Override
	protected Map<String, String> doGetTableComments() {
		Map<String, String> tableComments = new LinkedHashMap<String, String>();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "select * from USER_TAB_COMMENTS";
		try{
			stmt = getConn().createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				tableComments.put(rs.getString("TABLE_NAME").toLowerCase(), rs.getString("COMMENTS")) ;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			JdbcUtil.safelyClose(rs, stmt);
		}
		return tableComments;
	}

}
