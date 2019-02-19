
import java.util.Random;
import java.util.Scanner;
class MoveExit {
	
	public void move() {
		
	}
	
	
}


public class Exam13_11 {

	public static void main(String[] args) {
		Random ran = new Random();		
		Scanner sc = new Scanner(System.in);
		
		///생성
		int[][]arr = new int[3][3];
		int n = 1;
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				arr[i][j] = n;
				n = n+1;
			}
		}
		printArr(arr);
	
	
	//움직이는 로직
	System.out.println("[ Move ] a:Left s:Right w:Up z:Down");
	System.out.println("[ Exit ] k:Exit");
	System.out.print("이동키를 입력하세요 : ");
	String move = sc.next();
	
	

	
	if(move=="a") {
		
	}
	
	



	
// 1. 움직이는 로직 만든 후 
//	2. 셔플로직
//	3. 컴퓨터가 섞고
//	다음 풀기
	
	
	
	}
	public static void printArr(int[][]arr1) {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				if(arr1[i][j]==9) System.out.print("x");
				else              System.out.print(arr1[i][j]+"\t");				
			}System.out.println();
		}System.out.println();
	}
		

}
