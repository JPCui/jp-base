package cn.cjp.base;

import org.apache.log4j.Logger;
import org.junit.Test;


public class AppTest {
	
	private static final Logger logger = Logger.getLogger(AppTest.class);

	public static void main(String[] args) {
	}
	
	@Test
	public void test(){
		String s = "asd.html";
		
		System.out.println(s.replaceAll("[.\\s]", "/"));
		
		logger.info("123");
	}
	
}
