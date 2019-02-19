import java.util.Scanner;

//class Grade {
//	
//	int[][] Grade(int[][]arr){
//		for(int i=0; i<4; i++) {
//			System.out.println("국어,영어,수학,국사 점수를 입력하시오.");
//			for(int j=0; j<4; j++) {
//			} return arr;
//	}
//	
//}



public class Exam13_8 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int[][]arr = new int[5][5];
		int sum1 = 0;
		int sum2 = 0;
		
		//각 인원당 점수 입력
		for(int i=0; i<4; i++) {
			System.out.println("국어,영어,수학,국사 점수를 입력하시오.");
			for(int j=0; j<4; j++) {
				arr[j][i] = sc.nextInt();			
				}
		}
		//개인총점
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				sum1 = sum1 + arr[j][i];
				if(j==4) {
					arr[j][i] = sum1;
				}
			} sum1 = 0;
		}
		//과목 총점
		for(int i=0; i<4; i++) {
			for(int j=0; j<5; j++) {
				sum2 = sum2 + arr[i][j];
				if(j==4) {
					arr[i][j] = sum2;
				}
			} sum2 = 0;
		}
		//입력 점수, 각 총점 출력
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				System.out.print(arr[i][j] + "\t");
			}System.out.println();
		}
		
		//성적표 출력
		System.out.print("구분  이순신  강감찬  을지문덕  권율  총점 \n");
		for(int i=0; i<5; i++) {
			if(i==0) System.out.print("국어 \t");
			if(i==1) System.out.print("영어 \t");
			if(i==2) System.out.print("수학 \t");
			if(i==3) System.out.print("국사 \t");
			if(i==4) System.out.print("총점 \t");
			for(int j=0; j<5; j++) {
				System.out.print(arr[i][j] + "\t");
			}System.out.println();
		}
	}
}


