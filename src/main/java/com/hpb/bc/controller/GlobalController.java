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

package com.hpb.bc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.service.GlobalService;
import io.hpb.web3.protocol.core.methods.response.HpbBlockNumber;
import io.hpb.web3.protocol.core.methods.response.HpbGasPrice;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import io.hpb.web3.protocol.core.methods.response.HpbHashrate;
import io.hpb.web3.protocol.core.methods.response.HpbMining;
import io.hpb.web3.protocol.core.methods.response.HpbSyncing;

@RestController
@RequestMapping("/hpb")
public class GlobalController extends BaseController {
	@Autowired
	private GlobalService globalService;

	@RequestMapping("/getBalance")
	public Result<HpbGetBalance> hpbGetBalance(String address, String name) {
		Result<HpbGetBalance> result = globalService.hpbGetBalance(address, name);
		return result;
	}
	@RequestMapping("/syncing")
	public Result<HpbSyncing> hpbSyncing() {
		Result<HpbSyncing> result = globalService.hpbSyncing();
		return result;
	}
	@RequestMapping("/mining")
	public Result<HpbMining> hpbMining() {
		Result<HpbMining> result = globalService.hpbMining();
		return result;
	}
	@RequestMapping("/hashrate")
	public Result<HpbHashrate> hpbHashrate() {
		Result<HpbHashrate> result = globalService.hpbHashrate();
		return result;
	}
	@RequestMapping("/gasPrice")
	public Result<HpbGasPrice> hpbGasPrice() {
		Result<HpbGasPrice> result = globalService.hpbGasPrice();
		return result;
	}
	@RequestMapping("/blockNumber")
	public Result<HpbBlockNumber> hpbBlockNumber() {
		Result<HpbBlockNumber> result = globalService.hpbBlockNumber();
		return result;
	}

}
