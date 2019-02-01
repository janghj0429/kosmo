class Car
{
	int fuel;
	int velocity;
	
	Car(){
		fuel = 10;
		velocity = 0;
	}
	void doAccel() {
		velocity = velocity + 1;
		fuel = fuel - 1;
	}
	
	void doBreak() {
		velocity = velocity - 1;
	}
	
	void status() {
		System.out.println("연료량 " + fuel);
		System.out.println("속도 " + velocity);
	}
}

public class MyExample {

	public static void main(String[] args) {
		Car myCar1 = new Car();
		myCar1.status();
		
		myCar1.doAccel();
		myCar1.doAccel();
		myCar1.status();
		
		myCar1.doBreak();
		myCar1.status();
		
		Car myCar2 = new Car();
		myCar2.doAccel();
		myCar2.doAccel();
		myCar2.doAccel();
		myCar2.doAccel();
		myCar2.status();
		
		myCar1.status();
	}

}
