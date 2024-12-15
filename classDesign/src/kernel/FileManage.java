package kernel;

import java.util.ArrayList;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;




/*
 * FileManage类主要是实现对文件和文件夹的管理，主要分三部分：
 * 1.实现功能的底层操作
 * 2.文件管理操作
 * 3.文件夹管理操作
 */

/*
 * 某些操作可能只修改一两个地方，如果用缓冲区来弄有点杀鸡用牛刀，效率低
 * 下，后面如果有时间就加一个只修改单比特信息的文件操作夹在Disk类中
 * 8.31 by 骚明
 */

/*
 * 基本上的文件操作测试基本上完成了，接下来一个月的任务是对文件操作
 * 的代码进行测试，还有对其添加异常类来为后面的界面化做准备
 * 9.6 by 骚明
 */

public class FileManage {
    static byte[] buffer1 = new byte[64];
	static byte[] buffer2 = new byte[64];
	public static OpenedFile ofLine = new OpenedFile();
	
	public FileManage() {
		
	}
    
	//*********************************************
    //*      以下内容为实现功能的基础操作         *
	//*********************************************
	
	public static void init() {
		buffer1 = new byte[64];
		buffer2 = new byte[64];
		ofLine = new OpenedFile();
	}
	
	//寻找空闲磁盘块
    //  ************未经测试**************
	public static int searchFreeBlock() {
		buffer2 = Disk.bufferRead(0);
		
		//先寻找FAT前半段，因为0、1号块是FAT，
		//2号块是根目录，所以从3号块开始找
		for(int i=3;i<64;i++) {
			if(buffer2[i] == 0) {
				return i;
			}
		}
		
		buffer2 = Disk.bufferRead(1);
		//前半段找不到，就找FAT后半段
		for(int i=0;i<64;i++) {
			if(buffer2[i] == 0) {
				return i+64;
			}
		}
		return -1; //找不到则返回-1,代表磁盘空间已满
	}
	
	//寻找同名文件或目录项
	//  ************未经测试**************
	//注：之前想弄一个同一文件夹同时有一个目录项和一个文件
	//项同名也能兼容的功能，后面发现比较复杂就不搞了
	public static int searchName(char[] name,int blockNum) {
		buffer2 = Disk.bufferRead(blockNum);
		int j;
		//后面再补
		for(int i=0;i<8&&buffer2[i*8]!=0;i++) {
			for(j=0;j<3;j++) {
			    if((byte)(name[j]) != buffer2[i*8+j]) {
				     break;
			    }
		    }
			if(j>=3) {
				return i;
			}
		}		
		return -1;
	}
	
	//这个也是寻找同名文件或目录项
	//不过这个是直接传一整个缓冲区，因为我担心缓冲区会有覆盖的问题,
	//导致出错，如果原来的会出错就用这个，而且这个不用多次读取内容到模拟缓冲区
	public static int searchName(char[] name,byte[] buffer) {
		int j;
		for(int i=0;i<8&&buffer[i*8]!=0;i++) {
			for(j=0;j<3;j++) {
			    if((byte)(name[j]) != buffer[i*8+j]) {
				     break;
			    }
		    }
			if(j>=3) {
				return i;
			}
		}		
		return -1;
	}
	
	//获取目录长度
	public static int getListLength(int blockNum) {
		byte[] buffer = Disk.bufferRead(blockNum);
		for(int i = 0;i<8;i++) {
			if(buffer[i*8]==0) {
				return i;
			}
		}
		return 8;
	}
	
	//用起始块号寻找目录项的位置
	public static int getListNum(int blockNum,byte[] buffer) {
		for(int i=0;i<8&&buffer[i*8]!=0;i++) {
			if(buffer[i*8+6]==blockNum) {
				return i;
			}
		}
		return -1;
	}
	
	//删除目录项
	public static void deletElement(int BlockNum,int listNum) {
		buffer2 = Disk.bufferRead(BlockNum);
		if(listNum<7&&listNum>=0) {   //目录项不在最后一个位置
			for(int i=listNum;i<7&&buffer2[i*8]!=0;i++) {
			    for(int j = 0;j<8;j++) {
				    buffer2[i*8+j] = buffer2[i*8+j+8];
			    }
		    }
		}
		for(int j = 0;j<8;j++) {
		    buffer2[56+j] = 0;
	    }
		//模拟缓冲区内容写回磁盘
		Disk.bufferWrite(BlockNum, buffer2);
	}
	
