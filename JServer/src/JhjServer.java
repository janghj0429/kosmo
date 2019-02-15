
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;


public class JhjServer 
{
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	ServerSocket serverSocket = null;
	Socket socket = null;
	Map<String, PrintWriter> clientMap;
	String title; //방이름	
		

	Map<String, String> chatUserMap;
	Map<String, Integer> roomInfoMap;     //전체 리스트
	Map<String, PrintWriter> noWaitMap;	  //챗하는
	Map<String, Integer> openInfoMap;
	
	//비공개방
	Map<String, String> secretUserMap;
	Map<String, Integer> pwInfoMap;
	Map<String, Integer> secretInfoMap;
	
	Map<String, String> roomOwnerMap;
	Map<String, PrintWriter> guestMap;
	
	String answer; //초대 대답
	
	//생성자
	public JhjServer() {
		//클라이언트의 출력스트림을 저장할 해쉬맵 생성
		clientMap = new HashMap<String, PrintWriter>();
		//해쉬맵 동기화 설정.
		Collections.synchronizedMap(clientMap);
		
		chatUserMap = new HashMap<String, String>();
		Collections.synchronizedMap(chatUserMap);
		roomInfoMap = new HashMap<String, Integer>();
		Collections.synchronizedMap(roomInfoMap);
		noWaitMap = new HashMap<String, PrintWriter>();
		Collections.synchronizedMap(noWaitMap);
		openInfoMap = new HashMap<String, Integer>();
		Collections.synchronizedMap(openInfoMap);
		
		secretUserMap = new HashMap<String, String>();
		Collections.synchronizedMap(secretUserMap);
		pwInfoMap = new HashMap<String, Integer>();
		Collections.synchronizedMap(pwInfoMap);
		secretInfoMap = new HashMap<String, Integer>();
		Collections.synchronizedMap(secretInfoMap);
		
		roomOwnerMap = new HashMap<String, String>();
		Collections.synchronizedMap(roomOwnerMap);
		guestMap = new HashMap<String, PrintWriter>();
		Collections.synchronizedMap(guestMap);
		
	}
	
	public void init()
	{
		try {
			serverSocket = new ServerSocket(9999); //9999포트로 서버소켓 객체 생성.
			System.out.println("서버가 시작되었습니다...");
			
			while(true) {
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + ":" + socket.getPort());
				
				Thread mst = new MultiServerT(socket); //쓰레드 생성
				mst.start(); // 쓰레드 시동
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	//접속자 리스트 보내기
	public void list(PrintWriter out)
	{
		//출력스트림을 순차적으로 얻어와서 해당메시지를 출력한다.
		Iterator<String> it = clientMap.keySet().iterator();
		String msg = "사용자 리스트 [";
		while(it.hasNext()) {
			msg += (String)it.next() + ",";
		}
		msg = msg.substring(0, msg.length()-1) + "]";
		out.println(msg);
	}
	
	//귓속말
	public void sendTo(String name, String s)
	{
//		Iterator<String> it = clientMap.keySet().iterator();
//		
//		StringTokenizer token = new StringTokenizer(s);
//		String order = token.nextToken();
//		String toname = token.nextToken();
//		String msg = token.nextToken();
//		
//		PrintWriter to_out = (PrintWriter)clientMap.get(toname);
//		to_out.println("["+name+"]"+"->"+ "["+toname+"] "+msg);
//		System.out.println("rere");
		
		int start = s.indexOf(" ")+1;
		int end = s.indexOf(" ",start);
		if (end != -1) {
			String toname = s.substring(start,end);
			String msg = s.substring(end+1);
			Object obj = clientMap.get(toname);
			if(obj != null) {
				PrintWriter to_out = (PrintWriter)obj;
				to_out.println("["+name+"]"+"->"+ "["+toname+"] "+msg);
				to_out.flush();
			}
		}
		
//		while(it.hasNext())
//		{
//			String mapName = it.next();
//			try {
//				PrintWriter to_out = (PrintWriter)clientMap.get(it.next());
//				if(name.equals(mapName))
//				{
//					to_out.println("["+name+"] "+msg);
//				}else
//				{
//					mapName=it.next();
//					continue;
//				}
//		
//			} catch(Exception e) {
//				System.out.println("예외 to" +e);
//			}
//		}
	}
	public void guestMsg(String name, String s)
	{
		Iterator<String> it = guestMap.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				PrintWriter it_out = (PrintWriter) guestMap.get(it.next());
				if(name.equals(""))
					it_out.println(s);
				else
					it_out.println("["+name+"] "+s);
			}catch(Exception e) {
				System.out.println("예외:"+e);
			}
		}
	}
	
