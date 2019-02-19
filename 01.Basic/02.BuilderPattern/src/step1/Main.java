package step1;

public class Main {

	public static void main(String[] args) {
		
	}
	
	public static Person createPersonForTesting() {
		step1.Person person = new Person();
		person.setFirstName("FirstName");
		person.setLastName("LastName");
		person.setAddressOne("Address1");
		//..
		return person;
	}

}
