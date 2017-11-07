jp-base
========

# 一些有用的工具

## Json

[JacksonUtil](https://github.com/JPCui/jp-base/blob/master/src/main/java/cn/cjp/utils/JacksonUtil.java)

## 分布式锁

[DistributeLock](https://github.com/JPCui/jp-base/tree/master/src/main/java/cn/cjp/core/lock/DistributeLock.java)

## 调用栈

[StopWatch](https://github.com/JPCui/jp-base/tree/master/src/main/java/cn/cjp/core/stopWatch/README.md)

## 布隆过滤器（Redis）

用于爬虫去重

[bloomfilter](https://github.com/JPCui/jp-base/tree/master/src/main/java/cn/cjp/algorithm/bloomfilter)

## 仿 SpringCache 的业务缓存方案

[RedisCacheManager](https://github.com/JPCui/jp-base/tree/master/src/main/java/cn/cjp/core/cache)

# Maven

```
<repository>
	<id>jp-base</id>
	<name>jp-base</name>
	<url>https://raw.githubusercontent.com/jpcui/jp-base/master/repository</url>
</repository>
```
```
<dependency>
	<groupId>cn.cjp</groupId>
	<artifactId>jp-base</artifactId>
	<version>v2017.0228</version>
</dependency>
```
