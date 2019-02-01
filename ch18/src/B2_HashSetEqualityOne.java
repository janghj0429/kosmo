import java.util.HashSet;

class Num1 {
	private int num;
	
	public Num1(int n) {
		num = n;
	}
	
	@Override
	public String toString() {
		return String.valueOf(num);
	}
}

class B2_HashSetEqualityOne {

	public static void main(String[] args) {
		HashSet<Num1> set = new HashSet<>();
		set.add(new Num1(7799));
		set.add(new Num1(9955));
		set.add(new Num1(7799));
		
		System.out.println("인스턴스 수: " + set.size());
		
		for(Num1 n : set)
			System.out.print(n.toString() + '\t');
		
		System.out.println();

	}

}
