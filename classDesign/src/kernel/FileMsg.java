package kernel;

public class FileMsg{
	char[] fileName;  //文件名
	char[] backName;  //文件后缀
	byte attribute;   //性质
	byte startBlock;   //起始块号
	byte length;     //文件长度
	Path path;   //文件路径
	
	public FileMsg(char[] name,char[] bName,byte attri,byte blockNum,byte length,Path path) {
		fileName = name;
		backName = bName;
		attribute = attri;
		startBlock = blockNum;
		this.path = path;
	}
	
	public FileMsg(byte[] buffer,int index,Path upPath) {
		fileName = new char[4];
		backName = new char[3];
		
		for(int i=0;i<3;i++) {
			fileName[i] =  (char)(buffer[index*8+i]);
		}
		fileName[3] = '\0';
		for(int i=0;i<2;i++) {
			backName[i] = (char)(buffer[index*8+i+3]);
		}
		backName[2] ='\0';
		attribute = buffer[index*8+5];
		startBlock = buffer[index*8+6];
		length = buffer[index*8+7];
		path = upPath.getPathDown(fileName, startBlock);
		//path.PathDown(fileName, startBlock);
	}
	
	public int getNum() {
		return startBlock;
	}
	
	public char[] getName() {
		return fileName;
	}
	
	public int getAttribute() {
		return attribute;
	}
	
	public char[] getbackName() {
		return backName;
	}
	
	public Path getPath() {
		return path;
	}
	
	public String getAllName() {
		StringBuffer str = new StringBuffer();
		for(int i=0;i<3;i++) {
			if(fileName[i]=='\0') {
				break;
			}
			str.append(fileName[i]);
		}
		if(attribute>=8) {
			str.append('.');
			for(int i=0;i<3;i++) {
				if(backName[i]=='\0') {
					break;
				}
				str.append(backName[i]);
			}
		}
		String s = new String(str);
		return s;
	}
	
	public String getPathName() {
		StringBuffer str = new StringBuffer(getAllName());
		if(attribute<8) {
			str.append(".");
			str.append(new String(backName));
		}
		String s = new String(str);
		return s;
	}
	
}
