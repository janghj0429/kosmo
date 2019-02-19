import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

class E2_ObjectOutput {

	public static void main(String[] args) {
		E1_SBox box1 = new E1_SBox("Robot");
		E1_SBox box2 = new E1_SBox("Strawberry");
		
		try(ObjectOutputStream oo = 
				new ObjectOutputStream(new FileOutputStream("Object.bin"))) {
			oo.writeObject(box1);
			oo.writeObject(box2);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
				

	}

}
