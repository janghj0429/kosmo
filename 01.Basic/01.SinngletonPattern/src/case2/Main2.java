package case2;

import java.time.LocalDateTime;
import java.util.Random;

public class Main2 {

	public static void main(String[] args) {
		
		Runnable task = () -> {
			try {
				Random rand = new Random();
				int n1 = rand.nextInt(100);
				Thread.sleep(n1);
				int n2 = rand.nextInt(100);
				
				LogWriter logger = LogWriter.getInstance();
				logger.log("***" + LocalDateTime.now() + ":" + n2 +"***");
			} catch(Exception e) {
			}
		};
		
		for(int i=0; i<50; i++) {
			Thread t = new Thread(task);
			t.start();
		}
	}

}
