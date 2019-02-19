interface Printable1 {
	void print();
}

class Papers1 {
	private String con;
	
	public Papers1(String s) {
		con = s;
	}
	
	public Printable1 getPrinter() {
		return new Printer1();
	}
	
	private class Printer1 implements Printable1 {
		public void print() {
			System.out.println(con);
		}
	}
}

class B2_UseMemberInner {

	public static void main(String[] args) {
		Papers1 p = new Papers1("서류 내용: 행복합니다.");
		Printable1 prn = p.getPrinter();
		prn.print();			
	}

}

// 멤버 클래스는 클래스의 정의를 감추어야 할 떄 유용하게 사용이 된다.
// 다른 클래스와 연관되어 사용되지 않고 나만 사용할 떄
// 관리를 편하게 하기 위애(하나의 소스 파일로 관리) 사용이 된다.
