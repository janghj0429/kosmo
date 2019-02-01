import java.sql.Connection;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		try {
			ConnectionPool cp = ConnectionPool.getInstance(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott","tiger", 1, 5);
		} catch(Exception sqle) {
			sqle.printStackTrace();
		}
		
		for(int i=0; i<100; i++) {
			TestThread test = new TestThread(i);
			test.start();
			Thread.sleep(50);
		}
		

	}

}
