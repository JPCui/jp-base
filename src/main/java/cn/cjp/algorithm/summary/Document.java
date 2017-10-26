package cn.cjp.algorithm.summary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import cn.cjp.utils.StopWordsUtil;


/**
 * 文档类
 * <br>设置相应的字段，会自动断句，分词
 * <br>该类不考虑词在全局中的位置，只考虑词所在的段落，句子及在句子中的位置
 */
public class Document {

	/** 标题 */
	public String title;
	/** 段落集合 */
	public List<Paragraph> paragraphs = new ArrayList<Document.Paragraph>();
	/** 所有的词 */
	public List<Word> allWords;
	/** 词在某段的词频，若词在一段中没有出现，则 null */
	Map<String, Map<Integer, Integer>> fullWordsFrequencyMap;
	/** 词频 */
	Map<String, Integer> wordsFrequencyMap;
	/** 跨段词频 */
	Map<String, Integer> wordsCrossParFrequencyMap;
	private static StopWordsUtil stopWordsUtil = new StopWordsUtil();
	
	@SuppressWarnings("unused")
	private Document() {	}
	/**
	 * 
	 * @param title 标题
	 * @param paragraphs 段落数组
	 */
	public Document(String title, List<String> paragraphs)
	{
		this.title = title;
		//遍历段落
		for(int parIndex=0; parIndex<paragraphs.size(); parIndex++)/* parIndex : 段的下标 */
		{
			this.paragraphs.add(new Paragraph().setParagraph(paragraphs.get(parIndex), parIndex));
		}
		this.computWeight();
	}
	
	/**
	 * 段落
	 */
	public class Paragraph{
		
		/** 内容 */
		public String str;
		/** 权重 */
		public double weight = 0;
		/** 句子集合 */
		public List<Sentence> sentences = new ArrayList<Document.Sentence>();
		
		private Paragraph(){}
		public Paragraph setParagraph(String parStr, int sInPar_index)
		{
			Paragraph paragraph = new Paragraph();
			paragraph.str = parStr;
			//分句
			List<String> sens = TextUtil.divSentence(paragraph.str);
			for(int i=0; i<sens.size(); i++)
			{
				paragraph.sentences.add(new Sentence().setSentence(sens.get(i), sInPar_index, i));
			}
			return paragraph;
		}
		
	}
	
	/**
	 * 句子
	 */
	public class Sentence{
		
		/** 句子内容 */
		public String str;
		/** 权重 */
		public double weight = 0;
		/** 词集合 */
		public List<Word> words = new ArrayList<Document.Word>();
		/** 所属段落 */
		public int par_index;
		/** 在段中的位置 */
		public int sInPar_index;
		
		private Sentence(){}
		public Sentence setSentence(String str, int par_index, int sInPar_index)
		{
			Sentence sentence = new Sentence();
			sentence.str = str;
			sentence.sInPar_index = sInPar_index;
			
			List<Term> terms = NlpAnalysis.parse(str);//分词
			for(int i=0; i<terms.size(); i++)
			{
				sentence.words.add(new Word().setWord(terms.get(i), par_index, sInPar_index, i));
			}
			return sentence;
		}
		
	}
	
	public class Word{
		
		/** 内容 */
		public String str;
		/** 权重 */
		public double weight = 0;
		/** 词性 */
		public String speech;
		/** 所属段落 */
		public int p_index;
		/** 所属句子 */
		public int s_index;
		/** 词在句子中的位置 */
		public int wInSentence_index;
		
