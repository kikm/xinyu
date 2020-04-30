package com.xinyu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    //注册UserDetailsService的bean
    @Autowired
    private UserDetailsService customerUserService;
    
    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterMetadataSource; //权限过滤器（当前url所需要的访问权限）

    @Autowired
    private AuthenticationEntryPointImpl entryPoint;// 配置验证异常处理
    
    @Autowired
    private AccessDecisionManagerImpl deniedManager;//权限决策管理器
    
    @Autowired
    private CustomAccessDeineHandler deniedHandler;//决策异常处理器
    

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerUserService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //安全策略
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	CusUsernamePasswordAuthenticationFilter upfilter = new CusUsernamePasswordAuthenticationFilter();
    	upfilter.setAuthenticationManager(authenticationManager());
    	http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
        http.addFilterAt(upfilter,UsernamePasswordAuthenticationFilter.class)
        		.authorizeRequests()
        		.and()
                .formLogin()
                .loginPage("/login")
                //.loginProcessingUrl("/login")
                .and()
                .authorizeRequests()        						// 定义哪些URL需要被保护、哪些不需要被保护
                .antMatchers("/login.html","/login","/mobileTenLogin","/mobileCusLogin","/mobile/**","/wx/**","/img/**").permitAll()    // 设置所有人都可以访问登录页面
                .anyRequest()               						// 任何请求,登录后可以访问
                .authenticated()
				.and().exceptionHandling().authenticationEntryPoint(entryPoint)
				.accessDeniedHandler(deniedHandler)
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
  				  
					@Override public <O extends FilterSecurityInterceptor> O postProcess(O o) {
						o.setSecurityMetadataSource(filterMetadataSource); 
						o.setAccessDecisionManager(deniedManager);
						
						return o; 
				}})
                
                .and()
                .logout()
                .permitAll()
        		.and()
        		.csrf().disable();

    }

    //解决静态资源被拦截的问题
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/style/**");
        web.ignoring().antMatchers("/scripts/**");
        web.ignoring().antMatchers("/images/**");
        web.ignoring().antMatchers("/wedemo/**");
        web.ignoring().antMatchers("/plugins/**");
    }
    
}
