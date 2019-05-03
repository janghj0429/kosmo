import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import oracle.net.aso.f;

// 오라클 데이터 베이스 접속!!
// 회원가입 완료 -> 클라이언트 3 부터
// 금칙어 체크 완료
// 채팅방 개설 및 입장 완료
// 비밀방 개설 및 입장 완료
// 방 강퇴, 방 벤, 개인 금칙어
// 개인 대화 및 차단 추가

public class MultiServer13 {

	ServerSocket serverSocket = null;
	Socket socket = null;
	Map<String, PrintWriter> clientMap;
	Map<String, String> roomMap;
	
	// 오라클 데이터 베이스 재료
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	String sql = null;
	
	//금칙어에서 사용
	//StringBuilder ckmsg = null; 
	
	// 오라클 접속
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		System.out.println("데이터 베이스에 연결되었습니다.");
	}
	
	// 생성자
	public MultiServer13() {
		// 클라이언트의 출력스트림을 저장할 해쉬맵 생성.
		clientMap = new HashMap<String, PrintWriter>();
		roomMap = new HashMap<String, String>();

		List<String> black = new ArrayList<>();
		List<String> bWord = new ArrayList<>();
				
		// 해쉬맵 동기화 설정
		Collections.synchronizedMap(clientMap);
		Collections.synchronizedMap(roomMap);
		
	}
	
	public void init() {
		try {			
			serverSocket = new ServerSocket(9999); // 9999 포트로 서버소켓 객체생성.
			System.out.println("서버가 시작되었습니다.");		
			
			while (true) {
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + ":" + socket.getPort());
				
				Thread msr = new MultiServerT(socket); // 스레드 생성.
				msr.start(); // 스레드 시동
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	// 명령어 기능 메서드들 모음
	
	// 접속자 리스트 보내기
	public void list(PrintWriter out) {
		// 출력스트림을 순차적으로 얻어와서 해당 메시지를 출력한다.
		List<String> chatUserList = new ArrayList<String>();
		
		Iterator<String> it = clientMap.keySet().iterator();
		while (it.hasNext()) {
			chatUserList.add(it.next()); //출력할 문장을 계속 더해 나간다.
		}
		out.println("||모든 유저 리스트||");
		out.println("==========================================");
		out.println(chatUserList);
		out.println("==========================================");
	}

	public void roomUserList(String name, String title, PrintWriter out) 
	{
		if(title == null) {
			out.println("현재 대기실 입니다. \n"
					+ "모든 채팅 사용자 리스트로 출력합니다.");
			list(out);
		}else {
			try {
				Iterator<String> it = roomMap.keySet().iterator(); // 룸맵의 유저닉네임으로 반복하기 위한 선언
				List<String> roomUser = new ArrayList<String>();   // 같은 룸의 유저를 선별해서 저장하기 위한 리스트
				
				
				while(it.hasNext()) { // 맵에 값이 있을 때까지 반복
					String user =  it.next(); //키값은 유저 닉네임
					if(String.valueOf(roomMap.get(user)).equals(title)) // 맵 값은 타이틀 String
						roomUser.add(user); // 같을 경우 저장
				}
				// 출력
				out.println("||방 유저 리스트||");
				out.println("==========================================");
				out.println(roomUser);
				out.println("==========================================");
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void waitUserList(PrintWriter out)
	{
		List<String> roomUser = new ArrayList<String>(); // 같은 룸의 유저를 선별해서 저장하기 위한 리스트
		List<String> allUser = new ArrayList<String>();  // 모든 유저를 선별해 저장하기 위한 리스트
		List<String> waitUser = new ArrayList<String>(); // 대기실 유저를 선별해 저장하기 위한 리스트
		
		Iterator<String> it = clientMap.keySet().iterator();      // 모든 유저닉네임 반복하기 위한 선언
		while(it.hasNext()) 
		{
			allUser.add(it.next());	
		}
		
		it = roomMap.keySet().iterator(); // 룸맵의 유저닉네임으로 반복하기 위한 선언
		while(it.hasNext()) 
		{
			roomUser.add(it.next());
		}
		
		String ck1 = null; // 모든 유저를 대입
		String ck2 = null; // 방에 있는 유저를 대입
		
		for(int i = 0; i < allUser.size(); i++) {
			ck1 = allUser.get(i);
			out.println(ck1 + ":");
			for(int j = 0 ; j < roomUser.size(); j++) {
				ck2 = roomUser.get(j);
				out.println(ck2);
				if(ck1.equals(ck2)) {}
				else if(ck2 == null) { waitUser.add(ck1); }   //비교 대상이 null 값일때 비교값이 없어 추가되지 않음
				else { waitUser.add(ck1); }					
			}
		}		
		
		// 출력
		out.println("||대기실 유저 리스트||");
		out.println("==========================================");
		out.println(waitUser);		
		out.println("==========================================");
	}
	
	public void roomlist(PrintWriter out) {

		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			List<String> roomTitle = new ArrayList<String>();			
			List<String> pwRoomTitle = new ArrayList<String>();
			
			sql = "select DISTINCT TITLE from chatroom";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				roomTitle.add(rs.getString("TITLE"));
			}
			
			sql = "select DISTINCT TITLE from chatroom where pw != ' '";
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				pwRoomTitle.add(rs.getString("TITLE"));
			}
			
			for(int i=0;i<pwRoomTitle.size();i++)
				roomTitle.remove(pwRoomTitle.get(i));
			
			if(roomTitle.size() == 0 && pwRoomTitle.size() == 0) {
				out.println("채팅방이 없습니다.");
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
				return;
			}
			out.println("==== 공개방 리스트 ====");
			for(int i=0; i<roomTitle.size();i++) {
				out.println((i+1) +".["+ roomTitle.get(i)+"]");
			}
			
			out.println("==== 비공개방 리스트 ====");
			for(int i=0; i<pwRoomTitle.size();i++) {
				out.println((i+1) +".["+ pwRoomTitle.get(i)+"]");
			}
			
			if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
			stmt.close();
			con.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
				//e.printStackTrace();
				}
		}
	}
	
	// 귓속말 보내기
	public void whisperMsg(String name, String user, String s, PrintWriter out) {
		
		out.println("[귓속말 나]: " + s);
		out = clientMap.get(user); // 보낼 상대방 지정
		out.println("[귓속말 "+name+"]: " + s);
	
	}	
	
	
	// 채팅방 인원만큼 메시지가 뿌려지는 오류 수정 필요
	// 채팅방에 입장한 클라이언트에게 메시지 전달
	public void sendRoomMsg(String name, String title, String msg) {

		try {
			Iterator<String> it = roomMap.keySet().iterator(); // 룸맵의 유저닉네임으로 반복하기 위한 선언
			List<String> roomUser = new ArrayList<String>();   // 같은 룸의 유저를 선별해서 저장하기 위한 리스트
			
			while(it.hasNext()) { // 맵에 값이 있을 때까지 반복
				String user =  it.next(); //키값은 유저 닉네임
				if(String.valueOf(roomMap.get(user)).equals(title)) // 맵 값은 타이틀 String
					roomUser.add(user); // 같을 경우 저장
			}
			
			for(int i=0; i<roomUser.size();i++) {   //리스트의 유저 정보로
				PrintWriter r_out = clientMap.get(roomUser.get(i)); //클라이언트 아웃값을 전달
				r_out.println("<"+title.substring(0,2)+">"+ 
						"[" + name + "]: " + msg);
			}			
			
		} catch(Exception e) {
			e.printStackTrace();
			}

	}
	
	public void sendAllMsg(String name, String s) {		
		// 방에 입장하지 않은 유저에게만 메시지 전달
		// 출력스트림을 순차적으로 얻어와서 검사 한다.
		
		String title = null;  // 최초값은 null로 초기화
		PrintWriter out_it = null;
		Iterator<String> it = clientMap.keySet().iterator();
		while (it.hasNext()) 
		{
			String user = it.next();
			title = roomMap.get(user);
			try {		

				if(title == null)
					out_it = (PrintWriter) clientMap.get(user);
				else
					continue;
				
				if (name.equals(""))
					out_it.println(s);			
				else
					out_it.println("[" + name + "]: " + s);
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void sendManagerMsg(String name, PrintWriter out, BufferedReader in) 
			throws IOException {
		//모든 유저에게 공지를 날린다.
		String msg = null;
		if(ckAuthor(name, "", 0) == true) {
			out.println("모든 사용자에게 보낼 공지 메시지를 작성해주세요!");
			msg = in.readLine();
			Iterator<String> it = clientMap.keySet().iterator();

			while (it.hasNext()) {
				try {
					out = (PrintWriter) clientMap.get(it.next());
					if (name.equals(""))
						out.println(msg);
					else
						out.println("[** 공지사항 **]: " + msg);
				} catch (Exception e) {
					//System.out.println("예외: " + e);
				}
			}
		}
		else {
			out.println("관리자 권한이 없습니다.");	
		}		
	}
	// 개인 금칙어 추가
	public void addUserBWord(String name, PrintWriter out, BufferedReader in) 
			throws IOException 
	{
		String word = null;
		try 
		{
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();

			out.println("개인 금칙어로 추가할 단어를 입력하세요.");
			
			while(true) {
				word = in.readLine();
				if(word.length() > 6 || word.contains(" ")) //공백이 있을경우
					continue;
				else
					break;
			}			
			
			String msg = "|addword|"+word+"|";
			out.println(msg); // 클라이언트로 명령어 발송
			
			sql = " insert into banword values ('"+ name +"', '"+word+"')";
			stmt.executeUpdate(sql);
			sql = "commit";
			stmt.executeUpdate(sql);
			out.println("개인 금칙어 추가 성공!");
			
		} catch(Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}			
	}
	
	// 개인 금칙어 취소
	public void delUserBWord(String name, PrintWriter out, BufferedReader in) 
			throws IOException 
	{
		String word = null;
		try 
		{
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			
			out.println("개인 금칙어에서 삭제할 단어를 입력하세요.");
			
			word = in.readLine();
			String msg = "|delword|"+ word +"|";
			out.println(msg); // 클라이언트로 명령어 발송
			sql = " delete from banword   "
					+ " where word = '"+ word +"'"
					+ " and nick = '" + name + "'";					
			stmt.executeUpdate(sql);
			sql = "commit";
			stmt.executeUpdate(sql);
	
			//out.println("개인 금칙어 리스트에 존재하지 않는 단어 입니다.");						
		} catch(Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}			
	}
	
	public void addBanUser(String name, PrintWriter out, BufferedReader in) 
			throws IOException
	{
		String user = null;
		try 
		{
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();

			out.println("대화를 차단할 유저의 닉네임을 입력해주세요.");
			
			user = in.readLine();
			
			sql = " select * from chat "
					+ " where nick = '"+user+"' ";
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if(rs.getString("NICK") != null) {
					System.out.println(111);
					String msg = "|banuser|"+ user +"|";
					out.println(msg);   // 클라이언트로 명령 메시지 발송
					
					sql = " insert into userban "
							+ " values ('"+name+"', '"+user+"')";							
					stmt.executeUpdate(sql);
					
					sql = "commit";
					stmt.executeUpdate(sql);
				}
				else {
					out.println("입력하신 유저가 검색되지 않습니다.");
				}
			}			
		} catch(Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}			
	}
	// 개인 차단 유저 해제
	public void delBanUser(String name, PrintWriter out, BufferedReader in) 
			throws IOException
	{
		String user = null;
		try 
		{
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();

			out.println("대화를 차단을 해제할 유저의 닉네임을 입력해주세요.");
			
			user = in.readLine();
			
			sql = " select * from userban "
					+ " where nick = '"+name+"' "
							+ "and banuser = '"+ user+"' ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if(rs.getString("banuser").equals(user)) {
					String msg = "|delbanuser|"+ user +"|";
					out.println(msg);   // 클라이언트로 명령 메시지 발송
					
					sql = " delete from userban "
							+" where name = '"+name+"' "
									+ " and banuser = '"+user+"' ";
					stmt.executeUpdate(sql);
					sql = "commit";
					stmt.executeUpdate(sql);
				}
				else {
					out.println("입력하신 유저가 검색되지 않습니다.");
				}
			}			
		} catch(Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}			
	}
	
	// 회원 탈퇴
	public void withdrawal(String name, PrintWriter out, BufferedReader in) throws IOException {
		if (in.readLine().equalsIgnoreCase("Y")) {
			try {

				Connection con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe", 
						"scott", 
						"tiger");
				Statement stmt = con.createStatement();
				
			
				String sql1 = " select * from chat where nick = '"+name+"' ";
				ResultSet rs = stmt.executeQuery(sql1);
			
				String pw = null;
				String pwck = null;

				while (rs.next()) {
					pwck = rs.getString("PW");
				}

				out.println("비밀번호를 입력해주세요.");

				pw = in.readLine();

				if (pw.equals(pwck)) {
					//Thread.sleep(1000);
				
					sql1 = " delete from Chat where NICK = '" + name + "'";
					stmt.executeUpdate(sql);
					System.out.println(11112);
					sql1 = " delete from Chatroom where NICK = '" + name + "'";
					stmt.executeUpdate(sql);
					System.out.println(11113);
					sql1 = " delete from banword where NICK = '" + name + "'";
					stmt.executeUpdate(sql);
					sql1 = " delete from chatroomban where NICK = '" + name + "'";
					stmt.executeUpdate(sql);
					sql1 = " delete from userban where NICK = '" + name + "'";
					stmt.executeUpdate(sql);
					sql1 = " delete from userban where banuser = '" + name + "'";
					stmt.executeUpdate(sql);
					sql1 = "commit";
					stmt.executeUpdate(sql);

					roomMap.remove(name);
					clientMap.remove(name);

					out.println("회원탈퇴가 정상적으로 진행 되었습니다.");
					out.println('Q');

					con.close();
					stmt.close();
				} else {
					out.println("비밀번호가 일치하지 않습니다.");
					con.close();
					stmt.close();
					return;
				}
			} catch (Exception e) {
			}
		} else {
			out.println("회원탈퇴 진행이 취소되었습니다.");
			return;
		}
	}
	//=========관리자 전용 명령어
	// 관리자 로그인
	public void adminLogin (String name, PrintWriter out, BufferedReader in) {
		
		try 
		{
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			
			boolean author = false;
			
			sql = "select * from chat where NICK = '" + name + "'";
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("AUTHOR").equalsIgnoreCase("MASTER"))
					author = true;
			}
						
			if(author == true) {
				out.println("이미 관리자 권한을 가졌습니다.");
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
				return;
			}
			
			out.println("관리자 로그인을 위한 패스워드를 입력하세요!");
			int ck = 0;
			while(true) {
				String adminPw = "1234";
				out.print("관리자 비밀번호 입력>>: ");
				String ckadmin = in.readLine();
				if (adminPw.equals(ckadmin))
					break;
				ck++;
				out.println(ck+"회 관리자 비밀번호 오류");
				if(ck == 3) {
					out.println("관리자 비밀번호를 확인해주세요!");
					if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
					stmt.close();
					con.close();
					return;
				}					
			}
			
			sql = "update chat " + 
				     " set  AUTHOR = 'MASTER' " + 
				     " where NICK = '"+ name +"' ";
			stmt.executeUpdate(sql);			
			sql = "commit";
			stmt.executeUpdate(sql);
			
			out.println(">> 관리자 로그인을 하셨습니다.");
			out.println(">> 관리자 명령어 사용이 가능하니 /? or /help로 확인해보세요!");
		
		} catch(Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {e.printStackTrace();}
		}	
	}
	
	// 관리자 로그아웃
	public void adminLogout(String name, PrintWriter out, BufferedReader in) {

		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			
			boolean author = ckAuthor(name, "", 0);
			
			//sql = "select * from chat where NICK = '" + name + "'";
			
			//ResultSet rs = stmt.executeQuery(sql);
			//while (rs.next()) {
			//	if (rs.getString("AUTHOR").equalsIgnoreCase("MASTER"))
			//		author = true;
			//}
						
			if(author == false) {
				out.println("현재 관리자 권한을 가지고 있지 않습니다.");
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
				return;
			}
			
			out.println("관리자 로그아웃을 위한 패스워드를 입력하세요!");
			int ck = 0;
			while(true) {
				String adminPw = "1234";
				out.print("관리자 비밀번호 입력>>: ");
				String ckadmin = in.readLine();
				if (adminPw.equals(ckadmin))
					break;
				ck++;
				out.println(ck+"회 관리자 비밀번호 오류");
				if(ck == 3) {
					out.println("관리자 비밀번호를 확인해주세요!");
					if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
					stmt.close();
					con.close();
					return;
				}					
			}
			
			sql = "update chat " + 
				     " set AUTHOR = ' ' " + 
				     " where NICK = '"+name+"'";
			stmt.executeUpdate(sql);			
			sql = "commit";
			stmt.executeUpdate(sql);
			
			out.println(">> 관리자 로그아웃을 하셨습니다.");
		
		} catch(Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {e.printStackTrace();}
		}	
	}
	
	public boolean ckAuthor (String name, String user, int n) 
	{
		String author = null;
		try 
		{
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();

			sql = "select * from chat where NICK = '" + name + "'";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				author = rs.getString("AUTHOR");    // 권한 MASTER일 경우 관리자
			}
			if(author.equalsIgnoreCase("MASTER")) {
				if(n == 0) // 관리자 명령 0 체크만해라
					return true;
				// 0을 가질 경우 관리자만 체크
				if(n == 1) // 블랙리스트 추가
				{
					sql = "update chat " + 
						     " set  BAN = 'ban' " + 
						     " where NICK = '"+ user +"' ";
					stmt.execute(sql);
												
					sql = "commit";
					stmt.execute(sql);
					return true;
				}
				else if(n == 2) // 블랙리스트 해제
				{
					sql = "update chat " + 
						     " set  BAN = ' ' " + 
						     " where NICK = '"+ user +"' ";
					stmt.execute(sql);
					
					sql = "commit";
					stmt.execute(sql);
					return true;
				}
			} else {
				return false;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {}
		}
		return false;
	}
	
	public void inBlackList (String name, String s, PrintWriter out, BufferedReader in) throws IOException {  // 블랙리스트에 추가
		// 관리자 권한 여부 확인
		String userck = null;
		String user = "";
		out.println("대상의 닉네임을 입력하세요.");
		userck = in.readLine();
		
		if(ckAuthor(name, userck, 1) == true) // user는 블랙리스트 추가시킬사람, 1 블랙리스트 추가까지
		{
			System.out.println(11111111);
			Iterator<String> it = clientMap.keySet().iterator();
			while (it.hasNext()) 
			{
				user = it.next();
				if (userck.equals(user)) { // 대상이 같을 경우 블럭
					System.out.println(222222);
					out.println(user + "님을 블럭 시켰습니다.");
					out = clientMap.get(user); 
					out.println('Q');
				}				
			}		
		} else {
			out.println("권한이 없거나 대상의 닉네임이 정확하지 않습니다.");
		}
	}
	
	public void outBlackList (String name, String s, PrintWriter out, BufferedReader in) throws IOException {  // 블랙리스트에서 제외
		// 관리자 권한 여부 확인
		String userck = null;
		String user = "";
		out.println("대상의 닉네임을 입력하세요.");
		userck = in.readLine();
		
		if(ckAuthor(name, userck, 2) == true) // user는 블랙리스트 추가시킬사람, 1 블랙리스트 추가까지
		{
			Iterator<String> it = clientMap.keySet().iterator();
			while (it.hasNext()) 
			{
				user = it.next();
				if (userck.equals(user)) { // 대상이 같을 경우 블럭 해제
					out.println(user + "님의 블럭을 해제 시켰습니다.");
				}				
			}		
		} else {
			out.println("권한이 없습니다.");
		}
	}
	
	public void ban(String name, String user, String s, PrintWriter out) {
		// 관리자 권한 여부 확인
		if(ckAuthor(name,"", 0) == true)  // 0 프로그램 종료만
		{  
			Iterator<String> it = clientMap.keySet().iterator();
			while (it.hasNext()) {
				String tmp = it.next();
				if (String.valueOf(s.substring(5, (5 + tmp.length()))).equals(tmp)) {
					out = clientMap.get(tmp); 
					out.println("강제 퇴장으로 프로그램이 종료 되었습니다.");
					out.println('Q');						
				}
			}
		}
		else 
		{
			out.println("권한이 없습니다.");
		}
	}
	
	// 방생성 메서드
	public void createChatRoom(String name, PrintWriter out, BufferedReader in) 
			throws IOException 
	{
		
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			String title = null;
			String limit = null;
			String roompw = null;
			
			// 채팅방 내에서는 방생성 불가
			int rck = 0; 
			sql = "select count(*) from chatroom where NICK = '" + name + "'";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				rck = Integer.valueOf(rs.getString(1));
			}
			if (rck != 0) {
				out.println("채팅방에서는 새로운 채팅방을 생성할 수 없습니다.");
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
				return;
			}
						
			// 채팅방 이름 중복 체크
			while(true) 
			{
				out.println("생성할 채팅방 이름을 입력해주세요.");
				title = in.readLine();
				
				int tck = 0; 
				sql = "select count(*) from chatroom where TITLE = '" + title + "'";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					tck = Integer.valueOf(rs.getString(1));
				}
				if (tck == 0)
					break;
				out.println("같은 이름의 채팅방이 존재하고 있습니다.");
			}
			
			// 비밀방 생성여부 체크
			int ChecksRoom = 0;
			while(true) 
			{
				out.println("채팅방 비밀번호를 설정 하시겠습니까?");	
				out.println("1. 채팅방 비밀번호 설정");				
				out.println("2. 채팅방 비밀번호 미설정");
				ChecksRoom = Integer.valueOf(in.readLine());				
				if(ChecksRoom == 1) {
					out.println("비밀방의 비밀번호를 입력해주세요!");
					roompw = in.readLine();
					break;
				}else if (ChecksRoom == 2) {
					break;
				}else
					out.println("1 또는 2의 숫자로 입력해주세요!");
					continue;
			}
						
			// 채팅방 정원 체크
						
			while(true)
			{
				out.println("생성할 채팅방의 정원을 설정해주세요.");
				limit = in.readLine();
				
				char cklimit = ' ';
				int ck = 0;				
				for (int i = 0; i < limit.length(); i++) {
					cklimit = limit.charAt(i);
					if (cklimit >= 0x30 && cklimit <= 0x39) {}
					else {ck++;} // 1보다 클경우 문자포함					
				}
				
				if(ck == 0 && Integer.valueOf(limit) > 1)  // 100이 최대값
					break;
				
				out.println("정원은 1보다 큰 숫자로 입력하셔야 합니다.");
			}			
			
			if(roompw != null) 
			{
				sql = "insert into chatroom(TITLE, PW, LIMIT, NICK, ADMIN) " + 
						" values('"+title+"', '"+roompw+"', '"+limit+"', '"+name+"', 'master')";
				stmt.executeUpdate(sql);			
				sql = "commit";
				stmt.executeUpdate(sql);
				out.println("[" + title + "]비밀방에 입장하셨습니다.");
			}
			
			else 
			{
				sql = "insert into chatroom(TITLE, LIMIT, NICK, ADMIN) " + 
						" values('"+title+"', '"+limit+"', '"+name+"', 'master')";
				stmt.executeUpdate(sql);			
				sql = "commit";
				stmt.executeUpdate(sql);
				out.println("[" + title + "]방에 입장하셨습니다.");
			}		
			
			roomMap.put(name, title);
			
			if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
			stmt.close();
			con.close();
			
		} catch (SQLException e) {
		} finally {
			try {
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}		
	}
	
	// 방입장 메서드
	public void inChatRoom(String name, PrintWriter out, BufferedReader in) 
			throws IOException {
		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			int limit = 0;
			int index;
			String title = null;
			// 입장이 불가능 한 경우////////
			// 채팅방 입장해 있을 경우 O
			// 비밀번호 오류 O
			// 채팅방 정원 초과 O
			// 채팅방에서 영구BAN 당한 경우 O
			 
			int rck = 0; 
			sql = "select count(*) from chatroom where NICK = '" + name + "'";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				rck = Integer.valueOf(rs.getString(1));
			}
			if (rck != 0) { // 채팅방 입장한 경우
				out.println("채팅방에서는 다른 채팅방에 입장할 수 없습니다.");
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
				return;
				
			}
			 
			List<String> tList = new ArrayList<>();
			
			sql = "select DISTINCT(title), PW from chatroom";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tList.add(rs.getString("TITLE"));
			}
		
			// 뽑아낸 리스트 번호 매기기!
			for (int i = 0; i < tList.size(); i++) {
				out.println((i + 1) + ". [" + tList.get(i) + "]");
			}
			
			// 채팅방 선택
			out.println("입장하실 채팅방리스트의 번호를 입력해주세요!");
			
			// 숫자 체크 맞을 경우 title
			while(true)
			{
				title = null;
				index = (Integer.valueOf(in.readLine()) - 1);  //입력
				title = tList.get(index);
				if(title != null)
					break;
			}
			
			String banCk = null;
			sql = "select * from chatroomban where bantitle = '" + title + "'";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				banCk = rs.getString(1);
			}
			if(banCk != null) { // 채팅방에서 영구BAN 당한 경우
				out.println("현재 <"+banCk+">채팅방 BAN 상태입니다.");
				out.println("채팅방 방장에게 문의하세요!");
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
				return;
			}
			
			// 정원 체크
			String ckpw = null; // 받는김에 비밀번호도 받음
			int ckp = 0;
			sql = "select * from chatroom where TITLE = '"+title+"'"; // 채팅방에 몇명이 있는지 확인
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				limit = Integer.valueOf(rs.getString("LIMIT"));
				ckpw = rs.getString("PW");
				ckp++;
			}
		

			if(ckp >= limit) { //채팅방 정원 초과
				out.println("채팅방 정원 초과: 입장할 수 없습니다.");
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
				return;
			}
			
			// 비밀방일 경우 5회 불일치시 대기실로 이동
			if(ckpw != null) { 
				out.println("비밀방 입니다.");
				out.println("비밀방 비밀번호를 입력해주세요!");
				int i = 0;
				while(true) {
					String roompw = in.readLine();
					if(ckpw.equals(roompw)) {
						sql = "insert into chatroom(TITLE, PW, LIMIT, NICK) " + 
								" values('"+title+"','"+roompw+"','"+limit+"','"+name+"')";
						stmt.execute(sql);
						sql = "commit";
						stmt.execute(sql);
						
						out.println("현재 채팅방 입장인원 : " + (ckp+1) +"명");
						roomMap.put(name, title);
						out.println("[" + title + "]방에 입장하셨습니다.");			
						sendRoomMsg(name, title, "님이 입장하셨습니다.");
						
						if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
						return;
					}
					i++;
					out.println("비밀방 비밀번호 불일치" + i + "회");
					if(i == 5) { // 비밀번호 오류
						out.println("방장에게 확인해주세요.");
						return;
					}						
				}				
			}else {
				out.println("현재 채팅방 입장인원 : " + ckp +"명");
				sql = "insert into chatroom(TITLE, LIMIT, NICK) " + 
						" values('"+title+"','"+limit+"','"+name+"')";
				stmt.execute(sql);
				
				sql = "commit";
				stmt.execute(sql);
				roomMap.put(name, title);
				out.println("[" + title + "]방에 입장하셨습니다.");			
				sendRoomMsg(name, title, "님이 입장하셨습니다.");	
			}			
		} catch (SQLException e) {
		} finally {
			try {
				if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}		
	}
	
	// 방 리스트 확인 메서드
	public void roomlist(String name, PrintWriter out) 	{
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			
			List<String> tList = new ArrayList<>();

			sql = "select DISTINCT title from chatroom";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tList.add(rs.getString("TITLE"));
			}

			for (int i = 0; i < tList.size(); i++) {
				out.println((i + 1) + ". [" + tList.get(i) + "]");
			}
		} catch (SQLException e) {
		} finally {
			try {
				if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
	}
	
	// 방입장 여부 확인
	public String ckRoom (String name, PrintWriter out) {		
		String title = null;
		String tmp = null;
		try {			 
			Iterator<String> it= roomMap.keySet().iterator();
			while(it.hasNext()) {
				tmp = it.next(); // 키값을 대입
				if(name.equals(tmp)) // 키값이 닉네임과 같을때
					title = roomMap.get(tmp); // 맵값이 타이틀
			}								
		} catch (Exception e) {
		}		
		return title;
	}
	
	// 방퇴장 메서드
	public void outRoom(String name, PrintWriter out) {

		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			String title = null;
			String admin = null;
			
			sql = "select * from chatroom where NICK = '" + name + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				title = rs.getString("TITLE");
				admin = rs.getString("ADMIN");
			}
			
			if(title == null) 
			{
				out.println("현재 대기실입니다.");
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
				return;
			}
			
			if(admin != null) {
				out.println("방장은 퇴장할 수 없습니다.");
				if(rs !=null) rs.close();    // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
				return;
			}				
			
			sendRoomMsg(name, title, "님이 퇴장하였습니다.");
			roomMap.remove(name);
			sql = "delete from chatroom where nick = '"+name+"'";
			stmt.execute(sql);

			sql = "commit";
			stmt.execute(sql);


		} catch (SQLException e) {
		} finally {
			try {
				if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
	}
	
	// 방장 권한 메서드////////////////////////////////
	
	// 방폭 = 방장 방퇴장 메서드	
	public void boomRoom(String name, PrintWriter out) {
		// 방장만 가능
		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			String title = null;
			String admin = null;
			
			List<String> rUser = new ArrayList<String>();
			sql = "select ADMIN, TITLE from chatroom where NICK = '"+name+"'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				title = rs.getString("TITLE");
				admin = rs.getString("ADMIN");
			}
			sql = "select NICK from chatroom where TITLE = '" + title + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				rUser.add(rs.getString("NICK"));
			}
			if(admin.equalsIgnoreCase("master")) {
				sendRoomMsg(" ", " ", "방장이 퇴장하여 대기실로 입장합니다.");
				sql = "delete FROM chatroom where TITLE = '"+title+"'";
				stmt.execute(sql);
				sql = "commit";
				stmt.execute(sql);
				for(int i = 0 ;i < rUser.size();i++) {
					roomMap.remove(rUser.get(i));
				}					
				return;
			}
			out.println("권한이 부족합니다.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
	}
	
	// 방장 권한 이전
	public void ChangeRM (String name, PrintWriter out, BufferedReader in) 
			throws IOException {
		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			String title = null;
			String admin = null;
			
			sql = "select * from chatroom where NICK = '" + name + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				title = rs.getString("TITLE");
				admin = rs.getString("ADMIN");
			}
			
			if(admin != null){
				out.println("방장을 위임할 유저 닉네임을 입력해 주세요!");
				String mUser = in.readLine(); // 위임 받을 유저의 닉네임
				//먼저 이 사람이 같은방 사람인지 확인 필요
				sql = "select * from chatroom where NICK = '" + mUser + "'";
				rs = stmt.executeQuery(sql);
				String ckTitle = null;
				while (rs.next()) {
					ckTitle = rs.getString("TITLE");
				}
				if(title.equals(ckTitle)) { // 같을 경우 방장권한 위임
					out.println("방장 권한을["+mUser+"]님에게 위임합니다.");
					sendRoomMsg(name, title, "방장 변경: " + mUser);
					sql = "update chatroom "
							+ " set ADMIN = 'master' "
							+ " where NICK = '"+mUser+"'";
					stmt.execute(sql);
					
					sql = "update chatroom "
							+ " set ADMIN = ' ' "
							+ " where NICK = '"+name+"'";
					stmt.execute(sql);
					
					sql = "commit";
					stmt.execute(sql);
				} else {
					out.println("다른 채팅방 사용자에게는 방장을 위임할 수 없습니다.");
				}				
			}
		} catch (SQLException e) {
		} finally {
			try {
				if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
	}
	
	// 채팅방 강퇴
	public void banRoom (String name, PrintWriter out, BufferedReader in) 
			throws IOException {
		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			String title = null;
			String admin = null;
			
			sql = "select * from chatroom where NICK = '" + name + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				title = rs.getString("TITLE");
				admin = rs.getString("ADMIN");
			}
			
			if(admin != null){
				out.println("강퇴할 유저의 닉네임을 입력해주세요.");
				String tUser = in.readLine(); // 위임 받을 유저의 닉네임
				//먼저 이 사람이 같은방 사람인지 확인 필요
				sql = "select * from chatroom where NICK = '" + tUser + "'";
				rs = stmt.executeQuery(sql);
				String ckTitle = null;
				while (rs.next()) {
					ckTitle = rs.getString("TITLE");
				}
				if(title.equals(ckTitle)) { // 같을 경우 방장권한 위임
					out.println("["+tUser+"]님을 강퇴시킵니다.");
					sendRoomMsg(name, title, "["+tUser+"]님이 채팅방에서 나가셨습니다.");
					
					roomMap.remove(tUser);
					
					sql = "delete from chatroom where nick = '"+tUser+"'";
					stmt.execute(sql);
					
					sql = "commit";
					stmt.execute(sql);
				} else {
					out.println("유저를 찾을 수 없습니다.");
				}				
			}
		} catch (SQLException e) {
		} finally {
			try {
				if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
	}
	
	// 방 영구 강퇴
		public void ban2Room(String name, PrintWriter out, BufferedReader in) 
				throws IOException {
			try {
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
				Statement stmt = con.createStatement();
				ResultSet rs = null;
				String title = null;
				String admin = null;
				
				sql = "select * from chatroom where NICK = '" + name + "'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					title = rs.getString("TITLE");
					admin = rs.getString("ADMIN");
				}
				
				if(admin != null){
					out.println("영구 강퇴시킬 유저의 닉네임을 입력해주세요.");
					String tUser = in.readLine(); // 위임 받을 유저의 닉네임
					//먼저 이 사람이 같은방 사람인지 확인 필요
					sql = "select * from chatroom where NICK = '" + tUser + "'";
					rs = stmt.executeQuery(sql);
					String ckTitle = null;
					while (rs.next()) {
						ckTitle = rs.getString("TITLE");
					}
					if(title.equals(ckTitle)) { // 같을 경우 방장권한 위임
						out.println("["+tUser+"]님을 영구강퇴시킵니다.");
						sendRoomMsg(name, title, "["+tUser+"]님이 \n<"+title+"> 방에서 영구강퇴 되셨습니다.");
						roomMap.remove(tUser);
						
						sql = "delete from chatroom where nick = '"+tUser+"'";
						stmt.execute(sql);
						
						sql = "insert into chatroomban (nick, bantitle)"
								+" values('"+tUser+"', '"+ title +"')";
						stmt.execute(sql);
						
						sql = "commit";
						stmt.execute(sql);
					} else {
						out.println("유저를 찾을 수 없습니다.");
					}				
				}
			} catch (SQLException e) {
			} finally {
				try {
					if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
					stmt.close();
					con.close();
				} catch (Exception e) {
				}
			}			
		}
		
	
	
	//////////////////////////////////
	
		
	 // 방 초대
	public void inviteRoom(String name, PrintWriter out, BufferedReader in) 
			throws IOException {
		
		// 방장만 가능
		try {
				
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			String title = null;
			String admin = null;
			String ckUser = null;
			
			sql = "select * from chatroom where NICK = '" + name + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				title = rs.getString("TITLE");
				admin = rs.getString("ADMIN");
			}			
			
			if(admin != null) { // 방장일 경우 
				out.println("초대할 사용자의 닉네임을 작성해주세요!");
				ckUser = in.readLine();
				
				sql = "select NICK from chat where NICK = '" + ckUser + "'";
				rs = stmt.executeQuery(sql);				
				String invUser = null;
				while (rs.next()) 
				{
					invUser = rs.getString(1);
				}
				
				sql = "select count(*) from chatroom where NICK = '"+invUser+"'";
				
				int ck = 0;
				while (rs.next()) {
					ck = rs.getInt(1);
				}
				
				if(ck != 0) {
					out.println("채팅방에 입장 중인 경우 초대할 수 없습니다.");
					return;
				}
				
				if(invUser != null) 
				{
					String invMsg = "|invroom|" + title+"| 채팅방에서 |" + name + "|님이 초대를 하셨습니다.";					
					out = clientMap.get(invUser); 
					out.println(invMsg); // 초대 메시지 발송
					out.println("'/agree' 명령어 입력시 입장합니다.");
					return;
				}
				else {
					out.println("상대방 닉네임을 찾을 수 없습니다. \n"
							+ "확인 후 다시 시도해주세요.");
					return;					
				}				
			}
			out.println("권한이 부족합니다. 방장에게 문의하세요!");
			
		} catch (SQLException e) {
		} finally {
			try {
				if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
	}
	 // 방 초대 수락
	public void invAgree(String msg, String name, PrintWriter out) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			Statement stmt = con.createStatement();
			
			//System.out.println(msg);
			
			StringTokenizer s = new StringTokenizer(msg, "|");
			s.nextToken("|");
			String title = s.nextToken("|");
			
			sql = "select TITLE from chatroom where NICK = '"+name+"'";
			rs = stmt.executeQuery(sql);
			String ckTitle = null;
			while (rs.next()) {
				ckTitle = rs.getString(1);
			}
			
			if(title.equals(ckTitle)) {
				out.println("이미 <" + title + ">방에 초대되어 입장하셨습니다.");
				return;
			}
			
			// 서버에 저장
			roomMap.put(name, title);
			out.println("[" + title + "]방에 입장하셨습니다.");			
			sendRoomMsg(name, title, "님이 입장하셨습니다.");					
			
			// DB에 저장
			sql = "select * from chatroom where TITLE = '"+title+"'";
			rs = stmt.executeQuery(sql);
			String limit = null;
			while (rs.next()) {
				limit = rs.getString("LIMIT");
			}
			
			sql = "insert into chatroom(TITLE, LIMIT, NICK) " + 
					" values('"+title+"','"+limit+"','"+name+"')";
			stmt.execute(sql);
			sql = "commit";
		} catch (SQLException e) {
		} finally {
			try {
				if(rs !=null) rs.close(); // 오류 java.lang.NullPointerException
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// 메인
	
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException{
		// 서버객체 생성.
		MultiServer13 ms = new MultiServer13();
		ms.init();
	}
	////////////////////////////////////////////////////////////////
	// 내부 클래스
	// 클라이언트로부터 읽어온 메시지를 다른 클라이언트(socket)에 보내는 역할을 하는 메서드

	class MultiServerT extends Thread {
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;

		// 생성자
		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(
						this.socket.getInputStream()));
			} catch (Exception e) {
				//System.out.println("예외 : " + e);
			}
		}

		// 스레드를 사용하기 위해서 run()메서드 재정의
		@Override
		public void run() {			
			
			String id = null;
			String name = null; // 클라이언트로부터 받은 이름을 저장할 변수
			String user = null; // 귓속말 고정 세팅
			String title = null; // 채팅방 고정 세팅  채팅방 닉네임 체크 있을 경우 not null 
			List<String> bWord = new ArrayList<>(); // 처음 채팅프로그램 서버접속시 금칙어 리스트 받기
			
			Date date = new Date(); // 현재 시간 날짜
			StringTokenizer d = new StringTokenizer(date.toString());
			String week = d.nextToken();
			String month = d.nextToken();
			String day = d.nextToken();
			String time = d.nextToken();
			String timezone = d.nextToken();
			String year = d.nextToken();
			
			String ip = socket.getInetAddress() + ":" + socket.getPort();
			out.println("채팅을 시작합니다.");
			 // 시간을 원하는 곳에 나타내기 위해 값을 대입받음
			
			try {				
				id = in.readLine(); // 클라이언트에서 처음으로 보내는 메세지는 id 입니다.
			
				name = idLogin(id, bWord, out);
				String logFileName = year+"_"+month+"_"+day+ "_log_"+name+"_1.txt";  // 파일명
				
				File f = new File(logFileName);
				int num = 1;
				while (true) { // 로그 파일명이 동일한게 있을 경우 파일명 숫자가 1 증가					
					if (f.isFile() == true) {
						num++;
						logFileName = year+"_"+month+"_"+day+ "_log_"+name+"_"+num+".txt";
						f = new File(logFileName);
					} else {
						break;
						}			
				}			
				BufferedWriter log = null;
				try {
					log = new BufferedWriter
							(new OutputStreamWriter
									(new FileOutputStream(logFileName),"UTF8"));
				} catch (UnsupportedEncodingException | FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				log.write(time+" | "+id+" | "+name+"_Login | [" + (Thread.activeCount() - 1) + "] User Login");
				log.newLine(); 
				log.write("Time | ID | NICKNAME | CHATROOM | WHISPER | OUT_MESSAGE");
				log.newLine(); 
				
				sendAllMsg("", name + "님이 입장을 하셨습니다.");
				clientMap.put(name, out);
				System.out.println("채팅 프로그램 총 접속자 수: " + (Thread.activeCount() - 1) + "명");				
				String s = null;
				while (in != null) {
					s = in.readLine();           // 메시지를 클라이언트로 부터 전달 받음
					s = ckMsg(name, s, bWord);   // 금칙어 체크
					title = ckRoom(name, out);   // 현재 방에 입장 중인지 체크
					
					log.write(time+"|"+id+"|"+name+"|"+title+"|"+user+"| 입력: "+ s); // 파일에 입력
					log.newLine(); // 라인 추가
					log.flush();  
					
					if (s.substring(0, 1).equals("/")){
						mGoto(name, user, title, s, out, in); //명령어로 이동
						if(s.startsWith("/to")==true)
							user = wUser(name, s, out);  // 귓말 고정
					} else { 			
						if (user != null) {
							whisperMsg(name, user, s, out);
						}  // 귓속말 고정							
						
						else if (title != null)
							//System.out.println("채팅방에서 대화");
							sendRoomMsg(name, title, s);
						else // 대기실 대화
							//System.out.println("대기실 대화");
							sendAllMsg(name, s);
						}

					//System.out.println("["+name+"]:" + s);
				}
//				System.out.println("Bye...");

			} catch (Exception e) {
				System.out.println("예외 11 : " + e);
			} finally {
				// 예외가 발생할때 퇴장. 해쉬맵에서 해당 데이터 제거.
				// 보통 종료하거나 나가면 java.net.SocketException: 예외 발생
				clientMap.remove(name);
				idLogout(id);
				sendAllMsg("", name + "님이 퇴장하셨습니다.");
				System.out.println("현재 접속자 수는 " + clientMap.size() + "명 입니다.");
				try {
					in.close();
					out.close();
					socket.close();
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		}
		
		//귓속말 유저 고정
		public String wUser(String name, String s, PrintWriter out) { 
			String user = null;
			Iterator<String> it = clientMap.keySet().iterator();
			while (it.hasNext()) {
				String tmp = it.next();
				//System.out.println(tmp);
				try {
					//if (String.valueOf(s.substring(4, (4 + tmp.length()))).equals(tmp)) {
					if (s.endsWith(tmp) == true) {
						user = tmp;
						//System.out.println(user);
						if(user.equals(name)) {
							out.println("본인에게는 귓속말을 할 수 없습니다.");
							user = null;
						}
							
						return user;
					} else {
						//System.out.println(user);
						//return user;
					}										
				}catch (Exception e) { 
				}
			}
			return user;
		}
		
		// 금칙어 체크
		public String ckMsg(String name, String s, List <String> bWord) {
									
			String msg = s;
			
			for(int i=0;i<bWord.size();i++) {
				msg = msg.replace(bWord.get(i), "**");
			}
			return msg;
		}
		
		public String ckName(String id) {
			String name = null;
			try {
				Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
				Statement stmt = con.createStatement();
				
				sql = "select * from chat where id = '" + id + "'";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) {
					name= rs.getString("nick");
				}
				
				sql = "commit";
				stmt.executeUpdate(sql);
				
			} catch (Exception e) {
			}  finally {
				try {
					if(rs !=null) rs.close();   // 오류 java.lang.NullPointerException
					stmt.close();
					con.close();
				} catch (Exception e) {}
			}				
			
			return name;
		}
		
		public String idLogin(String id, List<String> bWord, PrintWriter out) {  
			// 로그인시 필요한 값을 받고 닉네임을 전달
			
			String name = null;
			List<String> banUser = new ArrayList<>();
			List<String> userbWord = new ArrayList<>(); // 처음 채팅프로그램 서버접속시 개인 금칙어 리스트 받기
			
			try {
				Connection con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe",
						"scott",
						"tiger");
				
				Statement stmt = con.createStatement();
				
				String ip = socket.getInetAddress() + ":" + socket.getPort();
				String sql = "update chat " + 
						     " set ip = '"+ip+"' " + 
						     " where id = '"+id+"' ";
			
				stmt.executeUpdate(sql);
				// 로그인시 금칙어 리스트 추가
				sql = "select * from Bword";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					bWord.add(rs.getString(1));
				}
				
				// 로그인시 닉네임 구해서 메서드 리턴 값으로 사용
				sql = "select * from chat where id = '"+id+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					name = rs.getString("NICK");
				}
				
				// 로그인시 입장한 채팅방 정보 불러오기
				String title = null;
				sql = "select * from chatroom where nick = '"+name+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					title = rs.getString("TITLE");
				}				
				roomMap.put(name, title);
				
				// 로그인시 개인 금칙어 리스트 불러오기
				sql = "select * from banword where nick = '"+name+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					userbWord.add(rs.getString("WORD"));  // 유저 개인 금칙어 받음
				}
				if(userbWord.size()>0) {  // 0이면 없는 상태
					for(int i=0;i<userbWord.size();i++) {
						out.println("|addword|"+userbWord.get(i)+"|");
					}
				}
				
				// 로그인시 대화차단 리스트 불러오기
				sql = "select * from userban where nick = '"+name+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					banUser.add(rs.getString("BANUSER"));  // 유저 개인 금칙어 받음
				}
				if(banUser.size()>0) {  // 0이면 없는 상태
					for(int i=0;i<userbWord.size();i++) {
						out.println("|banuser|"+banUser.get(i)+"|");
					}
				}				
				sql = "commit";
				stmt.executeUpdate(sql);
				
				rs.close();
				stmt.close();
				con.close();
				return name;
			} catch (SQLException e) 
			{
				e.printStackTrace();
			} finally {
				try	{
					if (rs != null)
						rs.close(); // 오류 java.lang.NullPointerException
					stmt.close();
					con.close();
					return name;
				} 
				catch (Exception e)	{}
			}return name;
		}
		
		public void idLogout(String id) {
			try {
				Connection con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe",
						"scott",
						"tiger");
				
				Statement stmt = con.createStatement();
				
				String ip = socket.getInetAddress() + ":" + socket.getPort();
				String sql = "update chat " + 
						" set ip = '"+ ip +"' " + 
						" where id = '"+ id +"' ";
			
				stmt.executeUpdate(sql);		
				sql = "commit";
				stmt.executeUpdate(sql);
				
				stmt.close();
				con.close();				
			} catch (SQLException e) {
			}
		}
		
		// check 알파벳
		public boolean checkAlpbetNumber(String ck) {
			char idChar;
			for (int i = 0; i < ck.length(); i++) {
				idChar = ck.charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크
				if (idChar >= 0x61 && idChar <= 0x7A) {				// 영문(소문자) OK!
				} else if (idChar >= 0x41 && idChar <= 0x5A) {
					// 영문(대문자) OK!
				} else if (idChar >= 0x30 && idChar <= 0x39) {
					// 숫자 OK!
				} else {
					return false; // 영문자도 아니고 숫자도 아님!
				}
			}
			return true;
		}
		
		// check 숫자
		public boolean ckNumber(String ckNum) {
			char cklimit = ' ';
			int ck = 0;
			for (int i = 0; i < ckNum.length(); i++) {
				cklimit = ckNum.charAt(i);
				if (cklimit >= 0x30 && cklimit <= 0x39) {
				} else {
					ck++;
				} // 1보다 클경우 문자포함
			}
			if (ck == 0) // 100이 최대값
				return true;
			else
				return false;
		}
		
		public void mGoto(String name, String user, String title, String s, 
				PrintWriter out, BufferedReader in) {
			try {
				//System.out.println("명령어 목록 확인: /? or /help");
				if (s.startsWith("/?") || s.startsWith("/help"))
//				if (String.valueOf(s.substring(1, 2)).equals("?")||
//						(s.substring(1, 5)).equals("help")) 
				{
					out.println("<<명령어 목록>>");
					out.println("======================================================");
					out.println("[/to]      : '/to + 대상'  귓속말 on");
					out.println("             '/to' 입력시 귓속말 off");
					out.println("[/list]    : 모든 사용자 리스트 출력");
					out.println("[/waituser]: 대기실 유저 리시트 출력");
					out.println("[/rlist]   : 채팅방 리스트 출력");
					out.println("[/adword]  : 개인 금칙어 설정 추가");
					out.println("[/delword] : 개인 설정 금칙어 삭제");
					out.println("[/adduser] : 지정 유저의 대화 차단");
					out.println("[/deluser] : 지정 유저의 대화 차단 해제");
					out.println("[/agree]   : 채팅방 초대 '수락' 명령어");
					out.println("	          방장 초대가 없을 경우 사용불가");
					out.println("[/adlogin]	: 관리자 아이디 로그인");
					out.println("[/withdrawal]	: 회원탈퇴");					
					out.println("======================================================");
					
					if (ckRoom(name, out) != null) {
						out.println("<<채팅방 명령어 목록>>");
						out.println("======================================================");
						out.println("[/room]    : 채팅방 생성");
						out.println("[/rjoin]   : 채팅방 선택 후 입장");
						out.println("[/ruser]   : 채팅방 유저 리스트 출력");
						out.println("[/rout]    : 현재 채팅방에서 퇴장");
						out.println("[/rinv]    : '/rinv + 대상' 채팅방에 초대(방장 전용)");
						out.println("[/rboom]   : 방장의 채팅방 폭파 및 퇴장(방장 전용)");
						out.println("[/rban]    : 채팅방 퇴장 명령(방장 전용)");
						out.println("[/rblock]  : 채팅방 영구 퇴장 명령(방장 전용)");
						out.println("[/rmchage] : 방장 위임 명령어(방장 전용)");
						out.println("======================================================");
							
					}
					if (ckAuthor(name, user, 0) == true) {
						out.println("<<관리자 전용 명령어 목록>>");
						out.println("======================================================");
						out.println("[/noti]    : 모든 사용자에게 공지 메시지를 발송합니다.");
						out.println("[/ban]     : '/ban + 대상' 채팅 프로그램 종료");
						out.println("[/addblock]   : 입력 대상 종료 및 접속 블랙리스트 추가");
						out.println("[/delblock]   : 입력 대상 블랙리스트 추가 해제");
						out.println("[/adlogout]	: 관리자 아이디 로그아웃");
						out.println("======================================================");
					}
				}
				else if (s.startsWith("/to"))
				{
					StringTokenizer cks = new StringTokenizer(s);
					cks.nextToken(); // 미사용
					user = cks.nextToken().intern();
					s = s.substring(5 + user.length());					
					whisperMsg(name, user, s, out);
				}
				else if (s.startsWith("/ban"))
				{
					out.println("대상 퇴장 명령을 실행합니다.");
					ban(name, user, s, out);					
				} 
				else if (s.startsWith("/room")) 
				{
					out.println("채팅방 생성 명령을 실행합니다.");
					createChatRoom(name, out, in);					
				}				
				else if (s.startsWith("/rinv"))				
				{
					out.println("대상을 초대합니다.");
					inviteRoom(name, out, in);				
				}
				else if (s.startsWith("/rban"))
				{
					out.println("대상의 채팅방 퇴장 명령을 실행합니다.");
					banRoom(name, out, in);
				}
				else if (s.startsWith("/rblock"))
				{
					out.println("대상의 채팅방 영구 퇴장 명령을 실행합니다.");
					ban2Room(name, out, in);
				}
				else if (s.startsWith("/list"))				
				{
					out.println("모든 채팅 사용자를 보여줍니다.");
					list(out);				
				} 
				else if (s.startsWith("/rout"))				
				{
					out.println("채팅방 퇴장 명령을 실행합니다.");
					outRoom(name, out);	
				}
				else if (s.startsWith("/addblock"))				 
				{
					out.println("채팅 블랙리스트 추가 명령을 실행합니다.");
					inBlackList(name, s, out, in);
				}
				else if (s.startsWith("/delblock"))
				{
					out.println("채팅 블랙리스트 제외 명령을 실행합니다.");
					outBlackList(name, s, out, in);
				}
				else if (s.startsWith("/adduser"))
				{
					out.println("지정 유저의 대화를 차단 합니다.");
					addBanUser(name, out, in);
				}
				else if (s.startsWith("/deluser"))
				{
					out.println("지정 유저의 대화를 차단을 해제 합니다.");
					delBanUser(name, out, in);
				}
				else if (s.startsWith("/rjoin"))
				{
					out.println("채팅방 입장 명령을 실행합니다.");
					inChatRoom(name, out, in);
				}
				else if (s.startsWith("/ruser"))
				{
					out.println("채팅방 내부 유저 리스트를 보여줍니다.");
					roomUserList(name, title, out);
				}
				else if (s.startsWith("/rboom"))
				{
					sendRoomMsg(name, title, "펑~ 방을 폭파합니다!");
					out.println("채팅방 폭파 명령을 실행합니다.");
					boomRoom(name, out);
				}
				else if (s.startsWith("/rmchange"))
				{
					out.println("방장의 권한을 위임 합니다.");
					ChangeRM(name, out, in);
				}
				else if (s.startsWith("/rlist"))
				{
					out.println("현재 존재하는 채팅방 리스트를 출력합니다.");
					roomlist(out);	
				}				
				// 대기실 사용자 리스트
				else if (s.startsWith("/waituser"))
				{
					out.println("대기실 유저 리스트를 보여줍니다.");
					waitUserList(out);
				}
				///// Client 에서 직접 받는 명령어 모음
				else if (s.startsWith("/invagree")) // /agree 유저에게 보여지지 않는 명령어임 
				{
					out.println("초대방에 입장합니다.");
					invAgree(s, name, out);
				}
				else if (s.startsWith("/adword")) // /agree 유저에게 보여지지 않는 명령어임 
				{
					out.println("개인 금칙어를 추가합니다.");
					addUserBWord(name, out, in);
				}
				else if (s.startsWith("/delword")) // /agree 유저에게 보여지지 않는 명령어임 
				{
					out.println("개인 금칙어를 삭제합니다.");
					delUserBWord(name, out, in);
				}
				else if (s.startsWith("/adlogin")) 
				{
					out.println("관리자 로그인을 시작합니다.");
					adminLogin(name, out, in);
				}
				else if (s.startsWith("/adlogout")) // /agree 유저에게 보여지지 않는 명령어임 
				{
					out.println("관리자 로그아웃을 시작합니다.");
					adminLogout(name, out, in);
				}
				else if (s.startsWith("/noti")) {
					out.println("관리자 공지작성 및 발송을 시작합니다.");
					sendManagerMsg(name, out, in);
				}
				
				else if (s.startsWith("/withdrawal")||s.startsWith("/탈퇴")) {
					out.println("회원탈퇴를 진행하시겠습니까? (Y/N)");
					withdrawal(name, out, in);
				}
				else
				out.println("'/help'입력 후 명령어를 확인하세요!");
				
			} catch (Exception e) {
				//e.printStackTrace();// 명령어 예외
			} 				
		}
	}
}