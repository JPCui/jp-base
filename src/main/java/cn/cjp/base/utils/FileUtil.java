package cn.cjp.base.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

public class FileUtil {

	/**
	 * 拷贝文件
	 * 
	 * @param srcFile
	 * @param destFile
	 */
	public static void moveFile(File srcFile, File destFile) {
		try {
			FileUtils.copyFile(srcFile, destFile);
			FileUtils.deleteQuietly(srcFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拷贝文件，并删除源文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param filePath
	 *            目标文件路径
	 * @param destFile
	 *            目标文件
	 * @param updateFileName
	 *            如果有重复文件名，是否自动更新文件名
	 */
	public static void moveFile(File srcFile, String filePath, File destFile,
			boolean updateFileName) {
		if (updateFileName) {
			// 首先判断 destFile是否存在
			String destFileName = destFile.getName();
			int index = 0;
			while (destFile.exists()) {
				index++;
				if (destFile.getName().startsWith("(Copy" + (index) + ")"))
					continue;
				if (destFile.getName().startsWith("(Copy" + (index - 1) + ")"))
					destFile.renameTo(new File(filePath + "(Copy" + (index)
							+ ")" + destFileName.substring(7)));
				else
					destFile.renameTo(new File(filePath + "(Copy" + (index)
							+ ")" + destFileName));
			}
		}
		// 移动文件
		try {
			FileUtils.copyFile(srcFile, destFile);
			try {
				if (!srcFile.delete()) {
					SecurityManager manager = new SecurityManager();
					manager.checkDelete(srcFile.getAbsolutePath());
					srcFile.deleteOnExit();
				}
			} catch (Exception e) {
			}
		} catch (IOException e) {
		}
	}

	/**
	 * 按行读取文件，忽略空行
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<String> read(String fileName) {
		File dirFile = new File(fileName);
		if (!dirFile.exists()) {
			return new ArrayList<String>();
		}
		List<String> contents = new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
			String s = "";
			while ((s = br.readLine()) != null) {
				s.replace(" ", "");
				if (s.trim().equals(""))
					continue;
				contents.add(s);
			}
			br.close();
		} catch (Exception e) {
		}
		return contents;
	}

	/**
	 * 按行读取文件，忽略空行
	 * 
	 * @param file
	 * @return
	 */
	public static List<String> read(File file) {
		List<String> contents = new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String s = "";
			while ((s = br.readLine()) != null) {
				s.replace(" ", "");
				if (s.trim().equals(""))
					continue;
				contents.add(s);
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return contents;
	}

	public static List<String> read(BufferedReader br) {
		List<String> contents = new ArrayList<String>();

		try {
			String s = "";
			while ((s = br.readLine()) != null) {
				if (s.trim().equals(""))
					continue;
				s.replace(" ", "");
				contents.add(s);
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		return contents;
	}

	/**
	 * @param str
	 *            要写的内容
	 * @param fileName
	 *            （文件路径）文件名
	 * @param append
	 *            是否追加
	 * @throws IOException
	 */
	public static void write(String str, String fileName, boolean append)
			throws IOException {
		// 写入文件的url
		String urlcontent = null;
		BufferedWriter bw1;
		urlcontent = str + '\n';
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		bw1 = new BufferedWriter(new FileWriterWithEncoding(file, "UTF-8",
				append));
		bw1.write(urlcontent);
		bw1.flush();
		bw1.close();
	}

	/**
	 * 写文件
	 * 
	 * @param str
	 *            要写的内容
	 * @param fileName
	 *            （文件路径）文件名
	 * @param code
	 *            编码
	 * @param append
	 *            是否追加
	 * @throws IOException
	 */
	public static void write(String str, String fileName, String code,
			boolean append) throws IOException {
		BufferedWriter bw1;
		str = str + '\n';
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		bw1 = new BufferedWriter(new FileWriterWithEncoding(file, code, append));
		bw1.write(str);
		bw1.flush();
		bw1.close();
	}
}
