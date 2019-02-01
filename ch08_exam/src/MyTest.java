class Calculator
{
	int num1;
	int num2;
	int result;
	
	Calculator(){
		//디폴트 생성자:사용자가 만들지 않아도 컴파일러가 자동으로만든다.
		//클래스이름과같다.
		//리턴형 타입이 없다.
		//파라미터(매개변수)도 없고 동작도 없는 생성자
	}
	
	int Add(int num1, int num2) {
		return num1 + num2;
	}
	
	//함수의 오버로딩. 함수이름은 같고 파라미터 갯수나 종류가 다른것
	double Add(double num1, double num2) {
		return num1 + num2;
	}
	
	int Minus(int num1, int num2) {
		return num1 - num2;
	}
}
public class MyTest {

	public static void main(String[] args) {
		// 기능이 필요하면 기능을 하는 클래스를 통해 객체를 만든다.
		// 객체 지향 언어 : 메서드를 만드는 것이 아니다.
		
		// 클래스 타입의 변수는 new 를 통해 객체를 생성한다.
		// 클래스 변수는 스택에, 생성된 객체는 힙에 적재된다.
		Calculator calc1 = new Calculator();
		int num = calc1.Add(1, 2);
	
	}

}
