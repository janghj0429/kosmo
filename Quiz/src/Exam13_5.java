import java.util.Scanner;

public class Exam13_5 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("단어를 입력하시오. : ");
		String word = sc.nextLine();
		
		int nLen = word.length();
		String[] strArr = new String[nLen];
			
		//noon , level
		
		for(int i=0; i<nLen; i++) {
			strArr[i] = word.substring(i, i+1);
		}
		for(int i=0; i<nLen; i++) {
			System.out.print(strArr[i]);			
		}System.out.println();
		
		int i=0; 				
		int j=nLen-1;
//		int ans = 1;
		boolean result = false;
		while(i<nLen/2) {
			if( strArr[i].equals(strArr[j]) ) {		
				result = true;
			//	System.out.println("회문이 아닙니다.");
			//	break;
			}else {
				result = false;
				break;
	//			ans = 0;
			}
			i++;
			j--;
		}
		if(result == true) 		System.out.println("회문입니다.");		
		else		System.out.println("회문이 아닙니다.");
		
	//	if(ans == 0) System.out.println("회문입니다.");
		sc.close();
	}
}


