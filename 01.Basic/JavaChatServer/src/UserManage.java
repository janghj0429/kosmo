import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Iterator;
import java.util.Map;

public class UserManage extends ChatServer{
	
	private static DBConnector oracleDBConnector = DBConnector.getInstance();
	
	ResultSet rs;
	Connection con;
	Statement stmt;
	PreparedStatement pstmt;

	private String UserID;
	private String password;
	
	Socket socket;
	PrintWriter out;
	UserManage guest;
	BufferedReader in;
	Map<String, PrintWriter> clientMap;
		
	public UserManage()
	{
		
	}
	public UserManage(Socket socket, PrintWriter out, BufferedReader in, Map<String, PrintWriter> clientMap) 
	{ 
		this.clientMap = clientMap;
		this.socket = socket;
		this.out = out;
		this.in = in;

	}
	
	public UserManage(String userID, String password) 
	{
		this.UserID = userID;
		this.password = password;
	}
	

	public void showConnectMenu(UserManage userInfo) throws IOException
	{		
		String choice;
		while(true) {
				try {
				this.out.println("┌────────────────────┐");
				this.out.println("│ 1. 회원가입        │");
				this.out.println("│ 2. 회원탈퇴        │");
				this.out.println("│ 3. 로그인          │");
				this.out.println("└────────────────────┘");
				choice = this.in.readLine();			
				this.out.println(" ▶ 선택 : " + choice);
				if(choice.contentEquals("1"))
				{
					registerID();
					continue;
				}
				else if(choice.contentEquals("2"))
				{
					deleteID();
					continue;
				}
				else if(choice.contentEquals("3"))
				{
					logIn(userInfo);
					break;
				}
				else
				{
					this.out.println("┌────────────────────┐ ");
					this.out.println("│ 잘못된 선택입니다. │");
					this.out.println("└────────────────────┘");
					continue;
				}
			} 
			catch (Exception e) 
			{
				this.in.close();
				this.out.close();
				this.socket.close();
				break;
			}
		}
	}
	
	public void registerID () 
	{
		String userID;
		String password;
		String sql = null;
		int count = 0;
		while(true) {
			try 
			{	
				count = 0;
				con = oracleDBConnector.getConnection();
				this.out.println("┌──────────────────────────┐");
				this.out.println("│          SIGN UP         │");
				this.out.println("└──────────────────────────┘");
				this.out.println("┌──────────────────────────┐");
				this.out.println("│ 사용하실 ID를 입력하세요 │");
				this.out.println("└──────────────────────────┘");
				userID = this.in.readLine();
				this.out.println(" ▶ 입력 : " + userID);
				sql =  "select * from CLIENT";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();				
				while(rs.next())					
				{
					if(rs.getString(1).equals(userID))
					{
						this.out.println("┌──────────────────────────┐ ");
						this.out.println("│ 이미 존재하는 ID 입니다. │");
						this.out.println("│ 다른 ID를 사용해 주세요. │");
						this.out.println("└──────────────────────────┘");
						count++;
					}
				}
				if (count > 0) continue;
				this.out.println("┌──────────────────────────┐ ");
				this.out.println("│  비밀번호를 입력하세요   │");
				this.out.println("└──────────────────────────┘");
				password = this.in.readLine();
				this.out.println(" ▶ 입력 : " + password);
				sql = "INSERT INTO CLIENT (ID, PASSWORD, IPBLACK, IDBLACK, IP , YELLOWCARD , ISADMIN) VALUES ('" + userID + "', '" + password + "', 0, 0, '" + this.socket.getInetAddress() + "', 0, 0)";

				stmt = con.createStatement();
				stmt.executeUpdate(sql);
				
				pstmt.close();
				rs.close();	
				stmt.close();	
				con.close();
				break;
			} catch(Exception e) {	return; }
		}
		this.out.println("┌──────────────────────────┐ ");
		this.out.println("│      회원가입 완료       │");
		this.out.println("└──────────────────────────┘");
	}
			
