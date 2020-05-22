package util;
import java.sql.*;

public class DBUtil {
	private static final String url = "**********";
	private static final String user = "****";
	private static final String password = "**************";
	private Connection conn = null;
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		if(conn == null) {
			try {
				conn = DriverManager.getConnection(url, user, password);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	public ResultSet executeQuery(String sql) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery(sql);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet executeQuery(String sql, String...params) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = getConnection().prepareStatement(sql);
			for(int i = 1; i <= params.length; i++) {
				stmt.setString(i,  params[i-1]);
			}
			rs = stmt.executeQuery();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public int executeUpdate(String sql, String...params) {
		int status = 0;
		PreparedStatement stmt = null;
		try {
			stmt = getConnection().prepareStatement(sql);
			for(int i = 1; i <= params.length; i++) {
				stmt.setString(i, params[i - 1]);
			}
			status = stmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return status;
	}
}
