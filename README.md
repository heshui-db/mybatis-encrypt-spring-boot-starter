##### 本地打包
```properties
   git clone  https://github.com/dongbintop/mybatis-encrypt-spring-boot-starter.git
   mvn clean install
```


###### 项目引用

pom 文件
```properties
    <dependency>
        <groupId>com.dongbin</groupId>
        <artifactId>mybatis-encrypt-starter</artifactId>
        <version>1.0.0</version>
    </dependency>
 ```
 
 application.properties
 
 ```properties
     #是否开启加密
     mybatis.encrypt.enabled=true
     #对称加密的类，有默认值
     mybatis.encrypt.clazz=com.dongbin.mybatis.encrypt.AESUtil
     
     也可以在@MybatisEncrypt注解中指明加密类 但是必须实现IEncrypt
     
```
 Java 代码
 
  ```java
     @MybatisEncrypt
     private String name;
  ```
  
  ###### 注意
  
  - parameter 支持 map,collection 及 obj
  - returnValue 支持 collection 及 obj
  - 不支持嵌套（觉得没必要）
 

