import java.util.Scanner;

public class Exam_PickNum {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("데이터를 아무거나 무작위로 입력하시오.");
		String str = sc.nextLine();
		
		int sLen = str.length();
		int sum = 0;
		//boolean isnum = true;
		int num;
		
		for(int i=0; i<sLen; i++) {
			String st = str.substring(i, i+1);
			try {
				num = Integer.parseInt(st);
				sum = sum + num;
			}catch(Exception e) {
				//isnum = false;
				//break;
				
			}
			
		}
		System.out.println("숫자의 합: " + sum);
		sc.close();
	}

}
