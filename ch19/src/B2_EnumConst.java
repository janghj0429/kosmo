enum Person4 {
	MAN, WOMAN;
	
	@Override
	public String toString() {
		return "I am a cat person2";
	}
}

class B2_EnumConst {

	public static void main(String[] args) {
		System.out.println(Person4.MAN);
		System.out.println(Person4.WOMAN);   //toString 메서드의 반환 값 출력
	}

}

//앞의 예제와 비교해 보면 class가 enum 으로 변경되었을 뿐 같은결과나옴.
//'열거형 값' 이 해당 자료형의 인스턴스임.
//
//모든 열거형은 java.land.Enum<E> 클래스를 상속한다.
//그리고 Enum<E>는 Object 클래스를 상속한다.
//이런 측면에서 볼 때 열거형은 클래스이다.