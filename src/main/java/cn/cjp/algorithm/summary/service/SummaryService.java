package cn.cjp.algorithm.summary.service;

import java.util.ArrayList;
import java.util.List;

import cn.cjp.algorithm.summary.Document;

public class SummaryService {
	
	Document document = null;
	/**
	 * 设置标题，段落
	 * @param title 标题
	 * @param paragraphs 段落数组
	 */
	public SummaryService(String title, List<String> paragraphs)
	{
		if(title == null) title = "";
		if(paragraphs == null) paragraphs = new ArrayList<String>();
		document = new Document(title, paragraphs);
	}
	
	/**
	 * 将权重最高的top个句子筛选出，然后按位置排序作为摘要
	 * @param top
	 * @return
	 */
	public String getSummary(int top)
	{
		return document.getSummary(top);
	}

}
