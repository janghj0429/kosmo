import java.io.*;
import java.time.LocalTime;
import java.util.*;

public class ChatRoom extends ChatServer{
		String chatRoomName;
		String chatRoomPassword;
		String chatRoomLeader;
		String maxUserCount;
		int currentUserCount;
		int maxPnum;
		boolean isSecretRoom;
		List<String> blockedUserList;
		Map<String, PrintWriter> chatRoomMember;
		static PrintWriter out;
		static BufferedReader in;
		
		public ChatRoom(PrintWriter output, BufferedReader readin)
		{
			chatRoomMember = new HashMap<String, PrintWriter>();
			out = output;
			in = readin;
		}
			
		public ChatRoom(String userID, String chatRoomName, String maxUserCount, PrintWriter out)
		{
			blockedUserList = new ArrayList<String>();
			chatRoomMember = new HashMap<String, PrintWriter>();
			this.chatRoomName = chatRoomName;
			this.maxUserCount = maxUserCount;
			currentUserCount = 1;
			isSecretRoom = false;
		}
			
		public ChatRoom(String userID,String chatRoomName, String maxUserCount, String chatRoomPassword, PrintWriter out)
		{
			blockedUserList = new ArrayList<String>();
			chatRoomMember = new HashMap<String, PrintWriter>();
			this.chatRoomName = chatRoomName;
			this.chatRoomPassword = chatRoomPassword;
			this.maxUserCount = maxUserCount;
			currentUserCount = 1;
			isSecretRoom = true;
		}
		
		public void sendChatRoomMsg(String userID, String msg, Map<String, PrintWriter> chatRoomMember) 
		{
			LocalTime present = LocalTime.now();	
			Iterator<String> it = chatRoomMember.keySet().iterator();
			msg = filterMsg(msg, userID);
			while(it.hasNext()) {
				try {
					PrintWriter it_out = (PrintWriter)chatRoomMember.get(it.next());
					if (userID.equals(""))
						it_out.println(" [" + this.chatRoomName + "] " + msg);
					else
						it_out.println(" [" + this.chatRoomName + "] " + userID + " : " + msg + "  [" + present.getHour() + ":" + present.getMinute() + "]");
				} catch (Exception e) {
					System.out.println(" ▷ 채팅방 메시지 전송 실패");
				}
			}						
		}
			
		public ChatRoom createPublicChatRoom(String userID, PrintWriter out, BufferedReader in) 
		{
			String name = "";
			String maxCount = "";
			ChatRoom pChatRoom = null;
			while(true) {
			try {
				out.println("┌────────────────────────────┐ ");
				out.println("│  채팅방 이름을 입력하세요  │");
				out.println("└────────────────────────────┘");
				name = in.readLine();
				
				out.println("┌────────────────────────────┐ ");
				out.println("│  채팅방 인원을 입력하세요  │");
				out.println("└────────────────────────────┘");
				maxCount = in.readLine();
				pChatRoom = new ChatRoom(userID, name, maxCount, out);
				pChatRoom.chatRoomLeader = userID;	
				break;
				} catch (Exception e) {
					out.println("┌────────────────────────────────┐ ");
					out.println("│  채팅방 만들기에 실패했습니다  │");
					out.println("└────────────────────────────────┘");
					continue;
				}
			}
			return pChatRoom;
		}

		public ChatRoom createSecretChatRoom(String userID, PrintWriter out, BufferedReader in) 
		{
			String name = "";
			String password = "";
			String maxCount = "";
			ChatRoom sChatRoom = null;

			try {
				out.println("┌────────────────────────────┐ ");
				out.println("│  채팅방 이름을 입력하세요  │");
				out.println("└────────────────────────────┘");
				name = in.readLine();
				out.println("┌────────────────────────────┐ ");
				out.println("│  채팅방 인원을 입력하세요  │");
				out.println("└────────────────────────────┘");
				maxCount = in.readLine();
				out.println("┌───────────────────────────┐ ");
				out.println("│ 채팅방 암호를 입력하세요  │");
				out.println("└───────────────────────────┘");
				password = in.readLine();
				
				sChatRoom = new ChatRoom(userID, name,  maxCount, password, out);
				super.chatRoomList.add(sChatRoom);
				sChatRoom.chatRoomLeader = userID;	
				} catch (Exception e) {
					out.println("┌────────────────────────────────┐ ");
					out.println("│  채팅방 만들기에 실패했습니다  │");
					out.println("└────────────────────────────────┘");
					return sChatRoom;
				}
			return sChatRoom;
		}
	}