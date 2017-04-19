package account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import databaseConnect.DB_API;
import objects.*;

@WebServlet("/addRestaurantManager")
public class addRestaurantManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//reverse validate user
			restaurant theUser = loginManager.checkLogin(request); if( theUser != null ){ response.sendRedirect("index.html"); return; }
			
		//check if restname is available
			if(DB_API.getPassword(request.getParameter("restname")) != null){
				response.sendRedirect("./createAccount.html?message="+7); return;
			}
			
		//make restaurant, validate, add to database and forward to home (or fail back to create account)
			restaurant Restaurant = new restaurant(request.getParameter("restname"),request.getParameter("name"),request.getParameter("address"),request.getParameter("email"),request.getParameter("password"));	
			int message = restaurant.validate(Restaurant);
			if( message == 0 ){ 
				DB_API.addObject(Restaurant); 
				HttpSession session = request.getSession();
				session.setAttribute("theUser", Restaurant);
				response.sendRedirect("./home.jsp?message=0");
			}
			else{ response.sendRedirect("./createAccount.html?message="+message); }
	}

}
