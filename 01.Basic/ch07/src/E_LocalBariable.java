
public class E_LocalBariable {
	
	static int num1 = 11;
	
	public static void main(String[] args) {
		
		boolean ste = true;
		int num1 = 10;
		
		if(ste) {
			//int num1 = 22;          // 바깥 영역에서 선언한 변수는 동일한 변수로 선언 불가
			num1++;
			System.out.println(num1);			
		}
		
		{
			int num2 = 33;
			num2++;
			System.out.println(num2);
		}
		
		int num2 = 221;                // {} 내부에서 선언한 변수를 사용하지 않게 때문에 {} 밖에서 선언가능
		System.out.println(num2);	   // 바깥 영역에서는 {} 내부 선언 변수 불러오기 불가	
	}

}
