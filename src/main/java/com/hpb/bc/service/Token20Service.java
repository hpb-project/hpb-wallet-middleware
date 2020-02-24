package com.hpb.bc.service;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.TransactionHistoryInfo;
import com.hpb.bc.entity.result.Result;

import java.util.List;
import java.util.Map;


public interface Token20Service {
    PageInfo<TransactionHistoryInfo> list(List<String> reqStrList);
}