	//접속된 모든 클라이언트들에게 메시지를 전달.
	public void sendAllMsg(String user, String msg)
	{
		//출력스트림을 순차적으로 얻어와서 해당 메시지를 출력한다.
		Iterator<String> it = clientMap.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				PrintWriter it_out = (PrintWriter) clientMap.get(it.next());
				if(user.equals(""))
					it_out.println(msg);
				else
					it_out.println("["+user+"] "+msg);
			}catch(Exception e) {
				System.out.println("예외:"+e);
			}
		}
	}
	
//=======================================대기실메뉴=======================================//
	public void waitingRoomMenu(String name)
	{
		Object obj = clientMap.get(name);
		PrintWriter out = (PrintWriter)obj;
		
		out.println("/공개방만들기			: /makeroom");
		out.println("/비공개방 만들기		: /secretroom");
		out.println("/전체방리스트보기		: /roomlist");
		out.println("/공개방리스트보기		: /openlist");
		out.println("/비 공개방리스트보기	: /secretlist");
		out.println("/대화방참여			: /enter ");
	}
	// 공개방만들기
	public void makeRoom(String name,String title,int number, PrintWriter out)
	{			
		chatUserMap.put(name, title);
		roomInfoMap.put(title, number);
		openInfoMap.put(title, number);
		
		noWaitMap.put(name, out);
		guestMap.remove(name);		
		roomOwnerMap.put(title, name);
		
		out.println("공개채팅방 "+title+"가 개설, 입장하였습니다.");
//		Set set=chatUserMap.entrySet();
//		System.out.println(set);
//		System.out.println("ㅡㅡㅡㅡz");
	}
	///비공개방만들기
	public void secretroom(String name,String title,int number,int pw, PrintWriter out)
	{
		chatUserMap.put(name, title);
		roomInfoMap.put(title, number);
		secretInfoMap.put(title, number);
		pwInfoMap.put(title, pw);
		
		noWaitMap.put(name, out);
		guestMap.remove(name);	
		roomOwnerMap.put(title, name);	
		
		out.println("비공개채팅방 "+title+"가 개설, 입장하였습니다.");
	}
	
	//채팅방 입장 후에 채팅 메서드
	public void sendChatRoom(String name, String title, String s)
	{
		System.out.println("ㅡㅡㅡㅡ5");
		Iterator<String> itr = chatUserMap.keySet().iterator();
		while(itr.hasNext())
		{
			//
			if(chatUserMap.containsValue(title))
			{
				//String to_name = (String)(itr.next());
				Object obj = noWaitMap.get(itr.next());
				PrintWriter out = (PrintWriter)obj;
				out.println("["+name+"] : "+s);
			}else {
				System.out.println(s);
			}
		}
	}
	
