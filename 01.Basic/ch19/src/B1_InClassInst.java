class Person3 {
	public static final Person3 MAN = new Person3();
	public static final Person3 WOMAN = new Person3();
	
	@Override
	public String toString() {
		return "I am a cat person";
	}
}

class B1_InClassInst {

	public static void main(String[] args) {
		System.out.println(Person3.MAN);
		System.out.println(Person3.WOMAN);

	}

}
