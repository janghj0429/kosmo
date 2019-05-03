/** jdbc11 // Receiver 조정중 // 시안4 // [$$:변경완료 업뎃전] [##:추후수정] [@@:추후확인] **/
import java.io.DataInputStream;
import java.net.Socket;
import java.net.SocketException;

//서버로 메시지를 전송하는 클래스
public class Receiver extends Thread {

	Socket socket;
	DataInputStream in;

//	socket을 매개변수로 받는 생성자
	public Receiver(Socket socket) {
		this.socket = socket;

		try {
			in = new DataInputStream(this.socket.getInputStream());
			
		} catch (Exception e) {
			System.out.println(">> 예외/R_1:" + e);
		}
	}

	/** 메시지 파서 */
	public String[] getMsgParse(String msg) {
		String[] tmpArr = msg.split("[|]");
		return tmpArr;
	}
	
//	run()메소드 재정의
	@Override
	public void run() {
		while (in != null) {
			try {
				
				String msg = in.readUTF(); // 입력스트림을 통해 읽어온 문자열을 msg에 할당.
				String[] msgArr = getMsgParse(msg.substring(msg.indexOf("|") + 1));

				// 메세지 처리 ----------------------------------------------
				if (msg.startsWith("logon#yes")) { // 로그온 시도 (대화명)
					MultiClient.chatState = 1; // 채팅 상태를 로그온 된 상태로 변경. // logon#yes|그룹리스트
					System.out.println("=============================================");
					System.out.println("            대기실 명령어 목록               ");
					System.out.println("---------------------------------------------");
					System.out.println(" >> 채팅방 참여   : [/GO]   [/go]   [/G]");
					System.out.println(" >> 방 목록 보기  : [/ROOM] [/room] [/R]");
					System.out.println(" >> 대기실 명령어 : [/KEY]  [/key]  [/K]");
					System.out.println("---------------------------------------------");
					System.out.println(" ※ 채팅 프로그램 종료 : [/END] [/end] [/E]   ");
					System.out.println("=============================================");
					
				} else if (msg.startsWith("logon#no")) { // 로그온 실패 (대화명)

					MultiClient.chatState = 0;
					System.out.println("-----------------------------------------");
					System.out.println(" ※ 존재하는 대화명입니다.");
					System.out.println(" >> 다시 입력해 주세요.");
					System.out.println("-----------------------------------------");
					System.out.print(">> 대화명을 입력해 주세요 : ");
					// 1. 대화명이 중복될경우(서버전체 or 그룹) logon#no 패킷이 서버로부터 전달됨.

					
				} else if (msg.startsWith("enterRoom#yes")) { // 그룹입장

					// enterRoom#yes|방
					System.out.println(">> '" + msgArr[0] + "' 방에 입장하였습니다.");
					MultiClient.chatState = 2; // 챗 상태 변경 ( 채팅방입장 완료로 대화가능상태)

					System.out.println("=======================================================");
					System.out.println("                 채팅방 명령어 목록");
					System.out.println("-------------------------------------------------------");
					System.out.println(" >> 참여자 목록   : [/LIST]  [/list]  [/L]");
					System.out.println(" >> 공지사항 전달 : [/ALL]   [all]    [/A]");
					System.out.println(" >> 서버 금칙어   : [/XXX]   [/xxx/]  [/X]");
					System.out.println(" >> 대기실 이동   : [/OUT]   [/out]   [/O]");
					System.out.println(" >> 채팅방 명령어 : [/COM]   [/com]   [/C] ");
					System.out.println(" >> 방 목록 보기  : [/ROOM]  [/room]  [/R]");
					System.out.println(" >> 방 변경 하기  :  /room [변경할 방 이름]");
					System.out.println(" >> 귓속말        :  /to [상대방이름] [보낼메시지]");
					System.out.println("-------------------------------------------------------");
					System.out.println(" ※ 채팅 프로그램 종료 : [/END] [/end] [/E]   ");
					System.out.println("=======================================================");
					System.out.println(">> 채팅이 시작되었습니다.");
					
				} else if (msg.startsWith("enterRoom#no")) {
					
				} else if (msg.startsWith("show")) { // 서버에서전달하고자하는 메시지

					// show|메시지내용
					// msgArr[0]|아이디 // msgArr[1]|대화내용
					System.out.println(msgArr[0]);

				} else if (msg.startsWith("say")) { // 대화내용
					System.out.println("[" + msgArr[0] + "] " + msgArr[1]);

				} else if (msg.startsWith("whisper")) { // 귓속말
					System.out.println("('" + msgArr[0] + "'님의 귓속말) >> " + msgArr[1]);
				
				} else if (msg.startsWith("hisper")) { // 귓속말
					System.out.println("('" + msgArr[0] + "'님에게 보낸 귓속말) >> " + msgArr[1]);

				} else if (msg.startsWith("everyone")) { // 확성기 // sendAllMsg1
					System.out.println("["+msgArr[0]+"님의 공개 메시지] >> " + msgArr[1]);
					
				} else if (msg.startsWith("공지사항")) { // 공지사항 // sendAllMsg2
					System.out.println("[※공지사항※] >> " + msgArr[1]);
				}

			} catch (SocketException e) {
				System.out.println("-----------------------------------------");
				System.out.println(" >> 예외 : " + e);
				System.out.println(" >> 접속중인 서버와 연결이 끊어졌습니다.");
				System.out.println("-----------------------------------------");
				return;

			} catch (Exception e) {
				System.out.println("-----------------------------------------");
				System.out.println(" >> Receiver:run()예외 : " + e);
				System.out.println(" >> 접속중인 서버와 연결이 끊어졌습니다.");
				System.out.println("-----------------------------------------");

			}
		} // while //
	}// run() //
}