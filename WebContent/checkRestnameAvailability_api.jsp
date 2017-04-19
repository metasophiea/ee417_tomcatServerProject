<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="databaseConnect.DB_API" %>

<%
	//allow access
		String clientOrigin = request.getHeader("origin");
		response.setHeader("Access-Control-Allow-Origin",clientOrigin);
		response.setHeader("Access-Control-Allow-Methods","POST");
		response.setHeader("Access-Control-Allow-Headers","Content-Type");
		response.setHeader("Access-Control--Max-Age","86400");

	String returnMessage = "{\"result\":\"free\"}";
	if(DB_API.getPassword(request.getParameter("restname")) != null){ returnMessage = "{\"result\":\"taken\"}"; }
%>

<%= returnMessage %>