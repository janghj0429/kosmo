class Box9<T> {
	private T ob;
	
	public void set(T o) {
		ob = o;
	}
	public T get() {
		return ob;
	}
}

class B3_BoxInBox {

	public static void main(String[] args) {
		Box9<String> sBox = new Box9<>();
		sBox.set("I am so happy.");
		
		Box9<Box9<String>> wBox = new Box9<>();
		wBox.set(sBox);
		
		Box9<Box9<Box9<String>>> zBox = new Box9<>();
		zBox.set(wBox);
		
		System.out.println(zBox.get().get().get());

	}

}
