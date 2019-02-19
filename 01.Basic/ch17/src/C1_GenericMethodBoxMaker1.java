import javax.swing.Box;

class BoxC<T>{
	private T ob;
	
	public void set(T o) {
		ob = o;
	}
	public T get() {
		return ob;
	}
}

class BoxFactoryC {
	public static <T> BoxC<T> makeBox(T o) {
		BoxC<T> box = new BoxC<T>();
		box.set(o);
		return box;
	}
	
//	public static BoxC<String> makeBox2(String o) {
//		BoxC<String> box = new BoxC<String>();
//		box.set(o);
//		return box;
//	}
//	
//	public static BoxC makeBox2(String to) {
//		BoxC box = new BoxC();
//		box.set(o);
//		return box;
//	}
}
public class C1_GenericMethodBoxMaker1 {

	public static void main(String[] args) {
		BoxC<String> sBox = BoxFactoryC.<String>makeBox("Sweet");
		System.out.println(sBox.get());
		
		BoxC<Double> dBox = BoxFactoryC.<Double>makeBox(7.59);
		System.out.println(dBox.get());

	}

}
