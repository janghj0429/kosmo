
class F2_CopyOfSystem {

	public static void main(String[] args) {
		double[]org = {1.1, 2.2, 3.3, 4.4, 5.5};
		double[]cpy = new double[3];
		
		//미리 만들어져 있는 배열에 복사한다.
		System.arraycopy(org, 1, cpy, 0, 3);
		
		for(double d : cpy)
			System.out.print(d + "\t");
		
		System.out.println();
	}

}