		private Word(){}
		public Word setWord(Term term, int p_index, int s_index, int wInSentence_index)
		{
			Word word = new Word();
			word.str = term.getName();
			word.speech = term.getNatrue().natureStr;
			word.p_index = p_index;
			word.s_index = s_index;
			word.wInSentence_index = wInSentence_index;
			return word;
		}
	}
	/**
	 * 获取所有的词
	 * @return
	 */
	public List<Word> getAllWords()
	{
		if(this.allWords != null)	return this.allWords;
		this.allWords = new ArrayList<Document.Word>();
		for(int i=0; i<this.paragraphs.size(); i++)
		{
			Paragraph par = this.paragraphs.get(i);
			for(int j=0; j<par.sentences.size(); j++)
			{
				Sentence sen = par.sentences.get(j);
				for (int k = 0; k < sen.words.size(); k++) {
					allWords.add(sen.words.get(k));
				}
			}
		}
		return allWords;
	}
	/**
	 * 计算词频，跨段词频
	 * @return
	 */
	public void computWordFrequency()
	{
		if(this.allWords == null)
		{
			this.getAllWords();
		}
		fullWordsFrequencyMap = new HashMap<String, Map<Integer,Integer>>();
		List<Word> words = this.allWords;
		for(int i=0; i<words.size(); i++)
		{
			Word word = words.get(i);
			if(fullWordsFrequencyMap.get(word.str) == null)
			{
				Map<Integer, Integer> map = new HashMap<Integer, Integer>();
				map.put(word.p_index, 1);
				fullWordsFrequencyMap.put(word.str, map);
			}
			else
			{
				Map<Integer, Integer> map = fullWordsFrequencyMap.get(word.str);
				if(map.get(word.p_index) == null)
				{
					map.put(word.p_index, 1);
					fullWordsFrequencyMap.put(word.str, map);
				}
				else
				{
					map.put(word.p_index, map.get(word.p_index)+1);
					fullWordsFrequencyMap.put(word.str, map);
				}
				
			}
		}
		wordsFrequencyMap = new HashMap<String, Integer>();
		wordsCrossParFrequencyMap = new HashMap<String, Integer>();
		for(String str : fullWordsFrequencyMap.keySet())
		{
			Map<Integer, Integer> map = fullWordsFrequencyMap.get(str);
			wordsCrossParFrequencyMap.put(str, map.size());
			Integer sum = 0;
			for(Integer par_index : map.keySet())
			{
				sum += map.get(par_index);
			}
			wordsFrequencyMap.put(str, sum);
		}
	}
	
	/**
	 * 计算权重
	 * <br>词权重 ==》 句子权重 《== 初级段落权重
	 * <br>初级段落权重：只按段落的位置来判定
	 * <br>段落的权重还没写，目前还没必要
	 */
	private void computWeight(){
		//先计算词频
		if(this.wordsFrequencyMap == null)
		{
			computWordFrequency();
		}
		// 开始计算词频
		for(int i=0; i<this.paragraphs.size(); i++)
		{
			Paragraph par = this.paragraphs.get(i);
			for(int j=0; j<par.sentences.size(); j++)
			{
				Sentence sen = par.sentences.get(j);
				// 计算每个词的权重
				for (int k = 0; k < sen.words.size(); k++) {
					Word currWord = sen.words.get(k);	//当前遍历到的词
					double weight = 0;
					weight = SummaryConst.WEIGHT_OF_MULTY_IN_DOC * wordsFrequencyMap.get(currWord.str)
							+SummaryConst.WEIGHT_OF_CROSS_PARAGRAPH * wordsCrossParFrequencyMap.get(currWord.str)
							+SummaryConst.WEIGHT_OF_IN_TITLE * (isInTitle(currWord.str)?1:0) * 0.5
							+SummaryConst.WEIGHT_OF_FIRST_PARAGRAPH * (isFirstGraph(currWord)?1:0) * 0.5
							+SummaryConst.WEIGHT_OF_FINAL_PARAGRAPH * (isLastGraph(currWord)?1:0) * 0.5
							;
					this.setWeight(i, j, k, weight);
				}
				
				// 计算当前句子的权重
				double weight = 0;
				for (int k = 0; k < sen.words.size(); k++) {
					Word currWord = sen.words.get(k);	//当前遍历到的词
					if(!stopWordsUtil.isStopWord(currWord.str))
						weight += currWord.weight;
				}
				//判断句子所在的段落
				if(sen.par_index == 0)//第1段
				{
					weight = weight*SummaryConst.WEIGHT_OF_FIRST_PARAGRAPH;
				}
				else if(sen.par_index == this.paragraphs.size()-1)//最后一段
				{
					weight = weight*SummaryConst.WEIGHT_OF_FINAL_PARAGRAPH;
				}
				else
				{
					weight = weight*SummaryConst.WEIGHT_OF_OTHER_PARAGRAPH;
				}
				this.setWeight(i, j, weight);
			}
			// 设置段的权重
		}
		
	}
	
	/**
	 * 判断是否属于第一段
	 * @param currWord
	 * @return
	 */
	private boolean isLastGraph(Word currWord) {
		return (currWord.p_index == 0);
	}
	/**
	 * 判断是否属于最后一段
	 * @param currWord
	 * @return
	 */
	private boolean isFirstGraph(Word currWord) {
		int parNum = this.paragraphs.size();
		return (currWord.p_index == (parNum-1));
	}
	/**
	 * 判断 str 是否存在于标题
	 * @param str
	 * @return
	 */
	private boolean isInTitle(String wordStr)
	{
		return this.title.indexOf(wordStr) != -1;
	}
	