	public void deleteID()
	{
		String id;
		String pwd;
		String sql;
		this.out.println("┌──────────────────────────┐ ");
		this.out.println("│        WITHDRAWAL        │");
		this.out.println("└──────────────────────────┘");
		con = oracleDBConnector.getConnection();
		while(true)
		{		
			try 
			{									
				this.out.println("┌──────────────────────────┐ ");
				this.out.println("│  탈퇴할 ID를 입력하세요  │");
				this.out.println("└──────────────────────────┘");
				id = this.in.readLine();
				this.out.println(" ▶ 입력 : " + id );
				sql = "select * from CLIENT where ID = '" + id + "'";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next())
				{
					if(rs.getString(1).equals(id))
					{
						this.out.println("┌──────────────────────────┐ ");
						this.out.println("│  비밀번호를 입력하세요   │");
						this.out.println("└──────────────────────────┘");
						pwd = this.in.readLine();
						this.out.println(" ▶ 입력 : " + pwd);
						if(rs.getString(2).equals(pwd))
						{
							sql = "delete from CLIENT where ID = '" + rs.getString(1) + "'";
							stmt = con.createStatement();
							stmt.executeUpdate(sql);
							break;
						}
						else
						{
							this.out.println("┌──────────────────────────┐ ");
							this.out.println("│  비밀번호가 틀렸습니다   │");
							this.out.println("└──────────────────────────┘");
							continue;
						}
					}
					else
					{
						this.out.println("┌──────────────────────────┐ ");
						this.out.println("│  존재하지 않는 ID 입니다 │");
						this.out.println("└──────────────────────────┘");
						continue;
					}
				}
				rs.close();
				stmt.close();
				pstmt.close();
				con.close();
			} catch(Exception e) {
				e.printStackTrace();
				try {
					this.in.close();
					this.out.close();
					this.socket.close();
					break;
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println(" ▷ 사용자가 접속을 종료하였습니다.");
					break;
				}
			}
		}
		this.out.println("┌──────────────────────────┐ ");
		this.out.println("│      탈퇴되었습니다      │");
		this.out.println("└──────────────────────────┘");
	}
			
	public void logIn(UserManage userInfo)
	{
		String id;
		String pwd;
		String sql;
		while(true)
		{
			try 
			{	
				con = oracleDBConnector.getConnection();
				this.out.println("┌─────────────────────────┐");
				this.out.println("│          LOGIN          │");
				this.out.println("└─────────────────────────┘");
				this.out.println("┌─────────────────────────┐");
				this.out.println("│     ID를 입력하세요     │");
				this.out.println("└─────────────────────────┘");
				id = this.in.readLine();
				
				if(isLoggedIn(id)) 
				{
					this.out.println("┌───────────────────────────┐ ");
					this.out.println("│  이미 접속중인 ID 입니다  │");
					this.out.println("└───────────────────────────┘");
					continue;
				}
				this.out.println("┌─────────────────────────┐ ");
				this.out.println("│  비밀번호를 입력하세요  │");
				this.out.println("└─────────────────────────┘");
				pwd = this.in.readLine();	
				
				sql = "select * from CLIENT where ID = '" + id + "'";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();		
				if(rs.next()) 
				{			
					String UserID = rs.getString(1);
					String password = rs.getString(2);
					
					if(UserID.equals(id) && password.equals(pwd))
					{
						if(rs.getInt(4) == 0)
						{
							userInfo.setUserID(UserID);
							userInfo.setPassword(password);
							break;
						}
						else
						{
							this.out.println("┌────────────────────┐ ");
							this.out.println("│  차단된 ID 입니다  │");
							this.out.println("└────────────────────┘");
							userInfo.setUserID("BlackList");
							userInfo.setPassword(password);
							break;
						}
					}
					else
					{
						this.out.println("┌────────────────────────────────┐ ");
						this.out.println("│  로그인 정보가 잘못되었습니다  │");
						this.out.println("└────────────────────────────────┘");
						continue;
					}
				}
			
				rs.close();
				pstmt.close();
				con.close();
			} catch(Exception e) {
				try {
					this.in.close();
					this.out.close();
					this.socket.close();
					break;
				} catch (Exception e1) { 
					System.out.println(" ▷ 사용자가 접속을 종료하였습니다.");
					break;
				}
			}			
		}
	}
	
	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLoggedIn(String userID)
	{
		Iterator<String> it = clientMap.keySet().iterator();
		while(it.hasNext())
		{
			if(it.next().contentEquals(userID))
				return true;
		}
		return false;
	}
}