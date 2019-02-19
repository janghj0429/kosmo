import java.io.FileWriter;
import java.io.Writer;

class D3_TextWriter {

	public static void main(String[] args) {
		try(Writer out = new FileWriter("data1.txt")) {
			for(int ch=(int)'A'; ch < (int)('Z'+1); ch++)
				out.write(ch);
			
			out.write(13);
			out.write(10); ///두개합쳐서 줄바꿈. 0D, 0A
			
			for(int ch=(int)'A'+32; ch < (int)('Z'+1+32); ch++)
				out.write(ch);
		}
		catch(Exception e) {
			e.printStackTrace();
		}


	}

}
