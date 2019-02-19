interface PrintableA {
	void print(String s);
}

class PrinterA implements PrintableA {
	public void print(String s) {
		System.out.println(s);
	}
}
class C1_Lambda1 {

	public static void main(String[] args) {
		PrintableA prn = new PrinterA();
		prn.print("What is Lamda?");

	}

}
