
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class SLenComp implements Comparator<String> {
	@Override
	public int compare(String s1, String s2) {
		return s1.length() - s2.length();
	}
}

class D_SLenComparator {

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("Robot");
		list.add("Lambda");
		list.add("Box");
		
		Collections.sort(list, new SLenComp() ); // 정렬
		
		for(String s : list)
			System.out.println(s);

	}

}