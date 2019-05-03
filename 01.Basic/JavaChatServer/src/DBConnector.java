import java.sql.*;

public class DBConnector 
{
	private static DBConnector oracleDBConnector = new DBConnector();
	Connection con = null;
	private DBConnector() { }
	
	public static DBConnector getInstance()
	{
		if(oracleDBConnector == null)
			oracleDBConnector = new DBConnector();
		return oracleDBConnector;
	}

	public Connection getConnection() 
	{
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url="jdbc:oracle:thin:@localhost:1521:xe";
			String id = "scott";
			String password = "tiger";
			con = DriverManager.getConnection(url, id, password);			
		} catch (Exception e) { e.printStackTrace(); }
		
		return con;
	}
}
