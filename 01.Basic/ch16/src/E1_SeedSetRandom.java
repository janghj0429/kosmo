import java.util.Random;

class E1_SeedSetRandom {

	public static void main(String[] args) {
		Random rand = new Random(System.currentTimeMillis());
		
		for(int i=0; i<7; i++)
			System.out.println(rand.nextInt(10));
	}
}

//실행 때마다 다른 결과를 보인다.