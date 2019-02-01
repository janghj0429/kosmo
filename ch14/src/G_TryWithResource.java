import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class G_TryWithResource {

	public static void main(String[] args) {
		Path file = Paths.get("D:\\janghj0429\\javastudy\\Simple.txt");
		
		try(BufferedWriter writer = Files.newBufferedWriter(file);) {
			writer.write('A'); // IOException 가능
			writer.write('z'); // IOException 가능.
		}
		catch(IOException e) {
			e.printStackTrace();
		}

	}

}
