package com.accurascience.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 实现跨域请求，方便前端调用API
 * @author zhuchaojie
 *
 */
//@Configuration
public class CorsConfig {

	//@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 对接口配置跨域设置
        return new CorsFilter(source);
    }
	private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 允许域名
        corsConfiguration.addAllowedHeader("*"); // 允许请求头
        corsConfiguration.addAllowedMethod("*"); // 允许的方法
        return corsConfiguration;
    }

}
