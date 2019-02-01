package Game;
import java.util.Random;

public class RandomUse {

	public static void main(String[] args) {
		
		//Type #1
		Random randomV1 = new Random();
		
		System.out.println(randomV1.nextBoolean());
		System.out.println(randomV1.nextFloat());
		System.out.println(randomV1.nextInt());
		System.out.println(randomV1.nextInt(100)); // range : 0 ~ 99
	//	System.out.println(randomV1.nextInt(100) + 1 ); 1~100값
		
		//Type #2 : 1 ~ 100 사이의 값 출력
		double randomV2 = Math.random();
		System.out.println("1 : " + randomV2);
		int intVal = (int)(randomV2 * 100) + 1;
		System.out.println("2 : " + intVal);
		
		// (int)( Math.random() * (최대값 - 최소값 + 1) ) + 최소

	}

}