//====================================================================================//
//====================================================================================//	
	
	/////////////////////////////////// 전체방리스트출력
	public void roomlist(PrintWriter out)
	{
//		Iterator<String> it = roomInfoMap.keySet().iterator();
//		String msg = "전체 방 리스트 [";
//		while(it.hasNext()){
//			msg += (String)it.next() + ","; 
//		}
//		msg = msg.substring(0, msg.length()-1) + "]";
//		out.println(msg);
		if(roomInfoMap.size() > 0) {
			Set<String> keyset = roomInfoMap.keySet();
			out.println(keyset);
		}else {
			out.println("방이 없습니다.");
		}
	
	}
	public void openlist(PrintWriter out) //공방리스트
	{
		if(openInfoMap.keySet() != null) {
			Set keyset = openInfoMap.keySet();
			out.println(keyset);
		}else {
			out.println("방이 없습니다.");
		}
	}
	public void secretlist(PrintWriter out) //비방리스트
	{
		if(secretInfoMap.keySet() != null) {
			Set keyset = secretInfoMap.keySet();
			out.println(keyset);
		}else {
			out.println("방이 없습니다.");
		}
	}
	
	/////////////////////////////////////// 공개방입장
	public void enterRoom(String name, String title, PrintWriter out)
	{
		int count = openInfoMap.get(title);
		
		Iterator itr = chatUserMap.keySet().iterator();
		int i = 0;
		while(itr.hasNext())
		{
			String user = chatUserMap.get(itr.next());
			if(user.equals(title))
			{
				i = i+1;
			}
		}
		if(i<count)
		{
			chatUserMap.put(name, title);
			noWaitMap.put(name, out);
			guestMap.remove(name, out);
			out.println(title+" 방에 입장");
			String s = "입장하셧습니다.";
			sendChatRoom(name, title, s);
		}else {
			out.println("정원초과");
		}
		
	}
	////// 비공개방입장
	public void enterSecretRoom(String name, String title, PrintWriter out)
	{
		System.out.println("ㅡㅡㅡㅡㅁ");
		int count = secretInfoMap.get(title);		
		Iterator itr = chatUserMap.keySet().iterator();
		int i = 0;
		System.out.println("ㅡㅡㅡㅡㄴ");
		while(itr.hasNext())
		{
			System.out.println("ㅡㅡㅡㅡㄹ");
			String user = chatUserMap.get(itr.next());
			if(user.equals(title))
			{
				i = i+1;
			}
		}
		if(i<count)
		{
			chatUserMap.put(name, title);
			noWaitMap.put(name, out);
			guestMap.remove(name, out);
			out.println(title+" 방에 입장");
			String s = "입장하셧습니다.";
			sendChatRoom(name, title, s);
		}else {
			out.println("정원초과");
		}					
	}
//=====================================대기실메뉴=====================================//
	
