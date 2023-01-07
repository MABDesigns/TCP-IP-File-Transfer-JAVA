package java_tcp_ip_client;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class client {

	public static void main(String[] args) {
		final File[] fichierpourtransferer = new File[1];
		
		JFrame frame = new JFrame("Client Side");
		frame.setSize(450,450);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel titre = new JLabel("File Sender");
		titre.setFont(new Font("Arial", Font.BOLD, 25));
		titre.setBorder(new EmptyBorder(20,0,10,0));
		titre.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		JLabel choosefile = new JLabel("Choose File To Send");
		choosefile.setFont(new Font("Arial", Font.BOLD, 20));
		choosefile.setBorder(new EmptyBorder(50,0,0,0));
		choosefile.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel buttonarea = new JPanel();
		buttonarea.setBorder(new EmptyBorder(75,0,10,0));
		
		JButton send = new JButton("Send File");
		send.setPreferredSize(new Dimension(150,75));
		send.setFont(new Font("Arial", Font.BOLD, 20));
		
		JButton browse = new JButton("Choose File");
		browse.setPreferredSize(new Dimension(150,75));
		browse.setFont(new Font("Arial", Font.BOLD, 20));
		
		buttonarea.add(send);
		buttonarea.add(browse);
		
		browse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser choose = new JFileChooser();
				choose.setDialogTitle("Choose File To Send");
				
				if(choose.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fichierpourtransferer[0] = choose.getSelectedFile();
					choosefile.setText("The File You Want To Send Is: "+ fichierpourtransferer[0].getName());
				}
			}
		});
		
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(fichierpourtransferer[0] == null) {
					choosefile.setText("Please choose a file first.");
				} else {
					try {
					FileInputStream fileinput = new FileInputStream(fichierpourtransferer[0].getAbsoluteFile());
					try (Socket socket = new Socket("localhost", 1099)) {
						DataOutputStream fileoutput = new DataOutputStream(socket.getOutputStream());
						
						String fileName = fichierpourtransferer[0].getName();
						byte[] fileNameBytes = fileName.getBytes();
						
						byte[] fileContentBytes = new byte[(int)fichierpourtransferer.length];
						fileinput.read(fileContentBytes);
						
						fileoutput.writeInt(fileNameBytes.length);
						fileoutput.write(fileNameBytes);
						
						fileoutput.writeInt(fileContentBytes.length);
						fileoutput.write(fileContentBytes);
					}
					} catch (IOException error) {
						error.printStackTrace();
					}
				}
			}
		});
		
		frame.add(titre);
		frame.add(choosefile);
		frame.add(buttonarea);
		frame.setVisible(true);
		
	}

}
