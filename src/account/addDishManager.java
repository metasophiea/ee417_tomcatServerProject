package account;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import databaseConnect.DB_API;
import objects.*;

@WebServlet("/addDishManager")
public class addDishManager extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//validate user
			restaurant theUser = loginManager.checkLogin(request); if( theUser == null ){ response.sendRedirect("index.html"); return; }
			
		//check price for decimal point. If none found, add one
			String price = request.getParameter("price");
			if( !price.contains(".") ){ price += "."; }
		
		//make dish, validate, add to database and forward to home with a message (or fail back to add dish)
			dish Dish = new dish(DB_API.findNextDishID(), theUser.getRestname(), request.getParameter("name"), price, request.getParameter("description"));	
			int message = dish.validate(Dish);
			if( message == 0 ){ DB_API.addObject(Dish); response.sendRedirect("./home.jsp?message=0"); }
			else{ response.sendRedirect("./addDish.jsp?message="+message); }
	}
		
}
