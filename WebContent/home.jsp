<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="objects.*" %>
<%@ page import="databaseConnect.DB_API" %>
<%@ page import="account.*" %>
<% 
	//validate user
		restaurant theUser = loginManager.checkLogin(request); if( theUser == null ){ response.sendRedirect("index.html"); return; }
%>
		

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Home</title>
	
    <link rel="stylesheet" type="text/css" href="./style.css">
    <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
    
    <script>
    	function Go(){//initial function ran after loading
    		//use search systems to generate list of users dishes
    		search("<%= theUser.getName() %>","restaurantName","dish");
    	}

    	//call search_api.jsp, sending it the search to be performed
        function search(searchTerm,searchType,searchFor){
            var xhttp = new XMLHttpRequest();
            xhttp.onloadend = function(){ processData(xhttp); }
            xhttp.val_searchFor = searchFor;
            xhttp.open('get',"./search_api.jsp?searchTerm="+searchTerm+"&searchType="+searchType+"&searchFor="+searchFor,true);
      	  	xhttp.send();
        }

        function processData(xhttp){
        	//extract JSON data and clear whatever was being shown	
	            var data = JSON.parse(xhttp.responseText);
	            document.getElementById('searchResults').innerHTML = "";

	        //depending on what was being searched for, produce cards for the results
	            switch(xhttp.val_searchFor){
	                case "dish": 
	                    for(var a = 0; a < data.length; a+=2){
	                        var article = document.createElement('article');
	                            article.setAttribute('class','card');
	
	                        var title = document.createElement('h2');
	                            title.setAttribute('class','searchResult_title');
	                            title.innerHTML = data[a].name;
	                        var restaurantName = document.createElement('h3');
	                            restaurantName.setAttribute('class','searchResult_restaurantName');
	                            restaurantName.innerHTML = data[a+1].name;
	                        var description = document.createElement('p');
	                            description.setAttribute('class','searchResult_description');
	                            description.innerHTML = data[a].description;
	                        var price = document.createElement('p');
	                            price.setAttribute('class','searchResult_price');
	                            price.innerHTML = "€" + coinageTocost(data[a].price);
	                            
	                       	var spacer = document.createElement('br');            
	                            
	                        var edit = document.createElement('a');
	                        	edit.setAttribute('class','button');
	                        	edit.href = "./editDish.jsp?item="+data[a].id;
	                        	edit.innerHTML = "edit";
	                        var remove = document.createElement('a');
	                        	remove.setAttribute('class','button');
	                        	remove.href = "./removeDishManager?item="+data[a].id;
	                        	remove.innerHTML = "remove";                  	   	
	
	                        article.appendChild(title);   
	                        article.appendChild(restaurantName);              
	                        article.appendChild(description);
	                        article.appendChild(price);
	                        article.appendChild(spacer);
	                        article.appendChild(edit);
	                        article.appendChild(remove);
	                                            
	                        document.getElementById('searchResults').appendChild(article);
	                    }
	                break;
	                default:
	                	console.log("Search error");
	                    console.log(data);
	                break;
	            }
	        
	       	//if there were no results, produce "no results" card
            if(data.length == 0){
                var article = document.createElement('article');
                    article.setAttribute('class','card');

                var NRF = document.createElement('p');
                    NRF.setAttribute('class','searchResult_price');
                    NRF.innerHTML = "no results found";

                article.appendChild(NRF);
                    
                document.getElementById('searchResults').appendChild(article);
            }
            
        }

      	//tool for converting from coinage values
        	function coinageTocost(coinage){ return (parseInt(coinage)/100).toFixed(2); }

    </script>
	
</head>
<body onload='Go()'>

	<!-- addDish, editAccount, logout -->
	    <section id='header'>
	        <table cellspacing='0'style='padding: 0% 1%; width: 100%;'><tr>
	            <td> <h1 class='mainTitle'><%= theUser.getName() %></h1> </td>
	            <td style='width: 76px;'> <a href='./addDish.jsp' class='button'>add dish</a> </td>
	            <td style='width: 70px;'> <a href='./editAccount.jsp' class='button'>settings</a> </td>
				<td style='width: 80px;'> <a href='./logoutManager' class='button'>sign out</a> </td>
			</tr></table>
	    </section>
	    
	<!-- results section -->
	    <section id='searchResults'></section>

</body>
</html>