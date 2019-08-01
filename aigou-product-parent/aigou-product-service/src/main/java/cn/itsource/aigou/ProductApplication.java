package cn.itsource.aigou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
@EnableFeignClients(basePackages = "cn.itsource.common.client")
@MapperScan("cn.itsource.aigou.mapper")
//feign的client接口不在启动类的同包和子包中，手动配置扫描的包
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
}
