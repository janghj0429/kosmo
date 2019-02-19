class BoxA3{
	private String conts;
	
	BoxA3(String cont){
		this.conts = cont;
	}

	public String getConts() {
		return conts;
	}

	
//	BoxA3(String cont){
//		this.conts = cont;
//	}
//	
//	public String toString() {
//		return conts;
//	}

}


class A3_BoxArray {

	public static void main(String[] args) {
		BoxA3[] ar = new BoxA3[3];
		
		// 배열 인스턴스 저장
		ar[0] = new BoxA3("First");
		ar[1] = new BoxA3("Second");
		ar[2] = new BoxA3("Third");
		
		//저장된 인스턴스의 종료
		System.out.println(ar[0].getConts());
		System.out.println(ar[1].getConts());
		System.out.println(ar[2].getConts());

	}

}
