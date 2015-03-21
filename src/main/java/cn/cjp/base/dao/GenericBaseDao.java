package cn.cjp.base.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericBaseDao<T> extends Serializable {

	/**
	 * save or update
	 * 
	 * @param entity
	 */
	public void save(T entity);

	/**
	 * 根据ID查询
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public T findById(Serializable id);

	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void update(T entity);

	/**
	 * delete by id
	 * 
	 * @param id
	 */
	public void delete(long id);
	
	public List<T> findAll();
	
	public List<T> findAll(int pageNum, int pageSize);

	/**
	 * 获取总数
	 * 
	 * @return
	 */
	public long count();

}
