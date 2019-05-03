import java.io.*;
import java.net.Socket;
import java.sql.*;

public class AdminMenu {
	private static DBConnector oracleDBConnector = DBConnector.getInstance();
	ResultSet rs;
	Connection con;
	Statement stmt;
	PreparedStatement pstmt;
	
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	
	public AdminMenu(Socket socket, BufferedReader in, PrintWriter out) {
		this.socket = socket;
		this.out = out;
		this.in = in;
	}
	
	public void showClientInfo()
	{
		String sql = "select * from CLIENT";
		try {
			con = oracleDBConnector.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				this.out.println("ID : " + rs.getString(1));
				this.out.println("Password : " + rs.getString(2));
				this.out.println("IPBlck : " + rs.getInt(3));
				this.out.println("IDBlack : " + rs.getInt(4));
				this.out.println("IP : " + rs.getString(5));
				this.out.println("YElloCard : " + rs.getInt(6));
				this.out.println("IsAdmin : " + rs.getInt(7));
			}
		} catch (Exception e) { this.out.println(" ▶ 회원 목록 불러오기 실패 "); return; }
	}
	
	public void showBannedWords()
	{
		String sql = "select * from BADWORDS";
		try {
			con = oracleDBConnector.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int i = 1;
			while(rs.next())
			{
				this.out.println( i + ": " + rs.getString(1));
				i++;
			}
		} catch (Exception e) { this.out.println(" ▶ 회원 목록 불러오기 실패 "); return; }
	}
	
	public void addBannedWords(String word)
	{
		String sql = "insert into BADWORDS VALUES('" + word + "')";
		int updateCount = 0;
		try {
			con = oracleDBConnector.getConnection();
			stmt = con.createStatement();
			updateCount = stmt.executeUpdate(sql);
			if(updateCount > 0)
				this.out.println(" ▶ 금칙어 추가 완료.");
			else
				this.out.println(" ▶ 금칙어 추가 실패");
			
		} catch (Exception e) { this.out.println(" ▶ 금칙어 추가 중 오류 발생"); }
	}
	
	public void addIDBlackList(String userID)
	{
		String sql = "update CLIENT set IDBLACK = 1 where ID ='" + userID + "'";
		int updateCount = 0;
		try {
			con = oracleDBConnector.getConnection();
			stmt = con.createStatement();
			updateCount = stmt.executeUpdate(sql);
			if(updateCount > 0)
				this.out.println(" ▶ 블랙리스트 ID 추가 완료.");
			else
				this.out.println(" ▶ 블랙리스트 ID 추가 실패");
			
		} catch (Exception e) { this.out.println(" ▶ 블랙리스트 ID 추가 중 오류 발생"); }
	}
		
	public void addIPBlackList(String userID)
	{
		String sql = "update CLIENT set IPBLACK = 1, IDBLACK = 1 where ID ='" + userID + "'";
		int updateCount = 0;
		try {
			con = oracleDBConnector.getConnection();
			stmt = con.createStatement();
			updateCount = stmt.executeUpdate(sql);
			if(updateCount > 0)
				this.out.println(" ▶ 블랙리스트 IP 추가 완료.");
			else
				this.out.println(" ▶ 블랙리스트 IP 추가 실패");
			
		} catch (Exception e) { this.out.println(" ▶ 블랙리스트 IP 추가 중 오류 발생"); }
	}
}
