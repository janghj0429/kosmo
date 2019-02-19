import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class A4_IteratorCollection {

	public static void main(String[] args) {
		List<String> list = new LinkedList<>();
				
		//인스턴스의 저장: 순서 있음. 중복 허용
		list.add("Toy");
		list.add("Box");
		list.add("Robot");
		list.add("Box");
		
		Iterator<String> itr = list.iterator(); // 반복자 획득

		//반복자를 이용한 순차적 참조
		while(itr.hasNext())
			System.out.print(itr.next() + '\t');
		
		System.out.println();
		
		itr = list.iterator(); // 반복자 다시 획득
		
		//Box 인스턴스 삭제
		String str;
		while(itr.hasNext()) {
			str = itr.next();
			
			if(str.equals("Box"))
				itr.remove();
		}
		
		itr = list.iterator(); // 반복자 다시 획득
		
		
		//삭제 후 인스턴스 참조
		while(itr.hasNext())
			System.out.print(itr.next() + '\t');
		
		System.out.println();
	}

}