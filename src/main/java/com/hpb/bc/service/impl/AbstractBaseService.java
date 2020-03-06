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

package com.hpb.bc.service.impl;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.BaseService;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;

public abstract class AbstractBaseService implements BaseService {
	
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	public Result<?> execute(String methodName,List<String> reqStrList){
		
		Class<? extends AbstractBaseService> clazz = this.getClass(); 
		
        try {
			Method method = clazz.getDeclaredMethod(methodName);
			method.setAccessible(true);
			return (Result<?>) method.invoke(this, reqStrList);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error(e.getMessage(), e);
			return new Result<>(ResultCode.EXCEPTION, e.getMessage());
		}
	
	}

}
