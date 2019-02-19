
public class E2_ByTimes {

	public static void main(String[] args) {
		System.out.println("------------------");
		
		for(int i = 1; i < 10; i++) {
			System.out.println();
			for(int j = 2; j < 10; j++) {
				System.out.printf("[%2dx%2d = %2d]", j, i, ( i* j));
			}
		}
		System.out.println("\n\n"+"------------------");
	}

}
