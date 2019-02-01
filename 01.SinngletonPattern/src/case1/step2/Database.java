package case1.step2;

public class Database {
	private static Database singleton;
	
	private String name;

	public Database(String name) 
	{
		try {
			Thread.sleep(100);
			this.name = name;
		} catch (Exception e) {
			
		}
		
	}
	
	public static synchronized Database getInstance(String name)
	{
		if(singleton == null) {
			singleton = new Database(name);
		}
		return singleton;
	}

	public String getConnection() {
		return name;
	}	
}
