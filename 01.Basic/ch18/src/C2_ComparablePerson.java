import java.util.Set;
import java.util.TreeSet;

class Person implements Comparable<Person> {
	private String name;
	private int age;
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	@Override
	public String toString() {
		return name + " : " + age;
	}
	
	@Override
	public int compareTo(Person p) {
		//검색조건을 필요에 따라 자유롭게 변경할 수 없어 불편하다.
		//조건 변경시 재 컴파일 필요
//		return this.age - p.age;
//		return this.name.compareTo(p.name);
//		return p.name.compareTO(this.name);
		
		return p.age - this.age;
	}
}

class C2_ComparablePerson {

	public static void main(String[] args) {
		Set<Person> tree = new TreeSet<>();
		tree.add(new Person("YOON", 37));
		tree.add(new Person("HONG", 53));
		tree.add(new Person("PARK", 22));
		
		for(Person p : tree)
			System.out.println(p);

	}

}
