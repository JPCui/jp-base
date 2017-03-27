package cn.cjp.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * 对Value进行排序，从大到小，如果 Value 相等，按 Key 从小到大排序 <br>
 * map 是源数据，SortedMap 需要对比的数据的来源
 * 
 * @author Jinpeng Cui
 *
 * @param <K>
 *            一般 K 为 String，所以默认 K 是继承了 Comparable
 * @param <V>
 *            implement of Comparable
 */
public class ValueComparator<K extends Comparable<K>, V extends Comparable<V>> implements Comparator<K> {

	Map<K, V> map;

	public ValueComparator(Map<K, V> map) {
		this.map = map;
	}

	/**
	 * @throws NullPointerException
	 *             确保 V 是存在的
	 */
	@Override
	public int compare(K k1, K k2) {
		V v1 = map.get(k1);
		V v2 = map.get(k2);

		int comp = v2.compareTo(v1);
		// 不能返回0，否则 V 相同的值会被后面的覆盖
		//
		// 如果v1==v2，返回1/-1按情况决定
		return comp == 0 ? k1.compareTo(k2) : comp;
	}

	public static void main(String[] args) {
		Map<String, Double> tempResult = new HashMap<>();
		ValueComparator<String, Double> comparator = new ValueComparator<>(tempResult);
		SortedMap<String, Double> result = new TreeMap<>(comparator);

		tempResult.put("a", 2.0);
		tempResult.put("b", 6.0);
		tempResult.put("c", 4.0);
		tempResult.put("d", 8.0);
		result.putAll(tempResult);

		tempResult.put("e", 6.0);
		result.put("e", 6.0);

		System.out.println(tempResult);
		System.out.println(result);
	}

}
