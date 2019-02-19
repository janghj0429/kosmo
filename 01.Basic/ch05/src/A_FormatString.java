
public class A_FormatString {

	public static void main(String[] args) {
		
		int age = 20;
		double height = 178.2;
		String name = "Hong Gil Dong";
		
		System.out.println(" name: "+name+"\n");
		
		System.out.printf(" name: %s \n", name);
		System.out.printf(" age: %d \n height: %e \n", age, height);
		
		System.out.printf(" %d - %o - %x \n\n", 77, 77, 77);  // 정수 - 8진수 - 16진수
		System.out.printf(" %g \n", 0.00014);   // float로 표기
		System.out.printf(" %g \n", 0.000014);  // e표기법
		
	}

}
