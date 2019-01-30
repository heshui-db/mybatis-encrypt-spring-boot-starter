package com;

import com.dongbin.mybatis.encrypt.AESUtil;
import org.junit.Test;

public class AESUtilTest {

    private final String param = "com/dongbin";

    @Test
    public void encrypt() {
        AESUtil aesUtil = new AESUtil();
        String encrypt = aesUtil.encrypt(param);
        System.out.println(encrypt);
        System.out.println(aesUtil.decrypt(encrypt));
    }
}
