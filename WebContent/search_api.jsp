<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="databaseConnect.DB_API" %>
<%@ page import="objects.*" %>
<%! 
	String searchTerm; String searchType; String data; String searchFor; 
	dish tempDish; restaurant tempRestaurant;
%>
<%
	//log request
		System.out.println("search_api request");

	//allow access
		String clientOrigin = request.getHeader("origin");
		response.setHeader("Access-Control-Allow-Origin",clientOrigin);
		response.setHeader("Access-Control-Allow-Methods","POST");
		response.setHeader("Access-Control-Allow-Headers","Content-Type");
		response.setHeader("Access-Control--Max-Age","86400");
		
	//Fill 'searchTerm', 'searchType' and 'searchFor' with appropiate data from the URL
		if(request.getParameter("searchTerm") != null && request.getParameter("searchTerm").length() != 0 ){ searchTerm = request.getParameter("searchTerm");}
		else{ searchTerm = "";}
		if( request.getParameter("searchType") != null && request.getParameter("searchType").length() != 0 ){ searchType = request.getParameter("searchType"); }
		else{ searchType = "all"; }
		if( request.getParameter("searchFor") != null && request.getParameter("searchFor").length() != 0 ){ searchFor = request.getParameter("searchFor"); }
		else{ searchFor = "dish"; }

	//perform actual search
		String[] results = DB_API.search(searchTerm, searchType, searchFor);
		if(results == null){System.out.println("search.jsp: search error"); return;}
	
	//compile JSON into one big array for the JS
		data = "[";
		for(int a = 0; a < results.length; a++){
			if(searchFor.equals("dish")){
				tempDish = DB_API.getDish(results[a]);
				data += DB_API.toJSON(tempDish);
				data += ",";
				data += DB_API.toJSON(DB_API.getRestaurant(tempDish.getRestname()));
				if(a != results.length-1){ data += ","; }
			}
			else if(searchFor.equals("restaurant")){
				data += DB_API.toJSON(DB_API.getRestaurant(results[a]));
				if(a != results.length-1){ data += ","; }
			}
			else{
				System.out.println("unknown search performed; results:");
				System.out.println(results[a]);
			}
		}
		data += "]";
	
	//log end of request
		System.out.println("request complete");
%>
<%= data %>