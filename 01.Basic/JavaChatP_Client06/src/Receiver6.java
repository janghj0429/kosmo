import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;


//서버로 메시지를 전송하는 클래스
public class Receiver6 extends Thread{
	Socket socket;
	String id = null;
	BufferedReader in = null;
	static String title = null; //초대자 방의 정보를 갖습니다.
	
	//Socket을 매개변수로 받는 생성자
	public Receiver6(Socket socket, String id) {
		this.socket = socket;
		this.id = id;
		
		try {
			in = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
		} catch (Exception e) {
			System.out.println("예외1: " + e);
		}
	}
	//run() 메소드 재정의
	@Override
	public void run() {
		//String title = null;
		List<String> userBWord = new ArrayList<>(); // 개인 금칙어 저장
		List<String> banUser = new ArrayList<>(); // 개인 메시지 차단

		while (in != null) {
			try {
				String msg = in.readLine();  // 난 메시지 받음 읽어올게
				if(msg.equalsIgnoreCase("Q")) {					
					System.exit(1001);
				}
				if(msg.substring(0, 1).equals("|")) {
					StringTokenizer s = new StringTokenizer(msg, "|");
					String ms= s.nextToken("|");// 제어를 하기위한 명령문
					if (ms.equals("invroom")){
						title = s.nextToken("|");
						invAgreeMsg(msg); // 서버로 보낼 메시지							
					} // 추가적인 제어는 여기서 이어나가기
					if (ms.equals("addword")) {
						String word = s.nextToken("|"); 
						userBWord.add(word);
						System.out.println("개인 금칙어 리스트: "+userBWord);
						continue;
					}
					if (ms.equals("delword")) {
						String word = s.nextToken("|"); 
						userBWord.remove(word);
						System.out.println("개인 금칙어 리스트: "+userBWord);
						continue;
					}
					if (ms.equals("banuser")) {
						String user = s.nextToken("|"); 
						banUser.add(user);
						System.out.println("지정한 사용자가 차단되었습니다.");
						continue;
					}
					if (ms.equals("delbanuser")) {
						String user = s.nextToken("|"); 
						banUser.remove(user);
						System.out.println("지정한 사용자의 차단이 해제되었습니다.");
						continue;
					}
				}
				//////// 차단 유저 확인///////
				StringTokenizer s = new StringTokenizer(msg, "]");
				String ck1User = s.nextToken(); // [닉네임
				StringTokenizer s1 = new StringTokenizer(ck1User, "[");
				String ckUser = s1.nextToken(); // 닉네임
				int ck = 0;
				for(int i=0; i<banUser.size();i++) {
					if(ckUser.endsWith(banUser.get(i))==true)
						ck++;
					}
				if(ck != 0) {
					continue;
				}						
				////////
				msg = ckMsg(msg, userBWord);
				System.out.println(">>" + msg);
			} catch (java.net.SocketException ne) {
				break;
			} catch (Exception e) {
				System.out.println("예외2: " + e);
			}
		}		
		try {
			in.close();
		} catch (Exception e) {
			System.out.println("예외3: " + e);
		}
	}
	// 초대 메시지
	public static String invAgreeMsg(String msg)
	{
		if(title == null || title.equals("")) {
			System.out.println("초대된 채팅방이 없습니다.");
			msg = "|"; // 방이 없을경우 일반 메시지를 발송하기 위한 명령문을 ChatWin 클래스에 전달
			return msg;
		}			
		msg = "/invagree|"+ title +"|"+msg; //있을 경우 유저에게 보여주지 않고 명령문 발송
		return msg;
	}
	
	public String ckMsg(String msg, List<String> userBWord) //개인 금칙어
	{			
		for(int i=0;i<userBWord.size();i++) {
			msg = msg.replace(userBWord.get(i), "**");
		}
		return msg;
	}
}
