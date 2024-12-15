package test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

public class JRadioButtonTest {

// 视图组件初始化
private static JFrame f = new JFrame();
private static ButtonGroup bg = new ButtonGroup();
private static JRadioButton yes = new JRadioButton("YES");
private static JRadioButton no = new JRadioButton("NO");

public static void main(String[] args) {

// 同一按钮组的各个JRadioButton是具有互斥性的
bg.add(yes);
bg.add(no);
//建立内部类 实现点击事件
class Listener implements ActionListener {
public void actionPerformed(ActionEvent e) {
String selected = null;
if (yes.isSelected()) {
selected = "yes";
} else if (no.isSelected()) {
selected = "no";
}

System.out.println(selected);
}
}

yes.addActionListener(new Listener());
no.addActionListener(new Listener());

// 界面上看不到ButtonGroup的踪迹
f.setLayout(new FlowLayout());
f.getContentPane().add(yes);
f.getContentPane().add(no);

// 显示界面
f.setVisible(true);
}
}
