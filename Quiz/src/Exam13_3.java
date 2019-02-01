import java.util.Scanner;



public class Exam13_3 {

	public static void main(String[] args) {
		
		System.out.println("10개의 정수를 입력하시오.");
		int[]ar = new int[10];
		Scanner sc = new Scanner(System.in);
		for(int j=0;j<10;j++) {
			ar[j] = sc.nextInt();
		}
		int[]x=new int[5];
		int[]y=new int[5];
		
		int n1=0;
		int n2=0;
		
		for(int i=0;i<10;i++) {	

			if(ar[i]%2!=0) {
				x[n1] = ar[i];
				n1 = n1 + 1;
			}
			else {										//짝수
				y[n2] = ar[i];
				n2 = n2 + 1;
			}
		}
		System.out.printf("홀수: %d, %d, %d, %d, %d\n", x[0],x[1],x[2],x[3],x[4]);
		System.out.printf("짝수: %d, %d, %d, %d, %d", y[0],y[1],y[2],y[3],y[4]);
		sc.close();
	}
}

