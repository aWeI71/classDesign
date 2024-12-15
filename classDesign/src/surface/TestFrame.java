package surface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import kernel.Disk;

import javax.swing.JTextArea;

public class TestFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestFrame() {
		setTitle("调试窗口");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane jsp = new JScrollPane();
		jsp.setBounds(5, 5, 770, 440);
		contentPane.add(jsp);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(5, 5, 680, 440);
		jsp.setViewportView(textArea);
		/*
		textArea.append("标号：");
		for(int i=0;i<64;i++) {
			textArea.append("   "+i);
		}*/
		
		byte[] buffer = new byte[64];
		for(int i=0;i<128;i++) {
			buffer = Disk.bufferRead(i);
			textArea.append(" "+i+":");
			for(int j=0;j<64;j++) {
				textArea.append(" "+buffer[j]);
			}
			textArea.append("\n");
		}
		
	}

}
