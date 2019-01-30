package com.dongbin.mybatis.interceptor;

import com.dongbin.mybatis.annotation.MybatisEncrypt;
import com.dongbin.mybatis.encrypt.AESUtil;
import com.dongbin.mybatis.encrypt.IEncrypt;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ReflectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class})})
public class DBEncryptInterceptor implements Interceptor {


    private IEncrypt encrypt;

    public DBEncryptInterceptor(Class clazz) {

        try {
            this.encrypt = (IEncrypt) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            this.encrypt = new AESUtil();
        }
    }

    public Object intercept(Invocation invocation) throws Throwable {

        /**
         * encrypt
         */
        if ("update".equals(invocation.getMethod().getName())) {
            Object parameter = invocation.getArgs()[1];
            if (parameter instanceof Map) {
                Map map = (Map) parameter;
                for (Object o : map.values()) {
                    handle(o, false);
                }
            } else if (parameter instanceof Collection) {
                Collection collection = (Collection) parameter;
                for (Object o : collection) {
                    handle(o, false);
                }
            } else {
                handle(invocation.getArgs()[1], false);
            }
        }

        /**
         * decrypt
         */
        Object returnValue = invocation.proceed();
        if ("query".equalsIgnoreCase(invocation.getMethod().getName())) {
            if (returnValue instanceof Collection) {
                Collection collection = (Collection) returnValue;
                for (Object o : collection) {
                    handle(o, true);
                }
            } else {
                handle(returnValue, true);
            }
        }

        return returnValue;
    }

    /**
     * @param o
     * @param b is decrypt
     */
    private void handle(Object o, boolean b) {
        if (o == null) {
            return;
        }
        ReflectionUtils.doWithFields(o.getClass(), field -> {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MybatisEncrypt.class) && field.get(o) != null) {
                try {
                    String value = (String) field.get(o);
                    field.set(o, b ? encrypt.decrypt(value) : encrypt.encrypt(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        });

    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }
}
