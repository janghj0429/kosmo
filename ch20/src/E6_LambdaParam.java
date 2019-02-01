@FunctionalInterface
interface PrintableE {
	void print(String s);
}

class E6_LambdaParam {
	
	public static void ShowString(PrintableE p, String s) {
		p.print(s);
	}

	public static void main(String[] args) {
		ShowString( (s) -> { System.out.println(s); } , "What is Lambda?");

	}

}
