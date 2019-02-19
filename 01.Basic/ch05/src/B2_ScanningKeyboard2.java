import java.util.Scanner;

public class B2_ScanningKeyboard2 {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);  //Ctr + Shift + o 누르면  import java.util.Scanner; 자동 입력
		
		int num1 = sc.nextInt();
		int num2 = sc.nextInt();
		int num3 = sc.nextInt();
		
		int sum = num1 + num2 + num3;
		System.out.printf("%d + %d + %d = %d \n", num1, num2, num3, sum);
		
		sc.close();		
		
	}

}