	//增加目录项
	public static int addElement(int BlockNum,int listLength,char[] name,char[] typeName,byte attribute,byte startBlock,byte length) {
		//在调用该功能前已经检查过列表空间是否已满，所以就不用再检查，直接修改就行了
		buffer2 = Disk.bufferRead(BlockNum);
		
		//接下来批量复制信息
		buffer2[listLength*8] = (byte) name[0];
		System.out.println("listLength: "+listLength);
		System.out.println("name: "+name);
		System.out.println("length "+ name.length);
		buffer2[listLength*8+1] = (byte) name[1];
		buffer2[listLength*8+2] = (byte) name[2];
		buffer2[listLength*8+3] = (byte) typeName[0];
		buffer2[listLength*8+4] = (byte) typeName[1];
		buffer2[listLength*8+5] = attribute;
		buffer2[listLength*8+6] = startBlock;
		buffer2[listLength*8+7] = length;
		
		//接下来是将列表信息写回模拟硬盘
		Disk.bufferWrite(BlockNum, buffer2);
		
		return -1;
		
	}
	
	public static FileMsg getFileMsg(byte[] buffer,int index,Path upPath) {
		char[] fileName = new char[4];
		char[] backName = new char[3];
		
		for(int i=0;i<3;i++) {
			fileName[i] =  (char)(buffer[index*8+i]);
		}
		fileName[3] = '\0';
		for(int i=0;i<2;i++) {
			backName[i] = (char)(buffer[index*8+i+3]);
		}
		backName[2] ='\0';
		byte attribute = buffer[index*8+5];
		byte startBlock = buffer[index*8+6];
		byte length = buffer[index*8+7];
		Path path = upPath.getPathDown(fileName, startBlock);
		//path.PathDown(fileName, startBlock);
		return new FileMsg(fileName,backName,attribute,startBlock,length,path);
	}
	
	//修改FAT
	public static void setFAT(int index,byte num) {
		buffer2 = Disk.bufferRead(index/64);
		buffer2[index%64] = num;
		Disk.bufferWrite(index/64, buffer2);
	}
	
	//********************未测试***************************
	//产生路径名
	public static char[] getPathName(Path path,char[] backName) {
		StringBuffer str = new StringBuffer();
		int i;
		int j;
		for(i=0;i<=path.depth;i++) {
			for(j=0;j<3;j++) {
				if(path.PathName[i][j]=='\0') {
					break;
				}
				str.append(path.PathName[i][j]);
			}
			if(i<path.depth) {
				str.append('/');
			}
			else {
				str.append('.');
			}
		}
		str.append(backName);
		str.append('\0');
		String s = new String(str);
		
		return s.toCharArray();
	}
	
	//检查当前路径是否存在已打开文件
	public static boolean withOpedFile(Path path) {
		for(int i=0;i<ofLine.length;i++) {
			if(ofLine.fileList[i].path.depth>=path.depth) {
				if(ofLine.fileList[i].path.blockNum[path.depth]==path.blockNum[path.depth]) {
					return true;
				}
			}
		}
		return false;
	}
	
	//**********************************************
	//*           以下为文件操作                   *
	//**********************************************
	
	//创建文件
	//1.检查当前文件夹是否有重名文件或文件夹
	//2.检查磁盘空间是否已满
	//3.记录新建文件信息
	public static int create_file(char[] name,char[] type,byte attribute,Path path) throws FileException {
		Path newPath = null;
		
		int blockNum = searchFreeBlock();
		int listLength;
		
		if(searchName(name,path.blockNum[path.depth])!=-1) {
			//命名重复
			throw new FileException(-1);
		}
			
		if(blockNum==-1) {
			//磁盘空间已满
			throw new FileException(-2);
		}
		
		if((listLength=getListLength(path.blockNum[path.depth]))==8) {
			//上级目录已满
			throw new FileException(-3);
		}
		
		newPath = path.clone();
		newPath.PathDown(name, blockNum);
		
		//修改上级目录
		addElement(path.blockNum[path.depth],listLength,name,type,attribute,(byte)blockNum,(byte)1);
		
		//测试位点
		System.out.println("\n设置新文件，起始块号："+blockNum);
		
		//更新FAT
		/*
		buffer1 = Disk.bufferRead(blockNum/64);
		buffer1[blockNum%64] = -1;
		Disk.bufferWrite(blockNum/64, buffer1);
		*/
		Disk.writeOnlyByte(0, 0, blockNum, (byte)-1);
		
		//添加结束符
		/*
		buffer1 = Disk.bufferRead(blockNum);
		buffer1[0] = (byte)('#');
		Disk.bufferWrite(blockNum, buffer1);
		*/
		Disk.writeOnlyByte(blockNum, 0, 0, (byte)('#'));
		
		return blockNum;
	}
	
