
public class Exam13_7 {

	public static void main(String[] args) {
		int[][]arr1 = { {1,2,3,4}, {5,6,7,8} };
		int[][]arr2 = new int[4][2];
		for(int i=0; i<2; i++) {
			for(int j=0; j<4; j++) {				
				arr2[j][i] = arr1[i][j];
			//	System.out.print(arr2[j][i]);			
			}
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				System.out.print(arr2[i][j] + "\t");
			}System.out.println();
		}

	}

}
