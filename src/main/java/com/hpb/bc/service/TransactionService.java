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
