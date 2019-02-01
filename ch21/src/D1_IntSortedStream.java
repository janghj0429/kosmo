import java.util.stream.Stream;

class D1_IntSortedStream {

	public static void main(String[] args) {
		Stream.of("Box", "Apple", "Robot")
			.sorted() // 
			.forEach(s -> System.out.print(s + '\t'));
		System.out.println();
		
		Stream.of("Box", "Apple", "Rabbit")
//			.sorted((s1, s2) -> s1.length() - s2.length()) // 길이
			.sorted((s1, s2) -> s2.compareTo(s1)) // 내림
			
			.forEach(s -> System.out.print(s + '\t'));
		System.out.println();

	}

}
