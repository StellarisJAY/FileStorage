package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.util.Iterator;
import java.util.List;
import entity.User;
import entity.FileBean;
import DAO.FileDAO;
import DAO.UserDAO;
import java.time.LocalDate;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		HttpSession session = request.getSession();
		User user = ((User)session.getAttribute("user"));              // 获取当前用户
		String username = user.getUsername();                         // 用户名
		FileDAO fileDAO = (FileDAO)session.getAttribute("fileDAO");   // 获取当前用户的文件访问对象
		
		if(isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List<FileItem> items = upload.parseRequest(request);
				Iterator<FileItem> it = items.iterator();
				while(it.hasNext()) {
					FileItem item = it.next();
					
					// 获取文件数据，创建文件对象准备写入
					String filename = item.getName();
					//String path = "D:\\";                                          // 本地调试路径
					String path = "//opt//tomcat//webapps//storage//" + username;       // 服务端的linux路径
					File file = new File(path, filename);
					
					// 生成 文件ID
					int id = fileDAO.getNextId();
					String type = "null";
					
					// 判断上传后是否会超过最大容量
					int size = (int)item.getSize();
					if(user.getUsed_space() + size > user.getMax_space()) {
						out.println("<script>alert('文件过大，已超过网盘容量上限'); window.location='/FileStorage/index.jsp';</script>");
						out.close();
					}
					else {
						String uploadDate = LocalDate.now().toString();
						String url = "文件服务器地址" + username + "/" + filename;
					
						// 上传文件到服务器
						item.write(file);
						
						// 更改已使用空间大小
						UserDAO.setUsedSpace(username, size + user.getUsed_space());
						
						// 上传文件信息到数据库
						int status = fileDAO.uploadNewFile(new FileBean(id, filename, type, size, uploadDate, url));
						
						if(status == 0) {
							//System.out.println("数据库上传失败");
							out.println("<script>alert('上传失败，数据库错误'); window.location='/FileStorage/index.jsp';</script>");
						}
					}
					
				}
				
				session.setAttribute("fileDAO", fileDAO);
				out.println("<script>alert('上传成功'); window.location='/FileStorage/index.jsp';</script>");
				out.close();
 			}
			catch(FileNotFoundException e) {
				//System.out.println("文件上传错误");
				out.println("<script>alert('上传失败'); window.location='/FileStorage/index.jsp';</script>");
			}
			catch(FileUploadException e) {
				e.printStackTrace();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
