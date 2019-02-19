interface ColorPrintable extends Printable{
	void printCMYK(String doc);
}

class PrnColorDrvSamsung implements ColorPrintable{
	public void print(String doc) {
		System.out.println(doc + "\nFrom Samsung(2) : Black-White ver");
	}
	public void printCMYK(String doc) {
		System.out.println(doc + "\nFrom Samsung(2) : Color ver");
	}
}

class PrnColorDrvEpson implements ColorPrintable{
	public void print(String doc) {
		System.out.println(doc + "\nFrom Epson(2) : Black-White ver");
	}
	public void printCMYK(String doc) {
		System.out.println(doc + "\nFrom Epson(2) : Color ver");
	}
}

public class Polymorphism2_2 {

	public static void main(String[] args) {
		String doc = "프린터로 출력을 합니다.";
				
		Printable prn1 = new PrnDrvSamsung();
		prn1.print(doc);
		
		ColorPrintable prn2 = new PrnColorDrvSamsung();
		prn2.print(doc);
		prn2.printCMYK(doc);
		
		ColorPrintable prn3 = new PrnColorDrvEpson();
		prn3.print(doc);
		prn3.printCMYK(doc);		
	}

}
