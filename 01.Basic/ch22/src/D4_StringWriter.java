import java.io.BufferedWriter;
import java.io.FileWriter;

class D4_StringWriter {

	public static void main(String[] args) {
		String ks = "공부에 있어서 돈이 꼭 필요한 것은 아니다.";
		String es = "Lifr is long if you know how to use it.";
		
		try(BufferedWriter bw = 
				new BufferedWriter(new FileWriter("String.txt"))) {
			bw.write(ks, 0, ks.length());
			bw.newLine(); //줄바꿈 문자(운영체제 별로 줄 바꿈의 표시방법이다름)
			bw.write(es, 0, es.length());
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
