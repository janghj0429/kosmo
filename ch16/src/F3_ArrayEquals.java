import java.util.Arrays;

class Num1{
	private int t;
	
	public Num1(int r) {
		this.t = r;
	}
}

class F3_ArrayEquals {

	public static void main(String[] args) {
		int[]ar1 = {1,2,3,4,5};
		int[]ar2 = Arrays.copyOf(ar1, ar1.length); 
								//복사대상 복사할 길이
		
		System.out.println(Arrays.equals(ar1, ar2));
	//ar1, ar2 주소 다름 , 값 동일
	//각 배열의 주소는 다르나 들어가 있는 값은 같고
	//그 값은 주소는 없음.
		
		int a = 10;
		Num1 b = new Num1(5);
		Num1[]c = new Num1[2];
		c[0]=new Num1(2);
		c[1]=new Num1(3);
		
	}
}
