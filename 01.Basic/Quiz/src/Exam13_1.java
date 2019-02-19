import java.util.Scanner;

class MaxMinSum{
	
	int MAX(int[] x) {
		int max = x[0];
		for(int i=0;i<5;i++) {
			if(max < x[i]) { max = x[i]; }
		} return max;
	}
	
	int MIN(int[]y) {
		int min = y[0];
		for(int i=0;i<5;i++) {
			if(min > y[i]) { min = y[i]; }		
		}return min;
	}
	
	int sumOfArray(int[]z) {
		int sum = 0;
		for(int i=0; i<z.length; i++) {	sum = sum + z[i];	}
		return sum;
	}
	
}


public class Exam13_1 {

	public static void main(String[] args) {
		
		System.out.println("5개의 숫자를 입력하시오.");
		
		Scanner sc = new Scanner(System.in);
		int[] ar = new int[5];
		ar[0] = sc.nextInt();
		ar[1] = sc.nextInt();
		ar[2] = sc.nextInt();
		ar[3] = sc.nextInt();
		ar[4] = sc.nextInt();	
		
		MaxMinSum a = new MaxMinSum();
		int maxRtn = a.MAX(ar);
		System.out.println("최대값 : " + maxRtn);
	
		int minRtn = a.MIN(ar);
		System.out.println("최소값 : " + minRtn);
		
		int sum = a.sumOfArray(ar);
		System.out.println("모든 수 의 합 : " + sum);
		sc.close();
	}
}

		
		
		
//		int maxRtn = a(ar);
//		System.out.println("최대값 : " + maxRtn);
//	
//		int minRtn = b(ar);
//		System.out.println("최소값 : " + minRtn);
//		
//		int sum = c(ar);
//		System.out.println("모든 수 의 합 : " + sum);
//		
//		
//		
//	}
//	
//	public static int a(int[] ar) {
//		int max = ar[0];
//		for(int i=0;i<5;i++) {
//			if(max < ar[i]) {
//				max = ar[i];				
//			}
//		} return max;
//	}
//	
//	public static int b(int[] ar) {
//		int min = ar[0];
//		for(int i=0;i<5;i++) {
//			if(min > ar[i]) {
//				min = ar[i];				
//			}		
//		}return min;
//	}
//	
//	public static int c(int[]ar) {
//			int sum = 0;
//			for(int i=0; i<ar.length; i++) {
//				sum = sum + ar[i];
//			}
//			return sum;
//		}
//}
//
//	
