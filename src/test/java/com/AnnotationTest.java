package com;

import com.dongbin.mybatis.annotation.MybatisEncrypt;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class AnnotationTest {

    @Test
    public void test1() {
        B b = new B();
        ReflectionUtils.doWithFields(b.getClass(), field -> {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MybatisEncrypt.class)) {
                System.out.println("find annotation mybatisEncrypt");
            }
        });
    }

    @Test
    public void test2() {
        A a = new A();
        a.setA("a");
        A b = new B();
        b.setA("b");
        String a1 = b.a;
        System.out.println(a1);

        C c = new C();
        c.setA("");

        Field[] fields = c.getClass().getFields();

    }
}
