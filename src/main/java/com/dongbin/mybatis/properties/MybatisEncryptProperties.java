package com.dongbin.mybatis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mybatis.encrypt")
public class MybatisEncryptProperties {

    private String name;

    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
