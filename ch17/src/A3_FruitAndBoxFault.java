class Apple3 {
	public String toString() {
		return "I am an apple.";
	}
}

class Orange3 {
	public String toString() {
		return "I am an orange.";
	}
}

class Box3 {
	private Object ob;
	
	public void set(Object o) {
		ob = o;
	}
	
	public Object get() {
		return ob;
	}
}
public class A3_FruitAndBoxFault {		// 프로그래머의 실수가 컴파일 과정에서 발견X.

	public static void main(String[] args) {
		Box3 aBox = new Box3();
		Box3 oBox = new Box3();
		
		//과일을 박스에 담은 것일까? 사과와 오렌지가 아닌 문자열을 담았다.
		aBox.set("Apple");	//<-- aBox.set(new String("Apple");
		oBox.set("Orange");
		
		//박스에서 과일을 제대로 꺼낼 수 있을까?
		Apple3 ap = (Apple3)aBox.get();
		Orange3 og =(Orange3)oBox.get(); // 스트링과 클래스 오브젝트의 연관성없음 형변환x
		
		System.out.println(ap);
		System.out.println(og);
		

	}

}
