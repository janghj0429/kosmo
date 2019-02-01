import java.sql.*;


public class TestThread extends Thread{
	
	String noThread = "00";
	
	TestThread(int n){
		if(n<10)
			noThread = "0" + n;
		else
			noThread = "" + n;
	}
	
	public void run() {
		ConnectionPool cp = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			cp = ConnectionPool.getInstance(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott","tiger", 5, 5);
			
			synchronized (this) {
				con = cp.getConnection();
				pstmt = con.prepareStatement("select * from department");
				rs = pstmt.executeQuery();
				while(rs.next()) {
					System.out.println(con + " : " + 
									   rs.getInt(1) + ", " +
									   rs.getString(2) + ", " +
									   rs.getString(3));
				}
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) cp.releaseConnection(con);
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
}
