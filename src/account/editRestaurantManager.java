package account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import databaseConnect.DB_API;
import objects.restaurant;

@WebServlet("/editRestaurantManager")
public class editRestaurantManager extends HttpServlet { 
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//validate user
			restaurant theUser = loginManager.checkLogin(request); if( theUser == null ){ response.sendRedirect("index.html"); return; }
			
		//make restaurant, validate, add to database and forward to home (or fail back to edit account)
			//check to see if the password box was left empty, if so don't change the password
				String newPassword = theUser.getPassword();
				if( request.getParameter("password").length() != 0 ){ newPassword = request.getParameter("password"); }
			
			restaurant Restaurant = new restaurant(request.getParameter("restname"),request.getParameter("name"),request.getParameter("address"),request.getParameter("email"),newPassword);	
			int message = restaurant.validate(Restaurant);
			if( message == 0 ){ DB_API.editObject(Restaurant); response.sendRedirect("./home.jsp?message=0"); }
			else{ response.sendRedirect("./editAccount.jsp?message="+message); }
	}

}
