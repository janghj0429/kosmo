interface Greet
{
	void greet();
}

interface A
{
	void a();
}

interface B
{
	void b();
}

interface C
{
	void c();
}

interface Bye extends Greet
{
	void bye();
}

class Greeting implements Bye
{

	public void greet()
	{
		System.out.println("안녕하세요!");	
	}

	public void bye() 
	{
		System.out.println("안녕히 계세요.");
	}	
}

class D extends Greeting implements A,B,C
{

	@Override
	public void a() {
		System.out.println("ㅁㅁㄴㅇㄹ");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void c() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void b() {
		// TODO Auto-generated method stub
		
	}
	
}


class Meet {

	public static void main(String[] args) 
	{
		Greeting greeting = new Greeting();
		greeting.greet();
		greeting.bye();
		
		D d = new D();
		
	}

}
