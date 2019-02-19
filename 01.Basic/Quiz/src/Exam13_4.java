import java.util.Scanner;

public class Exam13_4 {

	public static void main(String[] args) {
		System.out.println("10개의 정수를 입력하시오.");
		Scanner sc = new Scanner(System.in);
		int[]arr = new int[10];
		int[]a2 = new int[10];
		
		int n1=0;
		int n2=arr.length - 1;
		
		for(int i=0; i<arr.length; i++){
			arr[i] = sc.nextInt();
			
			if(arr[i]%2 != 0) {
				a2[n1] = arr[i];
				n1 = n1 + 1;
			}else {
				a2[n2] = arr[i];
				n2 = n2 - 1;
			}		
		}
		System.out.print("결과 : ");
		for(int i=0; i<arr.length; i++) {
			System.out.printf(a2[i] + " ");
		}
		sc.close();
	}
}
