package surface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import kernel.*;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class CreateFrame extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField textField;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Path path = new Path();
					//FileManage fm = new FileManage();
					CreateFrame frame = new CreateFrame(path);
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
	public CreateFrame(Path path) {
		setTitle("文件创建界面");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 380, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ButtonGroup readOnly = new ButtonGroup();
		ButtonGroup generalFile = new ButtonGroup();
		ButtonGroup systemFile = new ButtonGroup();
		ButtonGroup isList = new ButtonGroup();
		
		JRadioButton readOnly_yes = new JRadioButton("是");
		readOnly_yes.setSize(60, 20);
		readOnly_yes.setLocation(140, 150);
		JRadioButton readOnly_no = new JRadioButton("否");
		readOnly_no.setSize(60, 20);
		readOnly_no.setLocation(220, 150);
		readOnly.add(readOnly_yes);
		readOnly.add(readOnly_no);
		
		JRadioButton general_yes = new JRadioButton("是");
		general_yes.setSize(60, 20);
		general_yes.setLocation(140, 180);
		JRadioButton general_no = new JRadioButton("否");
		general_no.setSize(60, 20);
		general_no.setLocation(220, 180);
		generalFile.add(general_yes);
		generalFile.add(general_no);
		
		JRadioButton system_yes = new JRadioButton("是");
		system_yes.setSize(60, 20);
		system_yes.setLocation(140, 210);
		JRadioButton system_no = new JRadioButton("否");
		system_no.setLocation(220, 210);
		system_no.setSize(60, 20);
		systemFile.add(system_yes);
		systemFile.add(system_no);
		
		JRadioButton isList_yes = new JRadioButton("是");
		isList_yes.setSize(60,20);
		isList_yes.setLocation(140, 240);
		JRadioButton isList_no = new JRadioButton("否");
		isList_no.setSize(60, 20);
		isList_no.setLocation(220, 240);
		isList.add(isList_yes);
		isList.add(isList_no);
		
		//设置默认属性
		readOnly_no.setSelected(true);
		general_yes.setSelected(true);
		system_no.setSelected(true);
		isList_no.setSelected(true);
		
		contentPane.add(readOnly_yes);
		contentPane.add(readOnly_no);
		
		contentPane.add(general_yes);
		contentPane.add(general_no);
		
		contentPane.add(system_yes);
		contentPane.add(system_no);
		
		contentPane.add(isList_yes);
		contentPane.add(isList_no);
		
		
		JLabel label = new JLabel("当前位置：");
		label.setBounds(40, 10, 80, 18);
		contentPane.add(label);
		
		JLabel nameFront = new JLabel("文件名：");
		nameFront.setBounds(40, 55, 80, 24);
		contentPane.add(nameFront);
		
		nameField = new JTextField();
		nameField.setBounds(140, 55, 80, 20);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JLabel backFront = new JLabel("文件后缀名：");
		backFront.setBounds(40, 85, 90, 24);
		contentPane.add(backFront);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(140, 85, 80, 20);
		contentPane.add(textField);
		
		JLabel lblNewLabel = new JLabel("选择文件性质：");
		lblNewLabel.setBounds(40, 120, 111, 18);
		contentPane.add(lblNewLabel);
		
		JLabel label_1 = new JLabel("只读文件：");
		label_1.setBounds(40, 150, 80, 20);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("系统文件：");
		label_2.setBounds(40, 180, 80, 20);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("普通文件：");
		label_3.setBounds(40, 210, 80, 20);
		contentPane.add(label_3);
		
		JLabel lblNewLabel_1 = new JLabel("（只取前三位）");
		lblNewLabel_1.setBounds(216, 56, 110, 18);
		contentPane.add(lblNewLabel_1);
		
		JLabel label_4 = new JLabel("（只取前两位）");
		label_4.setBounds(216, 86, 110, 18);
		contentPane.add(label_4);		
		
		JButton createFileButton = new JButton("创建");
		createFileButton.setBounds(50, 300, 100, 35);
		createFileButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				char[] name = new char[3];
				char[] typeName = new char[2];
				String s1 = nameField.getText();
				String s2 = textField.getText();
				
				//字符串转字符数组，之前的字符串转字符数组的公式用了会在
				//截取长度不足的文件名时出问题，只能用笨方法，感觉很难受
				int i;
				for(i=0;i<3;i++) {
					if(i<s1.length()) {
						name[i] = s1.charAt(i);
					}
					else {
						name[i] = '\0';
					}
				}

				for (i = 0; i < 2; i++) {
					if (i < s2.length()) {
						typeName[i] = s2.charAt(i);
					} else {
						typeName[i] = '\0';
					}
				}
				byte n = 0;

				// 获取性质
				if (readOnly_yes.isSelected()) {
					n += 1;
				}
				if (general_yes.isSelected()) {
					n += 2;
				}
				if (system_yes.isSelected()) {
					n += 4;
				}
				if (isList_yes.isSelected()) {
					n += 8;
				}
				
				if (isList_no.isSelected()) {
					// 不知道这里会不会出bug,感觉可能性比较大，先做好记号
					// ******************************************************************
					//果然出bug了，幸好做好标记，找起来不费劲
					//                            by 骚明  9.25
					try {
						FileManage.create_file(name, typeName, n, path);
					} catch (FileException event) {
						JOptionPane.showMessageDialog(null, event.warnMess(), "错误信息", JOptionPane.WARNING_MESSAGE);
					}

				} else {
					try {
						FileManage.md(name, path,n);
					}catch(FileException event) {
						JOptionPane.showMessageDialog(null, event.warnMess(), "错误信息", JOptionPane.WARNING_MESSAGE);
					}
				}

			}
			
		});
		
		contentPane.add(createFileButton);
		
		JButton defaultButton = new JButton("取消");
		defaultButton.setBounds(200, 300, 100, 35);
		defaultButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				//关闭当前窗口
				dispose();
			}
		});
		contentPane.add(defaultButton);
		
		JTextArea txtrNull = new JTextArea(path.getPathName());
		txtrNull.setBounds(125, 10, 200, 35);
		contentPane.add(txtrNull);
		
		JLabel label_5 = new JLabel("目录属性：");
		label_5.setBounds(40, 240, 80, 20);
		contentPane.add(label_5);
	}
}
