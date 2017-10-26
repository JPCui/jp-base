package cn.cjp.algorithm.bloomfilter;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

/**
 * use <tt>Bean</tt> implement RedisDao annotated by @Component("enableRedis")
 * <p>
 * <tt>Bean</tt> annotated by @Component("disableRedis") is useless
 * 
 * @author Jinpeng Cui
 * @see SentinelJedis
 * @see BaseDao
 */
public interface Redis extends Closeable {

	void close() throws IOException;

	Long zrank(String key, String member);

	Long zadd(String key, double score, String member);

	boolean hexists(String key, String field);

	String hget(String key, String field);

	Long hset(String key, String field, String value);

	Long del(String... key);

	Double zscore(String key, String member);

	Long zrem(String key, String... members);

	String set(String key, String value);

	public Boolean setbit(String key, long offset, boolean value);

	public Boolean getbit(String key, long offset);

	Boolean exists(String key);

	String get(String key);

	Long lpush(String key, String... strings);

	Long expire(String key, int seconds);

	Long llen(String key);

	List<String> lrange(String key, long start, long end);

	Long rpush(String key, String... value);

	Double zincrby(String cacheKey, int i, String k);

	Set<Tuple> zrevrangeWithScores(String cacheKey, int i, int j);

	Long zrevrank(String replace, String string);

	Long sadd(String key, String... string);

	Long hdel(String key, String... fields);

	Map<String, String> hgetAll(String key);

	Long scard(String key);

	boolean isClose();
}
