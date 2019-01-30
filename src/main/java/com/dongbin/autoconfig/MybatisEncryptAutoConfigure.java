package com.dongbin.autoconfig;

import com.dongbin.mybatis.interceptor.DBEncryptInterceptor;
import com.dongbin.mybatis.properties.MybatisEncryptProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

;

@Configuration
@ConditionalOnClass(DBEncryptInterceptor.class)
@EnableConfigurationProperties(MybatisEncryptProperties.class)
public class MybatisEncryptAutoConfigure {

    @Autowired
    private MybatisEncryptProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mybatis.encrypt", value = "enabled", havingValue = "true")
    public DBEncryptInterceptor initDBEncryptInterceptor() {
        return new DBEncryptInterceptor(properties.getClazz());
    }
}
