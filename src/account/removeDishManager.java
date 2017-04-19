package account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import databaseConnect.DB_API;
@WebServlet("/removeDishManager")
public class removeDishManager extends HttpServlet { 
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//validate user
			if( loginManager.checkLogin(request) == null ){ response.sendRedirect("index.html"); return; }
			
		//actual delete
			response.sendRedirect("./home.jsp?message="+DB_API.deleteObject("dish",request.getParameter("item")));
	}

}
