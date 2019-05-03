import java.io.*;
import java.net.*;

public class ChatClient 
{

	public static void main(String[] args) throws UnknownHostException, IOException
	{

		try {
			String ServerIP = "localhost";
			if (args.length > 0)
				ServerIP = args[0];
			Socket socket = new Socket(ServerIP, 9999);
			
			Thread receiver = new Receiver(socket);
			receiver.start();
			
			new ChatWin(socket);
			
		} catch(Exception e) {
			System.out.println("┌─────────────────────┐");
			System.out.println("│채팅이 종료되었습니다│");
			System.out.println("└─────────────────────┘");
			return;
		}		
	}
}
