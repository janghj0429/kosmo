// 키워드 this 사용하기
// 클래스의 은닉화 :
// - 변수에 직접적인 접근을 막는다.
// - getter, setter 를 통ㅎ애 변수에 접근한다.
class SimpleBox{
	private int num;
	
	SimpleBox(int num){
		this.num = num;
	}

	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num + 1;
	}
}

public class ThisUseEx {

	public static void main(String[] args) {
		SimpleBox box = new SimpleBox(5);
		box.setNum(10);
		System.out.println(box.getNum());
	}

}
