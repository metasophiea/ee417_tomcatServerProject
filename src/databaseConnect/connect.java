package databaseConnect;

import java.sql.*;
import java.util.*;

//class used for connecting to the database
abstract class connect{
	protected static String noAsterisksMessage = "databaseConnect does not allow the use of asterisks";
	
	//classed used for actual connection, and management of that connection
	private static class connection{
		private Connection connection = null;
		private Statement statement = null;
		private ResultSet results = null;
		private PreparedStatement preparedStatement = null;
		private String JDBCUrl = "jdbc:oracle:thin:@ee417.c7clh2c6565n.eu-west-1.rds.amazonaws.com:1521:EE417";
		private String username = "ee_user";
		private String password = "ee_pass";
		
		public void call(){
			try{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(JDBCUrl, username, password);
			}catch(Exception e){System.out.println("dataBaseConnect::connect::connection::call:" +  e);}	
		}
		
		public void makePreparedStatement(String statementString){			
			try{ 
				preparedStatement = connection.prepareStatement(statementString);
				preparedStatement.clearParameters();
			}catch(SQLException e){  
				System.out.println("dataBaseConnect::connect::connection::getPreparedStatement::SQL Error:");
				System.out.println(" -> " + statementString);
				while(e != null){
					System.out.println(" -> " + e.getMessage());
					e = e.getNextException();
				}
			}
		}
		
		public void preparedStatement_setInt(int index, int value){
			try{ preparedStatement.setInt(index+1,value); }
			catch(SQLException e){  
				System.out.println("dataBaseConnect::connect::connection::preparedStatement_setInt::SQL Error:");
				while(e != null){
					System.out.println(" -> " + e.getMessage());
					e = e.getNextException();
				}
			}
		}
		
		public void preparedStatement_setString(int index, String string){
			try{ preparedStatement.setString(index+1,string); }
			catch(SQLException e){  
				System.out.println("dataBaseConnect::connect::connection::preparedStatement_setString::SQL Error:");
				while(e != null){
					System.out.println(" -> " + e.getMessage());
					e = e.getNextException();
				}
			}
		}

		public void sendPreparedStatement(){
			try{ preparedStatement.execute(); }
			catch(SQLException e){  
				System.out.println("dataBaseConnect::connect::connection::sendPreparedStatement::SQL Error:");
				while(e != null){
					System.out.println(" -> " + e.getMessage());
					e = e.getNextException();
				}
			}
		}
		
		public void sendStatement(String statementString){
			try{
				statement = connection.createStatement();
				results = statement.executeQuery(statementString);
			}catch(SQLException e){ 
				System.out.println("dataBaseConnect::connect::connection::sendStatement::SQL Error:");
				System.out.println(" -> " + statementString);
				while(e != null){
					System.out.println(" -> " + e.getMessage());
					e = e.getNextException();
				}
			}
		}
		
		public Boolean nextResult(){
			Boolean isThereANext = false;
			try{ isThereANext = results.next(); }
			catch(SQLException e){ 
				System.out.println("dataBaseConnect::connect::connection::nextResult::SQL Error:");
				while(e != null){
					System.out.println(" -> " + e.getMessage());
					e = e.getNextException();
				}
			}
			
			return isThereANext;
		}
		
		public String getString(String index){
			String response = null;
			
			try{ response = results.getString(index); }
			catch(SQLException e){ 
				System.out.println("dataBaseConnect::connect::connection::getString::SQL Error:");
				System.out.println(" -> " + index);
				while(e != null){
					System.out.println(" -> " + e.getMessage());
					e = e.getNextException();
				}
			}
			
			return response;
		}
		
		public void hangUp(){
			try{    
				if (results != null){ results.close(); }
				if (statement != null){ statement.close(); }
				if (connection != null){ connection.close(); }
			}
			catch (Exception ex) { System.out.println("dataBaseConnect::connect::connection: An error occurred while closing down connection/statement"); }
		}
	}
	
	protected static Boolean checkFor(String table, String where){
		//create statement
			String statementString = "select * from "+table+" where " + where;
			Boolean result = false;

		//make call
			connect.connection conn = new connect.connection(); 
			conn.call();
			conn.sendStatement(statementString);
		
		//check for result
			if(conn.nextResult()){result = true;}
		
		//hang up, and return whether one was found or not
			conn.hangUp();
			return result;
	}
	
