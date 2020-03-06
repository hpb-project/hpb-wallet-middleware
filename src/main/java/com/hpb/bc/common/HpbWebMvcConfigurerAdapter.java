/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpb.bc.common;

import com.hpb.bc.filter.ActionFilter;
import com.hpb.bc.interceptor.WebInterceptor;
import com.hpb.bc.resolver.FormatJsonReturnValueHandler;
import com.hpb.bc.resolver.JsonPathArgumentResolver;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;



@Configuration
public class HpbWebMvcConfigurerAdapter implements WebMvcConfigurer {
    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        return new ByteArrayHttpMessageConverter();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebInterceptor webInterceptor = new WebInterceptor();
        registry.addInterceptor(webInterceptor).addPathPatterns("/**").
                excludePathPatterns("/error/**").
                excludePathPatterns("/static/**").
                excludePathPatterns("/error").
                excludePathPatterns("/static");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean<ActionFilter> filterRegistrationBean() {
        FilterRegistrationBean<ActionFilter> registrationBean = new FilterRegistrationBean<ActionFilter>();
        registrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        registrationBean.setFilter(new ActionFilter());
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/**");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/error/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/error/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
        WebMvcConfigurer.super.configurePathMatch(configurer);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new MyErrorPageRegistrar();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toLogin").setViewName("login");
        registry.addViewController("/error").setViewName("/error/error.html");
        WebMvcConfigurer.super.addViewControllers(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JsonPathArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    @Bean
    public FormatJsonReturnValueHandler JsonReturnHandler() {
        FormatJsonReturnValueHandler formatJsonReturnValueHandler = new FormatJsonReturnValueHandler();
        return formatJsonReturnValueHandler;
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(JsonReturnHandler());
        WebMvcConfigurer.super.addReturnValueHandlers(returnValueHandlers);
    }

    public static class MyErrorPageRegistrar implements ErrorPageRegistrar {
        @Override
        public void registerErrorPages(ErrorPageRegistry registry) {
            registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
        }
    }
}