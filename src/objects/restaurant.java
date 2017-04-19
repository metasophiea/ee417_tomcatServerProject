package objects;

public class restaurant {
	private String restname = "";
	private String name = "";
	private String address = "";
	private String email = "";
	private String password = "";
	
	public restaurant(String restname, String name, String address, String email, String password){
		this.restname = restname;
		this.name = name;
		this.address = address;
		this.email = email;
		this.password = password;
	}
	
	public String getRestname(){ return restname; }
	public void setRestname(String restname){ this.restname = restname; }
	public String getName(){ return name; }
	public void setName(String name){ this.name = name; }
	public String getAddress(){ return address; }
	public void setAddress(String address){ this.address = address; }
	public String getEmail(){ return email; }
	public void setEmail(String email){ this.email = email; }
	public String getPassword(){ return password; }
	public void setPassword(String password){ this.password = password; }
	
	public static int validate(restaurant input){
		if( (input.getRestname()).length() <= 0 ){return 1;}
		if( (input.getName()).length() <= 0 ){return 2;}
		if( (input.getEmail()).length() <= 0 ){return 3;}
		if( (input.getPassword()).length() <= 0 ){return 4;}
		
		if( (input.getRestname()).matches("/[^a-z0-9_]/i") ){return 5;}
		if( (input.getEmail()).matches("/@/") ){return 6;}
		
		return 0;
	}
}
