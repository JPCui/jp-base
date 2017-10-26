package cn.cjp;

import com.google.common.html.HtmlEscapers;

public class Main {

	public static void main(String[] args) {
		System.out.println("htts://baidu.com".matches(("(http|https)://.+")));
		
		System.out.println(HtmlEscapers.htmlEscaper().escape("<a href=\"#\">A</a>"));

	}

}
