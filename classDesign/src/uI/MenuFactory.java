package uI;

import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import kernel.FileMsg;

public class MenuFactory {
	enum MenuType{
		DIRECTORY,FILE,PANE	
	}
	
	public ContextMenu getMenu(MenuType t,FileMsg fileMsg) {
		switch(t) {
		case DIRECTORY:return getDirectoryMenu(fileMsg);
		case FILE: return getFileMenu(fileMsg);
		case PANE:return getPaneMenu(fileMsg);
		default: return null;
		}
	}
	
	public static ContextMenu getDirectoryMenu(FileMsg fileMsg) {
		MenuItem open = new MenuItem("显示目录内容");
		open.setOnAction(Listener.directoryOpenListener(fileMsg));
        MenuItem delete = new MenuItem("删除空目录");
        delete.setOnAction(Listener.directorydeleteListener(fileMsg));
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(open);
        menu.getItems().add(delete);
        return menu;
	}
	
	public static ContextMenu getFileMenu(FileMsg fileMsg) {
		MenuItem read = new MenuItem("打开并读取文件");
		read.setOnAction(Listener.fileReadListener(fileMsg));
        MenuItem write = new MenuItem("打开并写入文件");
        write.setOnAction(Listener.fileWriteListener(fileMsg));
        MenuItem delete = new MenuItem("删除文件");
        delete.setOnAction(Listener.filedeleteListener(fileMsg));
        MenuItem modifyAttribute = new MenuItem("修改文件属性");
        modifyAttribute.setOnAction(Listener.fileAttributeListener(fileMsg));
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(read);
        menu.getItems().add(write);
        menu.getItems().add(delete);
        menu.getItems().add(modifyAttribute);
        return menu;
	}
	
	public static ContextMenu getPaneMenu(FileMsg fileMsg) {
		MenuItem create = new MenuItem("新建");
		create.setOnAction(Listener.paneAddListener(fileMsg));
        MenuItem back = new MenuItem("返回");
        back.setOnAction(Listener.paneBackListener(fileMsg));
        MenuItem refresh = new MenuItem("刷新");
        refresh.setOnAction(Listener.paneRefreshListener());
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(create);
        menu.getItems().add(back);
        menu.getItems().add(refresh);
        return menu;
	}
}
