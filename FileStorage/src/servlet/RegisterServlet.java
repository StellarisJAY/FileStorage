package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import entity.User;
import DAO.UserDAO;


public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		// 从前端请求获得数据
		String username = request.getParameter("username");
		String password = request.getParameter("password1");
		String authCode = request.getParameter("invitecode");
		
		// 授权码：starfish
		if(!authCode.equals("starfish")) {
			out.println("<script>alert('授权码错误，注册失败'); window.location='/FileStorage/login.html';</script>");
		}
		
		// 初始的最大容量 500MB，初始已用空间0MB
		int max_size = 524288000;
		int used_size = 0;
		
		// 判断当前用户名是否已注册，已注册就返回到登陆界面
		if(UserDAO.getUserByName(username) == null) {
			// 生成用户id，默认为用户的顺序编号
			int id = UserDAO.getAllUsers().size() + 1;
			
			// 创建用户对象并加入到数据库和数据库访问对象
			User user = new User(id, username, password, max_size, used_size);
			int status = UserDAO.addNewUser(user);
			if(status == 0) {
				out.println("<script>alert('注册失败'); window.location='/FileStorage/login.html';</script>");
			}
			else {
				// 为新用户创建专属文件夹
				File file = new File("//opt//tomcat//webapps//storage//" + username);
				if(!file.exists()) {
					boolean done = file.mkdir();
					if(done == false) {
						out.println("<script>alert('文件路径创建失败'); window.location='/FileStorage/login.html';</script>");
					}
				}
				out.println("<script>alert('注册成功，点击转到登陆页面'); window.location='/FileStorage/login.html';</script>");
			}
		}
		else {
			out.println("<script>alert('当前用户名已注册'); window.location='/FileStorage/login.html';</script>");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
