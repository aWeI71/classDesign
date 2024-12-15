package kernel;

import java.util.ArrayList;

public class ControlerTable {

	/*
	 * 创建文件之后好像没有同时修改好FAT
	 */
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
        FileManage fm = new FileManage();
        String str = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        char[] name = new char[3];
        name[0] = name[1] = 'S';
        name[2] = 'R';
        char[] backName = new char[2];
        backName[0] = 'p';
        backName[1] = 'g';
        
        
        
        Path path = new Path();
        //path.PathDown(("abc").toCharArray(), 3);
        //path.PathDown(("def").toCharArray(), 4);
        //System.out.println(path.getPathName());
        //path = new Path();
        //Disk.start_statu();
        
        
        
        
        int blockNum = 0;
		try {
			blockNum = FileManage.create_file(name, backName, (byte)4, path);
		} catch (FileException e) {
			System.out.println("FileException:"+e);
		}
        if(blockNum<0) {
        System.out.println("create Failed!:"+blockNum);
        }
        else {
        //int blockNum = 4;
        
        	path.PathDown(name, blockNum);
        	
        	try {
				FileManage.open_file(path, 1);
			} catch (FileException e) {
				System.out.println("FileException:"+e);
			}
        	
        	System.out.println("           打开文件完成！");
        	
        	try {
				FileManage.write_file(path, str);
			} catch (FileException e) {
				System.out.println("FileException:"+e);
			}
        	
        	System.out.println("            写程序完成");
        	
            try {
				FileManage.write_file(path, str);
			} catch (FileException e) {
				System.out.println("FileException:"+e);
			}
        	
        	System.out.println("            写程序完成");
        	
        	Disk.bufferRead(2);
        	
        	FileManage.close_file((byte)path.blockNum[path.depth]);
        	
        	System.out.println("             文件已关闭\n已打开文件还有"+FileManage.ofLine.length+"个");
        	
        	try {
				FileManage.change(path, (byte)6);
			} catch (FileException e) {
				System.out.println("FileException:"+e);
			}
        	
        	System.out.println("文件性质已改变！");
        	
        	try {
				System.out.println("\n"+FileManage.read_file(path));
			} catch (FileException e) {
				System.out.println("FileException:"+e);
			}
        	
        	System.out.println("             文件阅读完成");
        	
        	FileManage.close_file((byte)path.blockNum[path.depth]);
        	
        	
        	System.out.println("              文件已关闭");
        	
            ArrayList<FileMsg> fmsg = new ArrayList<FileMsg>();
            
            Path folderPath = path.clone();
            folderPath.PathUp();
            
            fmsg = FileManage.dir(folderPath);
        	
        	System.out.println("接下来打印2号块存储的目录：");
        	
        	for(int i = 0;i<fmsg.size();i++) {
        		FileMsg fmg = fmsg.get(i);
        		//System.out.println("文件名:"+fmg.fileName.toString()+"."+fmg.backName.toString()
        		//+" 文件性质："+fmg.attribute+"\n文件起始块："+fmg.startBlock+" 文件长度："+fmg.length);
        		System.out.printf("文件名: %s.%s,文件性质:%d,文件起始块：%d,文件长度：%d\n",new String(fmg.fileName),new String(fmg.backName),fmg.attribute,fmg.startBlock,fmg.length );
        	}
        	
            System.out.println("接下来打印2号块存储的目录：");
            fmsg.clear();
            fmsg = FileManage.dir(folderPath);
        	for(int i = 0;i<fmsg.size();i++) {
        		FileMsg fmg = fmsg.get(i);
        		//System.out.println("文件名:"+fmg.fileName.toString()+"."+fmg.backName.toString()
        		//+" 文件性质："+fmg.attribute+"\n文件起始块："+fmg.startBlock+" 文件长度："+fmg.length);
        		System.out.printf("文件名: %s.%s,文件性质:%d,文件起始块：%d,文件长度：%d\n",new String(fmg.fileName),new String(fmg.backName),fmg.attribute,fmg.startBlock,fmg.length );
        	}
        	
        	FileManage.delete_file(path);
        	
        	System.out.println("               文件已删除！");
        	/*
        	try {
        		//建立文件夹
        		System.out.println("建立文件夹\n");
        		path = new Path();
        		int bn = FileManage.md(("abc").toCharArray(), path, (byte)8);
        		//建立文件
        		System.out.println("建立文件\n");
        		path.PathDown(("abc").toCharArray() , bn);
        		//FileManage.md(backName, folderPath,(byte) 6);
        		
        		int fn =FileManage.create_file(("def").toCharArray(), ("tx").toCharArray(), (byte)4, path);
        		path.PathDown(("def").toCharArray(), fn);
        		
        		FileManage.write_file(path, str);
        		FileManage.write_file(path, str);
        		FileManage.close_file((byte)path.blockNum[path.depth]);
        		//删除文件夹
        		System.out.println("删除文件夹\n");
        		System.out.println("\t文件目录块："+path.blockNum[path.depth]+"\n");
        		path.PathUp();
        		System.out.println("\t文件目录块："+path.blockNum[path.depth]+"\n");
        		//path.PathUp();
        		//System.out.println("\t文件目录块："+path.blockNum[path.depth]+"\n");
        		FileManage.rd(path);
        		
        	}catch(FileException  e) {
        		System.out.println("错误:"+e.message+"\n");
        	}*/
        	
        	Disk.bufferRead(4);
        	Disk.bufferRead(3);
        	Disk.bufferRead(2);
        }
        
	}
}
