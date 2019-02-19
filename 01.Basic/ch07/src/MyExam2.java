
public class MyExam2 {

	public static void main(String[] args) {
		
		Add1();
		System.out.println(Add2());
		Add3(1, 2);
		System.out.println(Add4(1, 2));
		
	}
	public static void Add1() {
		System.out.println(1+2);
		
	}
	public static int Add2() {
		return (1+2);		
	}
	public static void Add3(int num1, int num2) {
		System.out.println(num1 + num2);
	}
	public static int Add4(int num1, int num2) {
		return (num1 + num2);
	}

}
