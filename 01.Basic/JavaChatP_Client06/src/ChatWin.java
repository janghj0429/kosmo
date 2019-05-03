import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatWin extends JFrame {

	private static final long serialVersionUID = 1L;
    JTextField tf;
    JPanel p;
    TextHandler handler = null;
    Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	Socket socket;
	PrintWriter out = null;
    String id;	

	ChatWin(Socket socket, String id) {

		this.setTitle("Chat Window: " + id);
	    this.setSize(600, 100);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    p = new JPanel();
	    p.setLayout(new FlowLayout());
	    
	    tf = new JTextField(40);
	    p.add(tf);
	    
	    this.setContentPane(p);
	    this.setVisible(true);

		handler = new TextHandler();
		tf.addActionListener(handler);

	    //-------------------------------------------------------------------
        
		this.out = out;
	    this.socket = socket;        
        try {
			out = new PrintWriter(this.socket.getOutputStream(), true);
            this.id = id;
            
	        //서버에 입력한 사용자 아이디를 보내준다.
            out.println(id);

        } catch(Exception e) {
            System.out.println("예외S3:"+e);
        }
	}
	
	//Inner Class TextHandler
	class TextHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			
			String msg = tf.getText();
			StringTokenizer s = new StringTokenizer(msg);
			if(msg.startsWith("/agree"))//초대 동의 응답 보내기
			{
				String tmp = msg;
				msg = Receiver6.invAgreeMsg(msg);
				if(msg.equals("|"))
					msg = tmp;
			}			
			if ("".equals(msg)) return;
			
			if ( msg.equals("q") || msg.equals("Q") ) {
				try {
					System.exit(1);
					out.close();
					socket.close();					
				} catch (IOException e1) {
				}
			}else if (msg.equalsIgnoreCase("cls"))
			{
				for(int i = 0 ;i < 50; i++)
					System.out.println();
			}else {
				out.println(msg);
			}

	        tf.setText("");
		}		
		public void ckMsg() 
		{
			
		}
	}
}