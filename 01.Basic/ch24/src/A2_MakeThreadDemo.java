class Task extends Thread {
	public void run() {
		int n1 = 10;
		int n2 = 20;
		String name = Thread.currentThread().getName();
		System.out.println(name + ": " + (n1 + n2));
	}
}
class A2_MakeThreadDemo {

	public static void main(String[] args) {
		Task t1 = new Task();
		
		t1.start();
		
		System.out.println("End " + Thread.currentThread().getName());

	}

}
