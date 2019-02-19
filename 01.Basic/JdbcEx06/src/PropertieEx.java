import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class PropertieEx {
	
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
		Properties pro= null;
		FileInputStream fis = null;
		
		try {
			pro = new Properties();
			fis = new FileInputStream("D:\\janghj0429\\oracle\\jdbc.properties.txt");
			pro.load(fis);
			
			String url = pro.getProperty("url");
			String user = pro.getProperty("user");
			String pwd = pro.getProperty("password");
			
			con = DriverManager.getConnection(
					url,
					user,
					pwd);
			
			String sql = "select password from members where id ='cccc'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				System.out.print("eno : " + rs.getString(1) + ", ");
//				System.out.println("ename : " + rs.getString(2));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (SQLException sqle) { }
		}
	}
}
