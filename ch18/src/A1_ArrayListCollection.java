import java.util.ArrayList;
import java.util.List;

public class A1_ArrayListCollection {

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		
		//인스턴스의 저장: 순서 있음. 중복 허용
		list.add("Toy");
		list.add("Box");
		list.add("Box");
		list.add("Robot");
		
		//인스턴스 참조
		for(int i=0; i < list.size(); i++)
			System.out.print(list.get(i) + '\t');
		System.out.println();
		
		//첫 번재 인스턴스 삭제
		list.remove(0);
		
		//삭제 후 인스턴스 참조
		for(int i=0; i < list.size(); i++)
			System.out.print(list.get(i) + '\t');
		System.out.println();
	}

}

// ArrayList<E> 클래스
//
// 배열 기반 자료구조 이지만 공간의 확보 및 확장은
// 리스트 처럼 ArrayList 인스턴스가 스스로 처리한다.

