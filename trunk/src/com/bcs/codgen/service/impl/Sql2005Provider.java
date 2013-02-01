package com.bcs.codgen.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
 * 针对MSSQLServer2005的数据库信息提供者
 * @author 黄天政
 *
 */
public class Sql2005Provider extends DbProvider {

	public Sql2005Provider(Connection conn) {
		super(conn);
	}
	
	public Sql2005Provider(JdbcConfig jdbcConfig) {
		super(jdbcConfig);
	}

	@Override
	protected Map<String, String> doGetColumnComments(String tableName) {
		Map<String, String> colComment = new LinkedHashMap<String, String>();
		String columnName = null, comment = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT " +
				"cast(d.name AS VARCHAR(100) ) as TABLE_NAME," +
				//"字段序号=a.colorder," +
				"cast(a.name AS VARCHAR(100) ) as COLUMN_NAME," +
				//"标识=case   when   COLUMNPROPERTY(   a.id,a.name, 'IsIdentity ')=1   then   '√ '   else   ' '   end," +
				//"主键=case   when   exists(SELECT   1   FROM   sysobjects   where   xtype= 'PK '   and   name   in   (     SELECT   name   FROM   sysindexes   WHERE   indid   in(       SELECT   indid   FROM   sysindexkeys   WHERE   id   =   a.id   AND   colid=a.colid     )))   then   '√ '   else   ' '   end," +
				//"类型=b.name," +
				//"占用字节数=a.length," +
				//"长度=COLUMNPROPERTY(a.id,a.name, 'PRECISION ')," +
				//"小数位数=isnull(COLUMNPROPERTY(a.id,a.name, 'Scale '),0)," +
				//"允许空=case   when   a.isnullable=1   then   '√ 'else   ' '   end," +
				//"默认值=isnull(e.text, ' ')," +
				"cast(g.[value] AS VARCHAR(100) ) as COMMENTS" +
				" FROM   syscolumns   a   left   join   systypes   b   on   a.xtype=b.xusertype" +
				"          inner   join   sysobjects   d   on   a.id=d.id     and   d.xtype= 'U '   and     d.name <> 'dtproperties '" +
				//"          left   join   syscomments   e   on   a.cdefault=e.id" +
				"          left   join   sys.extended_properties   g   on   d.id=g.major_id   and   a.colid=g.minor_id"+
				" where d.name='"+tableName+"'";
				//" where   g.name= 'MS_Description ' "//注释则只返回有描述的字段
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
		String sql = "select cast(objname AS VARCHAR(200) ) as TABLE_NAME," +
				" cast(value AS VARCHAR(max) ) as COMMENTS " +
				" from fn_listextendedproperty(null,'SCHEMA','dbo','table',null,null,null)";
		try{
			stmt = getConn().createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				tableComments.put(rs.getString("TABLE_NAME"), rs.getString("COMMENTS")) ;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			JdbcUtil.safelyClose(rs, stmt);
		}
		return tableComments;
	}

	/**
	 * 获取在catalog和schema范围内的所有表和所有视图（不包括系统视图）集合。
	 */
	@Override
	public List<String> getTableNames() {
		List<String> tblList = new ArrayList<String>();
		ResultSet rs = null;
		try{
			rs = getConn().getMetaData().getTables(getCatalog(), getSchema(), null, new String[]{"TABLE", "VIEW"});
			while(rs.next()){
				if(!"INFORMATION_SCHEMA".equalsIgnoreCase(rs.getString("TABLE_SCHEM"))
						&& !"sys".equalsIgnoreCase(rs.getString("TABLE_SCHEM"))){
					tblList.add(rs.getString("TABLE_NAME"));
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			JdbcUtil.safelyClose(rs);
		}
		return tblList;
	}
	
	
}
