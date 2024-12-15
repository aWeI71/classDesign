package uI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import kernel.FileException;
import kernel.FileManage;
import kernel.FileMsg;
import surface.ChangeFrame;
import surface.CreateFrame;
import surface.ReadFrame;
import surface.WriteFrame;

public class Listener {
	public static EventHandler<ActionEvent> directoryOpenListener(FileMsg fileMsg){
		EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				DirectoryScene scene = new DirectoryScene(fileMsg);
				Client.sceneList.add(scene);
				Client.stage.setScene(scene.scene);
			}
		};
		return listener;
	}
	
	public static EventHandler<ActionEvent>  directorydeleteListener(FileMsg fileMsg){
		EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					FileManage.rd(fileMsg.getPath());
					Client.refreshScene();
				} catch (FileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		return listener;
	}
	
	public static EventHandler<ActionEvent> fileReadListener(FileMsg fileMsg){
		EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				ReadFrame frame = new ReadFrame(fileMsg);
				frame.show();
			}
		};
		return listener;
	}
	
	public static EventHandler<ActionEvent> fileWriteListener(FileMsg fileMsg){
		EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				WriteFrame frame = new WriteFrame(fileMsg);
				frame.show();
				/*String read;
				try {
					read = FileManage.read_file(fileMsg.getPath());
					FileManage.close_file((byte)fileMsg.getPath().bottomNum());
				} catch (FileException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}
				Stage stage = new Stage();
				stage.setWidth(Client.STAGEWIDTH);
				stage.setHeight(Client.STAGEHEIGHT);
				TextArea area = new TextArea(read);
				Button button = new Button("保存");
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						String write = area.getText();
						try {
							FileManage.write_file(fileMsg.getPath(), write);
							FileManage.close_file((byte)fileMsg.getPath().bottomNum());
						} catch (FileException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				FlowPane pane = new FlowPane(button,area);
				area.setPrefWidth(Client.STAGEWIDTH*0.96);
				area.setPrefHeight(Client.STAGEHEIGHT*0.7);
				Scene scene = new Scene(pane);
				stage.setScene(scene);
				stage.show();
				stage.setOnCloseRequest(e -> {FileManage.close_file((byte) fileMsg.getNum());} );
				*/
			}
		};
		return listener;
	}
	
	public static EventHandler<ActionEvent> filedeleteListener(FileMsg fileMsg){
		EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				System.out.println("delete: "+new String(fileMsg.getName()));
				System.out.println("block: "+fileMsg.getPath().bottomNum());
				FileManage.delete_file(fileMsg.getPath());
				Client.refreshScene();
			}
		};
		return listener;
	}
	
	public static EventHandler<ActionEvent> fileAttributeListener(FileMsg fileMsg){
		EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				ChangeFrame cframe = new ChangeFrame(fileMsg.getPath(),fileMsg.getAttribute());
				cframe.setVisible(true);
			}
		};
		return listener;
	}
	
	public static EventHandler<ActionEvent> paneAddListener(FileMsg fileMsg){
		EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				CreateFrame frame = new CreateFrame(fileMsg.getPath());
				frame.setVisible(true);
				frame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						super.windowClosing(e);
						//加入动作
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
		};
		return listener;
	}
	
	public static EventHandler<ActionEvent> paneBackListener(FileMsg fileMsg){
		EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(Client.sceneList.size()<=1) {
					return;
				}
				Scene scene = Client.sceneList.get(Client.sceneList.size()-2).scene;
				Client.stage.setScene(scene);
				Client.sceneList.remove(Client.sceneList.size()-1);
			}
		};
		return listener;
	}
	
	public static EventHandler<ActionEvent> paneRefreshListener(){
		EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Client.refreshScene();
			}
		};
		return listener;
	}
}
