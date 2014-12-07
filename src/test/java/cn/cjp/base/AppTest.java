package cn.cjp.base;

import org.junit.Test;


public class AppTest {

	@Test
	public void test(){
		String s = "asd.html";
		
		System.out.println(s.replaceAll("[.\\s]", "/"));
	}
	
}
