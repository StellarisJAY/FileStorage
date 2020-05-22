package DAO;
import entity.User;
import java.util.*;
import util.DBUtil;
import java.sql.*;
public class UserDAO {
	private static HashMap<String, User> userList = null;
	
	public static HashMap<String, User> getAllUsers(){
		if(userList == null) {
			userList = new HashMap<String, User>();
			DBUtil db = new DBUtil();
			String sql = "select * from tb_user;";
			ResultSet rs = null;
			
			try {
				rs = db.executeQuery(sql);
				while(rs.next()) {
					int id = Integer.valueOf(rs.getString("id"));
					String username = rs.getString("username");
					String password = rs.getString("password");
					int max_space = Integer.valueOf(rs.getString("max_space"));
					int used_space = Integer.valueOf(rs.getString("used_space"));
					
					User user = new User(id, username, password, max_space, used_space);
					userList.put(username, user);
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return userList;
	}
	
	public static User getUserByName(String username) {
		return getAllUsers().get(username);
	}
	
	public static User getUserById(int id) {
		Set<String> keySet = getAllUsers().keySet();
		for(String key : keySet) {
			if(id == getAllUsers().get(key).getId()) {
				return getAllUsers().get(key);
			}
		}
		return null;
	}
	
	public static int addNewUser(User user) {
		int status = 0;
		DBUtil db = new DBUtil();
		
		String id = String.valueOf(user.getId());
		String username = user.getUsername();
		String password = user.getPassword();
		String max_size = String.valueOf(user.getMax_space());
		String used_space = String.valueOf(user.getUsed_space());
		
		status = db.executeUpdate("insert tb_user values(?,?,?,?,?);", id, username, password, max_size, used_space);
		if(status != 0) {
			getAllUsers().put(username, user);
		}
		return status;
	}
	
	
	public static int setUsedSpace(String username, int size) {
		if(size <= 0) return 0;
		int status = 0;
		DBUtil db = new DBUtil();
		String sql = "update tb_user set used_space=? where username=?;";
		status = db.executeUpdate(sql, String.valueOf(size), username);
		if(status != 0) {
			getAllUsers().get(username).setUsed_space(size);
		}
		return status;
	}
}
