import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MultiClient2 {

	public static void main(String[] args) throws UnknownHostException, IOException
	{
		System.out.println("이름을 입력해 주세요.");
		Scanner s = new Scanner(System.in);
		String s_name = s.nextLine();
		
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			//String ServerIP = "localhost";
			
			Socket socket = new Socket(args[0], 9999); //소켓 객체 생성
			System.out.println("서버와 연결이 되었습니다.");

			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream() ));
			
			out.println(s_name);
			
			while(out != null) {
				try {
					//입력
					if(in != null) {
						System.out.println("Receive : " + in.readLine());
					}
					
					//출력
					String s2 = s.nextLine();
					if(s2.equals("q") || s2.equals("Q") ) {
						out.println(s2);
						break;
					}else {
						out.println(s_name + "=>" + s2);
					}
				}catch(IOException e) {
					System.out.println("예외: " + e);
				}
			}
			
			in.close();
			out.close();
			
			socket.close();
		

		} catch(Exception e) {
			System.out.println("예외[MultiClient class]: " + e);
		}
	}
}



