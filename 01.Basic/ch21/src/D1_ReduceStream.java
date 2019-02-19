import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

class D1_ReduceStream {

	public static void main(String[] args) {
		List<String>ls = Arrays.asList("Box", "Simple", "Complex", "ㅁㅁㅁㅁㅁㅁㅁㅁ");
		
		BinaryOperator<String>lc = 
				(s1, s2) -> {
					if(s1.length() > s2.length())
						return s1;
					else
						return s2;
				};
				
		String str = ls.stream()
					.reduce("", lc); //임의의 연산 각 요소마다 수행, 기존 반복문으로 대체가능
				
		System.out.println(str);

	}

}
