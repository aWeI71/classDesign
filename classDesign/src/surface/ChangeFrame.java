package surface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import javafx.application.Platform;
import kernel.FileManage;
import uI.Client;
import kernel.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class ChangeFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeFrame frame = new ChangeFrame(new Path(),4);
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
	public ChangeFrame(Path path,int attribute) {
		setTitle("属性设置界面");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 360, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		
		ButtonGroup readOnly = new ButtonGroup();
		ButtonGroup generalFile = new ButtonGroup();
		ButtonGroup systemFile = new ButtonGroup();
		//ButtonGroup isList = new ButtonGroup();
		
		JRadioButton readOnly_yes = new JRadioButton("是");
		readOnly_yes.setSize(60, 20);
		readOnly_yes.setLocation(140, 80);
		JRadioButton readOnly_no = new JRadioButton("否");
		readOnly_no.setSize(60, 20);
		readOnly_no.setLocation(220, 80);
		readOnly.add(readOnly_yes);
		readOnly.add(readOnly_no);
		
		JRadioButton general_yes = new JRadioButton("是");
		general_yes.setSize(60, 20);
		general_yes.setLocation(140, 110);
		JRadioButton general_no = new JRadioButton("否");
		general_no.setSize(60, 20);
		general_no.setLocation(220, 110);
		generalFile.add(general_yes);
		generalFile.add(general_no);
		
		JRadioButton system_yes = new JRadioButton("是");
		system_yes.setSize(60, 20);
		system_yes.setLocation(140, 140);
		JRadioButton system_no = new JRadioButton("否");
		system_no.setLocation(220, 140);
		system_no.setSize(60, 20);
		systemFile.add(system_yes);
		systemFile.add(system_no);
		/*
		JRadioButton isList_yes = new JRadioButton("是");
		isList_yes.setSize(60,20);
		isList_yes.setLocation(140, 240);
		JRadioButton isList_no = new JRadioButton("否");
		isList_no.setSize(60, 20);
		isList_no.setLocation(220, 240);
		isList.add(isList_yes);
		isList.add(isList_no);
		*/
		//设置默认属性
		
		if(attribute%2==1) {
			readOnly_yes.setSelected(true);
		}
		else {
			readOnly_no.setSelected(true);
		}
		
		if((attribute/2)%2==1) {
			general_yes.setSelected(true);
		}
		else {
			general_no.setSelected(true);
		}
		
		if((attribute/4)%2==1) {
			system_yes.setSelected(true);
		}
		else {
			system_no.setSelected(true);
		}
		
		//general_yes.setSelected(true);
		//system_no.setSelected(true);
		//isList_no.setSelected(true);
		contentPane.setLayout(null);
		
		contentPane.add(readOnly_yes);
		contentPane.add(readOnly_no);
		
		contentPane.add(general_yes);
		contentPane.add(general_no);
		
		contentPane.add(system_yes);
		contentPane.add(system_no);
		
		JLabel lblNewLabel_1 = new JLabel("文件路径名：");
		lblNewLabel_1.setBounds(15, 15, 90, 24);
		contentPane.add(lblNewLabel_1);
		
		JLabel label_1 = new JLabel("只读文件：");
		label_1.setBounds(40, 80, 80, 20);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("系统文件：");
		label_2.setBounds(40, 110, 80, 20);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("普通文件：");
		label_3.setBounds(40, 140, 80, 20);
		contentPane.add(label_3);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(109, 15, 219, 53);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JButton changeButton = new JButton("修改");
		changeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int n = 0;
				//获取选定的文件属性
				if(readOnly_yes.isSelected()) {
					n+=1;
				}
				if(general_yes.isSelected()) {
					n+=2;
				}
				if(system_yes.isSelected()) {
					n+=4;
				}
				//其实我想禁掉文件夹属性修改功能的,认为没什么用,
				//只是不知道后面要求 会不会改动，就姑且保留着先吧
				n+=attribute/8*8;
				if(n!=attribute) {
					try {
						FileManage.change(path, (byte)n);
						JOptionPane.showMessageDialog(null, "属性已修改","提示信息",JOptionPane.INFORMATION_MESSAGE);
					}catch(FileException event) {
						JOptionPane.showMessageDialog(null, event.warnMess(),"错误类型",JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		changeButton.setBounds(40, 180, 100, 35);
		contentPane.add(changeButton);
		
		JButton returnButton = new JButton("返回");
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		returnButton.setBounds(160, 180, 100, 35);
		contentPane.add(returnButton);
	
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						//javaFX operations should go here
						Client.refreshScene();
					}
				});
			}
		}); 
	}
}
