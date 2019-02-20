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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class})})
public class DBEncryptInterceptor implements Interceptor {

    /**
     * 加密实体对象
     */
    private Map<String, IEncrypt> encryptMap = new ConcurrentHashMap<>();

    private final static String DEFAULT_ENCRYPT = "DEFAULT_ENCRYPT";

    public DBEncryptInterceptor(Class clazz) {

        try {
            encryptMap.put(DEFAULT_ENCRYPT, (IEncrypt) clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            encryptMap.put(DEFAULT_ENCRYPT, new AESUtil());
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

        List<Field> fields = getAllFiled(o.getClass());

        fields.forEach(field -> {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(MybatisEncrypt.class) && field.get(o) != null) {
                    String value = (String) field.get(o);
                    IEncrypt encrypt = getEncrypt(field);
                    field.set(o, b ? encrypt.decrypt(value) : encrypt.encrypt(value));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(false);
            }

        });

//        ReflectionUtils.doWithFields(o.getClass(), field -> {
//            field.setAccessible(true);
//            if (field.isAnnotationPresent(MybatisEncrypt.class) && field.get(o) != null) {
//                try {
//                    String value = (String) field.get(o);
//                    field.set(o, b ? encrypt.decrypt(value) : encrypt.encrypt(value));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            field.setAccessible(false);
//        });

    }

    private IEncrypt getEncrypt(Field field) {
        MybatisEncrypt annotation = field.getAnnotation(MybatisEncrypt.class);
        Class<? extends IEncrypt> annotationClass = annotation.value();
        if (annotationClass.getName().equals(AESUtil.class.getName())) {
            return encryptMap.get(DEFAULT_ENCRYPT);
        } else {
            if (encryptMap.get(annotationClass.getName()) == null) {
                try {
                    encryptMap.put(annotationClass.getName(), annotationClass.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return encryptMap.get(annotationClass.getName());
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }

    private List<Field> getAllFiled(Class clazz) {
        List<Field> fieldList = new ArrayList<>();

        try {
            Set<String> fieldName = new HashSet<>();
            for (Field field : clazz.getFields()) {
                if (fieldName.add(field.getName())) {
                    fieldList.add(field);
                }
            }
            for (Field field : clazz.getDeclaredFields()) {
                if (fieldName.add(field.getName())) {
                    fieldList.add(field);
                }
            }
        } catch (Exception e) {

        }

        return fieldList;
    }
}
