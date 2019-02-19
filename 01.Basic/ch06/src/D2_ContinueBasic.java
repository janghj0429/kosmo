
public class D2_ContinueBasic {

	public static void main(String[] args) {
		
		int num = 0;
		int count = 0;
		
		while((num++) < 100) {
// not or 문
//			if( ((num % 5) != 0) || ((num % 7) != 0) ) {
//				continue;      // 5와 7의 배수가 아니라면 나머지 건너띄고 위로 이동
//			}

// if 두개
//			if( (num % 5) != 0 )
//				continue;
//			if( (num % 7) != 0 )
//				continue;

// and 문
			if( ((num % 5) == 0) && ((num % 7) == 0) ) {
				count++;                  // 5와 7의 배수인 경우에만 실행
				System.out.println(num);  // 5와 7의 배수인 경우에만 실행
			}
		}
		
		System.out.println("count: " + count);
		
	}

}
