package surface;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DemoRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		
		BufferedImage bi=null;
		try {
			bi = ImageIO.read(new File("src.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (node.getLevel() == 1) {
			if (expanded) {
				this.setIcon(new ImageIcon(bi));
			} else {
				this.setIcon(new ImageIcon(bi));
			}
		}
		else if (node.getLevel() == 2) {
			this.setIcon(new ImageIcon(bi));
		}
		
		this.setText(value.toString());
		return this;
	}
}