//=====================================채팅방메뉴=====================================//
	public void chatRoomMenu(PrintWriter out)
	{
		out.println("채팅방 내 메뉴");
		out.println("대화방 리스트 보기			:	/wholeroomlist");
		out.println("대기실 사용자 리스트 보기	:	/waituserlist");
		out.println("내 룸 사용자리스트보기		:	/myroomlist");
		out.println("초대하기 -방장만 가능 		:	/invite");
		out.println("강퇴		-일회성 강퇴	:	/kick");
		out.println("			-영구성 강퇴	:	/ban");
		out.println("룸나가기					:	/exit");
		out.println("방장승계					:	/succession");
		out.println("방폭파						:	/closeroom");
	}
	public void wholeRoomList(PrintWriter out)
	{
		if(roomInfoMap.size() > 0 ) {
			Set<String> keyset = roomInfoMap.keySet();
			out.println(keyset);
		}else {
			out.println("방이 없습니다.");
		}
	}
	public void waitUserList(PrintWriter out)
	{
		if(guestMap.size() > 0) {
			Set<String> keyset = guestMap.keySet();
			out.println(keyset);
		}else {
			out.println("대기실 유저 없다");
		}
	}
	public void myRoomList(String name, PrintWriter out)
	{
		title = chatUserMap.get(name);
		Iterator<String> itr = chatUserMap.keySet().iterator();
		while(itr.hasNext())
		{
			name = chatUserMap.get(itr.next());
			if(chatUserMap.containsKey(title))
			{
				out.print(name + "\t");
			}else {
				System.out.println("???");
			}
		}
	}
	public void inviteCheck(String name, String man, PrintWriter out, String answer)
	{
		if(roomOwnerMap.containsValue(name) && answer.equalsIgnoreCase("y"))
		{
			System.out.println("-------3");
			title = chatUserMap.get(name);
			name = man;;
			Invite(name, title, out);
		}
		else
		{
			out.println("방장이 아니라 초대 불가능 or 상대방 거부");
		}
	}
	public void Invite(String name, String title, PrintWriter out) //초대받은이름, 방제, 초대받은사람 out
	{
		if(openInfoMap.containsKey(title)) //공방입장
		{
			System.out.println("-------4");
			enterRoom(name, title, out);
		}
		else //비방입장
		{
			System.out.println("-------5");
			enterSecretRoom(name, title, out);
		}
	}
	public void kickCheck(String name, String title, PrintWriter out, String kick, PrintWriter to_out)
	{
		if(!roomOwnerMap.containsValue(name))
		{
			System.out.println("-------3");
			System.out.println("방장이 아니라 안됨");
		}
		else
		{
			if( chatUserMap.get(name).equals(chatUserMap.get(kick)) )
			{
				Kick(name, title, out, kick, to_out);
			}
			else
			{
				System.out.println("대상 없음");
			}
		} 
	}
	public void Kick(String name, String title, PrintWriter out, String kick, PrintWriter to_out)
	{
		
	}
	public void openRoomExit(String name, String title, PrintWriter out)
	{
		System.out.println("--------ox1");
		chatUserMap.remove(name);
		noWaitMap.remove(name);
		guestMap.put(name, out);
		if(roomOwnerMap.containsValue(name))
		{
			System.out.println("--------ox2");
			roomOwnerMap.remove(title);
			if(chatUserMap.containsValue(title))
			{
				System.out.println("--------ox3");
				Iterator<String> itr = chatUserMap.keySet().iterator();
				name = chatUserMap.get(itr.next());
				roomOwnerMap.put(title, name);
				String s = "이 방에서 퇴장.";
				sendChatRoom(name, title, s);
				s = "으로 방장 변경.";
				sendChatRoom(name, title, s);
				Set set = roomOwnerMap.entrySet();
				System.out.println(set);
			}
			else
			{
				System.out.println("--------ox4");
				roomInfoMap.remove(title);	
				openInfoMap.remove(title);
			}
		}
		else
		{
			System.out.println("--------ox5");
			String s = "이 방에서 퇴장.";
			sendChatRoom(name, title, s);
		}
	}
	public void secretRoomExit(String name, String title, PrintWriter out)
	{
		System.out.println("--------s x");
		chatUserMap.remove(name);
		noWaitMap.remove(name);
		guestMap.put(name, out);
		if(roomOwnerMap.containsValue(name))
		{
			roomOwnerMap.remove(title);
			if(chatUserMap.containsValue(title))
			{
				Iterator<String> itr = chatUserMap.keySet().iterator();
				name = chatUserMap.get(itr.next());
				roomOwnerMap.put(title, name);
				String s = "이 방에서 퇴장.";
				sendChatRoom(name, title, s);
				s = "으로 방장 변경.";
				sendChatRoom(name, title, s);
				Set set = roomOwnerMap.entrySet();
				System.out.println(set);
//				String owner = roomOwnerMap.get(title);
//				System.out.println(roomOwnerMap.);
			}
			else
			{
				roomInfoMap.remove(title);	
				secretInfoMap.remove(title);
				pwInfoMap.remove(title);
			}
		}
		else
		{
			String s = "이 방에서 퇴장.";
			sendChatRoom(name, title, s);		
		}
	}
	public void successionOwner(String name, String title, String man)
	{
		roomOwnerMap.remove(title);
		roomOwnerMap.put(title, man);
		
		String s = "이 방장이 되었습니다.";
		sendChatRoom(name, title, s);	
	}
	public void closeRoom(String name, String title, PrintWriter out) 
	{
		if(roomOwnerMap.containsValue(name))
		{
			Iterator<String> itr = chatUserMap.keySet().iterator();
			while(itr.hasNext())
			{
				//String rn = chatUserMap.get(itr.next());
				if(chatUserMap.containsValue(title))
				{
					
					chatUserMap.remove(itr.next());
				}
			}
			chatUserMap.remove(name);
			
		}
		else
		{
			out.println("권한 없음.");
		}
	}
	public void closeSecretRoom(String name, String title, PrintWriter out)
	{
		
	}
	
