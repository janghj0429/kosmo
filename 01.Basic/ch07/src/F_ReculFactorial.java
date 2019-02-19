// 재귀 호출 대표예시 : 팩토리얼!
// n! = n x (n - 1)!

public class F_ReculFactorial {

	public static void main(String[] args) {
		System.out.println("3 factorial: " + factorial(3));
		System.out.println("12 factorial: " + factorial(12));
		
	}
	
	public static int factorial(int n) {
		// System.out.print(n); // 중간과정
		if(n == 1) {
			// System.out.print(" = ");  // 중간과정
			return 1;
			}
		else {
			// System.out.print(" * ");   // 중간과정
			return n*factorial(n-1);
			}
	}

}