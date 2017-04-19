package databaseConnect;

import java.util.*;
import objects.*;

//frontend class for connect.java, creating the functions required for this site with the function components from connect.java
public class DB_API extends connect{
	
	//check
		public static Boolean check(String type, String id){
			if(type.equals("restaurant")){ return checkFor("ee_user.walshb43_PTIYF_restaurant","restname = '" + id + "'"); }
			else if(type.equals("dish")){ return checkFor("ee_user.walshb43_PTIYF_dish","id = '" + id + "'"); }
			return null;
		}
	
	//get Object
		public static String getPassword(String restname){
			//catch *	
				if( restname.equals("*") ){ System.out.println("databaseConnect::DB_API::getPassword: " + noAsterisksMessage); return null;}
			
			//collect password
				HashMap<String,String>[] map = getObjects(new String[]{"password"},"ee_user.walshb43_PTIYF_restaurant","restname = '" + restname + "'");
	
			//check for multiple results
				if(map.length > 1){ System.out.println("databaseConnect::DB_API::getPassword: more than one password found for restname: " + restname); return null;}
			//check for none
				if(map.length == 0){ System.out.println("databaseConnect::DB_API::getPassword: no password found for the restname: " + restname); return null;}
	
			return map[0].get("password");
		}
		
		public static restaurant getRestaurant(String restname){ 
			//set up variables
				String[] keys = {"restname","name","address","email","password"};
				String whereString = "restname = '" + restname + "'";
				HashMap<String,String>[] map = getObjects(keys,"ee_user.walshb43_PTIYF_restaurant",whereString);
			
			//check for multiple results
				if(map.length > 1){ System.out.println("databaseConnect::DB_API::getRestaurant: more than one object found for restname: " + restname); return null;}
			//check for none
				if(map.length == 0){ System.out.println("databaseConnect::DB_API::getRestaurant: no object found for the restname: " + restname); return null;}
			
			//create and return object
				return new restaurant(map[0].get("restname"),map[0].get("name"),map[0].get("address"),map[0].get("email"),map[0].get("password"));	
		}
		public static dish getDish(String id){ 
			//set up variables
				String[] keys = {"id","restname","name","price","description"};
				String whereString = "id = '" + id + "'";
				HashMap<String,String>[] map = getObjects(keys,"ee_user.walshb43_PTIYF_dish",whereString);
		
			//check for multiple results
				if(map.length > 1){ System.out.println("databaseConnect::DB_API::getPassword: more than one dish found for id: " + id); return null;}
			//check for none
				if(map.length == 0){ System.out.println("databaseConnect::DB_API::getPassword: no dish found for the id: " + id); return null;}
			
			//create and return object
				return new dish(map[0].get("id"),map[0].get("restname"),map[0].get("name"),map[0].get("price"),map[0].get("description"));	
		}
	
	//search
		public static String[] search(String searchTerm, String searchType, String searchFor){
			//set up variables
				HashMap<String,String>[] map = null;
				ArrayList<String> results = new ArrayList<String>();
				String key = "", table = "", join = "", where = "";
				searchTerm = apostropheApostrapizer(searchTerm);
				
				//select what key to look for
					if(searchFor.equals("dish")){
						key = "id";
					}
					else if(searchFor.equals("restaurant")){
						key = "ee_user.walshb43_PTIYF_restaurant.restname";
					}
			
				//select what search to perform
					if(searchType.equals("all")){
						table = "ee_user.walshb43_PTIYF_dish";
						join = "ee_user.walshb43_PTIYF_restaurant on ee_user.walshb43_PTIYF_dish.restname = ee_user.walshb43_PTIYF_restaurant.restname";
						where = ""
								+ "   lower(ee_user.walshb43_PTIYF_dish.name) 			like lower('%" + searchTerm + "%')"
								+ "or lower(ee_user.walshb43_PTIYF_dish.description) 	like lower('%" + searchTerm + "%')"
								+ "or lower(ee_user.walshb43_PTIYF_restaurant.name) 	like lower('%" + searchTerm + "%')"
								+ "or lower(ee_user.walshb43_PTIYF_restaurant.address)	like lower('%" + searchTerm + "%')"
								+ "";
					}
					else if(searchType.equals("dishName")){
						table = "ee_user.walshb43_PTIYF_dish";
						join = "ee_user.walshb43_PTIYF_restaurant on ee_user.walshb43_PTIYF_dish.restname = ee_user.walshb43_PTIYF_restaurant.restname";
						where = ""
								+ "lower(ee_user.walshb43_PTIYF_dish.name) 			like lower('%" + searchTerm + "%')"
								+ "";
					}
					else if(searchType.equals("dishDescription")){
						table = "ee_user.walshb43_PTIYF_dish";
						join = "ee_user.walshb43_PTIYF_restaurant on ee_user.walshb43_PTIYF_dish.restname = ee_user.walshb43_PTIYF_restaurant.restname";
						where = ""
								+ "lower(ee_user.walshb43_PTIYF_dish.description) 	like lower('%" + searchTerm + "%')"
								+ "";
					}
					else if(searchType.equals("restaurantName")){
						table = "ee_user.walshb43_PTIYF_dish";
						join = "ee_user.walshb43_PTIYF_restaurant on ee_user.walshb43_PTIYF_dish.restname = ee_user.walshb43_PTIYF_restaurant.restname";
						where = ""
								+ "lower(ee_user.walshb43_PTIYF_restaurant.name) 	like lower('%" + searchTerm + "%')"
								+ "";
					}
					else if(searchType.equals("restaurantAddress")){
						table = "ee_user.walshb43_PTIYF_dish";
						join = "ee_user.walshb43_PTIYF_restaurant on ee_user.walshb43_PTIYF_dish.restname = ee_user.walshb43_PTIYF_restaurant.restname";
						where = ""
								+ "lower(ee_user.walshb43_PTIYF_restaurant.address)	like lower('%" + searchTerm + "%')"
								+ "";
					}
					
				//perform search
					map = DB_API.getObjects(new String[]{key},table,where,join,true);
					if(map == null){System.out.println("databaseConnect::DB_API::search: search error"); return null;}
					
				//fix key (if necessary)	
					key = key.split("\\.")[key.split("\\.").length -1];
					
				//extract keys and return
					for(int a = 0; a < map.length; a++){ results.add(map[a].get(key)); }
					return results.toArray(new String[results.size()]);
		}
		
