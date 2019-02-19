enum Person5 {
	MAN, WOMAN;
	
	private Person5() {
		System.out.println("Person constructor called");
	}
	
	@Override
	public String toString() {
		return "I am a cat person";
	}
}

class B3_EnumConstructor {

	public static void main(String[] args) {
		System.out.println(Person5.MAN);
		//System.out.println(Person5.WOMAN);

	}

}

// 열거형의 정의 에도 생성자가 없으면 디폴트 생성자가 삽입된다.
// 다만 이 생성자는 private으로 선언이되어 직접 인스턴스를 생성하는 것이
// 불가능하다.