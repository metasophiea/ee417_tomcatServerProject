package account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import databaseConnect.DB_API;
import objects.restaurant;

@WebServlet("/deleteRestaurantManager")
public class deleteRestaurantManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//validate user
			restaurant theUser = loginManager.checkLogin(request); if( theUser == null ){ response.sendRedirect("index.html"); return; }
			
		//sign out and delete account
			HttpSession session = request.getSession();
			session.removeAttribute("theUser");
			DB_API.deleteObject(theUser); 
			response.sendRedirect("./");
	}		

}
