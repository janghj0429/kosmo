class Apple2 {
	public String toString() {
		return "I am an apple.";
	}
}

class Orange2 {
	public String toString() {
		return "I am an orange.";
	}
}

class Box2 {
	private Object ob;
	
	public void set(Object o) {
		ob = o;
	}
	
	public Object get() {
		return ob;
	}
}

class A2_FruitAndBox2 {

	public static void main(String[] args) {
		Box2 aBox = new Box2();
		Box2 oBox = new Box2();
		
		//과일을 박스에 담는다.
		aBox.set(new Apple2());
		oBox.set(new Orange2());
		
		//박스에서 과일을 꺼는다.
		Apple2 ap = (Apple2)aBox.get();
		Orange2 og =(Orange2)oBox.get();
		
		System.out.println(ap);
		System.out.println(og);
		
	}

}
