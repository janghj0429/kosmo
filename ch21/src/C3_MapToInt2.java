import java.util.Arrays;
import java.util.List;

class C3_MapToInt2 {

	public static void main(String[] args) {
		List<String>ls = Arrays.asList("Box", "Robot", "Simple");
		
		ls.stream()
			.mapToInt(s -> s.length())
			.forEach(n -> System.out.print(n + "\t"));
		
		System.out.println();

	}

}
