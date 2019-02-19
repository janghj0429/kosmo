package pattern01.singleton.step3;

public class Database {
	private static Database singleton = new Database("product");
	private String name;

	public Database(String name) 
	{
		this.name = name;		
	}
	
	public static synchronized Database getInstance(String name)
	{
		return singleton;
	}

	public String getConnection() {
		return name;
	}	
}
