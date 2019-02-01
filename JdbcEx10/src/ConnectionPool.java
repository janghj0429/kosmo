import java.sql.*;
import java.util.*;

public final class ConnectionPool {
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	
	//사용하지 않은 커넥션 즉, 초기 커넥션을 저장하는 변수
	private ArrayList<Connection> free;
	
	//사용 중인 커넥션을 저장하는 변수
	private ArrayList<Connection> used;
	
	private String url;
	private String user;
	private String password;
	
	//초기 커넥션 수
	private int initialCons = 0;
	
	//최대 커넥션 수
	private int maxCons = 0;
	
	//총 커넥션 수
	private int numCons = 0;
	private static ConnectionPool cp;
	
	//접속
	public static ConnectionPool getInstance(
			String url,
			String user,
			String password,
			int initialCons, int maxCons)
	{
		try {
			if (cp == null) {
				synchronized(ConnectionPool.class) {
					cp = new ConnectionPool(
								url, user, password, initialCons, maxCons);
				}
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return cp;
	}
	
	private ConnectionPool(String url,
						   String user,
						   String password,
						   int initialCons,
						   int maxCons)
	throws SQLException
	{
		this.url = url;
		this.user = user;
		this.password = password;
		this.initialCons = initialCons;
		this.maxCons = maxCons;
		
		if(initialCons < 0)
			initialCons = 5;
		if(maxCons < 0)
			maxCons = 10;
		
		//초기 커넥션 개수를 각각의 ArrayList에 저장할 수
		//있도록 초기 커넥션 수만큼 ArrayList를 생성한다.
		free = new ArrayList<Connection>(initialCons);
		used = new ArrayList<Connection>(initialCons);
		
		//initialCons 수만큼 Connection을 생성(free) 한다.
		while (numCons < initialCons) {
			addConnection();
		}
	}
	
	//free에 커넥션 객체를 저장한다.
	private void addConnection() throws SQLException
	{
		free.add(getNewConnection());
	}
	
	//새로운 커넥션 객체를 생성한다.
	private Connection getNewConnection() throws SQLException
	{
		
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("connect to " + numCons + " : " + con);
		//커넥션 생성될 때마다 숫자가 증가한다.
		++numCons;
		return con;
	}
	
	//free에 있는 커넥션을 used로 옮기는 작업 => free --> used
	public synchronized Connection getConnection() throws SQLException
	{
		//free에 Connection이 없으면
		//maxCons 만큼 Connection을 더 생성한다.
		if(free.isEmpty()) {
			while(numCons < maxCons) {
				addConnection();
			}
		}
		Connection _con;
		_con = free.get(free.size()-1);
		free.remove(_con);
		used.add(_con);
		return _con;
	}
	
	//used에 있는 커넥션을 free 로 반납한다.
	public synchronized void releaseConnection(Connection _con) throws SQLException
	{
		boolean flag = false;
		if(used.contains(_con)) {
			used.remove(_con);
			numCons--;
			flag = true;
		} else {
			throw new SQLException("ConnectionPool 에 있지않네요!!");
		}
		try {
			if(flag) {
				free.add(_con);
				numCons++;
			} else {
				_con.close();
			}
		} catch(SQLException e) {
			try {
				_con.close();
			} catch(SQLException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	//모든 Connection 자원을 반납한다.
	public void closeAll() {
		//used에 있는 커넥션을 모두 삭제한다.
		for(int i=0; i<used.size(); i++) {
			Connection _con = (Connection)used.get(i);
			used.remove(i--);
			try {
				_con.close();
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		
		//free에 있는 커넥션을 모두 삭제한다.
		for(int i=0; i<free.size(); i++) {
			Connection _con = (Connection)free.get(i);
			free.remove(i--);
			try {
				_con.close();
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}
	
	public int getMaxCons() {
		return maxCons;
	}
	
	public int getNumCons() {
		return numCons;
	}
	
	public int getFreeCons() {
		return free.size();
	}
}


