import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetMetaDataEx {
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			
			String sql = "select * from employee";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumn = rsmd.getColumnCount();
			
			System.out.println("Number of Column : " + numberOfColumn);
			System.out.println("[Column Name] [DBMS Type] [Null Allowed]");
			
			for(int i=1; i<=numberOfColumn; i++) {
				String columnName = rsmd.getColumnName(i);
				String dbmsType = rsmd.getColumnTypeName(i);
				int isNull = rsmd.isNullable(i);
				
				//-----------------------------------------------
				String strNull = "Null";
				if(isNull == 0)
					strNull = "Not Null";
				
				String space1 = "";
				int max1 = 14 - columnName.length();
				for(int j=0; j<max1; j++) {
					space1 = space1 + " ";	
				}
				
				String space2 = "";
				int max2 = 12 - dbmsType.length();
				for(int j=0; j<max2; j++) {
					space2 = space2 + " ";	
				}
				//--------------------------------------
				
				System.out.print(columnName + space1);
				System.out.print(dbmsType + space2);
				System.out.println(strNull);
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
