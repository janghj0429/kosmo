
public class A1_IEBasic {

	public static void main(String[] args) {
		
		int n1 = 5;
		int n2 = 7;
		
		if(n1 < n2) {
			System.out.println("n1 < n2 is true");
			System.out.printf("%d 는 %d 보다 작습니다. \n", n1, n2);
		}
		
		if (n1 == n2) {
			System.out.println("n1 == n2 is ture");
			System.out.printf("%d == %d 는 동일합니다. \n", n1, n2);
		}
		else {
			System.out.println("n1 == n2 is false");
			System.out.printf("%d == %d 는 동일하지 않습니다. \n", n1, n2);
		}
	}

}
