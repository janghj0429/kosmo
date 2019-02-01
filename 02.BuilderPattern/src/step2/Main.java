package step2;

import java.time.LocalDate;
import java.time.Month;

public class Main {

	public static void main(String[] args) {
		Person test = Person.builder()
				.firstName("FirstName")
				.lastName("LastName")
				.addressOne("AddressOne")
				.addressTwo("AddressTwo")
				.birthDate(LocalDate.of(1955, Month.APRIL, 13))
				.sex("male")
				.driverLicence(true)
				.married(true)
				.build();
		System.out.println(test.getFirstName());
		
	}
	
//	public static Person createPersonForTesting() {
//		return Person.builder()
//				.firstName("FirstName")
//				.lastName("LastName")
//				.addressOne("AddressOne")
//				.addressTwo("AddressTwo")
//				.birthDate(LocalDate.of(1955, Month.APRIL, 13))
//				.sex("male")
//				.driverLicence(true)
//				.married(true)
//				.build();
//	}

}
