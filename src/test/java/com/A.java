package com;

import com.dongbin.mybatis.annotation.MybatisEncrypt;

public class A {

    @MybatisEncrypt
    public String a;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
