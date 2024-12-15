package uI;

import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.text.Font;

public class MyLabel extends Label{
	public MyLabel(String s) {
		super(" "+s);
		setWidth(DirectoryButton.BUTTONWIDTH);
		setFont(new Font(18));
		
	}
	
}
