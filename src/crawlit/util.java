package crawlit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class util {
	
	public static void delete_line_break(String str) throws Exception {
		
		str = str.replaceAll("[\\t\\n\\r]", "");//将内容区域的回车换行去除
		
	}

	public static void main(String[] args) throws Exception {
		System.out.println("main() in");
		
		String text = "auser1 home1b\n" +
				"auser2 home2b\n" +
				"auser3 home3b";
		System.out.println(text);
		
		delete_line_break(text);
		
		System.out.println("main() out");
	}
}
