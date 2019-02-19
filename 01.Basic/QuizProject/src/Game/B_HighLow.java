package Game;
import java.util.Random;
import java.util.Scanner;

public class B_HighLow {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Random rand = new Random();
		
		while(true)
		{
			System.out.println("나는 지금 0부터 100사이의 값중 하나를 생각하겠습니다.");
			System.out.println("당신은 그 숫자를 6회안에 맞혀야합니다.");
			
			int com = rand.nextInt(101);
			
			for(int i=0; i<6; i++) {
				int user = s.nextInt();
				
				if(com<user) System.out.println(user + "는 제가 정한숫자보다 큽니다.");
				if(com>user) System.out.println(user + "는 제가 정한숫자보다 작습니다.");
				if(com==user) {
					System.out.println(user + "정답입니다.");
					break;
				}
				
				if(i<5)
					System.out.println( (6-i-1) + "회 남았습니다.");
				else
					System.out.println("모든기회를 다 사용했습니다.");
			}
			
			System.out.println("게임을 더 진행하시겠습니까? (y/n)");
			String str = s.next();
			if (!str.equalsIgnoreCase("y")) {
				System.out.println("Good bye~");
				break;
			}
		}

	}

}
