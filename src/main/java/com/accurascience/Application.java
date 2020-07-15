package com.accurascience;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 项目启动入口
 * @author zhuchaojie
 * @version 1.0.0
 */
@EnableResourceServer
@EnableDiscoveryClient
@EnableTransactionManagement//开启事务管理
@EnableAsync
@SpringBootApplication
@NacosPropertySource(dataId = "whole-exome", autoRefreshed = true)
public class Application {
     
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
