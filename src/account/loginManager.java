package account;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import databaseConnect.DB_API;
import objects.restaurant;

@WebServlet("/loginManager")
public class loginManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		if( validateUser(request.getParameter("restname"), request.getParameter("password")) ){
			HttpSession session = request.getSession();
			session.setAttribute("theUser", DB_API.getRestaurant(request.getParameter("restname")));
			response.sendRedirect("./home.jsp");
		}
		else{ response.sendRedirect("./login.html?error=1"); }
	}
	
	//check if password exists (by extension, the account)
	private static boolean validateUser(String restname, String password){
		String pass = DB_API.getPassword(restname);	
		if( pass == null ){ return false; }
		if( !pass.equals(password) ){ return false; }
		return true;
	}
	
	//login check
	public static restaurant checkLogin(HttpServletRequest request){
 		HttpSession session = request.getSession(true);
 		restaurant theUser = (restaurant) session.getAttribute("theUser");
 		if(theUser == null){return null;} 
 		return theUser;
 	}
	
}
