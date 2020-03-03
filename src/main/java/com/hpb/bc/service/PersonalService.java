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

import java.util.List;
import java.util.Map;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.vo.WalletTransferVo;

public interface PersonalService {

	Result<String> hpbGetBalance(List<String> reqStrList);

	Result<Map<String,Object>> getlegalTenderBalance(List<String> reqStrList);

	Result<Map<String,Object>> walletTransfer(List<String> reqStrList);

	Result<Map<String,Object>> hpbGetNonce(List<String> reqStrList);

	Result<Map<String,String>> listBalance(List<String> reqStrList);

	Result<Map<String,Object>> listLegalBalances(List<String> reqStrList);
	
}
