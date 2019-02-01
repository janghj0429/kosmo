
public class Exam13_10 {

	public static void main(String[] args) {
		int[][]arr = new int[4][4];
		int num = 1;
		// 기본
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				arr[i][j] = num;
				num = num + 1;
				System.out.print(arr[i][j]+"\t");
			}			
			System.out.println();
		}
		
		//90
		System.out.println();
		num = 1;		
		for(int i=3; i>=0; i--) {
			for(int j=0; j<4; j++) {
				arr[j][i] = num;
				num = num + 1;
			}		
		}
		printArr(arr);
		
		//180
		System.out.println();
		num = 1;
		for(int i=3; i>=0; i--) {
			for(int j=3; j>=0; j--) {
				arr[i][j] = num;
				num = num + 1;
			}		
		}		
		printArr(arr);
		
		//270
		System.out.println();
		num = 1;
		for(int i=0; i<4; i++) {
			for(int j=3; j>=0; j--) {
				arr[j][i] = num;
				num = num + 1;
			}		
		}
		printArr(arr);
	}
		
	public static void printArr(int[][]arr1) {
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				System.out.print(arr1[i][j]+"\t");
			}System.out.println();
		}System.out.println();
	}
		
}