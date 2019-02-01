class Catt
{
	static int a = 5;
	int num = 3;//지역
	
	void Print(int num3) {//num3 매개
		System.out.println("a:" + a);
		num = num3;
		System.out.println("num:" + num);
	}
}
public class StaticEx1 {
	public static void main(String[] args) {
		int num1 = 5;
		int num2 = 2;
		System.out.println(num1);
		
		Catt cat1 = new Catt();
		cat1.num = 1;
		cat1.a = 10;
		cat1.Print(20);
		System.out.println(cat1.num);
		System.out.println(cat1.a);
		
		Catt cat2 = new Catt();
		cat2.num = 2;
		cat2.a = 11;
		cat2.Print(20);
		System.out.println(cat2.num);
		System.out.println(cat2.a);
		System.out.println(cat1.a);
	}

}
