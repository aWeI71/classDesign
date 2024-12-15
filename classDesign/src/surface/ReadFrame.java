package surface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import kernel.FileManage;
import kernel.FileMsg;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class ReadFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//ReadFrame frame = new ReadFrame();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ReadFrame(FileMsg fmsg) {
		setVisible(true);
		String pathName = fmsg.getPathName();
		String instrence = FileManage.typeFile(fmsg.getPath());
		
		setTitle("文件读取界面");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("文件路径名：");
		lblNewLabel.setBounds(15, 10, 90, 24);
		contentPane.add(lblNewLabel);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(100, 10, 315, 60);
		textArea.setText(pathName);
		contentPane.add(textArea);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(15, 80, 400, 220);
		textArea_1.setText(instrence);
		contentPane.add(textArea_1);
		
		JButton btnNewButton = new JButton("返回");
		btnNewButton.setBounds(15, 40, 80, 30);
		contentPane.add(btnNewButton);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			FileManage.close_file((byte)fmsg.getPath().bottomNum());
			}
		}); 
	}
}
