import java.util.Scanner;

public class G_InfRecul {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		showHi(sc.nextInt());
		sc.close();
	}
	
	public static void showHi(int cnt) {
		System.out.println("Hi~ " + cnt);
		
		if(cnt == 1) {
			return;
		}
		//showHi(cnt--); // cnt를 보낸 후 값이 감소 : 결과가 무한
		showHi(--cnt);   // cnt를 보내기 전 값이 감소
	}

}
