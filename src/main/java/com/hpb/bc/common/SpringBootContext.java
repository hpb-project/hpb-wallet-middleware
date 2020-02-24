package com.hpb.bc.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class SpringBootContext {
    private static ApplicationContext aplicationContext;

    public static ApplicationContext getAplicationContext() {
        return aplicationContext;
    }

    public static void setAplicationContext(ApplicationContext aplicationContext) {
        SpringBootContext.aplicationContext = aplicationContext;
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return aplicationContext.getBean(name, requiredType);
    }

}