	//删除文件
	public static void delete_file(Path path) {
		/*
		 * 用起始块号检查当前文件是否在已打开文件列表中
		 * 这里题目要求如果有已打开文件就中止操作，如果这样的话删
		 * 除文件就可能要先关闭文件再删除，考虑到系统操作的简便性
		 * 需要，我这里直接关闭文件再删除，连警告提示都不用写了
		 *                                  9.10 by 骚明
		 */
		for(int i=0;i<ofLine.length;i++) {
			if(ofLine.fileList[i].number==path.blockNum[path.depth]) {
				//文件在打开文件列表中，则关闭文件
				ofLine.subOFTLE(i);
				break;
			}
		}
		
		//释放文件占用的数据块
		int blockNum;
		int nextNum = path.blockNum[path.depth];
		do {
			blockNum = nextNum;
			buffer1 = Disk.bufferRead(blockNum/64);
			nextNum =buffer1[blockNum%64];
			Disk.freeBlock(blockNum);
		}while(nextNum>0);
		
		//修改上级目录
		buffer2 = Disk.bufferRead(path.blockNum[path.depth-1]);
		int listNum;
		System.out.println("path.blockNum[path.depth]" + path.blockNum[path.depth]);
		if((listNum = getListNum(path.blockNum[path.depth],buffer2))==-1) {
			//目录中没有该目录项
			System.out.println("\n目录中没有该目录项！");
			return;
		}
		System.out.println("******listLength: " + listNum);
		deletElement(path.blockNum[path.depth-1],listNum);
	}
	
	//打开文件
	//要处理以下可能会引起运行错误的情况：
	//1.该文件已打开
	//2.文件列表已满
	//3.没有该数据项信息
	public static int open_file(Path path,int operateType) throws FileException {
	    
		int index = 0;
		int indexOfList;
		
		if((index=ofLine.searchOpenedFile(path.blockNum[path.depth]))!=-1) {
			//该文件已在打开文件列表中			
			//发出提示信息(后面可能改）
			if(operateType==ofLine.fileList[index].flag) {
				System.out.println("该文件已打开!");
				return index;
			}
			else {
				System.out.println("该文件已打开但打开方式不同！");
				
				//这里我还是想不中止冲突直接关闭文件再打开
				//可能会出bug，这里先做好记号********************************
				ofLine.subOFTLE(index);
			}
		}
		
		if(ofLine.length>=5) {
			//当前打开文件列表已满
			throw new FileException(-4);
		}
		
		//读取上级文件夹，获得该文件的详细信息
		buffer1 = Disk.bufferRead(path.blockNum[path.depth-1]);
		
		indexOfList = searchName(path.PathName[path.depth],buffer1);
		
		if(indexOfList==-1) {
			//找不到该数据项
			throw new FileException(-5);
		}
		/*
		//检查访问方式是否非法
		if(operateType==)
		*/
		
		//添加打开文件目录项
		int pointer = indexOfList*8;
		char[] backName = new char[2];
		for(int i=0;i<2;i++) {
			backName[i] = (char)(buffer1[pointer+3+i]);
		}
		ofLine.addOFTLE(getPathName(path,backName), buffer1[pointer+5]
				, buffer1[pointer+6], buffer1[pointer+7], operateType, path);
		
		if(operateType==0) {
			ofLine.change(ofLine.length, ofLine.fileList[ofLine.length-1].number, 0);
		}
		else {
			//设置文件指针
			int nextDisk = ofLine.fileList[ofLine.length-1].number;
			int endBnum = 63;
			int endDnum;
			int flag =1;
			
			//寻找文件结束位置
			do {
				endDnum = nextDisk;
				buffer1 = Disk.bufferRead(endDnum/64);
				nextDisk = buffer1[endDnum%64];
				
				System.out.println("\nendDnum: "+endDnum+"nextDisk: "+nextDisk);
			
			
			buffer1 = Disk.bufferRead(endDnum);
			
			System.out.println("*********************************"+(byte)('#'));
			for(int i=0;i<64;i++) {
				System.out.println("buffer1[i]"+buffer1[i]);
				if(buffer1[i]==(byte)('#')) {
					System.out.println("hahahhahabreak");
					endBnum = i;
					flag = 0;
					break;
				}
			}
			}while(nextDisk!=-1&&flag==1);
			//
			System.out.println("Index of ofLine:"+(ofLine.length-1)+" \n");
			
			ofLine.change(ofLine.length-1, endDnum, endBnum);
		}
		return ofLine.length-1;
	}
	
