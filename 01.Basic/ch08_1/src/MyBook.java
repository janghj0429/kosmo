class Book
{
	String title;
	String author;
	int money;
}

class 사람
{
	int 키;
	int 나이;
	String 이름;
	
	void 인사() {
		System.out.println("안녕하세요");
	}
}

public class MyBook 
{
	public static void main(String[] args) 
	{
		// 사람 이란 설계도(클래스)를 이용해 사람(객체) 만들기(객체를 메모리에 적재)
		사람 man1 = new 사람();
		man1.키 = 175;
		man1.나이 = 25;
		man1.이름 = "홍길동";
		
		System.out.println(man1.키 + " : " +
				man1.나이 + " : " +
				man1.이름);
		man1.인사();
		
		
		Book book1 = new Book(); 
		book1.title	= "자바 프로그래밍";
		book1.author = "홍길동";
		book1.money	= 15000;
		
		System.out.println(book1.title + " : " +
							book1.author + " : " +
							book1.money);
	}

}
