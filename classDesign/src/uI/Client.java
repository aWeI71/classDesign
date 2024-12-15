package uI;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kernel.FileManage;
import kernel.FileMsg;

public class Client extends Application{
	public static Stage stage;
	public static List<DirectoryScene> sceneList;
	public static final double STAGEWIDTH = DirectoryButton.BUTTONWIDTH*4.8;
	public static final double STAGEHEIGHT = DirectoryButton.BUTTONHEIGHT*3.0;
	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage arg0) throws Exception {
		stage = arg0;
		stage.setWidth(STAGEWIDTH);
		stage.setHeight(STAGEHEIGHT);
		arg0.setTitle("文件管理系统");
		sceneList = new ArrayList<DirectoryScene>();
		DirectoryScene rootScene = new DirectoryScene(FileManage.getRootDirectory());
		sceneList.add(rootScene);
		arg0.setScene(rootScene.scene);
		arg0.show();
	}
	
	public static void refreshScene() {
		DirectoryScene scene = Client.sceneList.get(Client.sceneList.size()-1);
		FileMsg fileMsg = scene.fileMsg;
		Client.sceneList.remove(scene);
		scene = new DirectoryScene(fileMsg);
		Client.sceneList.add(scene);
		Client.stage.setScene(scene.scene);
		System.out.println("refresh");
	}
}
