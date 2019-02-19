import javax.swing.Box;

class BoxE<T>{
	private T ob;
	
	public void set(T o) {
		ob = o;
	}
	public T get() {
		return ob;
	}
}

class BoxFactoryE {
	public static <T extends Number> BoxE<T> makeBox(T o) {
		BoxE<T> box = new BoxE<T>();
		box.set(o);
		
		System.out.println("Boxed data: " + o.intValue());
		return box;
	}
}

class UnboxerE {
	public static <T extends Number> T openBox(BoxE<T> box) {
		System.out.println("Unboxed data: " + box.get().intValue());
		return box.get();
	}
}

class C3_BoundedGenericMethod {

	public static void main(String[] args) {
		BoxE<Integer> sBox = BoxFactoryE.makeBox(new Integer(5959));
		
		int n = UnboxerE.openBox(sBox);
		System.out.println("Returned data: " + n);

	}

}
