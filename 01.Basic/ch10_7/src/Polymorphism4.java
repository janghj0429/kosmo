abstract class Unit
{
	int HP;
	int MP;
	
	abstract void move();
	abstract void attack();
}

class Marine extends Unit
{
	// 자기만의 특징을 위주로 만든다.
	Marine(){
		System.out.println("마린이 마린답게 생성됨");
	}
	void move() {
		System.out.println("한 칸 움직이기");	// Unit의 공통 기능
		System.out.println("두 발로 걷기");		// 마린답게 꾸민것
	}

	void attack() {
		System.out.println("상대 HP 1 감소시키기");// Unit의 공통 기능
		System.out.println("총으로 쏘기");		// 마린답게 꾸민것		
	}		
}

class Zergling extends Unit
{
	Zergling(){
		System.out.println("저글링 생성됨");
	}
	void move() {
		System.out.println("한 칸 움직이기");	// Unit의 공통 기능
		System.out.println("네 발로 걷기");		// 저글링답게 꾸민것
	}

	void attack() {
		System.out.println("상대 HP 1 감소시키기");// Unit의 공통 기능
		System.out.println("앞 발로 찍기");		// 저글링답게 꾸민것
	}
}

public class Polymorphism4 {

	public static void main(String[] args) {
		// 공통 특성만을 이용해서 코딩한다.
		
		// 자식을 부모로 대입받기 : 다형성(폴리모피즘)
		Unit unit1 = new Marine();
		unit1.move();
		unit1.attack();
		
		Unit unit2 = new Zergling();
		unit2.move();
		unit2.attack();
	}

}
