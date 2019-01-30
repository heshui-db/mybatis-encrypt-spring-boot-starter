package com;

import com.dongbin.mybatis.utils.AESUtil;
import org.junit.Test;

public class AESUtilTest {

    private final String param = "com/dongbin";

    @Test
    public void encrypt() {
        String encrypt = AESUtil.encrypt(param);
        System.out.println(encrypt);
        System.out.println(AESUtil.decrypt(encrypt));
    }
}
