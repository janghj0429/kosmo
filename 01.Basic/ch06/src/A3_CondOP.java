
public class A3_CondOP {

	public static void main(String[] args) {

		int num1 = 50;
		int num2 = 100;
		
		int big;
		int diff;
		
		big = (num1 > num2) ? num1 : num2;      // 두 숫자를 비교해서 큰수로 대입
		diff = (num1 > num2) ? (num1 - num2) : (num2 - num1);  // 두 숫자를 비교해서 절대값 대입
		
		System.out.println("큰  수: " + big);
		System.out.println("절대값: " + diff);
		
		System.out.println("----if문을 이용하여 출력----");
		
		if (num1 > num2) {
			System.out.println("큰  수: " + num1);
		}else {
			System.out.println("큰  수: " + num2);
		}
		
		if (num1 > num2) {
			System.out.println("절대값: " + (num1 - num2));
		}else {
			System.out.println("절대값: " + (num2 - num1));
		}
		
	}

}
