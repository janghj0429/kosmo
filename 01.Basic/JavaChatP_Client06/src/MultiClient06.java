import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

// 회원가입까지 완료
// 아이디 블럭 완료
// 비밀번호 5회 입력 틀릴 경우 접속종료

public class MultiClient06 {

	public static void main(String[] args) 
			throws UnknownHostException, IOException {
		System.out.println("회원가입 유무를 체크합니다.");
		System.out.println("ID를 입력해 주세요.");
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		String id = s.nextLine();
		while(true) {
			if(check(id) == true)
				break;
			else {
				id = s.nextLine();
				continue;
			}
		}
		
		while(true){
			id = login(id);
			if(id.equalsIgnoreCase("Q"))
				return;
			else
				break;
		}	
		
		try {
			String ServerIP = "localhost";
//			String ServerIP = args[0];
			Socket socket = new Socket(ServerIP, 9999); // 소켓 객체 생성
			System.out.println("서버와 연결이 되었습니다.....");

			// 서버에서 보내는 메시지를 사용자의 콘솔에 출력하는 쓰레드.
			Thread receiver = new Receiver6(socket, id);
			receiver.start();
			new ChatWin(socket, id);
			
		} catch (Exception e) {
			System.out.println("예외[MultiClient class] : " + e);
		}
	}
	
	// 영문 숫자로만 입력 받음
	public static boolean check(String id) {
		if(id.length() < 6) {
			System.out.println("아이디는 6글자 이상 입력하셔야합니다.");
			return false;
		}
		char chrInput;
		for (int i = 0; i < id.length(); i++) {
			chrInput = id.charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크
			if (chrInput >= 0x61 && chrInput <= 0x7A) {				// 영문(소문자) OK!
			} else if (chrInput >= 0x41 && chrInput <= 0x5A) {
				// 영문(대문자) OK!
			} else if (chrInput >= 0x30 && chrInput <= 0x39) {
				// 숫자 OK!
			} else {
				System.out.println("ID에는 영문, 숫자만 사용가능합니다.");
				return false; // 영문자도 아니고 숫자도 아님!
			}
		}
		return true;
	}
	
	public static String login(String id) {
		Scanner s = new Scanner(System.in);
		String idck = null;
		String pw = null;
		String pwck = null;
		String name = null;
		String sql = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
			stmt = con.createStatement();

			sql = "select * from chat where id = '" + id + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				idck = rs.getString(2);
				pwck = rs.getString(3);
			}
			System.out.println("**회원가입 유무 체크 [" + id + "]**");
			// 가입이 되어 있는 경우 로그인
			if (id.equals(idck)) {
				System.out.println("ID가 존재합니다. 비밀번호를 입력해 로그인하세요!");
				System.out.println("PW를 입력해주세요.");
				
				int i = 0;
				String ckban = "";
				
				// 아이디 블럭 상태인지 여부 확인
				sql = "select ban from chat where id = '"+id+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) 
				{
					ckban = rs.getString(1);
				}
				
				while (true) {
					pw = s.nextLine();
					if (pwck.equals(pw)) {
						if (ckban.equalsIgnoreCase("ban")) { // 블럭 상태일 경우 종료
							id = "q";
							System.out.println("아이디가 블럭 상태입니다. 관리자에게 문의하세요!");
							return id;
						} else {
							// 로그인시 개인 금칙어 입력

							sql = "select * from chat where id = '" + id + "'";
							rs = stmt.executeQuery(sql);
							while (rs.next()) {
								name = rs.getString("NICK");
							}
						}
						return id;
					} else
						System.out.println((i + 1) + "회 비밀번호 오류: 확인 후 다시 입력하세요.");
					i++;
					if(i==5) {
						id = "q";
						System.out.println("비밀번호를 확인 후 다시 접속해주세요!");
						return id;
					}					
				}
			} 
			// 가입이 안되어 있는 경우 회원가입
			else   
			{  // 아이디 체크
				System.out.println("가입된 아이디가 없어 회원가입 절차로 넘어갑니다.");
				while (true) {
					System.out.println("사용할 ID를 입력해주세요.(6글자 이상)");
					id = s.nextLine();
					sql = "select * from chat where id = '" + id + "'";
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						idck = rs.getString(2);
					}
					if (id.equals(idck)) {
						System.out.println("이 아이디는 사용 중 입니다.");
						continue;
					}
					if (check(id) == false) {
						System.out.println("아이디는 영문, 숫자만 사용가능합니다.");
						continue;
					}
					if (id.length() < 6) {
						System.out.println("아이디는 6글자 이상을 사용해야 합니다.");
						continue;
					}
					break;
				}
				
				// 패스워드 체크
				while(true) {
					System.out.println("사용할 패스워드를 입력해주세요.");
					pw = s.nextLine();
					if(pw.length()< 6) {
						System.out.println("패스워드는 6자 이상 사용하셔야 합니다.");
						continue;
					}
					if (check(pw) == false) {
						System.out.println("패스워드는 영문, 숫자만 사용가능합니다.");
						continue;
					}
					System.out.println("다시한번 동일한 패스워드를 입력해주세요.");
					pwck = s.nextLine();
					
					if(pw.equals(pwck)) {
						break;
					}
					System.out.println("패스워드가 동일하지 않습니다.");
				}	
				// 닉네임 체크
				String nameck = null;
				while (true) {
					System.out.println("채팅방에서 사용할 닉네임을 입력해주세요.");
					name = s.nextLine();
					sql = "select * from chat where nick = '" + name + "'";
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						nameck = rs.getString("NICK");
					}
					if (name.equals(nameck)) {
						System.out.println("닉네임을 이미 사용 중 입니다.");
						continue;
					}
					break;
				}					
				
				sql = "insert into Chat(ID, PW, NICK) " + " values('" + id + "', '" + pw + "', '" + name + "')";
				//System.out.println(sql);
				stmt.executeUpdate(sql);
				return id;

			}
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
		return id;
	}
}