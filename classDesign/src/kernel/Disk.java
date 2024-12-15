package kernel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * 模拟磁盘文件管理系统假末重制版
 * 
 * 之前写的感觉像一坨翔，现在重新写一遍，顺便
 * 弄一下之前死活弄不出来的缓冲区读写文件功能 
 *               8.24 by 骚明
 */

public class Disk {
    static byte[][] disk = null;
	static RandomAccessFile raf = null;
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		/*
		disk = new byte[128][64];
        for(int i=0;i<128;i++) {
        	for(int j=0;j<64;j++) {
        		disk[i][j] = 0;
        	}
        }
        disk[0][2] = -1;
        save();
        load();
        
        byte[] buffer = new byte[64];
        for(int i=0;i<64;i++) {
        	buffer[i] = 49;
        }
        bufferWrite(4, buffer);
        
        diskPrint();
        
        freeBlock(4);
        
        diskPrint();
        */
		//diskPrint();
	}
	
	public static void start_statu() {
		 byte[] buffer = new byte[64];
	     for(int i=0;i<64;i++) {
	        buffer[i] = 0;
	     }
	     for(int i=0;i<=5;i++) {
	    	 bufferWrite(i,buffer);
	     }
	     writeOnlyByte(0,0,2,(byte)-1);
	}
	
    public static void load() {
    	//读取文件内容，看看缓冲区读写方法有没有问题，
    	//如果出问题解决不了就用这个加上二维数组来存取数据
    	//还有，如果打印磁盘文件内容太
    	try {
    		int n = 0;
    		raf = new RandomAccessFile("data.txt","rw");
    		for(long i=0;i<raf.length();i++) {
    			raf.seek(i);
    			n = raf.readByte();
    			disk[(int) (i/64)][(int) (i%64)] = (byte) n;
    			System.out.printf("%3d",n);
    			if(i%64==63) {
    				System.out.printf("\n");
    			}
    		}
    		raf.close();
    	}catch(IOException e) {
    		
    	}
    	finally {
    	
    	}
    	
    }
    public static void save() {
    	//存储内容，同上，只是作为后备方案使用
    	try {
    		raf = new RandomAccessFile("data.txt","rw");
    		for(int i=0;i<128;i++) {
    			for(int j=0;j<64;j++) {
    				raf.seek(i*64+j);
    				raf.writeByte((byte)(disk[i][j]));
    			}
    		}
    		raf.close();
    	}catch(IOException e) {
    		
    	}
    }
    
	public static byte[] bufferRead(int indexOfSector) {
		//读文件
    	byte[] buffer = null;
    	try {
    		raf = new RandomAccessFile("data.txt","rw");
    		long pointer = indexOfSector*64;
    		buffer = new byte[64];
    		
    		System.out.printf("\nRead %d :", indexOfSector);
    		
    		//逐个读取模拟磁盘内容到缓冲区中
    		for(int i=0;i<64;i++) {
    			raf.seek(pointer+i);
    			buffer[i] = raf.readByte();
    			System.out.printf(" %d",buffer[i]);
    		}
    		System.out.println("");
    		raf.close();
    	}catch(IOException e) {
    		System.out.println("读取文件失败："+e);
    	}finally {
    		
    	}
    	return buffer;
    }
    
    public static void bufferWrite(int indexOfSector,byte[] buffer) {
    	//往文件写
        try {
        	raf = new RandomAccessFile("data.txt","rw");
        	long pointer = indexOfSector*64;
        	
        	System.out.printf("\nWrite %d :", indexOfSector);
        	
        	//逐个将缓冲区单元写入到模拟磁盘中
        	for(int i=0;i<64;i++) {
        		raf.seek(pointer+i);
        		raf.writeByte(buffer[i]);
        		System.out.printf(" %d", buffer[i]);
        	}
        	System.out.println("");
        	raf.close();
        }catch(IOException e) {
        	System.out.println("写入文件失败："+e);
        }finally {
        	
        }
    }
    
    public static void writeOnlyByte(int blockNum,int listNum,int bNum,byte word) {
    	//写单个字符
    	try {
    		raf = new RandomAccessFile("data.txt","rw");
        	long pointer = blockNum*64+listNum*8+bNum;
        	raf.seek(pointer);
        	raf.writeByte(word);
        	
        	System.out.printf("\nWrite single word :%d in %dblock ,%d list,%d byte\n", word, blockNum,listNum,bNum,word);
        	
        	raf.close();
    	}catch(IOException e){
        	System.out.println("写入文件失败："+e);
    	}
    }
    
    public static byte readOnlyByte(int blockNum,int listNum,int bNum) {
    	byte b = -2;
    	try {
    		raf = new RandomAccessFile("data.txt","rw");
    		long pointer = blockNum*64+listNum*8+bNum;
    		raf.seek(pointer);
    		b = raf.readByte();
    		
            System.out.printf("\nRead single word :%d ,%d block ,%d list,%d byte", b, blockNum,listNum,bNum);
        	
        	raf.close();
    	}catch(IOException e) {
    		System.out.println("读取文件失败："+e);
    	}
    	return b;
    }
    
    public static byte[] listR(int index) {
    	//复制数组，也是后备方案用的
    	byte[] buffer = null;
    	buffer = new byte[64];
    	for(int i=0;i<64;i++) {
    		buffer[i] = disk[index][i];
    	}
    	return buffer;
    }
    
    public static void listW(int index,byte[] buffer) {
    	//复制数组到二维数组中，同上，后备方案
    	for(int i=0;i<64;i++) {
    		disk[index][i] = buffer[i];
    	}
    }
    
    public static void diskPrint() {
    	//打印模拟硬盘信息，主要用于调试系统
    	byte[] buffer = new byte[64];
    	for(int i=0;i<8;i++) {
    		buffer = bufferRead(i);
    		//for(int j=0;j<64;j++) {
    		//	System.out.printf("%3d",buffer[j]);
    		//}
    		System.out.println("");
    	}
    }
    
    public static void freeBlock(int index) {
    	byte[] buffer = new byte[64];
    	for(int i=0;i<64;i++) {
    		buffer[i] = 0;
    	}
    	bufferWrite(index, buffer);
    	
    	//修改FAT
    	buffer = bufferRead(index/64);
    	buffer[index%64] = 0;
    	bufferWrite(index/64,buffer);
    }
}
