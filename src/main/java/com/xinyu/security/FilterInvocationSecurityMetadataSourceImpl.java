package com.xinyu.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.xinyu.Constance;
import com.xinyu.model.Menu;
import com.xinyu.model.Role;
import com.xinyu.service.impl.MenuServiceImpl;


/**
 * @Author: lijie
 * @Description: FilterInvocationSecurityMetadataSource（权限资源过滤器接口）继承了 SecurityMetadataSource（权限资源接口）
 * Spring Security是通过SecurityMetadataSource来加载访问时所需要的具体权限；Metadata是元数据的意思。
 * 自定义权限资源过滤器，实现动态的权限验证
 * 它的主要责任就是当访问一个url时，返回这个url所需要的访问权限
 **/
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

	private Logger logger = LoggerFactory.getLogger(FilterInvocationSecurityMetadataSourceImpl.class);

	@Autowired
    private MenuServiceImpl menuService;
	
	private boolean stripQueryStringFromUrls;
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
    private Map<Object, Collection<ConfigAttribute>> mapToUse=new LinkedHashMap<Object, Collection<ConfigAttribute>>();

    
    public void asyncLoadMenu(){
    	new Thread(new Runnable(){

			@Override
			public void run() {
				loadMenu();
			}
    		
    	}).start();
    	
    }
    
	/**
     * @Description: 返回本次访问需要的权限，可以有多个权限
     **/
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		if(mapToUse.size() == 0) {
			loadMenu();
		}
        //去数据库查询资源
//        List<Menu> allMenu = menuService.getAllMenu();
//        //loadMenu(allMenu);
//        for (Menu menu : allMenu) {
//            if (StringUtils.isNotEmpty(menu.getUrl())&&antPathMatcher.match(menu.getUrl(), requestUrl)
//                    && menu.getRoles().size() > 0) {
//                Set<Role> roles = menu.getRoles();
//                List<Role> result = new ArrayList<>(roles);
//                int size = result.size();
//                String[] values = new String[size];
//                String[] attrValues = new String[size];
//                for (int i = 0; i < size; i++) {
//                    values[i] = result.get(i).getName();
//                    attrValues[i] = Long.toString(result.get(i).getId());
//                }
//                logger.info("当前访问路径是{},这个url所需要的访问权限是{}", requestUrl, values);
//                return SecurityConfig.createList(attrValues);
//            }
//        }
//        /**
//         * @Description: 如果本方法返回null的话，意味着当前这个请求不需要任何角色就能访问
//         	* 此处做逻辑控制，如果没有匹配上的，返回一个默认具体权限，防止漏缺资源配置
//         **/
//        logger.info("当前访问路径是{},这个url所需要的访问权限是{}", requestUrl, "ROLE_LOGIN");
        return lookupAttributes(requestUrl);
	}
	
	private void loadMenu(){
    	try{
    		LinkedHashMap<String, Collection<ConfigAttribute>> requestMap=new LinkedHashMap<String, Collection<ConfigAttribute>>();
    		List<Menu> opList = menuService.getAllMenu();
    		if(opList!=null&&opList.size()>0){
	    		Map<String,Set<Long>> urlRoleMap=new HashMap<String,Set<Long>>();
	    		for (Menu objects : opList) {
	    			String url=objects.getUrl(); 
	    			if(StringUtils.isNotEmpty(url)){
	    				if(!urlRoleMap.containsKey(url)){ urlRoleMap.put(url, new HashSet<Long>()); }
	    				for(Role role : objects.getRoles()) {
	    					urlRoleMap.get(url).add(role.getId()); 
	    				} 
	    			}
	    			
				}
				for (String url : urlRoleMap.keySet()) {
		    		if(StringUtils.isNotEmpty(url)){
		    			
		    			Set<Long> roleAppSet=urlRoleMap.get(url);
			    		List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>(roleAppSet.size());
			    		for (Long roleId : roleAppSet) {
			    			attributes.add(new MenuOperConfigAttribute(String.valueOf(roleId),null));
						}
		    			requestMap.put(url, attributes);
		    		}else{
		    			//二级菜单没有URL，只用于菜单的分级展示
		    		}
		    		
				}
	    		
	    	}	
	    	List<ConfigAttribute> defaultAttrList = new ArrayList<ConfigAttribute>();
	    	defaultAttrList.add(new MenuOperConfigAttribute(Constance.USER_DEFAULT_AUTHORITY,null));
	    	requestMap.put("/**", defaultAttrList);
	    	Map<Object, Collection<ConfigAttribute>> tempMapToUse=new LinkedHashMap<Object, Collection<ConfigAttribute>>();
	        for (Map.Entry<String, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
	        	tempMapToUse.put(entry.getKey(), entry.getValue());
	        }
	        mapToUse=tempMapToUse;
    	}catch(Exception e){
    		logger.error("加载菜单失败",e);
    	}
    }

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public final Collection<ConfigAttribute> lookupAttributes(String url) {
        if (stripQueryStringFromUrls) {
            int firstQuestionMarkIndex = url.indexOf("?");
            if (firstQuestionMarkIndex != -1) {
                url = url.substring(0, firstQuestionMarkIndex);
            }
        }
        Collection<ConfigAttribute> attributes = extractMatchingAttributes(url, mapToUse);
        return attributes;
    }
	
	 public void setStripQueryStringFromUrls(boolean stripQueryStringFromUrls) {
	        this.stripQueryStringFromUrls = stripQueryStringFromUrls;
	 }
	 
	 private Collection<ConfigAttribute> extractMatchingAttributes(String url, Map<Object, Collection<ConfigAttribute>> map) {
	        if (map == null) {
	            return null;
	        }
	        for (Map.Entry<Object, Collection<ConfigAttribute>> entry : map.entrySet()) {
	            boolean matched = antPathMatcher.match(entry.getKey().toString(), url);
	            if (matched) {
	                return entry.getValue();
	            }
	        }
	        return null;
	 }

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

}
