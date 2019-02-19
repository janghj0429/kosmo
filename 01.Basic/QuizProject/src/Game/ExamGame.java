package Game;
import java.util.Random;
import java.util.Scanner;

public class ExamGame {

	public static void main(String[] args) {
		while(true) {
			

			System.out.println("무엇을 내겠습니까?");
			System.out.println("<1: 가위, 2:바위, 3:보, 0:종료> : ");

			Random rand = new Random();
			int com = rand.nextInt(3)+1;
			Scanner sc = new Scanner(System.in);
			int user = sc.nextInt();
			
			if(user == 1) {
				switch(com) {
				case 1:
					System.out.println("사용자:가위, 컴퓨터:가위");
					System.out.println("비겼습니다.");
					break;
				case 2:
					System.out.println("사용자:가위, 컴퓨터:바위");
					System.out.println("졌습니다.");
					break;
				case 3:
					System.out.println("사용자:가위, 컴퓨터:보");
					System.out.println("이겼습니다.");
					break;
				}
				continue;
			} else if(user == 2) {
				switch(com) {
				case 1:
					System.out.println("사용자:바위, 컴퓨터:가위");
					System.out.println("이겼습니다.");
					break;
				case 2:
					System.out.println("사용자:바위, 컴퓨터:바위");
					System.out.println("비겼습니다.");
					break;
				case 3:
					System.out.println("사용자:바위, 컴퓨터:보");
					System.out.println("졌습니다.");
					break;
				}
				continue;
			} else if (user == 3) {
				switch(com) {
				case 1:
					System.out.println("사용자:보, 컴퓨터:가위");
					System.out.println("졌습니다.");
					break;
				case 2:
					System.out.println("사용자:보, 컴퓨터:바위");
					System.out.println("졌습니다.");
					break;
				case 3:
					System.out.println("사용자:보, 컴퓨터:보");
					System.out.println("비겼습니다.");
					break;
				}
				continue;
			} else if (user == 0) {
				System.out.println("*** 게임을 종료합니다. ***");
				sc.close();
				break;
			} else {
				System.out.println(" 재입력 ");		
				continue;
			} 			
		}
	}
}
