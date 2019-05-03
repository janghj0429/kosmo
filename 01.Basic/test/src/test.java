import java.util.Scanner;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[][] arr = new String[3][5];
		
		//배열을 구조내용대로 출력
		for(int i=0; i<3; i++) {
			for(int j=0; j<5; j++) {
				arr[i][j] = " ";
				if(i==0 && j==2) {
					arr[i][j] = "★";
				}
				if(i==1) {
					if(j==1||j==2||j==3) {
						arr[i][j] = "★";
					}
				}
				if(i==2) {
					arr[i][j] = "★";
				}
				
			}
		}
		
		for(int i=0;i<3;i++) {
			for(int j=0;j<5;j++) {
				System.out.print(arr[i][j]);
				if(j==4) {
					System.out.println();
				}
			}
		}
	}
}
