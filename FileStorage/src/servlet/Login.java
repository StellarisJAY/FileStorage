package servlet;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.User;
import DAO.UserDAO;
import DAO.FileDAO;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		String path = request.getContextPath();
		System.out.println(path);
		User user = UserDAO.getUserByName(username);
		if(user == null) {
			out.println("<script>alert('用户不存在，请重试'); window.location='/FileStorage/login.html';</script>");
			out.close();
		}
		else {
			if(password.equals(user.getPassword())) {
				HttpSession session = request.getSession();
				session.setAttribute("user", user);          // 当前session中保存用户信息
				FileDAO files = new FileDAO(user.getId());   // 为当前用户创建文件dao并存在session中
				files.getAllFiles();
				session.setAttribute("fileDAO", files);
				
				response.sendRedirect("/FileStorage/index.jsp");
			}
			else {
				out.println("<script>alert('密码错误，请重试'); window.location='/FileStorage/login.html';</script>");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
