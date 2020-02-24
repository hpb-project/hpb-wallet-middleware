package com.hpb.bc.service;

import com.hpb.bc.entity.result.Result;

import java.util.List;
import java.util.Map;


public interface TokenManageService {
    Result<Map<String, Object>> tokenManage(List<String> reqStrList);
}
