package com.furenqiang.system.config;

import com.alibaba.fastjson.JSONObject;
import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.entity.SysUser;
import com.furenqiang.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SysUserService sysUserService;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    @Override
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(sysUserService);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("执行授权方法");
        //shiro路径下所有人都可以访问，security只有vip可以访问
        http.
                httpBasic()
                .and().authorizeRequests().antMatchers("/login").permitAll()
                .and().exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(ResponseEnum.SUCCESS.getCode()); //
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().append("{\"code\":" + ResponseEnum.NOTLOGIN.getCode() + ",\"message\":\"" + ResponseEnum.NOTLOGIN.getMessage() + "\",\"data\":\"success\"}");
                })
                .accessDeniedHandler((request, response, ex) -> {
                    response.setStatus(ResponseEnum.SUCCESS.getCode()); //
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().append("{\"code\":" + ResponseEnum.NOTROLE.getCode() + " ,\"message\":\"" + ResponseEnum.NOTROLE.getMessage() + "\",\"data\":\"success\"}");
                })
                .and().authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                //下面一行代码表示其他所有资源只有认证通过才能访问
                .anyRequest().authenticated()
                .and()
                .cors().disable()//开启跨域访问
                .csrf().disable();//开启模拟请求，比如API POST测试工具的测试，不开启时，API POST为报403错误
        //也可以这样写http.csrf().disable();//关闭网站拦截get请求
        //开启---没有权限跳转到登录页面
        http.formLogin()
                .usernameParameter("username")//设置前端传的用户名字段名
                .passwordParameter("password")//设置前端传的密码字段名
                //.loginProcessingUrl("/login")//不需要自带的登录页面
                .successHandler((request, response, authentication) -> {
                    //获取当前用户名称
                    String currentUser = authentication.getName();
                    //获取用户信息
                    SysUser principal = (SysUser) authentication.getPrincipal();
                    //用户信息存session
                    request.getSession().setAttribute("userInfo", principal);
                    //logger.info("用户" + currentUser + "登录成功");
                    response.setStatus(ResponseEnum.SUCCESS.getCode());
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().append("{\"code\":" + ResponseEnum.SUCCESS.getCode() + ",\"message\":\"登录成功!\",\"data\":" + JSONObject.toJSONString(principal) + "}");
                })// 登录成功后走的自定义处理
                .failureHandler((request, response, ex) -> {
                    //logger.info("登录失败");
                    response.setStatus(ResponseEnum.SUCCESS.getCode()); // 403 普通用户访问管理员页面
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().append("{\"code\":" + ResponseEnum.ERROR.getCode() + ",\"message\":\"登录失败,用户名或密码不正确!\",\"data\":\"failed\"}");
                });// 登录失败后走的自定义处理
        http.logout().logoutSuccessHandler((request, response, authentication) -> {
            //logger.info("登出成功");
            response.setStatus(ResponseEnum.SUCCESS.getCode()); //
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().append("{\"code\":" + ResponseEnum.SUCCESS.getCode() + ",\"message\":\"登出成功!\",\"data\":\"success\"}");
        });
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("执行认证方法");
        auth.userDetailsService(sysUserService).passwordEncoder(passwordEncoder());
    }
}


