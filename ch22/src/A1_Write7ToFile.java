import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class A1_Write7ToFile {

	public static void main(String[] args) throws IOException {
		OutputStream out = new FileOutputStream("data.dat");
		out.write(65);
		out.close();

	}
	
	

}
