package com.hpb.bc.service;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.TransactionHistoryInfo;
import com.hpb.bc.entity.result.Result;

import java.util.List;
import java.util.Map;

public interface Token721Service {

    Result<Map<String, Object>> get721StockDetail(List<String> reqStrList);

    Result<Map<String, Object>> get721TokenIdDetail(List<String> reqStrList);

    PageInfo<TransactionHistoryInfo> get721transferRecord(List<String> reqStrList);

    Result<Map<String, Object>> getTokenId(List<String> reqStrList);

    Result<Map<String, Object>> get721TransferDetail(List<String> reqStrList);

    Result<Map<String, Object>> getTokenIdsByTxHash(List<String> reqStrList);
}
