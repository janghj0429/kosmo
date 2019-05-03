/** Jdbc20 // 채팅방 입장 가능 // 중복값 완료 // [$$:변경완료 업뎃전] [##:추후수정] [@@:추후확인] **/

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

//서버로 메시지를 전송하는 클래스 
class Sender extends Thread {

	Socket socket;
	DataOutputStream out;
	String name;

	// 생성자 ( 매개변수로 소켓과 사용자 이름 받습니다. )
	public Sender(Socket socket) { // 소켓과 사용자 이름을 받는다.
		this.socket = socket;
		try {
			out = new DataOutputStream(this.socket.getOutputStream());

		} catch (Exception e) {
			System.out.println(">> 예외S_1:" + e);
		}
	}

//	run()메소드 재정의
	@Override
	public void run() {

		Scanner s = new Scanner(System.in);
		System.out.print(">> 대화명을 입력해 주세요 : ");

		while (out != null) { // 출력스트림이 null이 아니면 반복
			try {
				
				String msg = s.nextLine();

				if (msg == null || msg.trim().equals("")) {
					msg = " "; /*continue; //콘솔에선 공백으로 넘기는것이 좀더 효과적임.*/ 
				}

				if (MultiClient.chatState == 0) { // 추후 대화명 관련 처리시 사용.

					if (!msg.trim().equals("")) {
						name = msg;
						out.writeUTF("req_logon|" + msg);
						// #C_[ >> 'name'님 환영합니다. 대기실입니다.] //***
					} else {
						System.out.println("-----------------------------------------");
						System.out.println(" ※ 공백은 입력 할 수 없습니다.");
						System.out.println(" >> 다시 입력해 주세요.");
						System.out.println("-----------------------------------------");
						System.out.print(">> 대화명을 입력해 주세요 : ");
					}
//====================================// ▼ 서버 대기실 시작 ▼ //=========================================//
				} else if (MultiClient.chatState == 1) { // 대기실
					
					if (!msg.trim().equals("")) {
						
						out.writeUTF("req_enterRoom|" + name + "|" + msg);
						// #C_[ [대기실:name] 나는 지금 대기실에 있어요(msg)]
					} else {
						System.out.println("-----------------------------------------");
						System.out.println(" ※ 공백은 입력 할 수 없습니다.");
						System.out.println(" >> 다시 입력해 주세요:");
						System.out.println("-----------------------------------------");
						System.out.print(">> 참여할 방이름을 입력해 주세요 : ");
					}
//====================================// ▲ 서버 대기실 시작 ▲ //=========================================//
				} else if (msg.trim().startsWith("/")) {
					/* // 명령어 기능 추가. //클라이언트단에서 체크
					 * msg.equals("/list") 접속자 
					 * msg.startsWith("/key") 명령어 목록
					 * msg.startsWith("/to") 귓속말
					 * msg.startsWith("/room") room 방
					 * ||msg.startsWith("/q") exit 종료
					 */
					if (msg.startsWith("/end") || msg.startsWith("/End") || msg.startsWith("/END") || 
							msg.startsWith("/E") || msg.startsWith("/e") || msg.startsWith("/q") || msg.startsWith("/Q")) {
						System.out.println("-----------------------------------------");
						System.out.println(" >> 종료되었습니다. \n >> 채팅창을 닫아주세요.");
						System.out.print("-----------------------------------------");
						System.exit(0);
						break;
					} else {       // req_cmdMsg|대화명|/접속자
						out.writeUTF("req_cmdMsg|" + name + "|" + msg);
					}
				} else {       // req_say|아이디|대화내용
					out.writeUTF("req_say|" + name + "|" + msg);
				}
			} catch (Exception e) {
				System.out.println(">> 예외S_2:" + e);
			}
		} // while------
	}// run()------
} 