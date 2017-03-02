package cn.cjp.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash<T> {

	private final HashFunction hashFunction;
	private final int numberOfReplicas;
	private final TreeMap<Integer, T> circle = new TreeMap<Integer, T>();

	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		// 初始化节点
		for (T node : nodes) {
			add(node);
		}
	}

	public void add(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.toString() + i), node);
		}
		System.out.println("添加节点：" + node);
		System.out.println("-- " + circle);
	}

	public void remove(T node) {
		System.out.println("删除节点：" + node);
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + i));
		}
		System.out.println("-- " + circle);
	}

	public T get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hashFunction.hash(key);
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

	public static void main(String[] args) {
		String node1 = "one";
		String node2 = "two";
		String node3 = "three";
		String node4 = "four";
		Object key = 97621962;
		List<String> nodes = new ArrayList<>();
		ConsistentHash<String> chash = new ConsistentHash<>(new HashFunction(), 2, nodes);

		chash.add(node1);
		System.out.println(chash.get(key));
		chash.add(node2);
		chash.add(node3);
		chash.add(node4);

		System.out.println("数据落在：" + chash.get(key));
		chash.remove(node4);
		System.out.println("数据落在：" + chash.get(key));

	}

}

class HashFunction {

	public int hash(Object key) {
		return key.hashCode();
	}

}