package cn.cjp.utils;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Page<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7659895996912452638L;
	/**
	 * 当前页
	 */
	private int currPage;
	/**
	 * 下一页
	 */
	private int nextPage;
	/**
	 * size of page
	 */
	private int sizeOfPage;
	/**
	 * 前一页
	 */
	private int prevPage;
	/**
	 * 总页数
	 */
	private int pageCount;
	/**
	 * 当前页开始的行号
	 */
	private long startRowInCurrPage;
	/**
	 * 当前页结束的行号
	 */
	private long endRowInCurrPage;
	/**
	 * 数据总数
	 */
	private long countOfData;

	private List<T> resultList;

	private Object object;

	public static <T> Page<T> fromSkipAndLimit(int skip, int limit, int count) {
		int sizeOfPage = limit;
		int currPage = (skip / limit) + 1;
		int countOfData = count;

		Page<T> page = new Page<>(currPage, sizeOfPage, countOfData);
		return page;
	}

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

	public Page(int currPage, int sizeOfPage, long countOfData, List<T> resultList) {
		this(currPage, sizeOfPage, countOfData);
		this.setResultList(resultList);
	}

	/**
	 * 计算出所有属性的值
	 */
	private void compute() {
		pageCount = (int) Math.ceil(countOfData * 1.0 / sizeOfPage);
		prevPage = currPage > 1 ? currPage - 1 : currPage;
		nextPage = currPage < pageCount ? currPage + 1 : pageCount;

		startRowInCurrPage = (this.currPage - 1) * sizeOfPage + 1;
		if (currPage == pageCount) {
			endRowInCurrPage = countOfData;
		} else {
			endRowInCurrPage = currPage * sizeOfPage;
		}
	}

	/**
	 * @return the resultList
	 */
	public List<T> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object
	 *            the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

}