//=========================================================================//	
//=========================================================================//	
	
	
	public static void main(String[] args) 
	{	
		//db연동하고 
		
		//서버객체 생성
		JhjServer ms = new JhjServer();
		ms.init();

	}
	
///////////////////////////////////////////////////////////////////////
//내부 클래스
//클라이언트로부터 읽어온 메시지를 다른 클라이언트(socket)에 보내는 역할을 하는 메서드
	
	class MultiServerT extends Thread
	{
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;
		
		//생성자
		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream() ));
							
			} catch(Exception e) {
				System.out.println("예외:" + e);
			}
		}
		
		//쓰레드를 사용하기 위해서 run()메서드 재정의
		@Override
		public void run()
		{
			//String s = "";
			String name = ""; //클라이언트로부터 받은 이름을 저장할 변수
			try {
				name = in.readLine(); //클라이언트에서 처음으로 보내는 메시지는
				  //클라이언트가 사용할 이름이다.
				
				sendAllMsg("", name +"님이 입장하셨습니다.");
				//현재 객체가 가지고있는 소켓을 제외하고 다른 소켓(클라이언트)들에게 접속알림
				clientMap.put(name, out);//해쉬맵에 키를 name으로 출력스트림 객체를 저장
				guestMap.put(name, out);//게스트 운영하는 맵
				System.out.println("현재 접속자 수는"+clientMap.size()+"명 입니다.");
				
				//입력스트림이 null이 아니면 반복
				String s = "";
				while(in != null) {
					s = in.readLine();
					System.out.println(s);
					StringTokenizer tokens = new StringTokenizer(s.substring(1));
//					if( s.equals("q") || s.equals("Q") )
//						break;
//					if ( s.equals("/list")) //list 출력
//						list(out);				
					
					System.out.println("ㅡㅡㅡㅡa");
					if (chatUserMap.containsKey(name))
					{
						if ( s.substring(0, 1).equals("/"))
						{
							StringTokenizer str = new StringTokenizer(s.substring(1));
							String order = str.nextToken();
							System.out.println("ㅡㅡㅡㅡaa");
							
//							Object obj = clientMap.get(name);
//							PrintWriter out = (PrintWriter)obj;
							System.out.println("------ee");
							if(order.equals("roommenu"))
							{
								System.out.println("--tt");
								chatRoomMenu(out);
							}
							else if(order.equals("wholeroomlist"))
							{
								wholeRoomList(out);
							}
							else if(order.equals("waituserlist"))
							{
								waitUserList(out);
							}
							else if(order.equals("myroomlist"))
							{
								System.out.println(name);
								myRoomList(name, out);
							}
//							else if(order.equals("invite"))
//							{
//								out.println("초대할사람입력: ");
//								String man = in.readLine();
//								PrintWriter to_out = (PrintWriter)clientMap.get(man);
//								out = to_out;
//								out.println(name+" 님이 초대함, y? or n?");
//								System.out.println("-------1");
//								
//								answer= in.readLine();
//															
//								System.out.println("-------2");
//								inviteCheck(name, man, out, answer);
//							}
							else if(order.equals("kick"))
							{
								out.println("강퇴할 사람 입력:");
								String kick = in.readLine();
								PrintWriter to_out = (PrintWriter)clientMap.get(kick);
								kickCheck(name, title, out, kick, to_out);
							}
							else if(order.equals("exit"))
							{
								if(openInfoMap.containsKey(title))
								{
									openRoomExit(name, title, out);								
								}
								else if(secretInfoMap.containsKey(title))
								{
									secretRoomExit(name,title, out);
								}
								else
								{
									out.println("잘못된 입력");
								}
							}
							else if(order.equals("succession"))
							{
								out.println("승계할 대상입력: ");
								String man = in.readLine();
								successionOwner(name, title, man);
							}
							else if(order.equals("close"))
							{
								if(openInfoMap.containsKey(title))
								{
									closeRoom(name, title, out);
								}
								else
								{
									closeSecretRoom(name, title, out);
								}
								
							}
									
							else
							{
								System.out.println("???");
							}
						}else {
							sendChatRoom(name, title, s);
						}
					}
					else
					{
						if ( s.substring(0, 1).equals("/"))
						{
							StringTokenizer str = new StringTokenizer(s.substring(1));
							String order = str.nextToken();
							System.out.println("ㅡㅡㅡㅡb");
							
							Object obj = clientMap.get(name);
							PrintWriter out = (PrintWriter)obj;
							out.println("/menu 로 명령어메뉴확인하세요");
							if(order.equals("to"))	
							{	sendTo(name, s);	}
							else if(order.equals("list"))
							{	list(out);		}
							else if(order.equals("menu"))
							{	waitingRoomMenu(name);}
							else if(order.equals("makeroom"))
							{
								System.out.println("ㅡㅡㅡㅡc");
								out.println("타이틀(방제목)설정 : ");
								title = in.readLine();
								out.println("정원 설정 : ");
								int number = Integer.parseInt(in.readLine());
								makeRoom(name,title,number,out);
							}
							else if(order.equals("secretroom"))
							{
								System.out.println("ㅡㅡㅡㅡc");
								out.println("타이틀(방제목)설정 : ");
								title = in.readLine();
								out.println("정원 설정 : ");
								int number = Integer.parseInt(in.readLine());
								out.println("비번 설정 : ");
								int pw = Integer.parseInt(in.readLine());
								secretroom(name,title,number,pw,out);
							}
							else if(order.equals("roomlist"))
							{
								roomlist(out);
							}
							else if(order.equals("openlist"))
							{
								openlist(out);
							}
							else if(order.equals("secretlist"))
							{
								secretlist(out);
							}
							else if (order.equals("enter"))
							{
								System.out.println("ㅡㅡㅡㅡd");
								out.println("방제목입력: ");
								title = in.readLine();
								System.out.println("ㅡㅡㅡㅡㅂ");
								//먼저 비공개방인지 아닌지 검사여부
								if(secretInfoMap.containsKey(title)) 
								{
									System.out.println("ㅡㅡㅡㅡㅈ");
									out.println("비번 입력 : ");
									int pw = Integer.parseInt(in.readLine());
									System.out.println("ㅡㅡㅡㅡdㄷ");
									if(pw == pwInfoMap.get(title))
									{
										enterSecretRoom(name, title, out);									
									}else {
										out.println("비번 틀림, Bye");
									}
								}else {
									enterRoom(name, title, out);						
								}
							}
							else {
								sendAllMsg(name, s);
								out.println("잘못된명령어ㅡㅡ");
							}
						}else {
							System.out.println("ㅡㅡㅡㅡ2");
							if(chatUserMap.containsKey(name))
							{
								System.out.println("ㅡㅡㅡㅡ3");
								title = chatUserMap.get(name);							
								sendChatRoom(name, title, s);
							}else {
								System.out.println("ㅡㅡㅡㅡ4");
								guestMsg(name, s);
								//sendAllMsg(name, s);	
								System.out.println(" ");
							}
						
						}
					
					}
				}	

				
//				System.out.println("Bye...");
				
			} catch(Exception e) {
				System.out.println("예외:" +e);
			} finally {
				//예외가 발생할 때 퇴장. 해쉬맵에서 해당 데이터 제거.
				//보통 종료하거나 나가면 java.net.SocketException:예외발생
				
				clientMap.remove(name);
				sendAllMsg("", name + "님이 퇴장하셨습니다.");
				System.out.println("현재 접속자 수는 "+clientMap.size()+"명 입니다.");
				
				try {
					in.close();
					out.close();
					
					socket.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	//////////////////////////////////////////////////////////////
}
