interface Viewable3 {
	@Deprecated
	public void showIt(String str);
	
	public void brShowIt(String str);
}

class Viewer3 implements Viewable3 {
	@Override
	@SuppressWarnings("deprecation")
	public void showIt(String str) {
		System.out.println(str);
	}
	
	@Override
	public void brShowIt(String str) {
		System.out.println('[' + str + ']');
	}
};

class D3_AtSuppressWarnings {

	public static void main(String[] args) {
		Viewable3 view = new Viewer3();
		view.showIt("Hello Annotations");
		view.brShowIt("Hello Annotations");	
	}

}





