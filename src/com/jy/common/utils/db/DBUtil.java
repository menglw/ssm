package com.jy.common.utils.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jy.common.mybatis.Page;
import com.jy.common.utils.PropertyUtil;
import com.jy.common.utils.base.Const;

/**
 * 数据库操作JDBC工具类 License：京缘网络
 */
public class DBUtil {

	private final static String TYPE_MYSQL = "mysql";

	private final static String TYPE_ORACLE = "oracle";

	private final static String TYPE_SQLSERVER = "sqlserver";

	private final static String DB = "jdbc.db.";

	private final static String DB_USERNAME = "username";

	private final static String DB_PASSWORD = "password";

	private final static String DB_DRIVER = "driver";

	private final static String DB_URL = "url";

	private final static String DB_IP = "ip";

	private final static String DB_PORT = "port";

	private final static String DB_TYPE = "type";

	private final static String DB_DBNAME = "dbName";

	private final static String REGEX_TYPE = "jdbc:(.*)://";

	private final static String REGEX_MYSQL = "jdbc:mysql://(.*?):(.*)/(.*)\\?";

	private final static String REGEX_ORACLE = "jdbc:oracle:thin:@(.*?):(.*):(.*)";

	private final static String REGEX_SQLSERVER = "jdbc:sqlserver://(.*?):(.*);DatabaseName\\=(.*)";

	private final static String CONN_MYSQL = "jdbc:mysql://";

	private final static String CONN_ORACLE = "jdbc:oracle:thin:@";

	private final static String CONN_SQLSERVER = "jdbc:sqlserver://";

	private final static String SELECT_FROM = "select * from ";

	private static Map<String, String> dbConfig = new HashMap<String, String>();

	private static String dbtype;


	public static synchronized Map<String, String> getDBConfig() {
		if (dbConfig == null || dbConfig.size() == 0) {
			if (dbConfig == null)
				dbConfig = new HashMap<String, String>();
			Map<String, String> propConfig = PropertyUtil.getPropertyMap(Const.DB_CONFIG);

			String propUrl = propConfig.get(DB + DB_URL);
			Matcher m = Pattern.compile(REGEX_TYPE).matcher(propUrl);
			while (m.find())
				dbtype = m.group(1);
			dbConfig.put(DB_TYPE, dbtype);
			if (TYPE_MYSQL.equals(dbtype)) {
				dbConfig.put(DB_DRIVER, propConfig.get(DB + DB_DRIVER));
				dbConfig.put(DB_USERNAME, propConfig.get(DB + DB_USERNAME));
				dbConfig.put(DB_PASSWORD, propConfig.get(DB + DB_PASSWORD));
				dbConfig.put(DB_PASSWORD, propConfig.get(DB + DB_PASSWORD));
				Matcher m_mysql = Pattern.compile(REGEX_MYSQL).matcher(propUrl);
				while (m_mysql.find()) {
					dbConfig.put(DB_IP, m_mysql.group(1));
					dbConfig.put(DB_PORT, m_mysql.group(2));
					dbConfig.put(DB_DBNAME, m_mysql.group(3));
				}
			} else if (TYPE_ORACLE.equals(dbtype)) {
				dbConfig.put(DB_DRIVER, propConfig.get(DB + DB_DRIVER));
				dbConfig.put(DB_USERNAME, propConfig.get(DB + DB_USERNAME));
				dbConfig.put(DB_PASSWORD, propConfig.get(DB + DB_PASSWORD));
				dbConfig.put(DB_PASSWORD, propConfig.get(DB + DB_PASSWORD));
				Matcher m_oracle = Pattern.compile(REGEX_ORACLE).matcher(propUrl);
				while (m_oracle.find()) {
					dbConfig.put(DB_IP, m_oracle.group(1));
					dbConfig.put(DB_PORT, m_oracle.group(2));
					dbConfig.put(DB_DBNAME, m_oracle.group(3));
				}
			} else if (TYPE_SQLSERVER.equals(dbtype)) {
				dbConfig.put(DB_DRIVER, propConfig.get(DB + DB_DRIVER));
				dbConfig.put(DB_USERNAME, propConfig.get(DB + DB_USERNAME));
				dbConfig.put(DB_PASSWORD, propConfig.get(DB + DB_PASSWORD));
				dbConfig.put(DB_PASSWORD, propConfig.get(DB + DB_PASSWORD));
				Matcher m_sqlserver = Pattern.compile(REGEX_SQLSERVER).matcher(propUrl);
				while (m_sqlserver.find()) {
					dbConfig.put(DB_IP, m_sqlserver.group(1));
					dbConfig.put(DB_PORT, m_sqlserver.group(2));
					dbConfig.put(DB_DBNAME, m_sqlserver.group(3));
				}
			}
		}
		return dbConfig;
	}

