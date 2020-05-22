package entity;

public class User {
	private int id;
	private String username;
	private String password;
	private int max_space;
	private int used_space;
	
	
	public User(int id, String username, String password, int max_space, int used_space) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.max_space = max_space;
		this.used_space = used_space;
	}
	
	public static String getStandardSize(int size) {
		if(size < 1024) {
			return String.valueOf(size) + "B";
		}
		else if(size < 1024 * 1024){
			return String.valueOf(size / 1024) + "KB";
		}
		else {
			return String.valueOf(size / (1024 * 1024)) + "MB";
		}
	}
	
	public String getUsagePersent() {
		return (int)((double)used_space / max_space * 100) + "%";
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public int getMax_space() {
		return max_space;
	}


	public void setMax_space(int max_space) {
		this.max_space = max_space;
	}


	public int getUsed_space() {
		return used_space;
	}


	public void setUsed_space(int used_space) {
		this.used_space = used_space;
	}

	
}
