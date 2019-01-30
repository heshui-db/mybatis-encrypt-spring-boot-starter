package com.dongbin.mybatis.encrypt;

public interface IEncrypt {

    /**
     * 加密方法
     *
     * @param var0
     * @return
     */
    String encrypt(String var0);

    /**
     * 解密方法
     *
     * @param var0
     * @return
     */
    String decrypt(String var0);
}
