import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class C2_ExecutorsDemo2 {

	public static void main(String[] args) {
		Runnable task1 = () -> {
			String name = Thread.currentThread().getName();
			System.out.println(name + ": " + (5 + 7));
		};
		
		Runnable task2 = () -> {
			String name = Thread.currentThread().getName();
			System.out.println(name + ": " + (7 - 5));
		};
		
		// 쓰레드 풀을 이용해 동시에 수행될 수 있는 쓰레드의
		// 총량을 제한하고 있다.
		ExecutorService exr = Executors.newFixedThreadPool(2);
		exr.submit(task1);
		exr.submit(task2);
		exr.submit(() -> {
			String name = Thread.currentThread().getName();
			System.out.println(name + ": " + (5 * 7));
		});
		
		exr.shutdown();

	}

}
