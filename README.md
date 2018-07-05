# dk-foundation

#### 项目介绍
spring 快速开发工具包，自动集成了mybatis,sharding-jdbc,redis等

#### 使用说明

1. dk-foundation-common模块为一些基础工具
2. dk-foundation-engine-sharding-jdbc是一个集成了sharding-jdbc的快速开发包
3. dk-foundation-engine-declare-datasource是一个可以通过@DataSource声明数据源的快速开发包。
   使用此开发包可以很方便地支持多数据源，方便地开发读写分离的软件服务。
   如果你的项目涉及到分库分表，可使用sharding-jdbc包，或选用其它数据库中间件。

#### 快速开发   

在你的项目启动文件前添加@EnableEngineStart即可，如下所示

```
@MapperScan("com.dk.test.*.mapper")
@EnableEngineStart
public class Startup {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Startup.class, args);
    }
}
```
