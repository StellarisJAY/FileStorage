package entity;

public class FileBean {
	private int id;
	private String filename;
	private String type;
	private int size;
	private String uploadDate;
	private String url;
	
	public FileBean(int id, String filename, String type, int size, String uploadDate, String url) {
		super();
		this.id = id;
		this.filename = filename;
		this.type = type;
		this.size = size;
		this.uploadDate = uploadDate;
		this.url = url;
	}
	
	public String getStandardFileSize() {
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
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setFileSize(int size) {
		this.size = size;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
