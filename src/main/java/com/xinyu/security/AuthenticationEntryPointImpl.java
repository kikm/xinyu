package com.xinyu.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

@Service("authenticationEntryPointImpl")
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

	/*
	 * 认证异常处理类
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String loginurl = "/login";
		String requestHeader = request.getHeader("user-agent");
		String[] deviceArray = new String[]{"android","mac os","windows phone"};
		Boolean isMobile = false;
		if(StringUtils.isNotBlank(requestHeader)) {
			requestHeader = requestHeader.toLowerCase();
			for(int i=0;i<deviceArray.length;i++){
				if(requestHeader.indexOf(deviceArray[i])>0){
					isMobile = true;
				}
			}
		}
		response.sendRedirect(request.getContextPath()+loginurl);
		
	}

}
