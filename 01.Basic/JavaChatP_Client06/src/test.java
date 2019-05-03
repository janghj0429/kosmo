import java.io.*;
import java.util.*;

public class test {
	public static void main(String[] args) {
		Date date = new Date(); // 현재 시간 날짜
		
//		try {
//			BufferedWriter logrecord = new BufferedWriter
//					(new OutputStreamWriter
//							(new FileOutputStream("out.txt"),"UTF8"));
//		} catch (Exception e) {
//			
//		}

		
		String invMsg = "[kosmo02]: sadfsadgas";
		
		StringTokenizer t = new StringTokenizer(invMsg, "]");
		String nick = t.nextToken("]");
		System.out.println(nick);

		System.out.println(nick.startsWith("kosmo02"));
		
		
		
//		System.out.println(s1.contains("MASTER"));
//		System.out.println(date);
//		
//		StringTokenizer s = new StringTokenizer(date.toString());
//		
//		System.out.println(s.nextToken()); // 요일
//		System.out.println(s.nextToken()); // 달
//		System.out.println(s.nextToken()); // 날짜
//		String time = s.nextToken(); // 시간
//		System.out.println(s.nextToken()); // KST < 시간대 현지시간 출력
//		System.out.println(s.nextToken()); // 년
//		
//		StringTokenizer t = new StringTokenizer(time , ":");
//		System.out.println(t.nextToken());
//		System.out.println(t.nextToken());
//		System.out.println(t.nextToken());
		
		//System.out.println(s.nextToken());
	}
}