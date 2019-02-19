package Game;

import java.util.Random;
import java.util.Scanner;

public class test {


	public static void main(String[] args) {
		int a,b,c;
		int x,y,z;
		while (true) {
			Random ran = new Random();
			a = ran.nextInt(10);
			b = ran.nextInt(10);
			c = ran.nextInt(10);
			if (a!=b && b!=c && c!=a) {
				String A = String.valueOf(a);
				String B = String.valueOf(b);
				String C = String.valueOf(c);
				String com = A+B+C;
				break;}
		}
		
		System.out.println("숫자로 하는 야구게임시작");
		Scanner sc = new Scanner(System.in);
		int n = 1;
		while (true) {			
			System.out.printf("세자리 숫자를 입력하세요. (%d 회)\n", n);
			String DEF = sc.nextLine();
			String st1 = DEF.substring(0, 1);
			String st2 = DEF.substring(1, 2);
			String st3 = DEF.substring(3);
			System.out.println(DEF);
			System.out.println(st1 +":"+ st2 +":" + st3);

			
//			if (abc == xyz) {
				
			}
	}
}


