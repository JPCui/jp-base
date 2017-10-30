# Cache

这里提供了两种 Cache 工具，接口 Cache

- MemoryCache
- RedisTemplateService

# RedisCacheManager

仿 Spring Cache

- @Cacheable 缓存注解
- @CacheEvict 缓存清除注解
- ~~ @CacheUpdate ~~ 这里暂不提供更新的功能

使用 String.format + args

## group

- Cacheable 用于存储一类业务的缓存
- CacheEvict 用于清理一类业务的缓存
 
    原理：在redis中建立一个以 group 命名的 list
    
    创建缓存时，在 list 中 push 缓存的key
    删除时，从 list 中获取所有的 key，然后全部删除

# Demo

[CacheTest](https://github.com/JPCui/jp-base/tree/master/src/test/java/cn/cjp/core/cache/CacheTest.java)

   
# 解决this不走缓存

test中演示了一种方式：

```
private UserService getThis() {
    return AopContext.currentProxy() != null ? (UserService) AopContext.currentProxy() : this;
}
```

See also : [spring aop类内部调用不拦截原因及解决方案](http://blog.csdn.net/dream_broken/article/details/72911148)