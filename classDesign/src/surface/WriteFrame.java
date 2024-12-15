package surface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import kernel.Disk;
import kernel.FileException;
import kernel.FileManage;
import kernel.FileMsg;
import kernel.Path;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class WriteFrame extends JFrame {

	private JPanel contentPane;
	private JTextField pathField;
	private JTextArea addField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					char[] name = new char[3];
					name[0] = 'a';
					name[1] = 'b';
					name[2] = 'b';
					
					char[] backName = new char[2];
					backName[0] = 'm';
					backName[1] = 'd';
					
					Path systemPath =new Path();
					
					FileManage.init();
					try {
						FileManage.create_file(name, backName,(byte) 0, systemPath);
					}catch(FileException event) {
						JOptionPane.showMessageDialog(null, event.warnMess(),"错误类型",JOptionPane.WARNING_MESSAGE);
					}
					//ArrayList<FileMsg> fml = new ArrayList<FileMsg>();
					//fml = FileManage.dir(systemPath);
					//FileMsg fmsg = fml.get(0);
					
					FileMsg fmsg = new FileMsg(Disk.bufferRead(2),0,systemPath);
					
					if(fmsg!=null) {
						WriteFrame frame = new WriteFrame(fmsg);
					    //WriteFrame frame = new WriteFrame(new FileMsg(name, backName, (byte)3, (byte)6, (byte)1, new Path()));
					    frame.setVisible(true);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WriteFrame(FileMsg fmsg) {
		setTitle("写文件窗口");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 90, 350, 350);
		contentPane.add(scrollPane);
		
		JTextArea textShow = new JTextArea();
		textShow.append(FileManage.typeFile(fmsg.getPath()));
		scrollPane.setViewportView(textShow);
		
		JLabel lblNewLabel = new JLabel("文件名：");
		lblNewLabel.setBounds(10, 15, 80, 24);
		contentPane.add(lblNewLabel);
		
		pathField = new JTextField();
		System.out.println(fmsg.getPathName());
		pathField.setText(fmsg.getPathName());
		pathField.setBounds(100, 15, 400, 24);
		contentPane.add(pathField);
		pathField.setColumns(10);
		
		addField = new JTextArea();
		addField.setBounds(380, 90, 390, 350);
		contentPane.add(addField);
		addField.setColumns(10);
		
		JButton writeButton = new JButton("写文件");
		writeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			     try {
			    	 FileManage.write_file(fmsg.getPath(), addField.getText());
			    	 textShow.append(addField.getText());
			    	 //textShow.setText(FileManage.typeFile(fmsg.getPath()));
			    	 addField.setText("");
			     }catch(FileException event) {
			    	 JOptionPane.showMessageDialog(null, event.warnMess(),"异常信息",JOptionPane.WARNING_MESSAGE);
			     }
			}
		});
		writeButton.setBounds(520, 12, 100, 30);
		contentPane.add(writeButton);
		
		JButton exitButton = new JButton("退出");
		exitButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		exitButton.setBounds(640, 12, 100, 30);
		contentPane.add(exitButton);
		
		JLabel lblNewLabel_1 = new JLabel("文件内容展示：");
		lblNewLabel_1.setBounds(10, 60, 110, 24);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("追加文件内容：");
		lblNewLabel_2.setBounds(380, 60, 110, 24);
		contentPane.add(lblNewLabel_2);
		
		/*addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			FileManage.close_file((byte)fmsg.getPath().bottomNum());
			}
		}); */
	}
}
