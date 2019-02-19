class Person{
	String name;
	
	public Person(String name) {
		this.name = name;
	}
	
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("destroyer: " + name);
	}
}
class A_ObjectFinalize {

	public static void main(String[] args) {
		Person p1 = new Person("홍길동");
		Person p2 = new Person("전우치");
		
		p1 = null;  //참조 대상을 가비지 컬렉션의 대상으로 만듬
		p2 = null;
		System.gc();
		System.runFinalization();
		
		System.out.println("end of program");

	}

}
