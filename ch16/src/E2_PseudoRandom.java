import java.util.Random;

class E2_PseudoRandom {

	public static void main(String[] args) {
		Random rand = new Random(12);
		
		for(int i=0; i<7; i++)
			System.out.println(rand.nextInt(10));
	}

} //실행때마다 같은 결과를 보인다.
