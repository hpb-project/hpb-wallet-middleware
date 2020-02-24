package com.hpb.bc.resolver;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

public class FormatJsonReturnValueHandler implements HandlerMethodReturnValueHandler {
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return (AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResultBeanResponseBody.class) != null
				|| returnType.getMethodAnnotation(ResultBeanResponseBody.class) != null);
	}

	@Override
	public void handleReturnValue(Object obj, MethodParameter methodParameter,
			ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
		modelAndViewContainer.setRequestHandled(true);
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface ResultBeanResponseBody {
	}
}