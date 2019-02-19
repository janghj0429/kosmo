interface Animal1 {
	int DOG = 1;
	int CAT = 2;
}

interface Person1 {
	int MAN = 1;
	int WOMAN = 2;
}

class A2_NonSafeConst {

	public static void main(String[] args) {
		// 정상적인 메소드 호출
		who(Person1.MAN);
		
		// 비정상적인 메소드 호출
		who(Animal1.DOG);
	}
	
	public static void who(int man) {
		switch(man) {
		case Person1.MAN:
			System.out.println("남성 손님입니다.");
			break;
		case Person1.WOMAN:
			System.out.println("여성 손님입니다.");
			break;
		}

	}

}
