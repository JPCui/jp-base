package cn.cjp.base.utils;


/**
 * 数字转换
 * 
 * @author 崔金鹏
 * 
 */
public class NumberParserUtil {

	private static String charSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String _10T62(Long N10) {
		String code = "";
		while (N10 != 0) {
			int yuShu = (int) (N10 % 62);
			N10 = N10 / 62;

			code = charSet.charAt(yuShu) + code;
		}
		return code;
	}

	/**
	 * 将62进制转换成10进制数
	 * 
	 * @param ident62
	 * @return
	 */
	public static long _62T10(String n62) {

		long n10 = 0;
		int length = n62.length();
		for (int i = 0; i < length; i++) {
			long l = charSet.indexOf(n62.charAt(i));

			l = l * (long) Math.pow(64, length - i - 1);

			n10 += l;
			
			System.out.print("n10 = "+n10);
			System.out.println("\tl = "+l);
		}

		return n10;
	}

	@SuppressWarnings("unused")
	private static String padLeft(String str, int len, char ch) {
		while (str.length() < len) {
			str += ch;
		}
		return str;
	}

	public static String MID2ID(String n62) {
		String[] surl = new String[3];
		surl[2] = n62.substring(n62.length() - 4);
		surl[1] = n62.substring(n62.length() - 8, n62.length() - 4);
		surl[0] = n62.substring(0, n62.length() - 8);
		
		System.out.println(surl[0] + "..." + surl[1] + "..." + surl[2]);
		
		surl[2] = _62T10(surl[2]) + "";// 倒数第二段，取3位，转10进制
		surl[1] = _62T10(surl[1]) + "";// 倒数第三段，取1位，转10进制
		surl[0] = _62T10(surl[0]) + "";// 第一段，取剩下位数，转10进制
		
//		return (Long.parseLong(surl[0]) + Long.parseLong(surl[1]) + Long.parseLong(surl[2]) + Long.parseLong(surl[3]))+"";
		System.out.println(surl[0]+"..."+surl[1]+"..."+surl[2]);
		return surl[0] + surl[1] + surl[2];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * 37494060310785987
		 * 3474 9208 9598 9800 L
		 */
		System.out.println(MID2ID("BiScXF9j3"));
		/**
		 * 3403 5804 8209 2801
		 */
		System.out.println(MID2ID("y1v8Y8MqR"));

//		System.out.println(_62T10("BiScXF9j3"));

	}

	public static Float toFloat(String s) {
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			return new Float(0);
		}
	}

}
