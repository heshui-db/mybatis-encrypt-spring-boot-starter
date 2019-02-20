package com.dongbin.mybatis.annotation;

import com.dongbin.mybatis.encrypt.AESUtil;
import com.dongbin.mybatis.encrypt.IEncrypt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MybatisEncrypt {
    Class<? extends IEncrypt> value() default AESUtil.class;
}
