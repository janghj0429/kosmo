interface Calculate4 {
	int cal(int a, int b);
}
class E3_TwoParamAndReturn {

	public static void main(String[] args) {
		Calculate4 c;
		c = (a, b) -> { return a + b; };
		// int num = c.cal(4,3); // 이와 같이 연산 결과가 남는다.
		System.out.println(c.cal(4, 3));
		
		c = (a , b) -> a + b;
		System.out.println(c.cal(4, 3));
	}

}
