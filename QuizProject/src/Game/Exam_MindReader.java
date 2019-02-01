package Game;


import java.util.Random;
import java.util.Scanner;




public class Exam_MindReader {

	public static void main(String[] args) {
		System.out.println("0부터 100사이의 값 중에 하나를 생각하세요.");
		System.out.println("제(컴)가 제시한 숫자가 생각한 숫자보다 크면 h를 입력하세요.");
		System.out.println("제(컴)가 제시한 숫자가 생각한 숫자보다 작으면 l를 입력하세요.");
		System.out.println("제(컴)가 숫자를 맞췃다면 y를 입력하세요.");
		
		Random ran = new Random();
		Scanner sc = new Scanner(System.in);		
		int max = 101;
		int min = 0;
		int ans;
		int i = 1;
		ans = ran.nextInt(101);
		
		
		while(true)
		{			
			
			System.out.printf("당신이 선택한 숫자는 %d 입니까?\n", ans);
			String str = sc.next();
			if(str.equalsIgnoreCase("y")) {
				System.out.printf("정답입니다.  [ %d 회차]", i);
				System.out.println(" Good Bye~");
				break;
			}
			if(str.equalsIgnoreCase("h"))
			{	
				
				max = ans;
				while (true) { 
					ans = ran.nextInt(101); 
					if(ans < max && min < ans ) { break; }
				}
			}
			if(str.equalsIgnoreCase("l"))
			{
				min = ans ;	
				while (true) { 
					ans = ran.nextInt(101);
					if( min < ans && ans < max ); {break;}				
				}
			}
			i++;
		}
	}
}

//0부터 100사이의 값 중에 하나를 생각하세요.
//제(컴)가 제시한 숫자가 생각한 숫자보다 크면 h를 입력하세요.
//제(컴)가 제시한 숫자가 생각한 숫자보다 작으면 l를 입력하세요.
//제(컴)가 숫자를 맞췃다면 y를 입력하세요.
//당신이 선택한 숫자는 12 입니까?
//l
//당신이 선택한 숫자는 70 입니까?
//h
//당신이 선택한 숫자는 51 입니까?
//h
//당신이 선택한 숫자는 48 입니까?
//l
//당신이 선택한 숫자는 18 입니까?


