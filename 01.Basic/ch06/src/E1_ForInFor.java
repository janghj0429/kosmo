
public class E1_ForInFor {

	public static void main(String[] args) {
		int mul = 0; 
		
		for(int i = 2; i < 10; i++) {
						
			for(int j = 1; j < 10; j++) {
				mul = i * j;
				System.out.printf("[%d x %d = %d] | ", j, i, mul);
				
			}
			System.out.print("\n");
		}		
	
		System.out.print("------------------------------------------");
		
	}

}
