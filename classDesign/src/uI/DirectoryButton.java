package uI;

import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import kernel.FileMsg;

public class DirectoryButton extends Button{
	public static final int BUTTONWIDTH = 100;
	public static final int BUTTONHEIGHT = 100;
	FileMsg fileMsg;
	
	public DirectoryButton(FileMsg fileMsg){
		this.fileMsg = fileMsg;
		setPicture();
		setListener();
	}
	
	private DirectoryButton getMyself() {
		return this;
	}
	
	private void setPicture() {
		Image btnImg = new Image("uI\\src.jpg");
		//Image btnImg = new Image("file:E:\\祈雨的白鹭\\chasingthewind\\backiee-108475.jpg");
        ImageView imageView = new ImageView(btnImg);
        imageView.setFitWidth(BUTTONWIDTH);
        imageView.setFitHeight(BUTTONHEIGHT);
        setGraphic(imageView);
	}
	
	private void setListener() {
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
            	MenuFactory.getDirectoryMenu(fileMsg).show(getMyself(), Side.BOTTOM,0,0);
            }
        });
	}
}
