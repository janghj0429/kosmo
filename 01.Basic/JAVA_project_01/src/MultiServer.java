/** Jdbc11 #프로젝트 시안4 // 서버 선택 // 금칙어 대기방 설정 완료 ## 작업 진행중**/ 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*콘솔 멀티채팅 서버 프로그램*/
public class MultiServer {
	static HashMap<String, HashMap<String, MultiServerT>> roomMap; // 방별 해쉬맵을 관리하는 해시맵
	ServerSocket serverSocket = null;
	Socket socket = null;
	static int connUserCount = 0; // 서버에 접속된 유저 카운트
//===================================================================================================//
	// #방 해쉬맵을 관리하는 생성자 -------------------------------------------------------------------------
	public MultiServer() { //$$
		roomMap = new HashMap<String, HashMap<String, MultiServerT>>();
		// 클라이언트의 출력스트림을 저장할 해쉬맵 생성.

		Collections.synchronizedMap(roomMap); // 해쉬맵 동기화 설정.

		HashMap<String, MultiServerT> group01 = new HashMap<String, MultiServerT>();
		Collections.synchronizedMap(group01); // 해쉬맵 동기화 설정.
		HashMap<String, MultiServerT> group02 = new HashMap<String, MultiServerT>();
		Collections.synchronizedMap(group02); // 해쉬맵 동기화 설정.
		HashMap<String, MultiServerT> group03 = new HashMap<String, MultiServerT>();
		Collections.synchronizedMap(group03); // 해쉬맵 동기화 설정.
		HashMap<String, MultiServerT> group04 = new HashMap<String, MultiServerT>();
		Collections.synchronizedMap(group04); // 해쉬맵 동기화 설정.

		roomMap.put("하나", group01);
		roomMap.put("두이", group02);
		roomMap.put("서이", group03);
		roomMap.put("너이", group04);
	} // 생성자 // --------------------------------------------------------------------------------------------------------
	// 서버 시작 -------------------------------------------------------------------------------------------------------
	public void init() {
		try {
			serverSocket = new ServerSocket(9999); // 9999포트로 서버소켓 객체생성.
			System.out.println("<< 서버가 시작되었습니다. >>");
			while (true) {
				socket = serverSocket.accept(); // 클라이언트의 접속을 기다리다가 접속이 되면 Socket객체를 생성.
				System.out.println(socket.getInetAddress() + ":" + socket.getPort()); // 클라이언트 정보 (ip, 포트) 출력

				Thread mst = new MultiServerT(socket); // 쓰레드 생성
				mst.start(); // 쓰레드 시동.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // 서버 시작 // -------------------------------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------------------
	/** 공개 메시지와 공지사항 **/ // 모든 클라이언트들에게 메시지 전달 //
	public void sendAllMsg(String msg) {
		Iterator roomMap_it = roomMap.keySet().iterator();
		while (roomMap_it.hasNext()) {
			try {
				HashMap<String, MultiServerT> it_hash = roomMap.get(roomMap_it.next());
				Iterator it = it_hash.keySet().iterator();
				while (it.hasNext()) {
					MultiServerT st = it_hash.get(it.next());
					st.out.writeUTF(msg);
				}
			} catch (Exception e) {
				System.out.println(">> 예외M_sendAllMsg: \n" + e);
			}
		}
	}// sendAllMsg() //-----------------------------------------------------------------------------------------
	// 클라이언트(자신)이 있는 방에서만 메시지 전달 --------------------------------------------------------
	public void sendGroupMsg(String loc, String msg) {

		HashMap<String, MultiServerT> rMap = roomMap.get(loc);
		Iterator<String> group_it = roomMap.get(loc).keySet().iterator();
		while (group_it.hasNext()) {
			try {
				MultiServerT st = rMap.get(group_it.next());
				st.out.writeUTF(msg);
			} catch (Exception e) { //[##]예외오류부근
//				System.out.println(">> 예외M_sendGroupMsg: \n" + e);
			}
		}
	} // sendGroupMsg ------------------------------------------------------------------------
	
	/** 귓속말 **/
	public void sendToMsg(String loc, String fromName, String toName, String msg) {

		try {
			roomMap.get(loc).get(toName).out.writeUTF("whisper|" + fromName + "|" + msg);
			roomMap.get(loc).get(fromName).out.writeUTF("hisper|" + fromName + "|" + msg);
			System.out.println("["+loc+"]에서 '" + fromName +"' >> '" + toName + "' = " + msg);
		} catch (Exception e) {
			System.out.println(">> 예외M_sendToMsg: \n" + e);
		}
	} // sendToMsg --------------------------------------------------------------------------------------

// |---------------------------------------------------------------------------------	
    public String getEachMapSize() {
    	return getEachMapSize(null);
    } // getEachMapSize()-----------
// |---------------------------------------------------------------------------------	
    /** 서버에 접속된 유저 수와 방 목록 반환 **/
	public String getEachMapSize(String loc) {
		Iterator roomMap_it = roomMap.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		int sum = 0;
		sb.append("================ 방 목록 ================" + System.getProperty("line.separator"));
		while (roomMap_it.hasNext()) {
			try {
				String key = (String) roomMap_it.next();

				HashMap<String, MultiServerT> it_hash = roomMap.get(key);
				int size = it_hash.size();
				sum += size;
				sb.append("  >>  "+ key + " : (" + size + "명)" + (key.equals(loc) ? "(*)" : "") + "\n");

			} catch (Exception e) {
				System.out.println(">> 예외M_getEachMapSize: \n" + e);
			}
		}
		sb.append("-----------------------------------------\n");
		sb.append(" ※ 대화에 참여 중인 유저 [ " + sum + "명 ]\n");
		sb.append("=========================================\n");
		return sb.toString();
	}// getEachMapSize() //
	// |---------------------------------------------------------------------------------	
	
	/** 방에 접속된 유저리스트 반환 **/ //[##] 내부클라스 고민중
	//출력스트림을 순차적으로 얻어와서 해당 메시지를 출력한다.
	public String showUserList(String loc, String name) {
		StringBuilder output = new StringBuilder();
		output.append("======= 채팅방 접속자 목록 =======\n");
		output.append("----------------------------------\n");
		Iterator it = roomMap.get(loc).keySet().iterator(); // 해쉬맵에 등록된 사용자이름을 가져옴.
		while (it.hasNext()) { // 반복하면서 사용자이름을 StringBuilder에 추가
			try {
				String key = (String) it.next();
				// out.writeUTF(output);
				if (key.equals(name)) { // 현재사용자 체크
					key = key + " (*) "; // key += " (*) ";
				}
				output.append("  >>  " + key + "\n");
			} catch (Exception e) {
				System.out.println(">> 예외M_showUserList: \n" + e);
			}
		} // while //
		int num = roomMap.get(loc).size();
		output.append("----------------------------------\n");
		output.append(" ※ "+loc+"에 접속 중인 유저 [ "+num+"명 ]\n");
		output.append("==================================\n");
		System.out.println(output.toString());
		return output.toString();
	} // showUserList()//
// |---------------------------------------------------------------------------------	
	
	
// |---------------------------------------------------------------------------------	
	/** 접속된 유저 중복체크 */
	public boolean isNameRoom(String name) {
		boolean result = false;
		Iterator<String> roomMap_it = roomMap.keySet().iterator();
		while (roomMap_it.hasNext()) {
			try {
				String key = roomMap_it.next();
				HashMap<String, MultiServerT> it_hash = roomMap.get(key);
				if (it_hash.containsKey(name)) {
					result = true; // 중복된 아이디가 존재.
					break;
				}

			} catch (Exception e) {
				System.out.println(">> 예외M_isNameRoom(): \n" + e);
			}
		}
		return result;
	} // isNameRoom //
// |---------------------------------------------------------------------------------	

	/** [##] 문자열 null 값 및 "" 은 대체 문자열로 삽입 **/
	public String nVL(String str, String replace) {
		String output = "";
		if (str == null || str.trim().equals("")) {
			output = replace;
		} else { output = str; }
		return output;
	}
// |---------------------------------------------------------------------------------	
	/** 금칙어 단어 **/
	public static String filterText(String msg) {
		Pattern p = Pattern.compile("바보|멍청|병신|미친|fuck|fucking|shit", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(msg);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, maskWord(m.group()));
		}
		m.appendTail(sb);
		return sb.toString();
	}
	/** 금칙어 단어 치환 **/
	public static String maskWord(String word) {
		StringBuffer buff = new StringBuffer();
		char[] ch = word.toCharArray();
		buff.append("[금칙어] ");
		for (int i = 0; i < ch.length; i++) {
			buff.append("*");
		}
		return buff.toString();
	}
// |---------------------------------------------------------------------------------	
	
	
	/**=======================================================// main 메서드 //=============**/
	public static void main(String[] args) {
		MultiServer ms = new MultiServer();
		ms.init(); 
	}
	/**=======================================================// 내부 클래스 //=============**/

	// 클라이언트로부터 읽어온 메시지를 다른 클라이언트(socket)에 보내는 메서드
	class MultiServerT extends Thread {

		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		String name = ""; // 이름 저장
		String loc = ""; // 방이름 저장
		String toNameTmp = null;//1:1대화 상대 // 귓속말 고정 상대
		boolean chatMode; //1:1대화모드 여부   // 귓속말 고정 여부

		// 생성자.
		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				// Socket으로부터 입력스트림을 얻는다.
				in = new DataInputStream(socket.getInputStream());
				// Socket으로부터 출력스트림을 얻는다.
				out = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				System.out.println("-----------------------------------------");
				System.out.println(" >> ServerRecThread 생성자 예외:" + e);
				System.out.println("-----------------------------------------");
			}
		}// 생성자 ------------

		/** 메시지 파서 **/
		public String[] getMsgParse(String msg) {
			System.out.println("   " + msg);
			String[] tmpArr = msg.split("[|]");
			return tmpArr;
		}
		
		
		
//[##run]//
//=============================================================================================//
		@Override
		public void run() { // 쓰레드를 사용하기 위해서 run()메서드 재정의
			
			Scanner s = new Scanner(System.in);
			System.out.print(" >> 대화명 :");
			
			HashMap<String, MultiServerT> clientMap = null; // 현재 클라이언트가 저장되어있는 해쉬맵

			try {
				while (in != null) { // 입력스트림이 null이 아니면 반복.
					String msg = in.readUTF(); // 입력스트림을 통해 읽어온 문자열을 msg에 할당.
					String[] msgArr = getMsgParse(msg.substring(msg.indexOf("|") + 1));
					
// %% [메세지 처리] || -------------------------------------------------------------------------------------
					if (msg.startsWith("req_logon")) { // 로그온 시도 // req_logon|대화명

						if (!(msgArr[0].trim().equals("")) && !isNameRoom(msgArr[0])) {
							name = msgArr[0]; // 대화명은 전역변수 name에 저장
							System.out.println(" >> '"+name+ "'님이 대기실에 입장하였습니다.");
							MultiServer.connUserCount++; // 접속자수 증가.
							System.out.println(" >> 현재 서버에 접속된 유저는 '"+MultiServer.connUserCount+ "'명 입니다.");

							out.writeUTF("logon#yes|" + getEachMapSize()); // 접속된 클라이언트에게 그룹목록 제공 #C_look
							out.writeUTF("show| >> '" +msgArr[0]+"'님 환영합니다. 대기실입니다."); // #C_look

						} else { out.writeUTF("logon#no|err01:로그인에 실패하였습니다."); 
						}
// %% [방 입장 시도] || -------------------------------------------------------------------------------------
					} else if (msg.startsWith("req_enterRoom")) { // 방 입장 시도 // req_enterRoom|대화명|방
						loc = msgArr[1]; // 메시지에서 방부분만 추출하여 전역변수에 저장

						if(isNameRoom(msgArr[0])){ // 로그인 실패
							out.writeUTF("logon#no|"+name);   

						} else if (roomMap.containsKey(loc)) { 
							sendGroupMsg(loc, "show|>> " + name + "님이 입장하셨습니다.");
							clientMap = roomMap.get(loc); // 현재그룹의 해쉬맵을 따로 저장.
							clientMap.put(name, this); // 현재 MultiServerRec인스턴스를 클라이언트맵에 저장.
							System.out.println(getEachMapSize()); // 서버에 그룹리스트 출력.
							out.writeUTF("enterRoom#yes|" + loc); // 접속된 클라이언트에게 그룹목록 제공

						} else {
							//(##)
							out.writeUTF("enterRoom#no|" + loc);
							//System.out.println("멀티서버창에 글자 적힌다.");
							//out.writeUTF("show| 창에 나옴"); // 내 창에 나온다.
							//msgArr[0] : 대화명 / msgArr[1] : 메시지
							out.writeUTF("show| ["+msgArr[0]+"] " + (msgArr[1])); // 자신에게만 보내짐. 대기실 대화 x.
//  [대기실 명령어]  || -------------------------------------------------------------------------------------
//   ▼ 명령어 목록   || -------------------------------------------------------------------------------------
							if (msgArr[1].trim().startsWith("/k") || msgArr[1].trim().startsWith("/K") ||
								msgArr[1].trim().startsWith("/key") || msgArr[1].trim().startsWith("/KEY") || 
								msgArr[1].trim().startsWith("/Key")) {
								out.writeUTF("show|=============================================");
								out.writeUTF("show|            대기실 명령어 목록               ");
								out.writeUTF("show|---------------------------------------------");
								out.writeUTF("show| >> 채팅방 참여   : [/GO]   [/go]   [/G]");
								out.writeUTF("show| >> 방 목록 보기  : [/ROOM] [/room] [/R]");
								out.writeUTF("show| >> 대기실 명령어 : [/KEY]  [/key]  [/K]");
								out.writeUTF("show|---------------------------------------------");
								out.writeUTF("show| ※ 채팅 프로그램 종료 : [/END] [/end] [/E]   ");
								out.writeUTF("show|=============================================");
//  [대기실 명령어]  || -------------------------------------------------------------------------------------
//     ▼ 방 입장     || -------------------------------------------------------------------------------------
							} else if (msgArr[1].trim().startsWith("/g") || msgArr[1].trim().startsWith("/G") || 
									   msgArr[1].trim().startsWith("/go") || msgArr[1].trim().startsWith("/GO") || 
									   msgArr[1].trim().startsWith("/Go")) {

									out.writeUTF("show|" + getEachMapSize(loc));
									out.writeUTF("show|>> 참여 할 방 이름을 입력해 주세요 : ");
							} 
						} 
					}
// %% [명령어] || ==============================================================================================================
					else if (msg.startsWith("req_cmdMsg")) { // 룸 명령어 전송
						// req_cmdMsg|대화명|/접속자
						
//     [명령어]      || -------------------------------------------------------------------------------------
//  ▼ 방 접속자 목록 || -------------------------------------------------------------------------------------
						if (msgArr[1].trim().startsWith("/L") || msgArr[1].trim().startsWith("/l") || 
							msgArr[1].trim().startsWith("/LIST") || msgArr[1].trim().startsWith("/list") || 
							msgArr[1].trim().startsWith("/List")) {
//							String[] msgSubArr = msg.split(" "); /***
							out.writeUTF("show|" + showUserList(loc, name)); // 접속자 출력
//     [명령어]      || -------------------------------------------------------------------------------------
//  ▼ 명령어 목록    || -------------------------------------------------------------------------------------
						} else if (msgArr[1].trim().startsWith("/C") || msgArr[1].trim().startsWith("/c") || 
								   msgArr[1].trim().startsWith("/CHAT") || msgArr[1].trim().startsWith("/chat") || 
								   msgArr[1].trim().startsWith("/Chat")) {
							out.writeUTF("show|=======================================================");
							out.writeUTF("show|                 채팅방 명령어 목록");
							out.writeUTF("show|-------------------------------------------------------");
							out.writeUTF("show| >> 참여자 목록   : [/LIST]  [/list]  [/L]");
							out.writeUTF("show| >> 공지사항 전달 : [/ALL]   [all]    [/A]");
							out.writeUTF("show| >> 서버 금칙어   : [/XXX]   [/xxx/]  [/X]");
							out.writeUTF("show| >> 대기실 이동   : [/OUT]   [/out]   [/O]");
							out.writeUTF("show| >> 채팅방 명령어 : [/CHAT]  [/chat]  [/C] ");
							out.writeUTF("show| >> 방 목록 보기  : [/ROOM]  [/room]  [/R]");
							out.writeUTF("show| >> 방 변경 하기  :  /room [변경할 방 이름]");
							out.writeUTF("show| >> 귓속말        :  /to [상대방이름] [보낼메시지]");
							out.writeUTF("show|-------------------------------------------------------");
							out.writeUTF("show| ※ 채팅 프로그램 종료 : [/END] [/end] [/E]   ");
							out.writeUTF("show|=======================================================");
//     [명령어]      || -------------------------------------------------------------------------------------
//▼ 방 → 대기실 → 방 || -------------------------------------------------------------------------------------
						} else if (msgArr[1].trim().startsWith("/O") || msgArr[1].trim().startsWith("/o") ||
								   msgArr[1].trim().startsWith("/OUT") || msgArr[1].trim().startsWith("/out") ||
								   msgArr[1].trim().startsWith("/Out")) {
							String[] msgSubArr = msg.split(" ");
							if (msgSubArr.length == 1) {

								clientMap.remove(name); // 현재 방 해쉬맵에서 해당 쓰레드를 제거. this=name=자신

								out.writeUTF("show|" + getEachMapSize(loc)); // 방 목록
								out.writeUTF("show|>> 참여 할 방이름을 입력해 주세요 : ");
								
								msg = in.readUTF();
								msgArr = getMsgParse(msg.substring(msg.indexOf("|") + 1));
								String tmpLoc = msgArr[1];
								//System.out.println("msgArr : *"+Arrays.toString(msgSubArr)); //[req_cmdMsg|2222|/out]
								//System.out.println("msg.substring : *" + msg.substring(0)); //req_say|2222|두리
								//System.out.println("msg : *" + msg); //req_say|2222|두리 // 명령어|자신|방이름
								
								if (roomMap.containsKey(tmpLoc)) { // 방 체크
									
									sendGroupMsg(loc, "show|>> " + name + "님이 퇴장하셨습니다.");
									System.out.println(">> "+name+"님이 "+ loc + " 방에서 퇴장하셨습니다.");
									
									out.writeUTF("show|-----------------------------------------");
									out.writeUTF("show| >> '" + tmpLoc + "' 방으로 이동합니다.");
									out.writeUTF("show|-----------------------------------------");

									loc = tmpLoc;
									clientMap = roomMap.get(loc); // 해쉬맵을 불러서 원하는 방(loc)으로 이동
									sendGroupMsg(loc, "show|>> " + name + "님이 입장하셨습니다.");
									clientMap.put(name, this); // 새로 변경 된 방에 서버쓰레드 저장.

								} else {
									out.writeUTF("show|----------------------------------------------");
									out.writeUTF("show| ※ 존재하지 않거나 이동 할 수 없는 방입니다.");
									out.writeUTF("show|----------------------------------------------");
									out.writeUTF("show|" + getEachMapSize(loc));
									out.writeUTF("show| >> 목록 확인 후 다시 입력바랍니다. : ");
								} // if-----
							} else {
								out.writeUTF("show|-----------------------------------------");
								out.writeUTF("show| ※ 명령어 사용법이 잘못 되었습니다.");
								out.writeUTF("show| >> 대기실 이동 : /out");
								out.writeUTF("show| >> 방 목록 보기 : /room");
								out.writeUTF("show| >> 방 변경 하기 : /room [변경할 방 이름]");
								out.writeUTF("show|-----------------------------------------");
							} // if---------
//     [명령어]      || -------------------------------------------------------------------------------------
//▼ 서버 금칙어 목록 || -------------------------------------------------------------------------------------
						} else if (msgArr[1].trim().startsWith("/X") || msgArr[1].trim().startsWith("/x") || 
								   msgArr[1].trim().startsWith("/XXX") || msgArr[1].trim().startsWith("/Xxx") || 
								   msgArr[1].trim().startsWith("/xxx")) {
							out.writeUTF("show|=============================================");
							out.writeUTF("show|              서버 금칙어 목록               ");
							out.writeUTF("show|---------------------------------------------");
							out.writeUTF("show| >> 바보    >> 멍청    >> 병신    >> 미친    ");
							out.writeUTF("show| >> shit    >> fuck    >> fucking  ");
							out.writeUTF("show|---------------------------------------------");
							out.writeUTF("show| ※ 금칙어는 [*]로 변합니다.   ");
							out.writeUTF("show| ※ 바른말 고운말을 사용합시다.   ");
							out.writeUTF("show|=============================================");
//     [명령어]      || -------------------------------------------------------------------------------------
//   ▼ 확성기 절반   || -------------------------------------------------------------------------------------
						} else if (msgArr[1].trim().startsWith("/every")) {
							String[] msgSubArr = msgArr[1].split(" ", 2);
							if (msgSubArr == null || msgSubArr.length < 2) {
								out.writeUTF("show|-----------------------------------------");
								out.writeUTF("show| ※ 확성기 사용법이 잘못 되었습니다.");
								out.writeUTF("show| >> /every [보낼 메시지]");
								out.writeUTF("show|-----------------------------------------");
							} else { //옳은 값, 인쇄 부분 ▼
							sendAllMsg("everyone|" + name + "|" + filterText(msgSubArr[1]));
							}
//     [명령어]      || -------------------------------------------------------------------------------------
//  ▼ 공지사항 전달  || -------------------------------------------------------------------------------------
						} else if (msgArr[1].trim().startsWith("/all")) {
							String[] msgSubArr = msgArr[1].split(" ", 2);
							if (msgSubArr == null || msgSubArr.length < 2) {
								out.writeUTF("show|-----------------------------------------");
								out.writeUTF("show| ※ 공지사항 사용법이 잘못 되었습니다.");
								out.writeUTF("show| >> /all [공지사항 메시지]");
								out.writeUTF("show|-----------------------------------------");
							} else { //옳은 값, 인쇄 부분 ▼
								sendAllMsg("공지사항|" + name  + "|" + filterText(msgSubArr[1]));
								//System.out.println("msgSubArr : *"+Arrays.toString(msgSubArr)); // [/all, 123]
							}
//     [명령어]      || -------------------------------------------------------------------------------------
//   ▼ 귓속말(1회)   || -------------------------------------------------------------------------------------
						} else if (msgArr[1].trim().startsWith("/to") || msgArr[1].trim().equalsIgnoreCase("/TO")) {
							String[] msgSubArr = msgArr[1].split(" ", 3); // 받아온 msg을 " "(공백)을 기준으로 3개 분리
							 //System.out.println("msgSubArr : *"+Arrays.toString(msgSubArr)); // [/to, 2222, 123]
							 //System.out.println("name : *"+name); // 1111 (보낸사람(자신))
							 //System.out.println("loc : *"+loc); // 하나 (방이름)
							if (msgSubArr == null || msgSubArr.length < 3) {
								out.writeUTF("show|-----------------------------------------");
								out.writeUTF("show| ※ 귓속말 사용법이 잘못 되었습니다.");
								out.writeUTF("show| >> /to [상대방이름] [보낼메시지]");
								out.writeUTF("show|-----------------------------------------");
							} else if (name.equals(msgSubArr[1])) {
								out.writeUTF("show|-----------------------------------------");
								out.writeUTF("show| ※ 자신에게 귓속말을 할 수 없습니다.");
								out.writeUTF("show| >> /to [상대방이름] [보낼메시지]");
								out.writeUTF("show|-----------------------------------------");
							} else { //옳은 값, 인쇄 부분 ▼
								if (clientMap.containsKey(msgSubArr[1])) { // 유저체크 // 귓속말!
									sendToMsg(loc, name, msgSubArr[1], msgSubArr[2]);
								} else {
									out.writeUTF("show|-----------------------------------------");
									out.writeUTF("show| ※ 해당 유저가 존재하지 않습니다.");
									out.writeUTF("show| >> 유저 목록 확인 : /list");
									out.writeUTF("show|-----------------------------------------");
								}
							} // if (msgSubArr == null || msgSubArr.length < 3) //

							//     [명령어]      || -------------------------------------------------------------------------------------
//   ▼ 귓속말(고정)   || -------------------------------------------------------------------------------------
						} else if (msgArr[1].trim().startsWith("/tt")) {
							String[] msgSubArr = msgArr[1].split(" ", 3); // 받아온 msg을 " "(공백)을 기준으로 3개 분리
							//System.out.println("msgSubArr : *"+Arrays.toString(msgSubArr)); // [/to, 2222, 123]
							//System.out.println("name : *"+name); // 1111 (보낸사람(자신))
							//System.out.println("loc : *"+loc); // 하나 (방이름)
							if (msgSubArr == null || msgSubArr.length < 3) {
								out.writeUTF("show|-----------------------------------------");
								out.writeUTF("show| ※ 귓속말 사용법이 잘못 되었습니다.");
								out.writeUTF("show| >> /to [상대방이름] [보낼메시지]");
								out.writeUTF("show|-----------------------------------------");
							} else if (name.equals(msgSubArr[1])) {
								out.writeUTF("show|-----------------------------------------");
								out.writeUTF("show| ※ 자신에게 귓속말을 할 수 없습니다.");
								out.writeUTF("show| >> /to [상대방이름] [보낼메시지]");
								out.writeUTF("show|-----------------------------------------");
							} else { //옳은 값, 인쇄 부분 ▼
								if (clientMap.containsKey(msgSubArr[1])) { // 유저체크 // 귓속말!
										if(msgArr[3].trim().equalsIgnoreCase("/N")) {
											sendToMsg(loc, name, msgSubArr[1], msgSubArr[2]);
									}
								} else {
									out.writeUTF("show|-----------------------------------------");
									out.writeUTF("show| ※ 해당 유저가 존재하지 않습니다.");
									out.writeUTF("show| >> 유저 목록 확인 : /list");
									out.writeUTF("show|-----------------------------------------");
								}
							} // if (msgSubArr == null || msgSubArr.length < 3) //

							
//     [명령어]      || -------------------------------------------------------------------------------------
//   ▼ 방 리스트와   || -------------------------------------------------------------------------------------
						} else if (msgArr[1].trim().startsWith("/R") || msgArr[1].trim().startsWith("/r") || 
								   msgArr[1].trim().startsWith("/ROOM") || msgArr[1].trim().startsWith("/room") || 
								   msgArr[1].trim().startsWith("/Room")) {
							String[] msgSubArr = msg.split(" ");
							if (msgSubArr.length == 1) { // "/room" 입력시
								out.writeUTF("show|" + getEachMapSize(loc));
//  ▼ 방에서 방 변경 || -------------------------------------------------------------------------------------
							} else if (msgSubArr.length == 2) {  // "/room [변경할 방 이름]" 입력시
								String tmpLoc = msgSubArr[1]; // msgSubArr[1] : 방 이름
							   // 'tmpLoc' : 명령어 뒤의 문구 // 'msgSubArr[0]' : 명령어(req_cmdMsg)
								if (loc.equals(tmpLoc)) {
									out.writeUTF("show|-----------------------------------------");
									out.writeUTF("show| ※ 이미 참여하고 있는 방입니다.");
									out.writeUTF("show| >> 방 목록 보기 : /room");
									out.writeUTF("show| >> 방 변경 하기 : /room [변경할 방 이름]");
									out.writeUTF("show|-----------------------------------------");
									continue;
								}
								if (roomMap.containsKey(tmpLoc)) { // 방체크
									out.writeUTF("show|-----------------------------------------");
									out.writeUTF("show| >> '" + tmpLoc + "' 방으로 이동합니다.");
									out.writeUTF("show|-----------------------------------------");

									clientMap.remove(name); // 현재 방 해쉬맵에서 해당 쓰레드를 제거.
									sendGroupMsg(loc, "show|>> " + name + "님이 퇴장하셨습니다.");

									System.out.println(">> "+name+"님이 "+ loc + " 방에서 퇴장하셨습니다.");
									loc = tmpLoc;
									clientMap = roomMap.get(loc);
									sendGroupMsg(loc, "show|>> " + name + "님이 입장하셨습니다.");
									clientMap.put(name, this); // 새로변경된 방에 서버쓰레드 저장.
								} else {
									out.writeUTF("show|----------------------------------------------");
									out.writeUTF("show| ※ 존재하지 않거나 이동 할 수 없는 방입니다.");
									out.writeUTF("show|----------------------------------------------");
									out.writeUTF("show|" + getEachMapSize(loc));
									out.writeUTF("show| >> 목록 확인 후 다시 입력바랍니다. : ");
								} // if (roomMap.containsKey(tmpLoc)) //
								
							} else {
								out.writeUTF("show|-----------------------------------------");
								out.writeUTF("show| ※ 명령어 사용법이 잘못 되었습니다.");
								out.writeUTF("show| >> 방 목록 보기 : /room");
								out.writeUTF("show| >> 방 변경 하기 : /room [변경할 방 이름]");
								out.writeUTF("show|-----------------------------------------");
							} // if if (msgSubArr.length == 1) //
						} 
// || ==============================================================================================================

					} else if (msg.startsWith("req_say")) { // 대화내용 전송 // 금칙어(filterText)
						sendGroupMsg(loc, "say|" + name + "|" + filterText(msgArr[1]));
						            //지역, 말하기, 닉네임, 내용
					}
				} // run()의 while() //------------------------------------------------------------------
				
			} catch (Exception e) {
//				System.out.println("-----------------------------------------");
//				System.out.println(" >> MultiServerRec:run():" + e.getMessage());
//				System.out.println("-----------------------------------------");
//				 e.printStackTrace();
			} finally {
				// 예외가 발생할때 퇴장. 해쉬맵에서 해당 데이터 제거.
				// 보통 종료하거나 나가면 java.net.SocketException: 예외발생
					sendGroupMsg(loc, "show|>> " + name + "님이 퇴장하셨습니다.");
					System.out.println(">> " + name + "님이 "+ loc +"에서 퇴장하셨습니다.");
					System.out.println(">> 현재 서버에 접속된 유저는 "+
					                    (--MultiServer.connUserCount) + "명 입니다.");
			}
		} // run() // ---------------------------------------------------------------------------------------------
	} // class MultiServerRec // 내부 클래스 // ----------------------------------------------------------------
}
