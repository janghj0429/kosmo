import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer2 {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String s = "";
		
		try {
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다...");
			
			socket = serverSocket.accept();
			System.out.println(socket.getInetAddress() + ":" + socket.getPort());
			
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream() ));
			
			while(in != null ) {
				s= in.readLine();
				if(s == null)
					break;
				if( s.equals("q") || s.equals("Q") )
					break;
				
				System.out.println(s);
				out.println(s);
			}
			
			System.out.println("Bye...");
			
		} catch(Exception e) {
			System.out.println("예외1: " + e);
			//e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				
				socket.close();
				serverSocket.close();
			} catch(Exception e) {
				System.out.println("예외2: " +e);
				//e.printStackTrace();
			}
		}

	}

}