	protected static int addObject(String[] itemNames, String[] values, String table){	
		//check input
			if(itemNames.length != values.length){ System.out.println("databaseConnect::connect::addObject: itemNames array is a different length to the values array length"); return 1; }
			else if(table == null || table.length() == 0){ System.out.println("databaseConnect::connect::addObject: table name error: " + table); return 2; }
			
		//make call
			connect.connection conn = new connect.connection(); 
			conn.call();
		
		//create statement
			String statementString = "insert into " + table + "("; String valueQuestionmarks = "";
			for(int a = 0; a < itemNames.length; a++){
				statementString += itemNames[a]; valueQuestionmarks += "?";
				if(a != itemNames.length-1){ statementString += ","; valueQuestionmarks += ","; }
			}
			statementString += ") values (" + valueQuestionmarks + ")";
		
		//create prepared statement	
			conn.makePreparedStatement(statementString);
			for(int a = 0; a < itemNames.length; a++){ conn.preparedStatement_setString(a, values[a]); }
		
		//send prepared statement
			conn.sendPreparedStatement();
		
		//hang up, and go home
			conn.hangUp();
			return 0;
	}
	
	protected static int editObjects(String[] itemNames, String[] newValues, String table, String where){
		//check input
			if(itemNames.length != newValues.length){ System.out.println("databaseConnect::connect::editObjects: itemNames array is a different length to the newValues array length"); return 1; }
			else if(table == null || table.length() == 0){ System.out.println("databaseConnect::connect::editObjects: table name error: " + table); return 2; }
			else if(where == null || where.length() == 0){ System.out.println("databaseConnect::connect::editObjects: where statement error: " + where); return 3;}
		
		//make call
			connect.connection conn = new connect.connection(); 
			conn.call();
		
		//create statement
			String statementString = "update " + table + " set ";
			for(int a = 0; a < itemNames.length; a++){ statementString += itemNames[a] + " = ?"; if(a != itemNames.length-1){ statementString += ","; } }
			statementString += " where " + where;
		
		//create prepared statement	
			conn.makePreparedStatement(statementString);
			for(int a = 0; a < itemNames.length; a++){ conn.preparedStatement_setString(a, newValues[a]); }
		
		//send prepared statement
			conn.sendPreparedStatement();
		
		//hang up, and go home
			conn.hangUp();	
			return 0;
	}
	
	protected static int deleteObjects(String table, String where){
		//check input
			if(table == null || table.length() == 0){ System.out.println("databaseConnect::connect::editObjects: table name error: " + table); return 1; }
			else if(where == null || where.length() == 0){ System.out.println("databaseConnect::connect::editObjects: where statement error: " + where); return 2;}
			
		//create statement
			String statementString = "delete from " + table + " where " + where;
			
		//make call
			connect.connection conn = new connect.connection(); 
			conn.call();
			conn.sendStatement(statementString);	
			
		//hang up
			conn.hangUp();	
			return 0;
	}
	
	protected static HashMap<String,String>[] getObjects(String[] itemNames, String table, String where){ return getObjects(itemNames, table, where, null, false); }
	protected static HashMap<String,String>[] getObjects(String[] itemNames, String table, String where, String join){ return getObjects(itemNames, table, where, join, false); }
	protected static HashMap<String,String>[] getObjects(String[] itemNames, String table, String where, Boolean distinct){ return getObjects(itemNames, table, where, null, distinct); }
	protected static HashMap<String,String>[] getObjects(String[] itemNames, String table, String where, String join, Boolean distinct){
		//check input
			if(itemNames.length == 0){ System.out.println("databaseConnect::connect::getObjects: itemNames array empty"); return null; }
			else if(table == null || table.length() == 0){ System.out.println("databaseConnect::connect::getObjects: table name error: " + table); return null; }
			else if(where == null || where.length() == 0){ System.out.println("databaseConnect::connect::getObjects: where statement error: " + where); return null;}
		
		//catch *	
			if( Arrays.asList(itemNames).contains("*") ){ System.out.println("databaseConnect::connect::getObjects: " + noAsterisksMessage); return null;}
			
		//setup variables
			ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> workingMap = null;
		
		//create statement
			String statementString = "select "; if(distinct){statementString += "distinct ";}
			for(int a = 0; a < itemNames.length; a++){ statementString += itemNames[a]; if(a != itemNames.length-1){ statementString += ","; } }
			statementString += " from " + table;
			if( join != null ){ if( join.length() != 0 ){statementString += " join " + join; }}
			statementString += " where " + where;
		
		//make call
			connect.connection conn = new connect.connection(); 
			conn.call();
			conn.sendStatement(statementString); 
		
		//collect data
			String temp = "";
			while(conn.nextResult()){
				workingMap = new HashMap<String,String>();
				for(int a = 0; a < itemNames.length; a++){
					temp = itemNames[a].split("\\.")[itemNames[a].split("\\.").length -1];
					workingMap.put(temp, conn.getString(temp));
				}
				result.add(workingMap);
			}
		
		//hang up, and return everything gathered
			conn.hangUp();
			return result.toArray(new HashMap[result.size()]);
	}
	
}