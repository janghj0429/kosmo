import java.util.Arrays;

class Person2 implements Comparable{
	private String name;
	private int age;
	
	public Person2(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	@Override
	public int compareTo(Object o) {
		Person2 p = (Person2) o;
		return this.age - p.age;
	}
	
	@Override
	public String toString() {
		return name + ": " + age;
	}
}
public class F9_ArrayObjectSearch {

	public static void main(String[] args) {
		Person2[]ar = new Person2[3];
		
		ar[0] = new Person2("홍길동", 29);
		ar[1] = new Person2("전우치", 15);
		ar[2] = new Person2("손오공", 37);
		
		Arrays.sort(ar);
		
		int idx = Arrays.binarySearch(ar, new Person2("Who are you?", 37));
		System.out.println(ar[idx]);
	}

}
