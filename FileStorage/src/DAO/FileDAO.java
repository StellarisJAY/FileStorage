package DAO;
import java.sql.*;
import java.util.*;
import entity.FileBean;
import util.DBUtil;
import java.io.*;

public class FileDAO {
	private int user_id;
	private HashMap<String, FileBean> fileList = null;
	/**
	 * 构造函数，每一个用户session都会保存一个对应的文件dao对象
	 * @param user_id
	 */
	public FileDAO(int user_id) {
		this.user_id = user_id;
	}
	
	/**
	 * 获取所有文件，该过程在用户登录后马上进行，之后的文件都是从内存中读取而非数据库
	 * 之后只有在删除和上传文件时才涉及到数据库操作
	 * @return
	 */
	public HashMap<String, FileBean> getAllFiles(){
		if(fileList == null) {
			fileList = new HashMap<String, FileBean>();
			String sql = "select * from tb_file where id in (select file_id from tb_user_file where user_id=?);";
			ResultSet rs = null;
			DBUtil db = new DBUtil();
			try {
				rs = db.executeQuery(sql, String.valueOf(user_id));
				while(rs.next()) {
					int id = Integer.valueOf(rs.getString("id"));
					String filename = rs.getString("filename");
					String type = rs.getString("type");
					int size = Integer.valueOf(rs.getString("size"));
					String uploadDate = rs.getString("upload_date");
					String url = rs.getString("url");
					
					FileBean file = new FileBean(id, filename, type, size, uploadDate, url);
					fileList.put(filename, file);
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return fileList;
	}
	
	/**
	 * 通过文件名检索文件
	 * @param filename
	 * @return
	 */
	public FileBean getFileByName(String filename) {
		return getAllFiles().get(filename);
	}
	
	/**
	 * 上传新文件的信息到数据库，并添加到当前用户的文件列表
	 * @param file
	 * @return
	 */
	public int uploadNewFile(FileBean file) {
		DBUtil db = new DBUtil();
		int status = 0;
		String sql = "insert tb_file values(?, ?, ?, ?, ?, ?);";
		// 从对象获得信息
		String id = String.valueOf(file.getId());
		String filename = file.getFilename();
		String type = file.getType();
		String size = String.valueOf(file.getSize());
		String uploadDate = file.getUploadDate();
		String url = file.getUrl();
		
		// 执行数据库更新操作，返回执行状态
		status = db.executeUpdate(sql, id, filename, type, size, uploadDate, url);
		if(status != 0) {
			// 文件上传成功后尝试上传用户与文件联系
			status = db.executeUpdate("insert tb_user_file values(?, ?);", String.valueOf(user_id), id);
			if(status != 0) {
				
				// 上述数据库操作成功后，添加文件到列表
				getAllFiles().put(filename, file);
			}
		}
		return status;
	}
	
	/**
	 * 通过 ID 获得文件对象
	 * @param id
	 * @return
	 */
	public FileBean getFileById(String id) {
		Set<String> keySet = fileList.keySet();
		for(String key : keySet) {
			if(fileList.get(key).getId() == Integer.valueOf(id)) {
				return fileList.get(key);
			}
		}
		return null;
	}
	
	/**
	 * 获取当前数据库中所有文件总数
	 * @return
	 */
	public int getAllFileCount() {
		String sql = "select count(id) from tb_file;";
		ResultSet rs = null;
		DBUtil db = new DBUtil();
		
		rs = db.executeQuery(sql);
		try {
			while(rs.next()) {
				return rs.getInt("count(id)");
			}
			return -1;
		} catch (SQLException e) {
			return -1;
		}
	}
	
	/**
	 * 文件ID生成器，这里考虑到文件数量并不会太多直接采用id递增的方式
	 * @return
	 */
	public int getNextId() {
		String sql = "select max(id) from tb_file;";
		ResultSet rs = null;
		DBUtil db = new DBUtil();
		
		rs = db.executeQuery(sql);
		try {
			while(rs.next()) {
				return rs.getInt("max(id)") + 1;
			}
			return -1;
		}
		catch(SQLException e) {
			return -1;
		}
	}
	
	/**
	 * 删除文件操作
	 * @param fileId
	 * @return
	 */
	public int deleteFile(String fileId) {
		int status = -1;
		ResultSet rs = null;
		int fileInUse = 0;
		File file = null;
		DBUtil db = new DBUtil();
		String sql = "delete from tb_user_file where file_id=? and user_id=?;";        // 从用户-文件关系表中删除文件
		String sql_check = "select count(user_id) from tb_user_file where file_id=" + fileId + ";";          // 查看是否还有用户与当前文件有联系
		
		// 执行数据库中删除操作
		status = db.executeUpdate(sql, fileId, String.valueOf(user_id));
		if(status > 0) {
			// 如果删除成功，从列表中移除文件
			Set<String> keySet = fileList.keySet();
			for(String key  : keySet) {
				if(fileList.get(key).getId() == Integer.valueOf(fileId)) {
					// 记录下文件的地址
					file = new File("//opt//tomcat//webapps//storage//" + UserDAO.getUserById(user_id).getUsername() + "//" + fileList.get(key).getFilename());
					fileList.remove(key);
					break;
				}
			}
			// 查找还有没有用户与该文件有联系，如果没有就彻底删除该文件
			rs = db.executeQuery(sql_check);
			try {
				while(rs.next()) {
					fileInUse = rs.getInt("count(user_id)");
				}
				
				if(fileInUse == 0) {
					String sql_delete = "delete from tb_file where id=?;";    // 从文件表移除文件的sql语句
					
					if(file.exists()) {
						System.out.println("found file");
						boolean is = file.delete();      // 从硬盘移除文件
						if(is == true) {
							System.out.println("file deleted");
						}
						
					}
					status = db.executeUpdate(sql_delete, fileId);   // 从数据库文件表移除文件
				}
			}
			catch(SQLException e) {
				return -2;
			}
		}
		return status;
	}
}
