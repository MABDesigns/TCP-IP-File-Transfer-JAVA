package java_tcp_ip_server;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class server {

		static ArrayList<myFile> myFiles = new 	ArrayList<>();
		
	
		public static void main(String[] args) throws IOException {
			int fileId = 0;
			
			
			JFrame frame = new JFrame("Server Side");
			frame.setSize(400,400);
			frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			
			JScrollPane panelscroll = new JScrollPane(panel);
			panelscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			JLabel titre = new JLabel("File Receiver");
			titre.setFont(new Font("Arial", Font.BOLD, 25));
			titre.setBorder(new EmptyBorder(20,0,10,0));
			titre.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			frame.add(titre);
			frame.add(panelscroll);
			frame.setVisible(true);
			
			
			try (ServerSocket serversocket = new ServerSocket(1099)) {
				while(true) {
					try {
						Socket socket = serversocket.accept();
						
						DataInputStream datainput = new DataInputStream(socket.getInputStream());
						
						int filenamelength = datainput.readInt();
						
						if(filenamelength > 0) {
							byte[] filenamebytes = new byte[filenamelength];
							datainput.readFully(filenamebytes,0,filenamebytes.length);
							String filename = new String(filenamebytes);
							
							int filecontentlength = datainput.readInt();
							
							if(filecontentlength >0) {
								byte[] filecontentbytes = new byte[filecontentlength];
								datainput.readFully(filecontentbytes,0,filecontentlength);
								
								JPanel filerow = new JPanel();
								filerow.setLayout(new BoxLayout(filerow, BoxLayout.Y_AXIS));
								
								JLabel filename1 = new JLabel(filename);
								filename1.setFont(new Font("Arial", Font.BOLD, 20));
								filename1.setBorder(new EmptyBorder(10,0,10,0));
								filename1.setAlignmentX(Component.CENTER_ALIGNMENT);
								
								if(getFileExtension(filename).equalsIgnoreCase("txt")) {
									filerow.setName(String.valueOf(fileId));
									filerow.addMouseListener(getMyMouseListener());
									
									filerow.add(filename1);
									panel.add(filerow);
									frame.validate();
								} else {
									filerow.setName(String.valueOf(fileId));
									filerow.addMouseListener(getMyMouseListener());
									
									filerow.add(filename1);
									panel.add(filerow);
									
									frame.validate();
									
								}
								myFiles.add(new myFile(fileId, filename, filecontentbytes, getFileExtension(filename)));
								fileId++;
								
							}
						}
					} catch (IOException error) {
						error.printStackTrace();
					}
				}
			}			
		}
		
	public static JFrame createFrame(String fileName, byte[] fileData,String fileExtension) {
		JFrame frames = new JFrame("File Downloader");
		frames.setSize(400,400);
		
		JPanel panels = new JPanel();
		panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));
		
		JLabel title = new JLabel("File Downloader");
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setFont(new Font("Arial", Font.BOLD, 25));
		title.setBorder(new EmptyBorder(20,0,10,0));
		
		JLabel prompet = new JLabel("Are you sure you want to download "+ fileName);
		prompet.setFont(new Font("Arial", Font.BOLD,20));
		prompet.setBorder(new EmptyBorder(20,0,10,0));
		prompet.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		JButton yes = new JButton("Yes");
		yes.setPreferredSize(new Dimension(150, 75));
		yes.setFont(new Font("Arial", Font.BOLD, 20));
		
		JButton no = new JButton("No");
		no.setPreferredSize(new Dimension(150, 75));
		no.setFont(new Font("Arial", Font.BOLD, 20));
		
		JLabel filecontent = new JLabel();
		filecontent.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		JPanel buttons = new JPanel();
		buttons.setBorder(new EmptyBorder(20,0,10,0));
		buttons.add(yes);
		buttons.add(no);
		
		if(fileExtension.equalsIgnoreCase("txt")) {
			filecontent.setText("<html>" + new String(fileData) + "</html>");
		}else {
			filecontent.setIcon(new ImageIcon(fileData));
		}
		
		yes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File filetodown = new File(fileName);
				
				try {
					FileOutputStream fileoutput = new FileOutputStream(filetodown);
					fileoutput.write(fileData);
					fileoutput.close();
					
					frames.dispose();
				} catch(IOException error) {
					error.printStackTrace();		
				}
			}
		});
		no.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frames.dispose();
			}
			
		});
		panels.add(title);
		panels.add(prompet);
		panels.add(filecontent);
		panels.add(buttons);
		
		frames.add(panels);
		
		
		return frames; 
		
		
	}
	public static MouseListener getMyMouseListener() {
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JPanel panel = (JPanel) e.getSource();
				
				int fileId = Integer.parseInt(panel.getName());
				
				for(myFile myFile: myFiles) {
					if(myFile.getId() == fileId) {
						JFrame framepreview = createFrame(myFile.getName(), myFile.getData(), myFile.getfileExtension());
						framepreview.setVisible(true);	
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {

					
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {

					
			}

			@Override
			public void mouseExited(MouseEvent e) {

					
			}
			 
		};
		
		
	}
	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		
		if(i>0) {
			return fileName.substring(i+1);
		}else {
			return "No Extension Found !";
			
		}
	}
	
}
