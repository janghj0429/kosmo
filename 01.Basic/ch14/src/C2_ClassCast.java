class Board { 
	int a = 5;
}
class PBoard extends Board { 
	int b = 10; // int a=5; 를 이미 상속받음.
}

class C2_ClassCast {

	public static void main(String[] args) {
		Board pbd1 = new PBoard();
		PBoard pbd2 = (PBoard)pbd1;  // OK cast:형변환
		
		System.out.println(".. intermediate location ..");
		Board ebd1 = new Board();
		PBoard ebd2 = (PBoard)ebd1; // EXception 

	}

}
