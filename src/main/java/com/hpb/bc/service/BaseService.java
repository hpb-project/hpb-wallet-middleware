package com.hpb.bc.service;

import java.util.List;

import com.hpb.bc.entity.result.Result;

public interface BaseService {
	Result<?> execute(String methodName,List<String> reqStrList);
}
