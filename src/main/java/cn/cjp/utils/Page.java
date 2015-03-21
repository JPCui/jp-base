package cn.cjp.utils;

public class Page {

	/**
	 * 当前页
	 */
	public int currPage;
	/**
	 * 下一页
	 */
	public int nextPage;
	/**
	 * size of page
	 */
	public int sizeOfPage;
	/**
	 * 前一页
	 */
	public int prevPage;
	/**
	 * 总页数
	 */
	public int pageCount;
	/**
	 * 当前页开始的行号
	 */
	public long startRowInCurrPage;
	/**
	 * 当前页结束的行号
	 */
	public long endRowInCurrPage;
	/**
	 * 数据总数
	 */
	public long countOfData;

	/**
	 * 初始化PageUtil
	 * 
	 * @param currPage
	 *            当前页
	 * @param sizeOfPage
	 *            每页大小
	 * @param countOfData
	 *            数据总数
	 */
	public Page(int currPage, int sizeOfPage, long countOfData) {
		this.currPage = currPage;
		this.sizeOfPage = sizeOfPage;
		this.countOfData = countOfData;

		this.compute();
	}
	
	/**
	 * 计算出所有属性的值
	 */
	private void compute(){
		pageCount = (int) Math.ceil(countOfData * 1.0 / sizeOfPage);
		prevPage = currPage > 1 ? currPage - 1 : currPage;
		nextPage = currPage < pageCount ? currPage + 1 : pageCount;
		
		startRowInCurrPage = (this.currPage-1)* sizeOfPage + 1;
		if(currPage == pageCount){
			endRowInCurrPage = countOfData;
		}else{
			endRowInCurrPage = currPage * sizeOfPage;
		}
	}

}
