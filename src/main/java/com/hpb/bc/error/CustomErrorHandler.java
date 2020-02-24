package com.hpb.bc.error;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class CustomErrorHandler extends DefaultResponseErrorHandler {  
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override  
    public void handleError(ClientHttpResponse response) throws IOException {  
    	logger.info("调用handleError");
    }  
  
}  