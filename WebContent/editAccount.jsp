<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="objects.*" %>
<%@ page import="databaseConnect.DB_API" %>
<%@ page import="account.*" %>
<% 
	//validate user
		restaurant theUser = loginManager.checkLogin(request); if( theUser == null ){ response.sendRedirect("index.html"); return; }
		
	//get restaurant data	
		String JSONdata = DB_API.toJSON(theUser);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Account Settings</title>
	
    <link rel="stylesheet" type="text/css" href="./style.css">
    <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
   
    <script>
		function Go(){//initial function ran after loading
    		//collect restaurant data, and place these details into the text boxes
			var data = <%= JSONdata %>;
			document.getElementById('formInput_name').value = data.name;
			document.getElementById('formInput_address').value = data.address;
			document.getElementById('formInput_restname').value = data.restname;
			document.getElementById('formInput_email').value = data.email;

        	//look for an error message in the URL, and display the correct text messgae if one is found
	        document.getElementById('errorMessage').innerHTML = "";
	        var url = window.location.href;
	        if(url.split("?").length <= 1){return;}
	
	        url = url.split("?")[url.split("?").length-1];
	
	        switch(url[8]){
	        	case "1": document.getElementById('errorMessage').innerHTML = "username missing"; break;
	            case "2": document.getElementById('errorMessage').innerHTML = "restaurant name missing"; break;
	            case "3": document.getElementById('errorMessage').innerHTML = "e-mail missing"; break;
	            case "4": document.getElementById('errorMessage').innerHTML = "password missing"; break;
	            case "5": document.getElementById('errorMessage').innerHTML = "only number, the letters A-Z and underscore are allowed in usernames"; break;
	            case "6": document.getElementById('errorMessage').innerHTML = "your email's going to need both an '@' symbol and '.' in there somewhere"; break;
        	}	
		}
		
		//validy check
        function check(){
            document.getElementById('errorMessage').innerHTML = "";

            //check if the email has '@' and dot symbols
            if( !(new RegExp(".@")).test(document.getElementById('formInput_email').value) && document.getElementById('formInput_email').value.length != 0 ){
                document.getElementById('errorMessage').innerHTML = "your email's going to need both an '@' symbol and '.' in there somewhere";
                return;
            }
        }
    </script>
	
</head>
<body onload='Go()'>
	
    <section class='card' style='text-align:center;'>
    	<h1 class='mainTitle'>Account Details</h1>
        
	 	<form method='POST' action='./editRestaurantManager' name='updateRestaurantForm' onchange='check()'>
            <p class='formInputText'>Name: <input id='formInput_name' type='text' name='name'/></p>
            <p class='formInputText'>Address: <input id='formInput_address' type='text' name='address'/></p>
            <p class='formInputText'>Unique Username: <input id='formInput_restname' type='text' name='restname' readonly/></p>
            <p class='formInputText'>e-mail: <input id='formInput_email' type='text' name='email'/></p>
            <p class='formInputText'>Password: <input id='formInput_password' type='password' name='password'/></p>
            <input class='button' type='submit' value='update'/>
        </form>
        <p id='errorMessage' class='errorMessage'></p>
        
	 	<form method='POST' action='./deleteRestaurantManager' name='deleteRestaurantForm'>
	 		<input class='dangerButton' type='submit' value='delete account' />
	 	</form>
    </section>	

</body>
</html>