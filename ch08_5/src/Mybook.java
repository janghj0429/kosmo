// 생성자는 클래스이름과 같은 메서드명
// 생성자는 메서드이지만 리턴형이 없다.
//
// 디폴트 생성자(파라미터 없는애)는 자동으로 생성된다.
//
// 파라미터가 잇는 생성자를 만들면
// 디폴트 생서자가 자동으로 생성되지 않는다.
class Book
{
	int price;
	int num = 0;
	String title;
	
	Book()
	{
		title = "C++";
		price = 7000;
	}
	
	Book(String t, int p)
	{
		title = t;
		price = p;
	}
	
	void print()
	{
		System.out.println("제    목: " + title);
		System.out.println("가    격: " + price);
		System.out.println("주문수량: " + num);
		System.out.println("합계금액: " + price * num);
	}
}

class Mybook 
{

	public static void main(String[] args) 
	{
		Book book1 = new Book();
		book1.print();
		System.out.println();
		
		Book book2 = new Book("자바 디자인패턴", 10000);
		book2.num = 10;
		book2.print();
		
	}
}
