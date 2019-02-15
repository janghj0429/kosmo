import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


class Login//로그인 클래스 
{
	public static void checkLogin() {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			
			Scanner sc = new Scanner(System.in);
			System.out.println("로그인 정보 입력");
			System.out.print("ID : ");
			String id = sc.nextLine();
			System.out.print("Password : ");
			String pw = sc.nextLine();
			System.out.println("ㅡㅡㅡㅡ");

			String search = "select * from members where id ='"+id+"'";
			pstmt = con.prepareStatement(search);
			rs = pstmt.executeQuery(search);
			System.out.println("ㅡㅡㅡㅡ");

			System.out.println("---");
			if(rs.next()==false)
			{	
				System.out.println("재입력");
				Login.checkLogin();
			}else {
				String getPass = rs.getString("password");
				if((getPass.equals(pw))==true) {
					System.out.println("------------------");
					blackCheck(id);
				}else {
					System.out.println("재입력");
					Login.checkLogin();
				}
			}
					sc.close();
		} catch(Exception e) {
			System.out.println("로그인 예외e"+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (SQLException sqle) { }
		}
	}
	static void blackCheck(String id)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			String search = "select * from blacklist where id ='"+id+"'";
			pstmt = con.prepareStatement(search);
			rs = pstmt.executeQuery(search);
			System.out.println("ㅡㅡㅡㅡ");
			if(rs.next())
			{
				System.out.println("블랙리스트 접속거부");
			}else {
				System.out.println("ㅜㅜㅜㅜㅜㅜ");
				System.out.println("로그인성공");
				connect(id);
			}
			
		}catch(Exception e) {
			System.out.println("로그인 예외e"+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (SQLException sqle) { }
		}
	}
	
	static void connect(String id)//로그인 시 접속
	{
		System.out.println("이름을 입력해 주세요.");
		Scanner s = new Scanner(System.in);
		String s_name = s.nextLine();      //대화명입력
		
//		String s_name = id;	//id를 대화명으로 입력
		try {
			
			String ServerIP = "localhost";
//			if(args.length > 0)
//				ServerIP = args[0];
			Socket socket = new Socket(ServerIP, 9999); //소켓 객체 생성		
			System.out.println("서버와 연결이 되었습니다.");			
			System.out.println("대기실로갑니다.");
			//서버에서 보내는 메시지를 사용자의 콘솔에 출력하는 쓰레드.
			Thread receiver = new Receiver6(socket);
			receiver.start();
			
			//사용자로부터 얻은 문자열을 서버로 전송해주는 역할을 하는 쓰레드.
//			Thread sender = new Sender6(socket, s_name);
//			sender.start();
			
			new ChatWin(socket, s_name);
			s.close();
			
		}catch(Exception e) {
			System.out.println("예외[MultiClient class]:"+e);
		}
	}
}

class Join//가입클래스
{
	public static void checkJoin() {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Scanner keyboard = new Scanner(System.in);
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
					
			System.out.println("가입할 ID, Password 입력");
			System.out.print("ID : ");
			String id = keyboard.nextLine();
			System.out.print("Password : ");
			String pw = keyboard.nextLine();
			System.out.println("ㅡㅡㅡㅡ");
			
			String search = "select * from members where id ='"+id+"'";
			pstmt = con.prepareStatement(search);
			rs = pstmt.executeQuery(search);
			System.out.println("ㅡㅡㅡㅡ");
			if (rs.next() == false || id.isEmpty() == true)
			{
				String insert = "insert into members "
						+ "values('"+id+"','"+pw+"')";
				pstmt = con.prepareStatement(insert);
				rs = pstmt.executeQuery(insert);
				Login.checkLogin();	
			} else {
				System.out.println("존재하는 id, 다시입력");
				Join.checkJoin();
			}
			keyboard.close();
		} catch(Exception e) {
			System.out.println("가입예외 e "+e);
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

class MenuViewer
{
	public static Scanner keyboard = new Scanner(System.in);	
	public static void showMenu()
	{
		System.out.println("선택하세요...");
		System.out.println("1. 로그인");
		System.out.println("2. 가입");
		System.out.println("3. 프로그램 종료");
		System.out.print("선택 : ");
	}
}

public class JhjClient {
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	

	public static void main(String[] args)  //throws UnknownHostException, IOException
	{		
		int choice;	
		MenuViewer.showMenu();
		choice=MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
//		while(true) {
//			switch(choice) {
//			case 1:
//				Login.checkLogin();
//				return;
//			case 2:
//				Join.checkJoin();
//				return;
//			case 3:
//				System.out.println("프로그램을 종료합니다.");
//				return;
//			}
//		}
	//	Login login = new Login();
	//	Join join = new Join();
		while(true) {
			if(choice == 1) 	 { 
				Login.checkLogin(); 
				break;
				}
			else if(choice == 2) { 
				Join.checkJoin();
				break;
				}
			else if(choice == 3) {
				System.out.println("프로그램을 종료합니다.");
				return;
			} else {
				System.out.println("잘못 눌럿음, 재입력");
				MenuViewer.showMenu();
				choice=MenuViewer.keyboard.nextInt();
				MenuViewer.keyboard.nextLine();
			}		
		}
		
//		System.out.println("이름을 입력해 주세요.");
//		Scanner s = new Scanner(System.in);
//		String s_name;
//		
//		try {
//			String ServerIP = "localhost";
//			if(args.length > 0)
//				ServerIP = args[0];
//			Socket socket = new Socket(ServerIP, 9999); //소켓 객체 생성
//			System.out.println("서버와 연결이 되었습니다.");
//			
//			//서버에서 보내는 메시지를 사용자의 콘솔에 출력하는 쓰레드.
//			Thread receiver = new Receiver6(socket);
//			receiver.start();
//			
//			//사용자로부터 얻은 문자열을 서버로 전송해주는 역할을 하는 쓰레드.
////			Thread sender = new Sender6(socket, s_name);
////			sender.start();
//			
//			new ChatWin(socket, s_name);
//			
//		}catch(Exception e) {
//			System.out.println("예외[MultiClient class]:"+e);
//		}
	}
}

