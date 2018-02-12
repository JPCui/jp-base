package cn.cjp.utils;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import cn.cjp.base.model.BaseEntityModel;

public class JacksonUtilTest {

    public List<BaseEntityModel> baseModelList() {
        return null;
    }

    @Test
    public void test() throws Exception {
        Method method = JacksonUtilTest.class.getDeclaredMethod("baseModelList");
        Type type = method.getGenericReturnType();
        JacksonUtil.fromJson("[]", type);
    }

}
