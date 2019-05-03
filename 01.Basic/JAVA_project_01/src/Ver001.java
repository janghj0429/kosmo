/** JAVA_project_01 // Ver001 : 로그인 부분 마감 // MultiClient와 연동 **/
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

interface INIT_MENU {
	int lOGIN = 1, INPUT = 2, DELETE = 3, EXIT = 4;
}

interface INIT_NUM {
	int ONE = 1, TWO = 2, THREE = 3, FOUR = 4;
}

interface INPUT_SELECT {
	int NORMAL = 1, SEARCH = 2, OUT = 3;
}

//범위 // 14.예외처리/프로그래머 정의 예외 클래스
// interface 번호 범위 이외의 선택 오류 검색
class MenuChoiceException extends Exception {
	int wrongChoice;
	public MenuChoiceException(int choice) {
		super("\n잘못된 선택이 이뤄졌습니다.\n");
		wrongChoice = choice;
	}
	public void showWrongChoice() {
		System.out.println("\n\n\n\n\n ※ "+ wrongChoice + 
				  "에 해당하는 선택은 존재하지 않습니다.");
	}
}

class ChatInfo implements Serializable { // 가입데이터
	
	String id;
	String pw;
	String hint;
	String list;

	public ChatInfo(String id, String pw, String hint, String list) {
		
		this.id = id;
		this.pw = pw;
		this.hint = hint;
		this.list = list;
		
	}

	public void showChatInfo() {
		System.out.println("ID : " + id);
		System.out.println("PW : " + pw);
		System.out.println("HINT : " + hint);
		System.out.println("list : " + list);
	}

	public int hashCode() {
		return id.hashCode();
	}

	public int hashCode1() {
		return hint.hashCode();
	}

	public boolean equals(Object obj) {
		
		ChatInfo cmp = (ChatInfo) obj;
		if (id.compareTo(cmp.id) == 0)
			return true;
		else
			return false;
	}
}

// 오라클 주소
class oracle {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
}

//--------------------------------------------------------------------------------------------------------------------//
//====================================================================================================================//
//===▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼===// ▼ class ChatBManager ▼ //====▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼===//
//====================================================================================================================//

class ChatBManager {
	HashSet<ChatInfo> infoStorage = new HashSet<ChatInfo>();

	static ChatBManager inst = null;
	public static ChatBManager createManagerInst() {
		if (inst == null)
			inst = new ChatBManager();

		return inst;
	}
	
	// #초기화면 //==================================================================================//
	public static void input() throws MenuChoiceException {
		MenuViewer.showMenu();
		int choice = MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
		
		try {
			if (choice < INIT_MENU.lOGIN || choice > INIT_MENU.EXIT)
				throw new MenuChoiceException(choice);

			switch (choice) {
			case INIT_MENU.lOGIN:
				loginList();
				break;
			case INIT_MENU.INPUT:
				inputData();
				input();
				break;
			case INIT_MENU.DELETE:
				deleteData();
				input();
				break;
			case INIT_MENU.EXIT:
				System.out.println("\n------------------------------------------");
				System.out.println(" >> 프로그램을 종료합니다.");
				System.out.println("------------------------------------------\n");
				System.exit(0);
			}
		} catch (MenuChoiceException e) {
			e.showWrongChoice();
			System.out.println("\n------------------------------------------");
			System.out.println(" >> 처음부터 다시 진행합니다. ");
			System.out.println("------------------------------------------\n");
		} catch (InputMismatchException e) {
			
			// try 내부의 코드가 InputMismatchException 을 던진다면, 예외를 받아온다.
			// catch가 실행되는 동안은 Program 이 종료되지 않는다.
			MenuViewer.keyboard = new Scanner(System.in); // 중복 멈추기
			System.out.println("\n------------------------------------------");
			System.out.println(" ※ 번호를 입력해주시기 바랍니다.");
			System.out.println(" >> 처음부터 다시 진행합니다.");
			System.out.println("------------------------------------------\n");
		}
	}
	
