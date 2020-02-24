package com.hpb.bc.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.hpb.bc.cache.CacheSessionConfiguration;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.entity.cache.CacheSession;
import com.hpb.bc.util.UUIDGeneratorUtil;


public abstract class BaseController{
	private static final String STATUS_CODE = "javax.servlet.error.status_code";
	private static final int _EXPIRY =-1;
	@Autowired
	protected CacheSessionConfiguration cacheSessionConfiguration;
	protected String getProccessId(HttpServletRequest request){
		return UUIDGeneratorUtil.generate(request);
	}
	protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
	

	protected String getSessionId(HttpServletRequest request,HttpServletResponse response) {
		String sessionId = setSessionId(request,response,_EXPIRY);
		CacheSession session = cacheSessionConfiguration.findBySessionId(sessionId);
		if(session!=null) {
			cacheSessionConfiguration.update(session);
		}
		return sessionId;
	}

	protected String setSessionId(HttpServletRequest request, 
			HttpServletResponse response,int expiry) {
		String sessionId=null;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
			for(Cookie c:cookies) {
				if(BcConstant.SESSION_KEY.equals(c.getName())) {
					sessionId=c.getValue();
					if(c.getMaxAge()!=expiry) {
						c.setMaxAge(expiry);
						response.addCookie(c);
					}
					break;
				}
			}
		}
		if(StringUtils.isBlank(sessionId)) {
			sessionId= getProccessId(request);
			Cookie cookie=new Cookie(BcConstant.SESSION_KEY,sessionId);
			cookie.setMaxAge(expiry);
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
		}
		return sessionId;
	}
}