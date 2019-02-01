interface Viewable2 {
	@Deprecated
	public void showIt(String str);
	
	public void brShowIt(String str);
}

class Viewer2 implements Viewable2 {
	@Override
	public void showIt(String str) {
		System.out.println(str);
	}
	
	@Override
	public void brShowIt(String str) {
		System.out.println('[' + str + ']');
	}
}

class D2_AtDeprecated {

	public static void main(String[] args) {
		Viewable2 view = new Viewer2();
		view.showIt("Hello Annotations");
		view.brShowIt("Hello Annotations");		
	}

}
