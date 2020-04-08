package com.xinyu.security;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.xinyu.Constance;
import com.xinyu.model.User;

public class CusUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private Logger logger = LoggerFactory
			.getLogger(CusUsernamePasswordAuthenticationFilter.class);
	
	@Autowired
	private UserDetailsService userService;
	
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		// CSRF攻击验证
//		String random_form = request.getParameter("random_form");
//		String random_session = (String) request.getSession().getAttribute("random_session");
//		if (StringUtils.isBlank(random_form)
//				|| StringUtils.isBlank(random_session)
//				|| !random_form.equals(random_session)) {
//			logger.warn("CSRF攻击");
//			throw new CredentialsExpiredException("非法登录");
//		}

		// 不管是否登录成功，都直接清除验证码
		//request.getSession().removeAttribute("RANDOMVALIDATECODEKEY");
		request.getSession().removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
		//登录成功记录用户名
//		Cookie cook = null;
//		Cookie cook2 = new Cookie("rememberPsw","N");
//		if("Y".equals(request.getParameter("rememberPsw"))){
//			cook = new Cookie("namepsw",request.getParameter("j_username")+"-"+request.getParameter("j_password"));
//			cook2 = new Cookie("rememberPsw","Y");
//		}else{
//			cook = new Cookie("namepsw",request.getParameter("j_username")+"-");
//		}
//		cook.setMaxAge(60*60*24*14);
//		response.addCookie(cook);
//		
//		cook2.setMaxAge(60*60*24*14);
//		response.addCookie(cook2);
		
		return super.attemptAuthentication(request, response);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException {
	
		super.successfulAuthentication(request, response, chain, authResult);
		// 添加登录用户ip信息
		logger.info("{}成功登陆系统",authResult.getName());
		request.getSession().setAttribute(Constance.USER_ID, authResult.getName());
		WebApplicationContext webContext=WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		FilterInvocationSecurityMetadataSourceImpl sms=webContext.getBean(FilterInvocationSecurityMetadataSourceImpl.class);
		sms.asyncLoadMenu();
		//登陆成功之后判断用户选择的是哪一种语言（中文、English）
		Locale locale = (Locale) request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if(locale==null){
			locale = request.getLocale();
			request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
			request.getSession().setAttribute(Constance.LOCALE,locale.toString());
		}
		
		User user=new User();
		user.setId(authResult.getName());
		request.getSession().setAttribute(Constance.USER,user);
	}
	
	protected boolean checkValidateCode(HttpServletRequest request) {   
        HttpSession session = request.getSession();  
          
        String sessionValidateCode = (String)session.getAttribute("RANDOMVALIDATECODEKEY");  
        //让上一次的验证码失效  
      
        String validateCodeParameter = request.getParameter("checkcode");  
    
        if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {    
          //  throw new AuthenticationServiceException("验证码错误！");    
        	return false;
        }    
        return true;
    } 
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		String errMsg = failed.getMessage();
		if (failed instanceof UsernameNotFoundException) {
			errMsg = "账号不存在";
			failed = new UsernameNotFoundException(errMsg);
		} 
		super.unsuccessfulAuthentication(request, response, failed);
	} 
	
	
    
    
}