	public static Connection getDBConn() throws Exception {
		Map<String, String> config = getDBConfig();
		Connection conn = getConn(config.get(DB_DRIVER), dbtype, config.get(DB_USERNAME), config.get(DB_PASSWORD),
				config.get(DB_IP), config.get(DB_PORT), config.get(DB_DBNAME));
		return conn;
	}


	public static Connection getConn(String driver, String type, String username, String password, String ip,
			String port, String databaseName) throws Exception {
		if (TYPE_MYSQL.equals(type)) {
			Class.forName(driver);
			return DriverManager.getConnection(
					CONN_MYSQL + ip + ":" + port + "/" + databaseName + "?user=" + username + "&password=" + password);
		} else if (TYPE_ORACLE.equals(type)) {
			Class.forName(driver);
			return DriverManager.getConnection(CONN_ORACLE + ip + ":" + port + ":" + username, databaseName, password);
		} else if (TYPE_SQLSERVER.equals(type)) {
			Class.forName(driver);
			return DriverManager.getConnection(CONN_SQLSERVER + ip + ":" + port + "; DatabaseName=" + databaseName,
					username, password);
		} else {
			return null;
		}
	}


	public static List<String> getTables() throws Exception {
		return getTables(getDBConn());
	}


	public static List<String> getTables(Connection conn) throws Exception {
		if (TYPE_MYSQL.equals(dbtype)) {
			return getTablesMysql(conn);
		} else if (TYPE_ORACLE.equals(dbtype)) {
			return getTablesOracle(conn);
		} else if (TYPE_SQLSERVER.equals(dbtype)) {
			return getTablesBySqlserver(conn);
		}
		return null;
	}


