package com.hpb.bc.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.TransactionHistoryInfo;
import com.hpb.bc.entity.result.Result;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;

/**
 *
 * @author zhangjian
 *
 */
public interface TransactionService {

	Result<Transaction> hpbGetTransactionByHash(List<String> reqStrList);

	Result<BigInteger> hpbGetTransactionCount(List<String> reqStrList);

	Result<String> hpbSendTransaction(List<String> reqStrList);

	Result<String> hpbSendRawTransaction(List<String> reqStrList);

	Result<TransactionReceipt> hpbGetTransactionReceipt(List<String> reqStrList);

	Result<PageInfo<TransactionHistoryInfo>> getTransactionHistory(List<String> reqStrList);

	Result<TransactionHistoryInfo> getTransactionDetail(List<String> reqStrList);

	Result<Map<String, Object>> historyList(List<String> reqStrList);

	Result<Map<String, Object>> getTypeList(List<String> reqStrList);
}
