package Game;

import java.util.Random;
import java.util.Scanner;



public class NumberBaseball {

	public static void main(String[] args) {
		int X1, X2, X3;
		int Y1, Y2, Y3;
		int Z;
		int sum;
		while (true) {
			Random ran = new Random();
			X1 = ran.nextInt(10);
			X2 = ran.nextInt(10);
			X3 = ran.nextInt(10);
			if ( X1!=X2 && X2!=X3 && X3!=X1 ) {
				sum = X1*100 + X2*10 + X3;
				System.out.printf("컴 %d :", sum );
				break;}
		}
		
		System.out.println("숫자로 하는 야구게임시작");
		Scanner sc = new Scanner(System.in);
		int n = 1;
		while (true) {
			
			System.out.printf("세자리 숫자를 입력하세요. (%d 회)\n", n);
			
			
			Z = sc.nextInt();
			Y1 = Z / 100;
			int num = Z - Y1 * 100;
			Y2 = num / 10;
			Y3 = num % 10;	
			if(Y1 > 9) {
				System.out.println("재입력(3자리숫자)");
				
				continue;
			} else {
				System.out.printf("%d:%d:%d ", Y1, Y2, Y3);
			}
			
			System.out.println();
				
			int s = 0;
			int b = 0;

			if (X1==Y1) {s=s+1;}
			if (X2==Y2) {s=s+1;}
			if (X3==Y3)	{s=s+1;} //Strike , 스트라이크 총3 경우

			if (X1==Y2 || X1==Y3) {b=b+1;}
			if (X2==Y1 || X2==Y3) {b=b+1;}
			if (X3==Y1 || X3==Y2) {b=b+1;} //Ball
				
			if (X1 != Y1 && X1 !=Y2 && X1 != Y3 &&
				X2 != Y1 && X2 !=Y2 && X2 != Y3 &&
				X3 != Y1 && X3 !=Y2 && X3 != Y3) {System.out.println("OUT");} //out
			
			System.out.printf("%d Strike 	%d Ball \n",s,b);
			if (s==3) {
				System.out.println("you win");
				break;
			}
			n++;
		}
		sc.close();
	}

}
