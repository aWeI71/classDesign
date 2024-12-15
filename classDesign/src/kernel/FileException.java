package kernel;

public class FileException extends Exception {
	String message;
	public FileException(int n) {
		switch(n){
		    case -1:
		    	message = "当前目录中存在同名文件或文件夹!";
			    break;
            case -2:
            	message = "磁盘空间已满，\n不能创建新文件或文件夹！";
			    break;
            case -3:
            	message = "上级目录已满，\n不能创建新文件或文件夹！!";
			    break;
            case -4:
            	message = "文件列表已满！\n不能打开新文件！";
			    break;
            case -5:
            	message = "找不到数据项，打开失败！";
			    break;
            case -6:
            	message = "目录中不存在该文件！";
			    break;
            case -7:
            	message = "打开方式非法：\n 写方式打开只读文件。";
			    break;
            case -8:
            	message = "打开文件失败！";
			    break;
            case -9:
            	message = "存储空间不足!";
			    break;
            case -10:
			    message = "该文件已打开，不能修改属性!";
			    break;
            
            case -14:
            	break;
            default:
            	
            	
		}
		
		/*
		if(n==-1) {
			message = "当前目录中存在同名文件或文件夹!";
		}
		else if(n==-2) {
			message = "磁盘空间已满，\n不能创建新目录或新文件！";
		}
		else if(n==-3) {
			message = "上级目录已满，\n不能创建新文件或文件夹!";
		}
		else if(n==-4) {
			message = "文件列表已满！\n不能打开新文件！";
		}
		else if(n==-5) {
			message = "找不到数据项，打开失败！";
		}
		else if(n==-6) {
			message = "目录中不存在该文件！";
		}
		else if(n==-7) {
			message = "打开方式非法：\n 写方式打开只读文件。";
		}
		else if(n==-8) {
			message = "打开文件失败！";
		}
		else if(n==-9) {
			message = "存储空间不足!";
		}
		*/
		//else if()
	}
	public String warnMess() {
		return message;
	}
}
