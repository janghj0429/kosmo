import java.io.FileWriter;
import java.io.Writer;

class D1_SimpleWriter {

	public static void main(String[] args) {
		try (Writer out = new FileWriter("data.txt")){
			out.write('A');
			out.write('한');
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

}
