import java.util.Random;

// 인스턴스 생성과 관계없이 static 변수가 메모리
// 공간에 할당될 때 실행이 된다.
public class StaticEx2 {
	static int num;
	
	// static 초기화 블록
	static {
		Random rand = new Random();
		//main 실행전에 이미 랜덤 값이 대입이된다.
		num = rand.nextInt(100);
	}

	public static void main(String[] args) {
		System.out.println(num);

	}

}
