import java.io.*;
import java.net.*;


public class MultiServer4 {
	
	ServerSocket serverSocket = null;
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	String s = "";
	
	//생성자
	public MultiServer4() {
			
	}
	
	public void init()
	{
		try {
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다...");
			
			socket = serverSocket.accept();
			System.out.println(socket.getInetAddress() + ":" + socket.getPort());
			
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream() ));
			
			while(in != null ) {
				s = in.readLine();
				if(s == null)
					break;
				if( s.equals("q") || s.equals("Q") )
					break;
				
				System.out.println(s);
				//out.println(s);
				sendAllMsg(s);
			}		
			
			System.out.println("Bye..");
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				
				socket.close();
				serverSocket.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//접속된 모든 클라이언트들에게 메시지를 전달.
	public void sendAllMsg(String msg)
	{
		try {
			out.println(msg);
		} catch(Exception e) {
			System.out.println("예외:" + e);
		}
	}
	
	public static void main(String[] args) 
	{
		//서버객체 생성
		MultiServer4 ms = new MultiServer4();
		ms.init();
	}

}
