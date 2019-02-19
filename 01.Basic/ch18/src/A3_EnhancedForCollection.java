import java.util.LinkedList;
import java.util.List;

public class A3_EnhancedForCollection {

	public static void main(String[] args) {
		List<String> list = new LinkedList<>();
		
		//인스턴스의 저장: 순서 있음. 중복 허용
		list.add("Toy");
		list.add("Box");
		list.add("Robot");
		
		//인스턴스 참조
		for(String s : list)
			System.out.print(s + '\t');
		System.out.println();
		
		//첫 번재 인스턴스 삭제
		list.remove(0);
		
		//삭제 후 인스턴스 참조
		for(String s : list)
			System.out.print(s + '\t');
		System.out.println();
	}

}