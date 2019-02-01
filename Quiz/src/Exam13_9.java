
public class Exam13_9 {

	public static void main(String[] args) {
		int[]arr = {3,5,2,1,4};
	
		int tmp;
		
		for(int i=0; i<5; i++) {		
			for(int j=i; j<5; j++) {
				if(arr[i] > arr[j]) {	// arr[0] 을 arr[1]부터 arr[4]까지 비교
					tmp = arr[j];		// 큰 수를 뒤로	
					arr[j] = arr[i];
					arr[i] = tmp;
				}
			}
		}
		for(int i=0; i<arr.length; i++) {
			System.out.print(arr[i] + " " );
		}
	}
}