	public static List<String> getTablesMysql(Connection conn) {
		try {
			List<String> listTb = new ArrayList<String>();
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				listTb.add(rs.getString(3));
			}
			return listTb;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConn(conn);
		}
		return null;
	}


	public static List<String> getTablesOracle(Connection conn) {
		try {
			List<String> listTb = new ArrayList<String>();
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				listTb.add(rs.getString(3));
			}
			return listTb;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConn(conn);
		}
		return null;
	}


	public static List<String> getTablesBySqlserver(Connection conn) {
		try {
			List<String> listTb = new ArrayList<String>();
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				if ("sys".equals(rs.getString(2)))
					continue;
				listTb.add(rs.getString(3));
			}
			return listTb;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConn(conn);
		}
		return null;
	}


	public static List<TColumn> getTableColumnsByTableName(String tableName) throws Exception {
		return getTableColumnsByTableName(getDBConn(), tableName);
	}


	public static List<TColumn> getTableColumnsByTableName(Connection conn, String tableName) throws Exception {
		return getTableColumns(conn, SELECT_FROM + tableName);
	}


	public static List<TColumn> getTableColumns(String sqlStr) throws Exception {
		return getTableColumns(getDBConn(), sqlStr);
	}


	public static List<TColumn> getTableColumns(Connection conn, String sqlStr) throws Exception {
		PreparedStatement pstmt = (PreparedStatement) conn
				.prepareStatement(SELECT_FROM + "(" + sqlStr + ") tcolumns where 0!=0");
		pstmt.execute(); 
		List<TColumn> columns = new ArrayList<TColumn>(); 
		ResultSetMetaData rsmd = (ResultSetMetaData) pstmt.getMetaData();
		for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
			columns.add(new TColumn(rsmd.getColumnName(i), rsmd.getColumnTypeName(i), rsmd.getPrecision(i),
					rsmd.getScale(i), rsmd.isNullable(i)));
		}
		return columns;
	}


	public static List<List<Object>> queryByTableName(String tableName) throws Exception {
		return queryByTableName(getDBConn(), tableName);
	}


	public static List<List<Object>> queryByTableName(Connection conn, String tableName) throws Exception {
		return query(conn, SELECT_FROM + tableName);
	}


	public static List<List<Object>> query(String sqlStr) throws Exception {
		return query(getDBConn(), sqlStr);
	}


	public static List<List<Object>> query(Connection conn, String sqlStr) throws Exception {
		List<TColumn> columns = new ArrayList<TColumn>(); 
		List<List<Object>> dataList = new ArrayList<List<Object>>(); 
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getDBConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlStr);
			columns = getTableColumns(conn, sqlStr);

			List<Object> columnList = new ArrayList<Object>(); 
			for (TColumn tc : columns) {
				columnList.add(tc.getName());
			}
			dataList.add(columnList);
			while (rs.next()) {
				List<Object> onedataList = new ArrayList<Object>(); 
				for (int i = 1; i < columns.size() + 1; i++) {
					onedataList.add(rs.getObject(i));
				}
				dataList.add(onedataList); 
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConn(conn);
		}
		return dataList;
	}


	public static Page<List<Object>> queryByTableName(Connection conn, String tableName, Page<List<Object>> page)
			throws Exception {
		return query(conn, SELECT_FROM + tableName, page);
	}


	public static Page<List<Object>> queryByTableName(String tableName, Page<List<Object>> page) throws Exception {
		return query(getDBConn(), SELECT_FROM + tableName, page);
	}


	public static Page<List<Object>> query(String sqlStr, Page<List<Object>> page) throws Exception {
		return query(getDBConn(), sqlStr, page);
	}


	public static Page<List<Object>> query(Connection conn, String sqlStr, Page<List<Object>> page) throws Exception {
		List<TColumn> columns = new ArrayList<TColumn>(); // 存放字段名
		List<List<Object>> dataList = new ArrayList<List<Object>>(); // 存放数据(从数据库读出来的一条条的数据)
		Statement stmt = null;
		ResultSet rs = null;
		String sqlPage = null;
		try {
			conn = getDBConn();
			stmt = conn.createStatement();

			rs = stmt.executeQuery(getCountSql(sqlStr));
			while (rs.next()) {
				page.setTotalRecord(rs.getInt(1));
				break;
			}

			if (TYPE_MYSQL.equals(dbtype)) {
				sqlPage = getMysqlPageSql(page, new StringBuffer(sqlStr));
			} else if (TYPE_ORACLE.equals(dbtype)) {
				sqlPage = getOraclePageSql(page, new StringBuffer(sqlStr));
			} else if (TYPE_SQLSERVER.equals(dbtype)) {
				sqlPage = getSqlserverPageSql(page, new StringBuffer(sqlStr));
			}

			rs = stmt.executeQuery(sqlPage);

			columns = getTableColumns(conn, sqlStr);

			List<Object> columnList = new ArrayList<Object>(); 
			for (TColumn tc : columns) {
				columnList.add(tc.getName());
			}
			dataList.add(columnList);
			while (rs.next()) {
				List<Object> onedataList = new ArrayList<Object>(); 
				for (int i = 1; i < columns.size() + 1; i++) {
					onedataList.add(rs.getObject(i));
				}
				dataList.add(onedataList); 
			}
			page.setResults(dataList);
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConn(conn);
		}
		return page;
	}


	public static int operate(String sqlStr) throws Exception {
		return operate(getDBConn(), sqlStr);
	}


	public static int operate(Connection conn, String sqlStr) throws Exception {
		int res = 0;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			res = stmt.executeUpdate(sqlStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConn(conn);
		}
		return res;
	}


	private static String getMysqlPageSql(Page<?> page, StringBuffer sqlBuffer) {
		// 计算第一条记录的位置，Mysql中记录的位置是从0开始的。
		int offset = (page.getPageNum() - 1) * page.getPageSize();
		sqlBuffer.insert(0, SELECT_FROM + "(").append(" ) spage limit ").append(offset).append(",")
				.append(page.getPageSize());
		return sqlBuffer.toString();
	}


	private static String getOraclePageSql(Page<?> page, StringBuffer sqlBuffer) {
		// 计算第一条记录的位置，Oracle分页是通过rownum进行的，而rownum是从1开始的
		int offset = (page.getPageNum() - 1) * page.getPageSize() + 1;
		sqlBuffer.insert(0, "select u.*, rownum r from (").append(") u where rownum < ")
				.append(offset + page.getPageSize());
		sqlBuffer.insert(0, "select * from (").append(") where r >= ").append(offset);
		return sqlBuffer.toString();
	}


	private static String getSqlserverPageSql(Page<?> page, StringBuffer sqlBuffer) {
		// 计算第一条记录的位置，Sqlserver中记录的位置是从0开始的。
		int startRowNum = (page.getPageNum() - 1) * page.getPageSize() + 1;
		int endRowNum = startRowNum + page.getPageSize();
		sqlBuffer
				.insert(0,
						"select appendRowNum.row,* from (select ROW_NUMBER() OVER (order by (select 0)) AS row,* from (")
				.append(") as innerTable)as appendRowNum where appendRowNum.row >= ").append(startRowNum)
				.append(" AND appendRowNum.row <= ").append(endRowNum);
		return sqlBuffer.toString();
	}


	private static String getCountSql(String sql) {
		return "select count(*) from (" + sql + ") as countRecord";
	}

	public static void closeConn(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
