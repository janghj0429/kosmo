import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class F4_CopyList {

	public static void main(String[] args) {
		List<String> src = Arrays.asList("Box", "Apple", "Toy", "Robot");
		
		// 복사본을 만들어서 사용
		List<String> dest = new ArrayList<>(src);
		
		// 정렬하여 그 결과를 출려
		Collections.sort(dest);
		System.out.println(dest);
		
		// 사정상 정렬 이전의 상태로 되돌려야 함
		Collections.copy(dest, src); 
		
		// 되돌림 확인
		System.out.println(dest);

	}

}
