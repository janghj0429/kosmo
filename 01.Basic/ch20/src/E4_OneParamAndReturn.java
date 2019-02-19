interface HowLong {
	int len(String s);
}

class E4_OneParamAndReturn {

	public static void main(String[] args) {
		HowLong hl = s -> s.length();
		//int num = hl.len("I am so happy"); // 이와 같이 연산결과가 남는다.
		System.out.println(hl.len("I am so happy"));

	}

}
