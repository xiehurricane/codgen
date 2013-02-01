package com.bcs.codgen.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.bcs.codgen.model.JdbcConfig;
import com.bcs.codgen.service.ColumnHandler;
import com.bcs.codgen.service.DbProvider;
import com.bcs.codgen.util.JdbcUtil;

/**
 * 针对MSSQLServer2000的数据库信息提供者
 * @author 黄天政
 *
 */
public class Sql2000Provider extends DbProvider {

	public Sql2000Provider(Connection conn) {
		super(conn);
	}
	

	public Sql2000Provider(JdbcConfig jdbcConfig) {
		super(jdbcConfig);
	}


	@Override
	protected Map<String, String> doGetColumnComments(String tableName) {
		Map<String, String> colComment = new LinkedHashMap<String, String>();
		String columnName = null, comment = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT sysobjects.name as TABLE_NAME " +
			", syscolumns.name COLUMN_NAME" +
			", sysproperties.[value] AS COMMENTS " + //", CONVERT(char, sysproperties.[value]) AS COMMENTS " +
			" FROM sysproperties RIGHT OUTER JOIN sysobjects INNER JOIN syscolumns ON sysobjects.id = syscolumns.id " +
			" INNER JOIN systypes ON syscolumns.xtype = systypes.xtype ON sysproperties.id = syscolumns.id AND " +
			" sysproperties.smallid = syscolumns.colid WHERE (sysobjects.xtype = 'u' OR sysobjects.xtype = 'v') " +
			" AND (systypes.name <> 'sysname')  AND (sysobjects.name = '"+tableName+"') ";
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
		List<String> tblList = getTableNames();
		for (String tbl : tblList) {
			tableComments.put(tbl, tbl);
		}
		return tableComments;
	}

}
