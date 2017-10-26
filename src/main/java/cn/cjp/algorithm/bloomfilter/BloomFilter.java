package cn.cjp.algorithm.bloomfilter;

import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * 使用redis实现bloom filter，借用 guava 的BloomFilter
 * 
 * @author sucre
 *
 * @param <T>
 * @see com.google.common.hash.BloomFilter
 */
public class BloomFilter<T> {

	/**
	 * bit array
	 */
	private Redis redis;

	private String name;

	/**
	 * bit 数组长度
	 */
	private long numBits;

	/**
	 * the desired false positive probability (must be positive and less than
	 * 1.0)
	 */
	private double fpp = 0.03;

	Funnel<T> funnel;

	int numHashFunctions;

	/**
	 * 
	 * @param expectedInsertions
	 *            预期插入的数据量
	 */
	public BloomFilter(long expectedInsertions) {

		long numBits = optimalNumOfBits(expectedInsertions, fpp);
		this.numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
	}

	public boolean mightContains(T t) {
		HashFunction hashFunc = Hashing.murmur3_128();
		HashCode hashCode = hashFunc.hashObject(t, funnel);
		long hash64 = hashCode.asLong();

		int hash1 = (int) hash64;
		int hash2 = (int) (hash64 >>> 32);

		for (int i = 1; i <= numHashFunctions; i++) {
			int combinedHash = hash1 + (i * hash2);
			// Flip all the bits if it's negative (guaranteed positive number)
			if (combinedHash < 0) {
				combinedHash = ~combinedHash;
			}

			if (!redis.getbit(name, combinedHash % numBits)) {
				return false;
			}
		}
		return true;
	}

	public boolean put(T t) {
		HashFunction hashFunc = Hashing.murmur3_128();
		HashCode hashCode = hashFunc.hashObject(t, funnel);
		long hash64 = hashCode.asLong();

		int hash1 = (int) hash64;
		int hash2 = (int) (hash64 >>> 32);

		boolean bitsChanged = false;
		for (int i = 1; i <= numHashFunctions; i++) {
			int combinedHash = hash1 + (i * hash2);
			// Flip all the bits if it's negative (guaranteed positive number)
			if (combinedHash < 0) {
				combinedHash = ~combinedHash;
			}
			bitsChanged |= redis.setbit(name, combinedHash % numBits, true);
		}
		return bitsChanged;
	}

	/**
	 * 
	 * @param n
	 * @param m
	 * @return
	 * @see com.google.common.hash.BloomFilter#optimalNumOfHashFunctions
	 */
	static int optimalNumOfHashFunctions(long n, long m) {
		// (m / n) * log(2), but avoid truncation due to division!
		return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
	}

	/**
	 * 
	 * @param n
	 *            expected insertions (must be positive)
	 * @param p
	 *            false positive rate (must be 0 < p < 1)
	 * @return
	 * @see com.google.common.hash.BloomFilter#optimalNumOfBits
	 */
	static long optimalNumOfBits(long n, double p) {
		if (p == 0) {
			p = Double.MIN_VALUE;
		}
		return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
	}

}
