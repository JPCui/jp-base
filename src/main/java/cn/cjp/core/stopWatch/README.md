# StopWatch

之前写了一个调用栈的代码，结合MongoDB实现了一个监控代码调用次数和业务调用栈的小功能（有点类似于SpringBoot提供的监控的其中一个功能），代码有点复杂。

另外，因为mongo提供了很方便的函数（inc: 用于增量函数调用次数…），所以可以实现上面说的功能，这里就不再详细说明。

后来发现 b3log里用到了一个 StopWatch 的类，来记录业务执行的时间，不过 b3log 需要手动在代码里添加，不够灵活，本人果断拿过来进行包装，23333。

> 后来又发现原来好多框架里原来都实现了这样一个东西。

不过这个只能实时输出调用信息，没有增量计算（调用了多少次，平均运行时长，某一业务内部的函数调用了多少次、运行了多久） 
不过可以结合自己情况，做一些修改。

代码：https://github.com/JPCui/jp-base/tree/master/src/main/java/cn/cjp/core/stopWatch

## Demo

[StopWatchTest](https://github.com/JPCui/jp-base/tree/master/src/test/java/cn/cjp/core/stopWatch/StopWatchTest.java)

## 运行结果示例

```
[100]%, [4357]ms [cn.cjp.core.service.UserService.func1()]
  [22.97]%, [1001]ms [cn.cjp.core.service.UserService.func2()]
  [22.95]%, [1000]ms [cn.cjp.core.service.UserService.func3()]
  [22.95]%, [1000]ms [cn.cjp.core.service.UserService.func2()]
```

核心：[Stopwatchs](https://github.com/b3log/latke/blob/master/latke/src/main/java/org/b3log/latke/util/Stopwatchs.java)