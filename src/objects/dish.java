package objects;

public class dish {
	private String id = "";
	private String restname = "";
	private String name = "";
	private String price = "";
	private String description = "";
	
	public dish(String id, String restname, String name, String price, String description){
		this.id = id;
		this.restname = restname;
		this.name = name;
		this.description = description;

		//work with price string input, converting it to coinage
		if( price.contains(".") ){ 
			String[] temp = price.split("\\.");
			int ones = Integer.parseInt(temp[0])*100;
			
			int cents = 0;
			if(temp.length > 1){
				if(temp[1].length() > 2){temp[1] = "" + temp[1].charAt(0) + temp[1].charAt(1);}
				cents = Integer.parseInt(temp[1]);
				if(temp[1].length() == 1){ cents = cents * 10; }
			}

			int total = ones + cents;
			this.price = ""+total;
		}else{ this.price = price; }
		
	}
	
	public String getId(){ return id; }
	public void setId(String id){ this.id = id; }
	public String getRestname(){ return restname; }
	public void setRestname(String restname){ this.restname = restname; }
	public String getName(){ return name; }
	public void setName(String name){ this.name = name; }
	public String getPrice(){ return price; }
	public void setPrice(String price){ this.price = price; }
	public String getDescription(){ return description; }
	public void setDescription(String description){ this.description = description; }
	
	public static int validate(dish input){
		if( (input.getName()).length() <= 0 ){return 1;}
		if( (input.getPrice()).length() <= 0 ){return 2;}
		
		if( (input.getPrice()).matches("/([^0-9.])/") ){return 3;}
		if( (input.getPrice()).split(".").length > 2 ){return 3;}
		
		return 0;
	}
}