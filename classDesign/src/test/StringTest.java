package test;

public class StringTest {

	public StringTest() {
		// TODO 自动生成的构造函数存根
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String s = new String("Sa");
		char s1[]=new char[3];
		//s.getChars(0, 3, s1, 0);
		//String s2=new String (s1);
		String s2 = s.substring(0,3);
		s1 = s2.toCharArray();
		System.out.printf("%d,%d",s2.length(),s2.toCharArray().length);
	}

}
