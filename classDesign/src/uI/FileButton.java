package uI;

import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import kernel.FileMsg;

public class FileButton extends Button{
	FileMsg fileMsg;
	
	public FileButton(FileMsg fileMsg){
		this.fileMsg = fileMsg;
		setPicture();
		setListener();
	}
	
	private FileButton getMyself() {
		return this;
	}
	
	private void setPicture() {
		Image btnImg = new Image("uI\\src.jpg");
		//Image btnImg = new Image("file:E:\\祈雨的白鹭\\chasingthewind\\backiee-108475.jpg");
        ImageView imageView = new ImageView(btnImg);
        imageView.setFitWidth(DirectoryButton.BUTTONWIDTH);
        imageView.setFitHeight(DirectoryButton.BUTTONHEIGHT);
        setGraphic(imageView);
	}
	
	private void setListener() {
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
            	MenuFactory.getFileMenu(fileMsg).show(getMyself(), Side.BOTTOM,0,0);
            }
        });
	}
}
