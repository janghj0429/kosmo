import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger"); 
			String sql = "create table test2(id varchar(10),    " +
						 "               password varchar(10))  ";
			pstmt = con.prepareStatement(sql);//문법검사 하는 명령.
			int updateCount = pstmt.executeUpdate();
			System.out.println("createCount : " + updateCount);
			
			//-----------------------------------------------------
			sql = "insert into test2 values(?, ?)";//처음 문법검사할때 문법만 검사해준상태
			pstmt = con.prepareStatement(sql);//문법검사 한번하고 다음 진행
			pstmt.setString(1, "aa");
			pstmt.setString(2, "12");
			updateCount = pstmt.executeUpdate();
			System.out.println("insertCount : " + updateCount);
			
			//-------------------------------------------------------
			sql = "select * from test2";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();//이때는 문법검사 안함
			while(rs.next()) {
				System.out.print("id : " + rs.getString(1));
				System.out.println(", password : " + rs.getString(2));
			}
			
			//--------------------------------------------------------
			sql = "drop table test2";
			pstmt = con.prepareStatement(sql);
			updateCount = pstmt.executeUpdate();
			System.out.println("dropCount : " + updateCount);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (SQLException sqle) { }
		}

	}

}
