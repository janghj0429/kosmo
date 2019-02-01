class B1_StringInst {
	public static void showString(String str) {
		System.out.println(str);
		System.out.println(str.length());
	}

	public static void main(String[] args) {
		// 둘다 String 인스턴스의 생성으로 이어지고
		// 그 결과 인스턴스의 참조 값이 반환된다.
		
		String str1 = new String("Simple String");
		String str2	= "The Best String";
		String str3	= new String("The Best String");	// Debug 모드로 id 살펴보기
		// new 를 해야지만 새로운인스턴스로 생성, new없으면 동일한 값 참조.
		
		System.out.println(str1);
		System.out.println(str1.length());
		
		System.out.println(); // 개행 : 메서드 오버로딩
		
		System.out.println(str2);
		System.out.println(str2.length());
		
		System.out.println();
		
		//showString("Funny String");
		showString(str3);
	}

}
