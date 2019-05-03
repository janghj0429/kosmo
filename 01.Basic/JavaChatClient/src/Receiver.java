import java.awt.Toolkit;
import java.io.*;
import java.net.*;

public class Receiver extends Thread{
	Socket socket;
	BufferedReader in = null;
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Receiver(Socket socket)
	{
		this.socket = socket;
		try 
		{
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream() ));			
		} catch(Exception e) {
			System.out.println("┌─────────────────────┐");
			System.out.println("│채팅이 종료되었습니다│");
			System.out.println("└─────────────────────┘");
			try {
				in.close();
			} catch(Exception e1) { 
				System.out.println("┌─────────────────────┐");
				System.out.println("│채팅이 종료되었습니다│");
				System.out.println("└─────────────────────┘");
			}
		}
	}
	
	@Override
	public void run() {
		
		while(in != null) {
			try {
				toolkit.beep();
				System.out.println(in.readLine());
			} catch (java.net.SocketException ne) {
				System.out.println("┌─────────────────────┐");
				System.out.println("│채팅이 종료되었습니다│");
				System.out.println("└─────────────────────┘");
				break;
			} catch (Exception e) {
				System.out.println("┌─────────────────────┐");
				System.out.println("│채팅이 종료되었습니다│");
				System.out.println("└─────────────────────┘");
				break;
			}
		} // while End
		
		try {
			in.close();
		} catch (Exception e) {
			System.out.println("┌─────────────────────┐");
			System.out.println("│채팅이 종료되었습니다│");
			System.out.println("└─────────────────────┘");
			return;
		}
	}
}
