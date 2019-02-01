abstract class Calcu
{
	int a;
	int b;
	int m;
	int n;
	abstract int result();
	
	void printResult()
	{
		System.out.println(result());
	}
	
	void setData(int m, int n)
	{
		a = m;
		b = n;
		System.out.println(m+""+n);
	}
}

class Plus extends Calcu
{
	int result() {return m+n;}
}
class Minus extends Calcu
{
	int result() {return a-b;}
}

class Calculation {

	public static void main(String[] args)
	{
		int x = 54, y= 12;
		
		Calcu calc1 = new Plus();
		Calcu calc2 = new Minus();
		
		calc1.setData(x, y);
		calc2.setData(x, y);
		
		System.out.print(x + " + " + y + " = ");
		calc1.printResult();
		
		System.out.print(x + " - " + y + " = ");
		calc2.printResult();

	}

}