	// #회원가입 //==================================================================================//
	static ChatInfo readFriendInfo() { // 회원가입 데이터 오라클에 저장**************
		System.out.println();
		
		Connection con = null; //연결
		PreparedStatement pstmt = null; //명령
		ResultSet rs = null; //결과

		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");

			System.out.print("아이디 : ");
			String id = MenuViewer.keyboard.nextLine();
			System.out.print("비밀번호 : ");
			String pw = MenuViewer.keyboard.nextLine();
			System.out.print("비밀번호 힌트 : ");
			String hint = MenuViewer.keyboard.nextLine();

			String sql = "SELECT id, hint FROM Chat  " + 
					"where id = ?               ";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(id.compareTo(rs.getString(1))==0) {
					
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 회원가입에 실패하였습니다.");
					System.out.println(" >> 존재하는 ID입니다.");
					System.out.println("------------------------------------------\n");
					inputData();
				} 
				}else {

					sql = "insert into Chat values(?, ?, ?, 'MEMBER')";
					pstmt = con.prepareStatement(sql);

					pstmt.setString(1, id);
					pstmt.setString(2, pw);
					pstmt.setString(3, hint);
					int update = pstmt.executeUpdate(); //실행->저장

					System.out.println("\n------------------------------------------");
					System.out.println(" >> 회원가입이 완료되었습니다.");
					System.out.println("------------------------------------------\n");
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (MenuChoiceException e) {
			e.printStackTrace();
		}
		return null;
	}


	// #회원가입창 //==================================================================================//
	public static void inputData() throws MenuChoiceException {
		System.out.println();
		
		System.out.println("1. 회원가입");
		System.out.println("2. 비밀번호 찾기");
		System.out.println("3. 메뉴창으로 나가기");
		System.out.print("선택 >> ");
		int choice = MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
		
		if (choice < INPUT_SELECT.NORMAL || choice > 3)
			throw new MenuChoiceException(choice);

		switch (choice) {
		case INPUT_SELECT.NORMAL:
			readFriendInfo();
			input();
			break;

		case INPUT_SELECT.SEARCH:
			searchData();
			input();
			break;
			
		case INPUT_SELECT.OUT:
			input();
			break;
		}
	}
	

	// #로그인 목록 (일반회원) //==================================================================================//
	public static void inputminiData1() throws MenuChoiceException {
		System.out.println();
		
		System.out.println("1. 로그인");
		System.out.println("2. 회원가입");
		System.out.println("3. 메뉴창으로 나가기");
		System.out.print("선택 >> ");
		int choice = MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
		
		if (choice < INIT_NUM.ONE || choice > INIT_NUM.THREE )
			throw new MenuChoiceException(choice);

		switch (choice) {
		case INIT_NUM.ONE:
			loginData(); //일반 회원 로그인
			return;
		case INIT_NUM.TWO:
			inputData();
			input();
			break;
		case INIT_NUM.THREE:
			input();
			break;
		}
	}
	
	//=========================================================================================================//
	//=======================================// ▼ 관리자 부분 ▼ //=============================================//
	//=========================================================================================================//
			
	// #관리자 전체 목록 //---------------------------------------------------------------------------------//
		public static void managerList() throws MenuChoiceException {
			System.out.println();
			
			System.out.println("1. 채팅방 입장");
			System.out.println("2. 회원 조회");
			System.out.println("3. 블랙리스트");
			System.out.println("4. 관리자 로그아웃");
			System.out.print("선택 >> ");
			int choice = MenuViewer.keyboard.nextInt();
			MenuViewer.keyboard.nextLine();
			
			if (choice < INIT_NUM.ONE || choice > INIT_NUM.FOUR )
				throw new MenuChoiceException(choice);
			
			switch (choice) {
			case INIT_NUM.ONE:
				managerLogin();
				return;
			case INIT_NUM.TWO:
				lookList();
				managerList();
				break;
			case INIT_NUM.THREE:
				blackList();
				managerList();
				break;
			case INIT_NUM.FOUR:
				input();
				break;
			}
		}
		
		// #로그인 (회원 로그인) //------------------------------------------------------------------------------//
		public static void managerLogin() {
			String id = "111";
			String pw = "111";
			String list = "BLACK";
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe", 
						"scott", 
						"tiger");
				
