package com.bcs.codgen.util;

import java.io.IOException;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bcs.codgen.model.JdbcConfig;

/**
 * Jdbc操作工具类
 * @author 黄天政
 *
 */
public class JdbcUtil {
	//private static final Log logger = LogFactory.getLog(JdbcUtil.class);
	/**
	 * 从结果集里当前行某一列数据，列号index从1开始
	 * @param rs
	 * @param index 列号 从1开始， 如1，2，3....
	 * @return
	 * @throws SQLException
	 */
	public static Object getResultSetValue(ResultSet rs, int index)
	throws SQLException
	{
		Object obj = rs.getObject(index);
		if (obj instanceof Blob) {
			obj = rs.getBytes(index);
		}
		else if (obj instanceof Clob) {
			obj = clobToString(rs.getClob(index));
		}
		else if ((obj != null) && (obj.getClass().getName().startsWith("oracle.sql.TIMESTAMP"))) {
			obj = rs.getTimestamp(index);
		}
		else if ((obj != null) && (obj.getClass().getName().startsWith("oracle.sql.DATE"))) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if (("java.sql.Timestamp".equals(metaDataClassName)) || ("oracle.sql.TIMESTAMP".equals(metaDataClassName)))
			{
				obj = rs.getTimestamp(index);
			}
			else {
				obj = rs.getDate(index);
			}
		}
		else if ((obj != null) && (obj instanceof Date) && 
				("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index)))) {
			obj = rs.getTimestamp(index);
		}

		return obj;
	}
	/**
	 * 把Oracle的Clob类型转化为String
	 * @param clob
	 * @return
	 */
	public static String clobToString(java.sql.Clob clob){
		String str="";
		Reader inStream;
		try {
			inStream = clob.getCharacterStream();
			char[] c = new char[(int) clob.length()];
			inStream.read(c);
			//data是读出并需要返回的数据，类型是String
			str = new String(c);
			inStream.close();
			return str;
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static void safelyClose(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void safelyClose(PreparedStatement pstmt) {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void safelyClose(Statement stmt) {
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void safelyClose(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void safelyClose(ResultSet rs,Statement stmt){
		safelyClose(rs);
		safelyClose(stmt);
	}
	public static void safelyClose(ResultSet rs,Statement stmt,Connection conn) {
		safelyClose(rs);
		safelyClose(stmt);
		safelyClose(conn);
	}
	public static void safelyClose(ResultSet rs,PreparedStatement pstmt,Connection conn) {
		safelyClose(rs);
		safelyClose(pstmt);
		safelyClose(conn);
	}
	public static void safelyClose(PreparedStatement pstmt, Connection conn) {
		safelyClose(pstmt);
		safelyClose(conn);
	}

	public static void safelyClose(Statement stmt, Connection conn) {
		safelyClose(stmt);
		safelyClose(conn);
	}
	
	public static Connection getConn(JdbcConfig jdbcConfig){
		String driver = jdbcConfig.getDriver();
		String url = jdbcConfig.getUrl();
		String user =jdbcConfig.getUser();
		String password =jdbcConfig.getPassword(); 
		try {
			//System.out.println("数据库驱动="+driver);
			Class.forName(driver).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, password);
			if(con!=null){
				//System.out.println("取得jdbc数据连接成功！");
			}else{
				System.out.println("取得jdbc数据连接失败！"+jdbcConfig);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
		
	}
}
