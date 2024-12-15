package kernel;

public class OpenedFile{
	OFTLE[] fileList;
	int length;
	public OpenedFile() {
		length = 0;
		fileList = new OFTLE[5];
		
		for(int i=0;i<5;i++) {
			fileList[i] = new OFTLE();
		}
		System.out.println("the space of OpenedFile:"+fileList.length+"\n");
	}
	
	//给定一个文件的起始块号，查看该文件是否在列表中
	public int searchOpenedFile(int index) {
		
		for(int i=0;i<length;i++) {
			if(fileList[i].number==index) {
				return i;
			}
		}
		return -1;
	}
	
	//添加新的文件打开列表信息(打开文件）
	public void addOFTLE(char[] name,byte attribute,int number,int length,int flag,Path path) {
		//若文件列表已满
		if(this.length>=5) {
			System.out.println("文件列表已满，打开文件失败");
		}
		
		if(name==null) {
			System.out.println("error: name is empty!");
		}
		
		String s = new String(name);
		
		System.out.println("\nIn index:"+this.length+"\n"+"open new file: "+s);
		System.out.println(attribute+"  "+number+"  "+length+"  "+flag);
		
		
		fileList[this.length].pathName = name;
		
		fileList[this.length].attribute = attribute;
		fileList[this.length].number = number;
		fileList[this.length].length = length;
		fileList[this.length].flag = flag;
		fileList[this.length].path = path;
		
		
		this.length++;
	}
	
	//从文件打开列表中删除信息(关闭文件)
	public void subOFTLE(int n) {
		for(int i = n;i<length-1;i++) {
			fileList[i] = fileList[i+1];
		}
		length--;
	}
	
	//批量修改文件打开列表信息(修改文件后要记得更新列表信息
	public void change(int n,char[] s,byte attribute,int number,int length,int flag) {
		fileList[n].pathName = s;
		fileList[n].attribute = attribute;
		fileList[n].number = number;
		fileList[n].length = length;
		fileList[n].flag = flag;
	}
	
	//修改文件指针
	public void change(int n,int dnum,int bnum) {
		if(fileList[n].flag==0) {
			fileList[n].read = new Pointer(dnum,bnum);
		}
		else {
			fileList[n].write = new Pointer(dnum,bnum);
		}
	}
	
	//获取文件信息
	public OFTLE getOfMsg(int n) {
		if(n>=0&&n<length) {
			return fileList[n];
		}
		return null;
	}
	
	//获取文件数
	public int getLength() {
		return length;
	}
}