package cn.cjp.base.summary;

/**
 * 文摘中权重的设置
 * @author 崔金鹏
 *
 */
public class SummaryConst {

	/**
	 * 每段第一句的权重
	 */
	public static final double WEIGHT_OF_FIRST_SENTENCE = 0.85;
	/**
	 * 每段中间的句子的权重
	 */
	public static final double WEIGHT_OF_OTHER_SENTENCE = 0.7;
	/**
	 * 每段最后一句的权重
	 */
	public static final double WEIGHT_OF_FINAL_SENTENCE = 0.5;
	
	/**
	 * 第1段的权重 1.5
	 */
	public static double WEIGHT_OF_FIRST_PARAGRAPH = 1.5;
	/**
	 * 中间段的权重 1
	 */
	public static double WEIGHT_OF_OTHER_PARAGRAPH = 1;
	/**
	 * 最后一段的权重 1.4
	 */
	public static double WEIGHT_OF_FINAL_PARAGRAPH = 1.4; 
	
	/**
	 * 词在标题中出现的权重 2.0
	 */
	public static double WEIGHT_OF_IN_TITLE = 2.0;
	/**
	 * 词在文档中的权重 0.3
	 */
	public static double WEIGHT_OF_MULTY_IN_DOC = 0.3;
	/**
	 * 词跨段的权重 0.6
	 */
	public static double WEIGHT_OF_CROSS_PARAGRAPH = 0.6;
}
