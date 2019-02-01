import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class F2_FinallyCase2 {

	public static void main(String[] args) {
		Path file = Paths.get("D:\\janghj0429\\javastudy\\Simple.txt");
		
		BufferedWriter writer = null;
		
		try {
			writer = Files.newBufferedWriter(file);
			
			writer.write('A');
			writer.write('z');
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(writer != null)
					writer.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}

	}

}
