package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import entity.User;
import entity.FileBean;
import DAO.FileDAO;
import DAO.UserDAO;

public class FileDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		String path = request.getContextPath();
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		// 获取要删除文件的各种信息和当前用户、数据库访问模块等
		String id = request.getParameter("id");
		User user = (User)session.getAttribute("user");
		FileDAO fileDAO = (FileDAO)session.getAttribute("fileDAO");
		FileBean file = fileDAO.getFileById(id);
		
		// 执行文件删除操作
		int status = fileDAO.deleteFile(id);
		
		// 如果删除操作返回0或-2作为数据库更改错误处理
		if(status == 0 || status == -2) {
			out.println("<script>alert('删除失败" + status + "'); window.location='/FileStorage/index.jsp';</script>");
			out.close();
		}
		else {
			// 删除操作成功执行后，修改当前用户使用空间，保存修改后的用户对象和文件访问对象
			int space = user.getUsed_space() - file.getSize();
			user.setUsed_space(space);
			UserDAO.setUsedSpace(user.getUsername(), space);
			session.setAttribute("fileDAO", fileDAO);
			session.setAttribute("user", user);
			response.sendRedirect(path + "/index.jsp");
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
