package cn.cjp.core.service;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import cn.cjp.core.cache.CacheEvict;
import cn.cjp.core.cache.Cacheable;

@Service
public class UserService {

    public void func1() {
        sleep();
        getThis().func2();
        getThis().func3();
        getThis().func2();
    }

    private UserService getThis() {
        return AopContext.currentProxy() != null ? (UserService) AopContext.currentProxy() : this;
    }

    public void func2() {
        sleep();

    }

    public void func3() {
        sleep();

    }

    private void sleep() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
    }

    @Cacheable(group = "User", key = "User:%s", args = "#id")
    public Object get(int id) {
        return String.format("User #%s", id);
    }

    @CacheEvict(key = "User:%s", args = "#id")
    public void update(int id) {
    }

    @CacheEvict(group = "User")
    public void removeAll() {
    }

}
