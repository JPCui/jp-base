package cn.cjp.core.cache;

public interface CacheCallback<T> {

    public T doIntern(Cache cache);

}
