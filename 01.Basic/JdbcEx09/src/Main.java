import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ConnectionPool cp = null;
		
		try {
			cp = ConnectionPool.getInstance(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott","tiger", 5, 10);
			System.out.println("getFreeCons : " + cp.getFreeCons());
			con = cp.getConnection();
			pstmt = con.prepareStatement("select * from department");
			rs = pstmt.executeQuery();
			System.out.println("getFreeCons : " + cp.getFreeCons());
			while(rs.next()) {
				System.out.println("deptno : " + rs.getInt(1) + ", ");
				System.out.println("dname : " + rs.getString(2) + ", ");
				System.out.println("loc : " + rs.getString(3) );
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) cp.releaseConnection(con);
				System.out.println("getFreeCons : " + cp.getFreeCons());
			} catch(SQLException _sqle) {
				_sqle.printStackTrace();
			}
		}
		cp.closeAll();
	}

}
