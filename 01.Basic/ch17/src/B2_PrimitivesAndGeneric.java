class Box8<T> {
	private T ob;
	
	public void set(T o) {
		ob = o;
	}
	public T get() {
		return ob;
	}
}

class B2_PrimitivesAndGeneric {

	public static void main(String[] args) {
		Box8<Integer> iBox = new Box8<Integer>();
		
		iBox.set(123);			//오토박싱진행     
		int num = iBox.get();	//오토 언박싱 진행
		
		System.out.println(num);

	}

}

//Box<int> box = new Box<int>();
//타입 인자로 기본 자료형이 올 수 없으므로 컴파일 오류 발생

//기본 자료형인 123이 Integer로 오브젝트로 박싱
//참조되는 객체였던123이 다시 기본자료형으로 언박싱