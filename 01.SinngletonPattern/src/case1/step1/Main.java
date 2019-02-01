package case1.step1;

public class Main {

	public static void main(String[] args) {
		Database database;
		
		//database = new Database();
		//database.setName("홍길동");
		
		//database = new Database("홍길동");
//		for(int i=0; i<100; i++) {
//			Database database = new Database("홍길동"+i);
//		}
		
		database = Database.getInstance("첫 번째 Database");
		System.out.println("This is the " + database.getConnection() + " !!!");
				
		database = Database.getInstance("두 번째 Database");
		System.out.println("This is the " + database.getConnection() + " !!!");

	}

}
