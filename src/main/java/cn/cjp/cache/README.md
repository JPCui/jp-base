# RedisCacheManager

仿 Spring Cache

- @Cacheable
- @CacheEvict

使用 String.format + args

## group

- Cacheable 用于存储一类业务的缓存
- CacheEvict 用于清理一类业务的缓存
 
    原理：在redis中建立一个以 group 命名的 list
    
    创建缓存时，在 list 中 push 缓存的key
    删除时，从 list 中获取所有的 key，然后全部删除

# 解决this不走缓存

所有需要拦截的类，使用 Aspect 代理
