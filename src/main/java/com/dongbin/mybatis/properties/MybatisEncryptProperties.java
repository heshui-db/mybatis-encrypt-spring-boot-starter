package com.dongbin.mybatis.properties;

import com.dongbin.mybatis.encrypt.AESUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mybatis.encrypt")
public class MybatisEncryptProperties {

    /**
     * 默认的加密方法
     */
    private Class clazz = AESUtil.class;

    /**
     * 是否开启加密
     */
    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
