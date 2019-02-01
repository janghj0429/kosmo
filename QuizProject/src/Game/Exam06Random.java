package Game;
import java.util.Random;

public class Exam06Random {

	public static void main(String[] args) {
		
		while(true) {
			Random randomV1 = new Random();
			int a = randomV1.nextInt(9)+1;
			int b = randomV1.nextInt(10);
			int c = randomV1.nextInt(10);
			
			if(a == b) {
				continue;
			}else if(b == c) {
				continue;
			}else if(c == a) {
				continue;
			}else {			
				System.out.printf("랜덤한 세자리 수 : %d%d%d ", a,b,c);
				break;
			}
		}
	}
}

