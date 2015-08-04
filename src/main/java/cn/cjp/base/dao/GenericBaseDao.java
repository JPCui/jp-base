package cn.cjp.base.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericBaseDao<T> extends Serializable {

	/**
	 * save or update
	 * 
	 * @param entity
	 * @deprecated use saveOrUpdate instead
	 */
	public void save(T entity);
	
	public List<Object> query(String hql);

	/**
	 * 如果存在Id，则update，否则save
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(T entity);

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
	 * @deprecated use saveOrUpdate instead
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