		public static String findNextDishID(){
			 //look for available dishID
			 	int newDishID = 0; int loopLimit = 1000;
			 	
			 //check(String type, String id)
				 while( newDishID < loopLimit && check("dish",""+newDishID) ){
					 newDishID++;
					 if(newDishID >= loopLimit){ System.out.println("dish id space full"); return null;}
				 }
			
			return ""+newDishID;
		}
	
	//add Object
		public static void addObject(restaurant restaurant){
			String[] itemNames = new String[]{"restname","name","address","email","password"};
			String[] itemValues = new String[]{restaurant.getRestname(),restaurant.getName(),restaurant.getAddress(),restaurant.getEmail(),restaurant.getPassword()};
			addObject(itemNames, itemValues, "ee_user.walshb43_PTIYF_restaurant");
		}
		public static void addObject(dish dish){
			String[] itemNames = new String[]{"id","restname","name","price","description"};
			String[] itemValues = new String[]{dish.getId(),dish.getRestname(),dish.getName(),dish.getPrice(),dish.getDescription()};
			addObject(itemNames, itemValues, "ee_user.walshb43_PTIYF_dish");
		}
	
	//edit Object
		public static void editObject(restaurant restaurant){
			String[] itemNames = new String[]{"restname","name","address","email","password"};
			String[] itemValues = new String[]{restaurant.getRestname(),restaurant.getName(),restaurant.getAddress(),restaurant.getEmail(),restaurant.getPassword()};	
			editObjects(itemNames, itemValues, "ee_user.walshb43_PTIYF_restaurant", "restname = '" + restaurant.getRestname() + "'");	
		}
		public static void editObject(dish dish){
			String[] itemNames = new String[]{"id","restname","name","price","description"};
			String[] itemValues = new String[]{dish.getId(),dish.getRestname(),dish.getName(),dish.getPrice(),dish.getDescription()};
			editObjects(itemNames, itemValues, "ee_user.walshb43_PTIYF_dish", "id = '" + dish.getId() + "'");
		}
	
	//delete Object
		public static int deleteObject(String type, String id){
			if(type.equals("restaurant")){ return deleteObjects("ee_user.walshb43_PTIYF_restaurant","restname = '" + id + "'"); }
			else if(type.equals("dish")){ return deleteObjects("ee_user.walshb43_PTIYF_dish","id = '" + id + "'"); }
			return 1;
		}
		public static int deleteObject(restaurant restaurant){ return deleteObjects("ee_user.walshb43_PTIYF_restaurant"," restname = '" + restaurant.getRestname() + "'"); }
		public static int deleteObject(dish dish){ return deleteObjects("ee_user.walshb43_PTIYF_dish"," restname = '" + dish.getId() + "'"); }
	
	//toJSON		
		private static String quoteSlasher(String input){
			if(input == null || input.length() == 0){return "";}
			
			for(int a = 0; a < input.length(); a++){
				if(input.charAt(a) == '"'){
					input = input.substring(0,a) + "\\" + input.substring(a,input.length());
					a++;
				}
			}
	
			return input; 
		}
		
		public static String apostropheApostrapizer(String input){
			if(input == null || input.length() == 0){return "";}
			
			for(int a = 0; a < input.length(); a++){
				if(input.charAt(a) == '\''){
					input = input.substring(0,a) + "'" + input.substring(a,input.length());
					a++;
				}
			}
	
			return input;
		}
		
		public static String toJSON(restaurant restaurant){ 
			return "{"
				+ "\"restname\":\""+restaurant.getRestname()+"\","
				+ "\"name\":\""+quoteSlasher(restaurant.getName())+"\","
				+ "\"address\":\""+quoteSlasher(restaurant.getAddress())+"\","
				+ "\"email\":\""+restaurant.getEmail()+"\""
				+ "}";
		}
		public static String toJSON(dish dish){
			return "{"
				+ "\"id\":\""+dish.getId()+"\","
				+ "\"restname\":\""+dish.getRestname()+"\","
				+ "\"name\":\""+quoteSlasher(dish.getName())+"\","
				+ "\"price\":\""+dish.getPrice()+"\","
				+ "\"description\":\""+quoteSlasher(dish.getDescription())+"\""
				+ "}";
		}
}
