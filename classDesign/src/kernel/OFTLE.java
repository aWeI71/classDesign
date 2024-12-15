package kernel;

class Pointer{
	//文件指针
	int dnum;  //磁盘块号
	int bnum;  //块内字节号
	public Pointer() {
		dnum = bnum = 0;
	}
	public Pointer(int d,int b) {
		dnum = d;
		bnum = b;
	}
}

public class OFTLE {
	 public char[] pathName; //文件绝对路径名
	 public byte attribute; //文件的属性，用 1 个字节表示
	 public int number; //文件起始盘块号
	 public int length; //文件长度，文件占用的字节数
	 public int flag; //操作类型，用“0”表示以读操作方式打开文件，用“1”表示以写操作方式打开文件
	 public Pointer read; //读文件的位置，文件打开时 dnum 为文件起始盘块号，bnum 为“0”
	 public Pointer write; //写文件的位置，文件刚建立时 dnum 为文件起始盘块号，
	                //bnum 为“0 ，打开文件时 dnum 和 bnum 为文件的末尾位置
	 public Path path;  //文件路径
	 void OFILE(){
		 pathName = new char[200];
		 read = new Pointer();
		 write = new Pointer();
	 }
}
