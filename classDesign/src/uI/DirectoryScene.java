package uI;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import kernel.FileManage;
import kernel.FileMsg;

public class DirectoryScene{
	FileMsg fileMsg;
	GridPane gp;
	Scene scene;
	public DirectoryScene(FileMsg fileMsg) {		
		this.fileMsg  = fileMsg;
		gp = new GridPane();
		initGp();
		scene = new Scene(gp);
		setListener();
	}
	
	private void initGp() {
		ArrayList<FileMsg> list = FileManage.dir(fileMsg.getPath());
		for(int i=0;i<list.size();i++) {
			int type = (list.get(i).getAttribute() & 0x08) == 0x08 ? 1 : 0;
			if(type==0) {
				FileButton fileButton = new FileButton(list.get(i));
				gp.add(fileButton, i%4, i/4*2);
			}else {
				DirectoryButton directoryButton = new DirectoryButton(list.get(i));
				gp.add(directoryButton, i%4, i/4*2);
			}
			MyLabel myLabel = new MyLabel(new String(list.get(i).getName()) +"."+ new String(list.get(i).getbackName()));
			gp.add(myLabel, i%4, i/4*2+1);
		}
	}
	
	private void setListener() {
		gp.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
            	MenuFactory.getPaneMenu(fileMsg).show(gp,Side.TOP,event.getX(),event.getY());
            }
        });
	}
	
	
	
}