	/**
	 * 设置词的权重
	 */
	private void setWeight(int p_index, int s_index, int w_index, double weight)
	{
		this.paragraphs.get(p_index).sentences.get(s_index).words.get(w_index).weight = weight;
	}
	/**
	 * 设置句子的权重
	 */
	private void setWeight(int p_index, int s_index, double weight)
	{
		this.paragraphs.get(p_index).sentences.get(s_index).weight = weight;
	}
	/**
	 * 设置段落的权重
	 */
	@SuppressWarnings("unused")
	private void setWeight(int p_index, double weight)
	{
		this.paragraphs.get(p_index).weight = weight;
	}
	
	public List<Sentence> getTopSentences(int top)
	{
		List<Sentence> sens = new ArrayList<Document.Sentence>();
		sens.addAll(this.getAllSentences());
		Collections.sort(sens, new Comparator<Sentence>() {

			public int compare(Sentence o1, Sentence o2) {
				if(o1.weight > o2.weight) return -1;
				else if(o1.weight == o2.weight)
				{
					if(o1.par_index < o2.par_index)
						return -1;
					else if(o1.par_index == o2.par_index)
					{
						if(o1.sInPar_index < o2.sInPar_index) return -1;
						else if(o1.sInPar_index == o2.sInPar_index) return 0;
						else return 1;
					}
					else return 1;
				}
				return 1;
			}
		});
		
		if(sens.size() <= top)
		{
			return sens;
		}
		else
		{
			return sens.subList(0, top);
		}
	}
	/**
	 * 将权重高的top个句子按位置排序
	 * @param top 将权重top X的句子作为摘要
	 * @return
	 */
	public String getSummary(int top)
	{
		String summary = "";
		List<Sentence> sens = getTopSentences(top);
		
		Collections.sort(sens, new Comparator<Sentence>() {

			public int compare(Sentence o1, Sentence o2) {
				if(o1.par_index < o2.par_index)
					return -1;
				else if(o1.par_index == o2.par_index)
				{
					if(o1.sInPar_index < o2.sInPar_index) return -1;
					else if(o1.sInPar_index == o2.sInPar_index) return 0;
					else return 1;
				}
				else return 1;
			}
		});
		for(Sentence sen : sens)
		{
			summary += sen.str+"。";
		}
		
		return summary;
	}
	
	/**
	 * 获取关键词（不是停用词，且不重复）
	 * @param top
	 * @return
	 */
	public List<Word> getTopWords(int top)
	{
		List<Word> oldWords = this.allWords;
		List<Word> words = new ArrayList<Document.Word>();
		/** 过滤 */
		for(Word oldWord : oldWords)
		{
			// 不是停用词，且不重复
			if(!stopWordsUtil.isStopWord(oldWord.str))
			{
				boolean exist = false;
				for(Word word : words)
				{
					if(word.str.equals(oldWord.str))
					{
						exist = true;
						break;
					}
				}
				if(!exist) words.add(oldWord);
			}
		}
		/** 按权重排序 */
		Collections.sort(words, new Comparator<Word>() {

			public int compare(Word o1, Word o2) {
				if(o1.weight > o2.weight) return -1;
				else if(o1.weight == o2.weight)
				{
					if(o1.p_index < o2.p_index)
						return -1;
					else if(o1.p_index == o2.p_index)
						if(o1.s_index < o2.s_index)
							return -1;
						else if(o1.s_index == o2.s_index)
							if(o1.wInSentence_index < o2.wInSentence_index)
								return -1;
					return 0;
				}
				return 1;
			}
		});
		
		if(words.size() <= top)
		{
			return words;
		}
		else
		{
			return words.subList(0, top);
		}
		
	}
	
	public List<Sentence> getAllSentences(){
		List<Sentence> sens = new ArrayList<Document.Sentence>();
		for(Paragraph par : this.paragraphs)
			for(Sentence sen : par.sentences)
				sens.add(sen);
		return sens;
	}
	/*	getter */
	
	public String getTitle() {
		return title;
	}
	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}
	public Map<String, Integer> getWordsFrequencyMap() {
		return wordsFrequencyMap;
	}
	public Map<String, Integer> getWordsCrossParFrequencyMap() {
		return wordsCrossParFrequencyMap;
	}
	public Map<String, Map<Integer, Integer>> getFullWordsFrequencyMap() {
		return fullWordsFrequencyMap;
	}
	
}
