/** Jdbc11 #프로젝트 시안5 // 오라클과 연동한 룸 대기 // Ver001(로그인입장)과 연동 **/ 
/*콘솔 멀티채팅 클라이언트 프로그램*/
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MultiClient {

	static boolean chatmode = false;
	static int chatState = 0;
	// 0 : 로그인 안된상태
	// 1 : 로그인 대기방 (대화X)
	// 2 : 채팅방 입장 (대화가능)
	// 3 : 1:1 대화 (귓속말)

	public static void main(String[] args) throws UnknownHostException, IOException {

		ChatBManager manager = ChatBManager.createManagerInst();
		int choice;

		while (true) {
			try {

				MenuViewer.showMenu();
				choice = MenuViewer.keyboard.nextInt();
				MenuViewer.keyboard.nextLine();

				if (choice < INIT_MENU.lOGIN || choice > INIT_MENU.EXIT)
					throw new MenuChoiceException(choice);

				switch (choice) {
				case INIT_MENU.lOGIN:
					manager.loginList();

		//=========================================================================================================//
		//======================================// ▼ 채팅서버 시작 ▼ //============================================//
					
					try {
						String ServerIP = "localhost";
						if (args.length > 0)
							ServerIP = args[0];
						Socket socket = new Socket(ServerIP, 9999); // 소켓 객체 생성
						System.out.println("<< 서버와 연결이 되었습니다 >>");

						//			사용자로부터 얻은 문자열을 서버로 전송해주는 역활을 하는 쓰레드
						Thread sender = new Sender(socket);
						sender.start(); // 스레드 시동

						//			서버에서 보내는 메시지를 사용자의 콘솔에 출력하는 쓰레드.
						Thread receiver = new Receiver(socket);
						receiver.start(); // 스레드 시동

					} catch (Exception e) {
						System.out.println("예외[MultiClient class]:" + e);
					}
					
		//======================================// ▲ 채팅서버 시작 ▲ //============================================//
		//=========================================================================================================//

					return;
				case INIT_MENU.INPUT:
					manager.inputData();
					break;
				case INIT_MENU.DELETE:
					manager.deleteData();
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
				System.out.println(" >> 메뉴창으로 돌아갑니다.");
				System.out.println("------------------------------------------\n");
			} catch (InputMismatchException e) {

				// try 내부의 코드가 InputMismatchException 을 던진다면, 예외를 받아온다.
				// catch가 실행되는 동안은 Program 이 종료되지 않는다.
				MenuViewer.keyboard = new Scanner(System.in); // 중복 멈추기
				System.out.println("\n------------------------------------------");
				System.out.println(" ※ 번호를 입력해주시기 바랍니다.");
				System.out.println(" >> 메뉴창으로 돌아갑니다. ");
				System.out.println("------------------------------------------\n");
			}
		} // while문 //
	} // main //
}