	//关闭文件
	public static void close_file(byte blockNum) {
		//用文件的起始地址进行比对
		for(int i=0;i<ofLine.length;i++) {
			if(ofLine.fileList[i].number==blockNum) {
				ofLine.subOFTLE(i);
				break;
			}
		}
	}
	
	//读文件
	//这里如果要比较真实地模拟读取大文件的话应该是多次返
	//回一个缓冲区的内容，同时修改文件指针来记录读取位置，
	//直到传回空文件，我为了省事就没有严格按照这个原则来做了
	public static String read_file(Path path) throws FileException{
		String s = null;
		StringBuffer str = new StringBuffer();
		
		int ofIndex;
		
		if((ofIndex = open_file(path,0))<0) {
			//打开错误
			System.out.println("打开错误！");
			return s;
		}
		
		//读指针初始化
		//不初始化的话就不能多次读文件了
		ofLine.change(ofIndex, ofLine.fileList[ofIndex].number, 0);
		
		int nextBlock;
		
		//读取文件内容到缓冲区中
		while(ofLine.fileList[ofIndex].read.dnum!=-1) {
			nextBlock = Disk.readOnlyByte(0, 0, ofLine.fileList[ofIndex].read.dnum);
			buffer1 = Disk.bufferRead(ofLine.fileList[ofIndex].read.dnum);
			if(nextBlock==-1) {
				for(int i = 0;i<64;i++) {
					if(buffer1[i]==(byte)('#')) {
						break;
					}
					str.append((char)(buffer1[i]));
				}
			}
			else {
				for(int i=0;i<64;i++) {
					str.append((char)(buffer1[i]));
				}
			}
			ofLine.change(ofIndex, nextBlock, 0);
		}
		
		/*
		//先读取前面一整块的数据块
		while((nextBlock = Disk.readOnlyByte(0, 0, thisBlock))!=-1) {
			buffer1 = Disk.bufferRead(thisBlock);
			for(int i = 0;i<64;i++) {
				
			}
			
			
			ofLine.fileList[ofIndex].read.dnum = nextBlock;
			
			thisBlock = nextBlock;
		}
		
		//再读取最后一块的数据
		buffer1 = Disk.bufferRead(thisBlock);
		for(int i=0;i<64;i++) {
			if(buffer1[i]==(byte)('#')) {
				break;
			}
			else {
				
			}
		}*/
		
		s = new String(str);
		
		return s;
	}
	
