//package com.furenqiang.system.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        //1、请求授权
//        http.authorizeHttpRequests(registry -> {
//            registry.requestMatchers("/robot/**").permitAll()// 允许/robot/**免授权认证
//                    .anyRequest().authenticated();//其他请求都得授权
//        });
//        //2、修改默认登录表单为自己的登录接口
//        http.formLogin(formLogin -> {
//            formLogin.loginPage("/login").permitAll();
//        });
//        return http.build();
//    }
//
//    @Bean
//    BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