				String sql = "SELECT * FROM Chat       " + 
				       	     "where id = '" + id +"'   ";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					if(id.compareTo(rs.getString(1))==0 && pw.compareTo(rs.getString(2))==0 
							&& list.compareTo(rs.getString(4))!=0) {
						System.out.println("\n------------------------------------------");
						System.out.println(" >> 로그인이 완료되었습니다.");
						System.out.println("------------------------------------------\n");
						///////////////////////////////////////////////////////////////
						///////////////////////////////////////////////////////////////
						////////////////// ▶ 서버 접속 장소 ◀ /////////////////////////
						///////////////////////////////////////////////////////////////
						///////////////////////////////////////////////////////////////
						return;
					} else { 
						System.out.println("\n------------------------------------------");
						System.out.println(" >> 로그인 과정에서 오류가 발생하였습니다.");
						System.out.println("------------------------------------------\n");
						inputData();
					}
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} catch (MenuChoiceException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (SQLException sqle) {
				}
			}
		}
		
		
		//=======================================// ▼ 회원 조회 부분 ▼ //==========================================//
		// #관리자 #회원 조회 목록 //---------------------------------------------------------------------------------//
		public static void lookList() throws MenuChoiceException {
			System.out.println();
			
			System.out.println("1. 전체 회원 조회");
			System.out.println("2. 개별 회원 조회");
			System.out.println("3. 메뉴창으로 나가기");
			System.out.print("선택 >> ");
			int choice = MenuViewer.keyboard.nextInt();
			MenuViewer.keyboard.nextLine();
			
			if (choice < INIT_NUM.ONE || choice > INIT_NUM.THREE )
				throw new MenuChoiceException(choice);
			
			switch (choice) {
			case INIT_NUM.ONE:
				allSearchMember();
				managerList();
				break;
			case INIT_NUM.TWO:
				SearchMember();
				managerList();
				break;
			case INIT_NUM.THREE:
				managerList();
				break;
			}
		}
		
		// #관리자 #전체 회원 조회 //---------------------------------------------------------------------------------//
		public static void allSearchMember() {
			System.out.println();

			Connection con = null; // 연결
			PreparedStatement pstmt = null; // 명령
			ResultSet rs = null; // 결과
			boolean ok =false; // 체크
			
			try {
				con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe", 
						"scott", 
						"tiger");

				String sql = "SELECT * FROM Chat order by list desc";
				
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				int num = 1;
				System.out.println("===============================================================");
				System.out.println(" >> 가입 회원 목록 ");
				System.out.println("---------------------------------------------------------------");
				while(rs.next()) {
					System.out.printf(" >> %2d. [ID] %-8s, ", num, rs.getString(1));
					System.out.printf("[PW] %-12s, ", rs.getString(2));
					System.out.printf("[MODE] %s \n", rs.getString(4));
					num++;
				}
				System.out.println("---------------------------------------------------------------");
				System.out.println("  MANAGER : 관리자 / MEMBER : 일반 회원 / BLACK : 블랙리스트   ");
				System.out.println("===============================================================");
				System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
				MenuViewer.keyboard.nextLine();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (SQLException sqle) {
				}
			}
		}
		
		// #관리자 #개별 회원 조회 //---------------------------------------------------------------------------------//
		public static void SearchMember() {
			System.out.println();
			
			System.out.println(" >> 조회 할 정보 아이디를 입력해주세요.");
			System.out.print("ID : ");
			String id = MenuViewer.keyboard.nextLine();

			Connection con = null; // 연결
			PreparedStatement pstmt = null; // 명령
			ResultSet rs = null; // 결과
			boolean ok =false; // 체크
			
			try {
				con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe", 
						"scott", 
						"tiger");
				

				String sql = "SELECT * FROM Chat  " + 
					       	 "where id = ?               ";
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					if(id.compareTo(rs.getString(1))==0) {
					System.out.println("==========================================");
					System.out.print(" >> 조회가 완료되었습니다. \n");
					System.out.println(" >> [ID] " + rs.getString(1) + ", ");
					System.out.println(" >> [PW] " + rs.getString(2) +", ");
					System.out.println(" >> [HINT] " + rs.getString(3) +", ");
					System.out.println(" >> [MODE] " + rs.getString(4));
					System.out.println("==========================================");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
					}
				} else {
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 해당하는 데이터가 존재하지 않습니다.");
					System.out.println("------------------------------------------");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
					if (con != null)
						con.close();
				} catch (SQLException sqle) {
				}
			}
		}
		//=======================================// ▲ 회원 조회 부분 ▲ //==========================================//
		
		//===================================// ▼ 블랙리스트 설정 부분 ▼ //========================================//
		// [##]블랙리스트 #아이디대조 //---------------------------------------------------------------------------//
		public static void blackList() throws MenuChoiceException {
			System.out.println();
			
			System.out.println("1. 블랙리스트 설정");
			System.out.println("2. 블랙리스트 해제");
			System.out.println("3. 메뉴창으로 나가기");
			System.out.print("선택 >> ");
			int choice = MenuViewer.keyboard.nextInt();
			MenuViewer.keyboard.nextLine();
			
			if (choice < INIT_NUM.ONE || choice > INIT_NUM.THREE )
				throw new MenuChoiceException(choice);
			
			switch (choice) {
			case INIT_NUM.ONE:
				blackYes();
				managerList();
				break;
			case INIT_NUM.TWO:
				blackNo();
				managerList();
				break;
			case INIT_NUM.THREE:
				managerList();
				break;
			}
		}
		// #블랙리스트 설정 //-------------------------------------------------------------------------------------//
		public static void blackYes() {
			System.out.println();
			
			System.out.println(" >> 블랙리스트 설정을 시작합니다.");
			System.out.print("블랙리스트 추가 아이디 : ");
			String id = MenuViewer.keyboard.nextLine();
			
			boolean ok =false;
			Connection con = null; //연결
			PreparedStatement ps = null; //명령
			ResultSet rs = null; //결과
			
			try {
				con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe",
						"scott",
						"tiger");
				
				String sql = "UPDATE Chat         " + 
						"SET LIST='BLACK'  " +
						"   WHERE ID=? ";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, id);
				
				int r = ps.executeUpdate(); //실행 성공 -> 1, 실패 -> 0
				if (r>0) {
					ok=true; //설정됨;
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 블랙리스트 설정이 완료되었습니다.");
					System.out.println("------------------------------------------");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
//					managerList();// 관리자용 메뉴창
					return;
				} else {
					System.out.println("\n------------------------------------------");
					System.out.println(" # 블랙리스트 설정에 실패하였습니다.");
					System.out.println(" >> ID 확인후 다시 입력 바랍니다.");
					System.out.println("------------------------------------------");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
//					managerList();// 관리자용 메뉴창
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
//			} catch (MenuChoiceException e) {
//				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (ps != null)
						ps.close();
					if (con != null)
						con.close();
				} catch (SQLException sqle) {
				}
			}
		}
		
		// #블랙리스트 해제 //-------------------------------------------------------------------------------------//
		public static void blackNo() {
			System.out.println();
			
			System.out.println(" >> 블랙리스트 해제를 시작합니다.");
			System.out.print("블랙리스트 해제 아이디 : ");
			String id = MenuViewer.keyboard.nextLine();
			
			boolean ok =false;
			Connection con = null; //연결
			PreparedStatement ps = null; //명령
			ResultSet rs = null; //결과
			
			try {
				con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe",
						"scott",
						"tiger");
				
				String sql = "UPDATE Chat         " + 
						"SET LIST='MEMBER'  " +
						"   WHERE ID=? ";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, id);
				
				int r = ps.executeUpdate(); //실행 성공 -> 1, 실패 -> 0
				if (r>0) {
					ok=true; //설정됨;
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 블랙리스트 해제가 완료되었습니다.");
					System.out.println("------------------------------------------");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
					managerList();// 관리자용 메뉴창
					return;
				} else {
					System.out.println("\n------------------------------------------");
					System.out.println(" # 블랙리스트 해제에 실패하였습니다.");
					System.out.println(" >> ID 확인후 다시 입력 바랍니다.");
					System.out.println("------------------------------------------");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
					managerList();// 관리자용 메뉴창
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} catch (MenuChoiceException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (ps != null)
						ps.close();
					if (con != null)
						con.close();
				} catch (SQLException sqle) {
				}
			}
		}
	//===================================// ▲ 블랙리스트 설정 부분 ▲ //========================================//
		
	//=========================================================================================================//
	//=======================================// ▲ 관리자 부분 ▲ //=============================================//
	//=========================================================================================================//
	

	
	//=========================================================================================================//
	//=====================================// ▼ 로그인 설정 부분 ▼ //==========================================//
	//=========================================================================================================//

	// #로그인 목록 // ----------------------------------------------------------------------------------------//
	public static void loginList() throws MenuChoiceException {
		System.out.println();
		
		System.out.println("1. 회원 로그인");
		System.out.println("2. 관리자 로그인");
		System.out.println("3. 메뉴창으로 나가기");
		System.out.print("선택 >> ");
		int choice = MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
		
		if (choice < INIT_NUM.ONE || choice > INIT_NUM.THREE )
			throw new MenuChoiceException(choice);

		switch (choice) {
		case INIT_NUM.ONE:
			loginData();
			return;
		case INIT_NUM.TWO:
			loginManager();
			return;
		case INIT_NUM.THREE:
			input();
			break;
		}
	}
	
	// #로그인 (회원 로그인) //------------------------------------------------------------------------------//
	public static void loginData() {
		System.out.println();
		
		System.out.println(" >> 로그인 아이디와 비밀번호를 입력해주세요.");
		System.out.print("ID : ");
		String id = MenuViewer.keyboard.nextLine();
		System.out.print("PW : ");
		String pw = MenuViewer.keyboard.nextLine();
		String list = "BLACK";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			
			String sql = "SELECT * FROM Chat       " + 
			       	     "where id = '" + id +"'   ";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(id.compareTo(rs.getString(1))==0 && pw.compareTo(rs.getString(2))==0 
						                            && list.compareTo(rs.getString(4))!=0) {
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 로그인이 완료되었습니다.");
					System.out.println("------------------------------------------\n");
					///////////////////////////////////////////////////////////////
					///////////////////////////////////////////////////////////////
					////////////////// ▶ 서버 접속 장소 ◀ /////////////////////////
					///////////////////////////////////////////////////////////////
					///////////////////////////////////////////////////////////////
					return;
				} else if(id.compareTo(rs.getString(1))==0 && pw.compareTo(rs.getString(2))==0
						                                   && list.compareTo(rs.getString(4))==0) { 
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 로그인이 거절되었습니다.");
					System.out.println(" >> 당신은 블랙리스트 회원입니다.");
					System.out.println(" >> 프로그램을 종료합니다.");
					System.out.println("------------------------------------------\n");
					System.exit(0);
				} else { 
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 비밀번호가 틀렸습니다.");
					System.out.println("------------------------------------------\n");
					inputminiData3();
					return;
			} 
			} else { 
				System.out.println("\n------------------------------------------");
				System.out.println(" >> 가입되지 않은 아이디 입니다.");
				System.out.println("------------------------------------------\n");
				inputData();
				return;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (MenuChoiceException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle) {
			}
		}
	}
	
	// #로그인 (관리자 로그인) //-------------------------------------------------------------------------//
	public static void loginManager() {
		System.out.println();
		
		System.out.println(" >> 관리자 아이디와 비밀번호를 입력해주세요.");
		System.out.print("ID : ");
		String id = MenuViewer.keyboard.nextLine();
		System.out.print("PW : ");
		String pw = MenuViewer.keyboard.nextLine();
		String list = "MANAGER";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			
			String sql = "SELECT * FROM Chat       " + 
					"where id = '" + id +"'   ";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(id.compareTo(rs.getString(1))==0 && pw.compareTo(rs.getString(2))==0 && list.compareTo(rs.getString(4))==0) {
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 관리자 로그인이 완료되었습니다.");
					System.out.println("------------------------------------------\n");
					managerList();
					return;
				} else if(id.compareTo(rs.getString(1))==0 && pw.compareTo(rs.getString(2))==0 && list.compareTo(rs.getString(4))!=0) { 
					System.out.println("\n------------------------------------------");
					System.out.println(" ＃ 관리자가 아닙니다.");
					System.out.println(" >> 일반 회원으로 로그인 바랍니다.");
					System.out.println("------------------------------------------\n");
					inputminiData1();
				} else { 
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 비밀번호가 틀렸습니다.");
					System.out.println("------------------------------------------\n");
					inputminiData0();
				} 
			} else { 
				System.out.println("\n------------------------------------------");
				System.out.println(" >> 가입되지 않은 아이디 입니다.");
				System.out.println("------------------------------------------\n");
				inputData();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (MenuChoiceException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle) {
			}
		}
	}
	
	// #로그인 (관리자 재로그인) //--------------------------------------------------------------------------------//
	public static void inputminiData0() throws MenuChoiceException {
		System.out.println();
		
		System.out.println("1. 관리자 로그인");
		System.out.println("2. 메뉴창으로 나가기");
		System.out.print("선택 >> ");
		int choice = MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
		
		if (choice < INIT_NUM.ONE || choice > INIT_NUM.TWO )
			throw new MenuChoiceException(choice);
		
		switch (choice) {
		case INIT_NUM.ONE:
			loginManager();
			return;
		case INIT_NUM.TWO:
			input();
			break;
		}
	}
	//=========================================================================================================//
	//=====================================// ▲ 로그인 설정 부분 ▲ //==========================================//
	//=========================================================================================================//
	
	//=========================================================================================================//
	//====================================// ▼ 비밀번호 찾기 부분 ▼ //=========================================//
	//=========================================================================================================//
	
	// #아이디 오류 비번찾기 //--------------------------------------------------------------------------------//
	public static void inputminiData2() throws MenuChoiceException {
		System.out.println();
		
		System.out.println("1. ID 재입력");
		System.out.println("2. 회원가입");
		System.out.println("3. 메뉴창으로 나가기");
		System.out.print("선택 >> ");
		int choice = MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
		
		if (choice < INIT_NUM.ONE || choice > INIT_NUM.THREE )
			throw new MenuChoiceException(choice);
		
		switch (choice) {
		case INIT_NUM.ONE:
			searchData();
			break;
		case INIT_NUM.TWO:
			inputData();
			break;
		case INIT_NUM.THREE:
			input();
			break;
		}
	}
	
	// #회원가입 #비번찾기 //--------------------------------------------------------------------------------//
	public static void inputminiData3() throws MenuChoiceException {
		System.out.println();
		
		System.out.println("1. ID 재입력");
		System.out.println("2. 비밀번호 찾기");
		System.out.println("3. 메뉴창으로 나가기");
		System.out.print("선택 >> ");
		int choice = MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
		
		if (choice < INIT_NUM.ONE || choice > INIT_NUM.THREE )
			throw new MenuChoiceException(choice);
		
		switch (choice) {
		case INIT_NUM.ONE:
			searchData();
			break;
		case INIT_NUM.TWO:
			searchData();
			break;
		case INIT_NUM.THREE:
			input();
			break;
		}
	}
	
	// #비번찾기 본데이터 //--------------------------------------------------------------------------------//
	public static void searchData() {
		System.out.println();
		
		System.out.println("아이디를 입력해주세요.");
		System.out.print("ID : ");
		String id = MenuViewer.keyboard.nextLine();

		Connection con = null; // 연결
		PreparedStatement pstmt = null; // 명령
		ResultSet rs = null; // 결과
		boolean ok =false; // 체크
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"scott", 
					"tiger");
			

			String sql = "SELECT id, hint FROM Chat  " + 
				       	 "where id = ?               ";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(id.compareTo(rs.getString(1))==0) {
					System.out.println();
					System.out.println("==========================================");
					System.out.print(" > ID가 확인되었습니다. \n");
					System.out.println(" >> 'ID : " + rs.getString(1) +"'에 대한 비밀번호 힌트는 ");
					System.out.println(" >> '" + rs.getString(2) +"'입니다.");
					System.out.println("==========================================");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
				}
			} else {
				System.out.println("\n------------------------------------------");
				System.out.println(" >> 해당하는 데이터가 존재하지 않습니다.");
				System.out.println("------------------------------------------\n");
				inputminiData2();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (MenuChoiceException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle) {
			}
		}
	}
	//=========================================================================================================//
	//====================================// ▲ 비밀번호 찾기 부분 ▲ //=========================================//
	//=========================================================================================================//

	//======================================// ▼ 회원 탈퇴 부분 ▼ //===========================================//
	// #회원탈퇴 //--------------------------------------------------------------------------------------------//
	public static void deleteData() {
		System.out.println();
		
		System.out.println(" >> 삭제할 아이디와 비밀번호를 입력해주세요.");

		System.out.print("ID : ");
		String id = MenuViewer.keyboard.nextLine();

		System.out.print("PW : ");
		String pw = MenuViewer.keyboard.nextLine();
		String list = "MANAGER";

		boolean ok =false ;
		Connection con =null;
		PreparedStatement ps =null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");

			String sql = "delete from Chat " +
					     "where id=? " +
					     "  and pw=? " +
			             "  and list!=? ";

			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, pw);
			ps.setString(3, list);
			
			int r = ps.executeUpdate(); //실행 성공 -> 1, 실패 -> 0
			
			sql = "SELECT * FROM Chat " +
			      "where list=? ";
			
			ps = con.prepareStatement(sql);
			ps.setString(1, list);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				if (r>0) {
					ok=true; //삭제됨;
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 데이터 삭제가 완료되었습니다.");
					System.out.println("------------------------------------------");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
					return;
				} else if (id.compareTo(rs.getString(1))==0 && list.compareTo(rs.getString(4))==0) {
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 관리자 데이터는 삭제할 수 없습니다.");
					System.out.println("------------------------------------------");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
				} else {
					System.out.println("\n------------------------------------------");
					System.out.println(" >> 아이디 혹은 비밀번호가 틀렸습니다.");
					System.out.println(" >> 확인 후 다시 입력 바랍니다.");
					System.out.println("------------------------------------------");
					System.out.println(" >> 엔터를 누르면 메뉴창으로 돌아갑니다.");
					MenuViewer.keyboard.nextLine();
				}
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	//======================================// ▲ 회원 탈퇴 부분 ▲ //===========================================//
}
//====================================================================================================================//
//===▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲===// ▲ class ChatBManager ▲ //====▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲===//
//====================================================================================================================//
//--------------------------------------------------------------------------------------------------------------------//


