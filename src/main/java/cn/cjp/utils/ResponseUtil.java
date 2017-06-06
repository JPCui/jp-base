package cn.cjp.utils;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {

	public static void print(HttpServletResponse response, String content) throws IOException {
		ServletOutputStream os = response.getOutputStream();
		os.print(content);
		os.flush();
	}

}
