import java.util.Scanner;

public class B2_ScanningKeyboard {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		System.out.print("숫자입력: ");
		long num1 = sc.nextLong();
		System.out.print("숫자입력: ");
		long num2 = sc.nextLong();
		System.out.print("숫자입력: ");
		long num3 = sc.nextLong();
		
		long sum = num1 + num2 + num3;
		System.out.printf("%d + %d + %d = %d \n", num1, num2, num3, sum);
		
		sc.close();		
		
	}

}
