package Game;
import java.util.Random;
import java.util.Scanner;

	

public class ExamHighLowGame {

	public static void main(String[] args) {
		
		System.out.println("나는 지금 0부터 100 사이의 값 중에 하나를 생각하겠습니다.");
		System.out.println("당신은 그 숫자를 6회 안에 맞추시면 됩니다..");
		Random rand = new Random();
		int num = rand.nextInt(101);
		Scanner sc = new Scanner(System.in);
		
		for(int i = 5; i >= 0; i--) {

			System.out.print("몇이라고 생각합니까? <0 to 100>	");
			
			int input = sc.nextInt();
			
			if(input == num) {
				System.out.printf("%d 는 정답입니다. 축하합니다! \n", input);
				System.out.println("High / Low 게임을 플레이 해주셔서 감사합니다.");
				System.out.println("다시 하시겠습니까? <y/n>...");				
				String str = sc.next();
				if(!str.equalsIgnoreCase("y")) {
					System.out.println("High / Low 게임을 플레이 해주셔서 감사합니다.");
					break;		
				}else {	
					System.out.println("나는 지금 0부터 100 사이의 값 중에 하나를 생각하겠습니다.");
					System.out.println("당신은 그 숫자를 6회 안에 맞추시면 됩니다..");
					num = rand.nextInt(101);
					i = 6;								
				}				
			}
			if(input < num)	{
				System.out.printf("%d 는 제가 정한 숫자보다 작습니다. \n", input);
				if (i != 0)	{
					System.out.printf("[ %d ]의 기회가 남았습니다. \n", i);
				}else{
					System.out.println("");
				}			
			}
			if(input > num) {
				System.out.printf("%d 는 제가 정한 숫자보다 큽니다. \n", input);
				if (i != 0){
					System.out.printf("[ %d ]의 기회가 남았습니다. \n", i);
				}else{
					System.out.println("");
				}			
			}
			if(i == 0)
			{		
				System.out.println("기회가 없습니다.");
				System.out.println("High / Low 게임을 플레이 해주셔서 감사합니다.");
				System.out.println("다시 하시겠습니까? <y/n>...");				
				String str = sc.next();
				if(!str.equalsIgnoreCase("y")) {
					System.out.println("High / Low 게임을 플레이 해주셔서 감사합니다.");
					break;	
				}else {	
					System.out.println("나는 지금 0부터 100 사이의 값 중에 하나를 생각하겠습니다.");
					System.out.println("당신은 그 숫자를 6회 안에 맞추시면 됩니다..");
					num = rand.nextInt(101);
					i = 6;					
				}				
			}			
		}
	sc.close();
	}
}

