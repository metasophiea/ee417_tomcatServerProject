<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="objects.*" %>
<%@ page import="databaseConnect.DB_API" %>
<%@ page import="account.*" %>
<% 
	//validate user
		restaurant theUser = loginManager.checkLogin(request); if( theUser == null ){ response.sendRedirect("index.html"); return; }
		
	//get dish data
		String JSONdata = DB_API.toJSON(DB_API.getDish(request.getParameter("item")));		
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Edit Dish</title>
	
    <link rel="stylesheet" type="text/css" href="./style.css">
    <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
    
    <script>
    	function Go(){//initial function ran after loading
    		//collect dish data, and place these details into the text boxes
			var data = <%= JSONdata %>;
			document.getElementById('formInput_id').value = data.id;
			document.getElementById('formInput_name').value = data.name;
			document.getElementById('formInput_price').value = parseInt(data.price)/100;
			document.getElementById('formInput_description').value = data.description;

        	//look for an error message in the URL, and display the correct text messgae if one is found
	        document.getElementById('errorMessage').innerHTML = "";
	        var url = window.location.href;
	        if(url.split("?").length <= 1){return;}
	
	        url = url.split("?")[url.split("?").length-1];
	
	        switch(url[8]){
	        	case "1": document.getElementById('errorMessage').innerHTML = "name missing"; break;
	            case "2": document.getElementById('errorMessage').innerHTML = "price missing"; break;
	            case "3": document.getElementById('errorMessage').innerHTML = "price error"; break;
	        }
    	}
    	
    	//validy check
        function check(){
            document.getElementById('errorMessage').innerHTML = "";
            
            //check if price value contains anything other than number and a dot, or if there's too many dots
            if( ((new RegExp("[^0-9.]")).test(document.getElementById('formInput_price').value) && document.getElementById('formInput_price').value.length != 0) ||
            	(document.getElementById('formInput_price').value.split('.').length > 2 && document.getElementById('formInput_price').value.length != 0)
            		){
                document.getElementById('errorMessage').innerHTML = "price error";
                return;
            }
        }
    	
    </script>
    
</head>
<body onload='Go()'>

    <section class='card' style='text-align:center;'>
        <h1 class='mainTitle'>Add Dish</h1>
        <form method='POST' action='./editDishManager' name='editDishForm' onchange='check()'>
        	<input id='formInput_id' type='text' name='id' hidden/>
            <p class='formInputText'>Name: <input id='formInput_name' type='text' name='name'/></p>
            <p class='formInputText'>Price: <input id='formInput_price' type='text' name='price' value='0.00'/></p>
            <p class='formInputText'>Description: <input id='formInput_description' type='text' name='description'/></p>
            <input class='button' type='submit' value='update'/>
        </form>
        <p id='errorMessage' class='errorMessage'></p>
    </section>
    
</body>
</html>