	//写文件
	public static void write_file(Path path,String content) throws FileException {
		int ofIndex;
		int listNum;
		int blockNum;
		int nextBlock;
		
		//检查文件属性
		buffer2 = Disk.bufferRead(path.blockNum[path.depth-1]);
		listNum = getListNum(path.blockNum[path.depth], buffer2);
		
		if(listNum<0) {
			throw new FileException(-6);
		}
		
		//断点测试
		//System.out.println("\n断前："+listNum);
		
	    if(buffer2[listNum*8+5]%2==1) {
	    	//该文件属性为只读，打开方式错误
	    	throw new FileException(-7);
	    }
		
	    System.out.println("openedFileLength: "+ofLine.length);

	    //打开文件
		if((ofIndex = open_file(path,1))<0) {
			throw new FileException(-8);
		}
		System.out.println("openedFileLength: "+ofLine.length);
		System.out.println("%%ofLine.fileList[ofIndex].write.bnum: "+ofLine.fileList[ofIndex].write.bnum);
		//好像没有什么要检查的了
		//开始写入
		//注意事项：
		//1.添加新块时要同时修改FAT、打开文件列表、存储缓冲区内容
		//2.记得最后加‘#’作为文件的结束符
		//
		//
		blockNum = ofLine.fileList[ofIndex].write.dnum;
		
		buffer1 = Disk.bufferRead(blockNum);
		/*
		int i =0;
		while(i<content.length()) {
			for(int j = ofLine.fileList[ofIndex].write.bnum;j<64;j++) {
				
				i++;
			}
		}*/
		
		//我这里不打算写满一个缓冲区的数据再申请新的空间
		//而是直接申请足够的空间，再慢慢写进去
		//这样可以降低写过程的逻辑复杂程度，不容易出错
		
		System.out.println("$$ofLine.fileList[ofIndex].write.bnum: "+ofLine.fileList[ofIndex].write.bnum);
		for(int i=0;i<(63-ofLine.fileList[ofIndex].write.bnum+content.length())/64;i++) {
			if((nextBlock=searchFreeBlock())==-1) {
				//申请空间不足
				System.out.println("申请空间不足!");
				
				//收回之前分配的空间
				for(blockNum = ofLine.fileList[ofIndex].number;blockNum!=-1;blockNum=Disk.readOnlyByte(0, 0, blockNum)) {
					Disk.writeOnlyByte(0, 0, blockNum, (byte)(-1));
				}
				throw new FileException(-9);
			}
			else {
				Disk.writeOnlyByte(0, 0, blockNum, (byte)nextBlock);
				Disk.writeOnlyByte(0, 0, (byte)nextBlock, (byte)-1);
				blockNum = nextBlock;
			}
		}
		
		System.out.println("**ofLine.fileList[ofIndex].write.bnum: "+ofLine.fileList[ofIndex].write.bnum);
		for(int i=0;i<content.length();i++) {
			if(ofLine.fileList[ofIndex].write.bnum==64) {
				//内存区已满
				
				//记录磁盘信息
				Disk.bufferWrite(ofLine.fileList[ofIndex].write.dnum, buffer1);
				
				//长度加一
				ofLine.fileList[ofIndex].length++;
				
				//修改文件指针
				ofLine.change(ofIndex, Disk.readOnlyByte(0, 0, ofLine.fileList[ofIndex].write.dnum), 0);
				
				//复制数据
				//这里我看到getbytes函数，后面有机会用一下看看能不能提高一下效率
				//逐个字符慢慢换实在是有点慢
				buffer1[0] = (byte)content.charAt(i);
			}
			else {
				buffer1[ofLine.fileList[ofIndex].write.bnum] = (byte)content.charAt(i);
			}
			System.out.println("ofLine.fileList[ofIndex].write.bnum: "+ofLine.fileList[ofIndex].write.bnum);
			ofLine.fileList[ofIndex].write.bnum++;
		}
		
		//加结束符
		buffer1[ofLine.fileList[ofIndex].write.bnum] = (byte)('#');
		
		//最后不足一个缓冲区的内容写回模拟磁盘
		Disk.bufferWrite(ofLine.fileList[ofIndex].write.dnum, buffer1);
		
		//最后修改上级目录的信息
		Disk.writeOnlyByte(path.blockNum[path.depth-1], listNum, 7,(byte) ofLine.fileList[ofIndex].length);
		
	}
	
	//显示文件内容
	public static String typeFile(Path path) {
		//检查文件是否在打开文件列表中
		String s = null;
		//为了文件系统的功能效果，还是把不能显示打开文件的规则去掉
		/*
		int listNum = ofLine.searchOpenedFile(path.blockNum[path.depth]);
		if(listNum!=-1) {
			//文件在已打开列表中
			//（这里我也不明白为什么不能显示已打开文件,但实验指导这
			//么要求了就这么写吧，我觉得其实显示已打开文件也没什么问题）
			System.out.println("\n该文件已打开，不能显示!\n");
			return s;
		}*/
		StringBuffer str = new StringBuffer();
		
		int blockNum = path.blockNum[path.depth];
		int nextBlock = blockNum;
		
		//用字符串缓冲来批量记录
		do {
			blockNum = nextBlock;
			buffer2 = Disk.bufferRead(blockNum/64);
			nextBlock = buffer2[blockNum%64];
			buffer1 =  Disk.bufferRead(blockNum);
			for(int i=0;i<64&&buffer1[i]!=(byte)('#');i++) {
				str.append((char)(buffer1[i]));
			}
		}while(nextBlock>0);
		
		s = new String(str);
		return s;
	}
	
	//改变文件属性
	public static void change(Path path,byte newAttribute) throws FileException {
		if(ofLine.searchOpenedFile(path.blockNum[path.depth])!=-1) {
			//该文件在打开文件列表中
			throw new FileException(-10);
		}
		else {
			//修改上级目录
		    buffer1 = Disk.bufferRead(path.blockNum[path.depth-1]);
			int listNum = getListNum(path.bottomNum(),buffer1);
			Disk.writeOnlyByte(path.blockNum[path.depth-1], listNum, 5, newAttribute);
		}
	}
	
