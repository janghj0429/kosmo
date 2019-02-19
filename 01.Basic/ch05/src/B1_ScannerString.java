import java.util.Scanner;

class B1_ScannerString {

	public static void main(String[] args) {
		
		String source = "1 13 56";   // source는 space, tap, enter로 구분지어줘야 함
		Scanner sc = new Scanner(source);  //Ctr + Shift + o 누르면  import java.util.Scanner; 자동 입력
		
		int num1 = sc.nextInt();
		int num2 = sc.nextInt();
		int num3 = sc.nextInt();
		
		int sum = num1 + num2 + num3;
		System.out.printf("%d + %d + %d = %d \n", num1, num2, num3, sum);
		
		sc.close();		

	}

}

// Scaaner 클래스의 인스턴트 생성은 데이터를 뽑아 올 대상과의 연결을 의미.
// 연결 후에는 데이터 스캔 가능!
