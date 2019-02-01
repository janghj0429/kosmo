
public class Main {

	public static void main(String[] args) {
		
		try
		{
			for(int i=0; i<20; i++) {
				TestThread test = new TestThread(i);
				test.start();
				Thread.sleep(10);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
