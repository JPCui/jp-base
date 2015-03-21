package cn.cjp.utils;

/**
 * 数学公式工具类
 * 
 * @author Administrator
 * 
 */
public class MathUtil {

	/**
	 * Returns the smallest (closest to negative infinity) double value that is
	 * greater than or equal to the argument and is equal to a mathematical
	 * integer
	 * 
	 * @param num
	 * @return
	 */
	public static double ceil(double num) {
		return Math.ceil(num);
	}

	public static void main(String[] args) {
		System.out.println(Math.ceil(1.1));
		System.out.println(Math.ceil(1.8));
		System.out.println(Math.floor(1.9));
		System.out.println(Math.floor(1.0));
		System.out.println(Math.floor(1.1));

	}

}
