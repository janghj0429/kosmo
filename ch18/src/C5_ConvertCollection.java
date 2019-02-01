import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

class C5_ConvertCollection {

	public static void main(String[] args) {
		List<String> lst = Arrays.asList("Toy", "Box", "Box", "Toy"); //중복허용 List
		ArrayList<String> list = new ArrayList<>(lst);
		
		for(String s : list)
			System.out.print(s.toString() + '\t');
		System.out.println();
		
		HashSet<String> set = new HashSet<>(list);  //중복을 허용않는 set
		list = new ArrayList<>(set);
		
		for(String s : list)
			System.out.print(s.toString() + '\t');
		System.out.println();
	}

}
