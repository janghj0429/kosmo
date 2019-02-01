class BoxA2{
	private String conts;

//	public String getConts() {
//		return conts;
//	}
//
//	public void setConts(String conts) {
//		this.conts = conts;
//	}
	BoxA2(){}
	
	BoxA2(String cont){
		this.conts = cont;
	}									// 생성자
	
	public String toString() {
		return "aaaa";
	}
	
	
	
}

class A2_ArrayIsInstance2 {

	public static void main(String[] args) {
		BoxA2[] ar = new BoxA2[5];
		System.out.println("length : " + ar.length);
		
		BoxA2 a = new BoxA2(); //생성자
		String b = a.toString();
		System.out.println(a.toString());
	}

}
