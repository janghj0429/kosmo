import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/*콘솔 멀티채팅 클라이언트 프로그램*/
public class MultiClient4 {

	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		System.out.println("이름을 입력해 주세요.");
		Scanner s = new Scanner(System.in);
		String s_name = s.nextLine();
		
		PrintWriter out = null;
		
		try {
			String ServerIP = "localhost";
			Socket socket = new Socket(ServerIP, 9999); //소켓 객체 생성
			System.out.println("서버와 연결이 되었습니다.");
			
			//서버에서 보내는 메시지를 사용자의 콘솔에 출력하는 쓰레드
			Thread receiver = new Receiver4(socket);
			receiver.start();
			
			out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println(s_name);
			
			while(out != null) {
				try {
					String s2 = s.nextLine();
					if( s2.equals("q") || s2.equals("Q") ) {
						out.println(s2);
						break;
					} else {
						out.println(s_name + "=>" + s2);
					}	
				
				} catch (Exception e) {
					System.out.println("예외: " + e);
				}
			}
			out.close();
			
			socket.close();
			
		}catch(Exception e) {
			System.out.println("예외[MultiClient class]:"+e);
		}

	}

}
