package cn.cjp.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.cjp.base.model.BaseEntityModel;

/**
 * 属性工具类
 * 
 * @author sucre
 *
 */
public class PropertyUtil {

	/**
	 * 从orig抽取目标类的属性集合
	 * 
	 * @param orig
	 * @param destClass
	 * @return
	 */
	public static List<String> extractProperties(Collection<String> orig, Class<?> destClass) {
		List<String> props = new ArrayList<>();

		Field[] fields = destClass.getDeclaredFields();
		List<String> fieldsList = new ArrayList<>();
		for (Field field : fields) {
			fieldsList.add(field.getName());
		}

		Iterator<String> origIt = orig.iterator();
		while (origIt.hasNext()) {
			String prop = origIt.next();
			if (fieldsList.contains(prop)) {
				props.add(prop);
			}
		}
		return props;
	}

	/**
	 * 从map中抽取目标类的属性集合
	 * 
	 * @param orig
	 * @param destClass
	 * @return
	 */
	public static Map<String, Object> extractProperties(Map<String, Object> orig, Class<?> destClass) {
		Map<String, Object> propsMap = new HashMap<>();

		Field[] fields = destClass.getDeclaredFields();
		List<String> fieldsList = new ArrayList<>();
		for (Field field : fields) {
			fieldsList.add(field.getName());
		}

		Iterator<String> origIt = orig.keySet().iterator();
		while (origIt.hasNext()) {
			String prop = origIt.next();
			if (fieldsList.contains(prop)) {
				propsMap.put(prop, orig.get(prop));
			}
		}
		return propsMap;
	}

	public static void main(String[] args) {
		// BaseEntityModel entity = new BaseEntityModel();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("id", 1);
		dataMap.put("isdelete", 1);
		dataMap.put("updateDate", new Date());
		System.out.println(extractProperties(dataMap.keySet(), BaseEntityModel.class));
	}
}
