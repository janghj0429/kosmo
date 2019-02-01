
class E3_PartiallyFilledArray {

	public static void main(String[] args) {
		int[][]arr	= {
				{11},
				{22, 33},
				{44, 55, 66}
		};
	//	System.out.println(arr.length); //j가 3
		// 배열의 구조대로 내용 출력
		for(int i=0; i<arr.length; i++) {
	//		System.out.println(arr[i].length);   //i가 3    합쳐서 3x3 배열
			for(int j=0; j<arr[i].length; j++) {
				System.out.print(arr[i][j] + "\t");
			}
			System.out.println();
		}

	}

}
