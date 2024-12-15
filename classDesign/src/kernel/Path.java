package kernel;

public class Path {
	//当前路径信息
    char[][] PathName;  //路径上的目录或文件名称，我有些代码是直接拿之前假期开始
                        //时写的版本，用小写开头结果出现很多冲突的地方，只能用不规范
                        //的开头大写来对文件进行命名    by 骚明
    int[] blockNum;    //路径途径目录起始块
    int depth; //当前路径深度
    
    public Path() {
    	PathName = new char[100][4];
    	blockNum = new int[100];
    	depth = 0;
    	PathName[depth][0] ='A';
    	PathName[depth][1] =':';
    	PathName[depth][2]='/';
    	PathName[depth][3] = '\0';
        blockNum[depth] = 2;
    }
    
    //返回路径
    public void PathUp() {
    	depth--;
    }
    
    //打开下级目录
    public void PathDown(char[] s,int downNum) {
    	depth++;
    	PathName[depth] = s;
    	blockNum[depth] = downNum;
    }
    
    public Path getPathUp() {
    	Path newPath = this.clone();
    	if(depth<1) {
    		return newPath;
    	}
    	newPath.PathUp();
    	return newPath;
    }
    
    public Path getPathDown(char[] s,int downNum) {
    	Path newPath = this.clone();
    	newPath.PathDown(s, downNum);
    	return newPath;
    }
    
    public int bottomNum() {
    	return blockNum[depth];
    }
    
    public char[] bottomName() {
    	return PathName[depth];
    }
    
    public Path clone() {
    	Path path = new Path();
    	path.PathName = PathName.clone();
    	path.blockNum = blockNum.clone();
    	path.depth = depth;
    	return path;
    }
    
    public String getPathName() {
    	StringBuffer str = new StringBuffer();
    	for(int i=0;i<=depth;i++) {
    		for(int j=0;j<3;j++) {
    			if(PathName[i][j]=='\0') {
    				break;
    			}
    			str.append(PathName[i][j]);
    		}
    		if(i<depth) {
    			str.append('/');
    		}
    	}
    	return (new String(str));
    }
    
}