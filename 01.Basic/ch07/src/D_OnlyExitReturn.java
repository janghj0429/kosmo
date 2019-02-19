
public class D_OnlyExitReturn {

	public static void main(String[] args) {
		divide(4, 2);
		divide(6, 2);
		divide(0, 1);
		divide(0, 9);
	}
	
	// void 이기 때문에 0일 때 return으로 메서드가 종료
	public static void divide(int num1, int num2) {     
//		if(num2 == 0) {
//			System.out.println("0으로 나눌 수 없습니다.");
//			return;
//		}
		System.out.println("나눗셈 결과: " + (num1 / num2));
	}
}
