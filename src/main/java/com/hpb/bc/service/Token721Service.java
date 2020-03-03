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
