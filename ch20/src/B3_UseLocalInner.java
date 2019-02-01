

interface Printable2 {
	void print();
}

class Papers2 {
	private String con;
	public Papers2(String s) { con = s; }
	
	public Printable2 getPrinter() {
		class Printer2 implements Printable2 {
			public void print() {
				System.out.println(con);
			}
		}
	//	Printable2 prn = new Printer2(); , return prn;
		return new Printer2();
	}
}

class B3_UseLocalInner {

	public static void main(String[] args) {
		Papers2 p = new Papers2("서류 내용: 행복합니다.");
		Printable2 prn = p.getPrinter();
		prn.print();
	}

}
