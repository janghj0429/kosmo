

public class MyExam {

	public static void main(String[] args) {
		// 파라미터 없는 메서드
		Hello();
		
		// 파라미터 있고 반환이 없는 메서드
		MyAdd(1, 2);
		
		// 파라미터 없고 반환이 있는 메서드
		String temper = Temper();
		System.out.println(temper);
		System.out.println(Temper());
		
		// 파라미터가 있고 반환이 있는 메서드
		int sum = TowNumAdd(1, 2);
		System.out.println("합은 " + sum);

	}
	public static int TowNumAdd(int num1, int num2) {   //파라미터 있음
		return num1 + num2;		
	}
	public static String Temper() { // 파라미터 없음
		return "안녕";	
	}	
	public static void MyAdd(int num1, int num2) {  // 파라미터 있음
		int nResult = num1 + num2;
		System.out.println(nResult);
	}
	public static void Hello() {      // 파라미터 없음
		System.out.println("Hello");
	}
	
}
