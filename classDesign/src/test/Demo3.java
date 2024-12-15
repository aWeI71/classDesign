package test;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
public class Demo3 extends JFrame {
    public void GUI() {
        setTitle("图像测试");
        JPanel panel = new JPanel();
        JLabel label = new JLabel("文件");
        ImageIcon img = new ImageIcon("src.jpg");// 创建图片对象
        label.setIcon(img);
        panel.add(label);
        add(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);// JFrame最大化
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 让JFrame的关闭按钮起作用
        setVisible(true);// 显示JFrame
    }
    public static void main(String args[]) {
        Demo3 d = new Demo3();
        d.GUI();
    }
}