	//***************************************************
	//*               以下为文件夹操作                  *
	//***************************************************
	
	//获取根目录：由cyh补充写入,后续引用该对象时只用到path属性,其它变量的值不一定符合规范。
		public static FileMsg getRootDirectory() {
			char[] name = "A://".toCharArray();
			char[] bName = "".toCharArray();
			byte attri = 8;
			byte blockNum = 2;
			byte length = 0;
			Path path = new Path();
			FileMsg rootMsg = new FileMsg(name,bName,attri,blockNum,length,path);
			return rootMsg;
		}
	
	//建立目录
	public static int md(char[] name,Path path,byte attribute) throws FileException{
		buffer2 = Disk.bufferRead(path.blockNum[path.depth]);
		int blockNum;
		int listLength;
		
		if(searchName(name,buffer2)!=-1) {
			//发生重名
			throw new FileException(-1);
		}
		
		if((blockNum=searchFreeBlock())==-1) {
			//磁盘空间不足
			throw new FileException(-2);
		}
		
		if((listLength=getListLength(path.blockNum[path.depth]))>=8) {
			//上级目录已满
			throw new FileException(-3);
		}
		
		char[] ch = new char[2];
		ch[0] = ch[1] =' ';
		
		//修改上级目录
		addElement(path.blockNum[path.depth],listLength,name,ch,attribute,(byte)blockNum,(byte)0);
		
		//修改FAT
		Disk.writeOnlyByte(0, 0, blockNum, (byte)-1);
		
		return blockNum;
	}
	
	//显示目录内容
	public static ArrayList<FileMsg> dir(Path path) {
		ArrayList<FileMsg> ary = new ArrayList<FileMsg>();
		int listLength = getListLength(path.blockNum[path.depth]);
		buffer1 = Disk.bufferRead(path.blockNum[path.depth]);
		for(int i=0;i<listLength;i++) {
			//之前这里转录文件信息时没有进行初始化
			//导致出现bug,后面加上上级路径传输和路径加工的代码
			//希望这里不会再出bug了       by 骚明 9.25
			ary.add(new FileMsg(buffer1,i,path));
			
			/*之前奕涵说调用这个函数有错，然后去看了一下发现调用这个函数后
			 * 文件路径发生变化，跑到下面去了，估计是调用修改的时候直接传地址而不是
			 * 传参数，后面我改了一下，给Path加了两个直接返回修改后的值的函数，调用这
			 * 两个函数来获取下级目录项的路径，测试了一下，没有发现错误，应该能过
			 *                   by 骚明 11.30
			 */
		}
		return ary;
	}
	
	//删除目录
	public static void rd(Path path) throws FileException {
		//先检查目录中是否存在已打开文件
		Path newPath;
		FileMsg fm;
		
		for(int i=0;i<ofLine.length;i++) {
			newPath = ofLine.fileList[i].path;
			if(path.depth<=newPath.depth) {
				//打开文件深度比要删除目录更浅，可以肯定该文件不在
				//要删除的目录中,故跳过该目录
				continue;
			}
			if(path.blockNum[path.depth]==newPath.blockNum[path.depth]) {
				//有已打开文件在该目录中
				throw new FileException(-14);
			}
		}
		
		//查看该目录
		buffer1 = Disk.bufferRead(path.blockNum[path.depth]);
		
		//要一边删除目录或文件，一边删除目录项
		//从目录尾部开始删，感觉这样不容易出错
		for(int i=getListLength(path.blockNum[path.depth])-1;i>=0;i--) {
			fm = new FileMsg(buffer1,i,path);
			//**************************************************************
			newPath = path;
			
			newPath.PathDown(fm.fileName, fm.startBlock);
			if((fm.attribute/8)%2==1) {	//是目录
				rd(newPath);            //递归删除文件夹
			}
			else {                     //是文件
				delete_file(newPath);
			}
			
			//最后修改列表
			//deletElement(path.blockNum[path.depth],i);
		}
		//修改根目录
		
		delete_file(path);
		//path.PathUp();
	    //System.out.println("修改根目录块"+path.blockNum[path.depth]+"对应的目录项");
		//int index = getListNum(path.blockNum[path.depth],Disk.bufferRead(path.blockNum[path.depth-1]));
		//deletElement(path.blockNum[path.depth-1],index);
		
		//修了个无法清除文件夹根目录对应注册项的bug
		//by 骚明  11.23
		return;
	}
}