class MenuViewer {
	public static Scanner keyboard = new Scanner(System.in);

	public static void showMenu() {
		System.out.println();
		System.out.println("==========================================");
		System.out.println(" >> 선택하세요.");
		System.out.println(" 1. 로그인");
		System.out.println(" 2. 회원가입");
		System.out.println(" 3. 회원탈퇴");
		System.out.println(" 4. 프로그램 종료");
		System.out.println("==========================================");
		System.out.print("선택 >> ");
	}
}

//// [@@]연동 됬기에 추후 확인 후 삭제
//class Ver001 {
//
//	public static void main(String[] args) {
//
//		ChatBManager manager = ChatBManager.createManagerInst();
//		int choice;
//
//		while (true) {
//			try {
//				
//				MenuViewer.showMenu();
//				choice = MenuViewer.keyboard.nextInt();
//				MenuViewer.keyboard.nextLine();
//
//				if (choice < INIT_MENU.lOGIN || choice > INIT_MENU.EXIT)
//					throw new MenuChoiceException(choice);
//
//				switch (choice) {
//				case INIT_MENU.lOGIN:
//					manager.loginData();
////  채팅창 시작 ↓↓↓↓↓↓↓↓↓--------------------------------------------------------------------------------------------------
////	채팅창 시작 ↑↑↑↑↑↑↑↑--------------------------------------------------------------------------------------------------
//					return;
//				case INIT_MENU.INPUT:
//					manager.inputData();
//					break;
//				case INIT_MENU.DELETE:
//					manager.deleteData();
//					break;
//				case INIT_MENU.EXIT:
//					System.out.println("프로그램을 종료합니다.");
//					return;
//				}
//			} catch (MenuChoiceException e) {
//				e.showWrongChoice();
//				System.out.println("처음부터 다시 진행합니다. \n");
//			} catch (InputMismatchException e) {
//
//				// try 내부의 코드가 InputMismatchException 을 던진다면, 예외를 받아온다.
//				// catch가 실행되는 동안은 Program 이 종료되지 않는다.
//				MenuViewer.keyboard = new Scanner(System.in); // 중복 멈추기
//				System.out.println("\n※ 번호를 입력해주시기 바랍니다.");
//				System.out.println(" >> 처음부터 다시 진행합니다. \n");
//			}
//		}
//	}
//}