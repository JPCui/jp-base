package cn.cjp.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.apache.commons.lang.StringUtils;

import cn.cjp.base.summary.Document.Word;

/**
 * 停用词工具类（分词，去停用词，判断停用词）
 *
 */
public class StopWordsUtil {
	private Map<String, Boolean> stopWords = new HashMap<String, Boolean>(); 
	
	public StopWordsUtil()
	{
		try {
			loadStopWords();
		} catch (Exception e) {
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		StopWordsUtil stopWordsUtil = new StopWordsUtil();
		
		List<List<String>> strs = new ArrayList<List<String>>();
		List<String> str = new ArrayList<String>();
		
		str.add("哈");
		str.add("任命");
		str.add("胡锦涛");
		str.add("啊");
		str.add("书记");
		strs.add(str);
		
		strs = stopWordsUtil.removeAllStopWords(strs);
	}
	
	/**
	 * 去除停用词
	 * @param words
	 * @return
	 */
	public List<List<String>> removeAllStopWords(List<List<String>> strs)
	{
		List<List<String>> freshStrs = new ArrayList<List<String>>();
		
		for(List<String> words : strs)
		{
			List<String> freshWords = this.removeStopWords(words);
			freshStrs.add(freshWords);
		}
		
		return freshStrs;
	}

	/**
	 * 去除停用词
	 * @param strs
	 * @return
	 */
	public List<String> removeStopWords(List<String> strs)
	{
		List<String> results = new ArrayList<String>();
		for(int i=0; i<strs.size(); i++)
		{
			String t = this.trim(strs.get(i));
			
			if(StringUtils.isBlank(t))continue;
			/** 判断是否为停用词 **/
			if(t.length() == 1) continue;//词长度为1，认为是停用词
			if(!isStopWord(t) && !(t.charAt(0)<='9' && t.charAt(0)>='0') && !(t.charAt(0)<='z' && t.charAt(0)>='a') && !(t.charAt(0)<='Z' && t.charAt(0)>='A'))
			{
				results.add( t );
			}
		}
		return results;
	}

	public List<Word> removeStopWord(List<Word> words) {
		List<Word> results = new ArrayList<Word>();
		for(int i=0; i<words.size(); i++)
		{
			String t = this.trim(words.get(i).str);
			
			if(StringUtils.isBlank(t))continue;
			/** 判断是否为停用词 **/
			if(t.length() == 1) continue;//词长度为1，认为是停用词
			if(!isStopWord(t) && !(t.charAt(0)<='9' && t.charAt(0)>='0') && !(t.charAt(0)<='z' && t.charAt(0)>='a') && !(t.charAt(0)<='Z' && t.charAt(0)>='A'))
			{
				results.add( words.get(i) );
			}
		}
		return results;
		
	}
	
	/**
	 * 判断是否是停用词
	 * @param word
	 * @return
	 */
	public boolean isStopWord(String word)
	{
		return stopWords.containsKey(word);
	}
	
	/**
	 * 加载词库
	 * @throws FileNotFoundException 
	 * @throws DirectoryNotEmptyException 
	 */
	public void loadStopWords() throws DirectoryNotEmptyException, FileNotFoundException
	{
		File dir = new File("D:\\zutnlp-data\\library\\stopwords");
		//File dir = new File("src\\main\\resources\\stopwords");
		if(!dir.isDirectory())
		{
			throw new DirectoryNotEmptyException("路径"+dir.getAbsolutePath()+"未找到");
		}
		File[] files = dir.listFiles();
		for(int i=0; i<files.length; i++)
		{
			BufferedReader br = new BufferedReader(new FileReader(files[i]));
			String line = "";
			try {
				while((line = br.readLine())!=null)
				{
					stopWords.put(line, true);
				}
				br.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 去除空格
	 * @param str
	 * @return
	 */
	public String trim(String str)
	{
		String _62_96 = new String(new byte[]{-62,-96});
		
		str = str.trim();
		while(str.startsWith(_62_96))
		{
			str = str.substring(1);
		}
		while(str.endsWith(_62_96))
		{
			str = str.substring(0, str.length()-1);
		}
		
		return str;
	}
	
	/**
	 * 分词
	 * @param strs 待分词字符串数组
	 * @return
	 */
	public List<List<String>> cutWords(List<String> strs)
	{
		System.err.println("Cutting Words ...");
		List<List<String>> cons = new ArrayList<List<String>>();
		for (String s : strs) {
			cons.add(this.cutWords(s));
		}
		return cons;
	}

	/**
	 * 分词
	 * @param str 待分词字符串
	 * @return
	 */
	public List<String> cutWords(String str)
	{
		List<String> strs = new ArrayList<String>();
		List<Term> terms = NlpAnalysis.parse(str);
		
		for (int i = 0; i < terms.size(); i++) {
			String t = terms.get(i).getName().trim();
			if(!StringUtils.isBlank(t))
				strs.add(t);
		}
		return strs;
	}

	public Map<String, Boolean> getStopWords() {
		return stopWords;
	}

	public void setStopWords(Map<String, Boolean> stopWords) {
		this.stopWords = stopWords;
	}

}
