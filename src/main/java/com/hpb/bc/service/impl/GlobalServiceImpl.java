package com.hpb.bc.service.impl;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.GlobalService;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class GlobalServiceImpl extends AbstractBaseService implements GlobalService {
	
	@Resource(name="hpbAdmin")
	private Admin admin;
	
	@Override
	public Result<HpbGetBalance> hpbGetBalance(String address, String name) {
		Result<HpbGetBalance> result = new Result<>(ResultCode.SUCCESS);
		DefaultBlockParameter defaultBlockParameter = DefaultBlockParameterName.fromString(name);
		try {
			result.setData(admin.hpbGetBalance(address, defaultBlockParameter).send());
		} catch (IOException e) {
			result.exception(e);
		}
		return result;
	}
	
	@Override
	public Result<HpbSyncing> hpbSyncing() {
		Result<HpbSyncing> result = new Result<>(ResultCode.SUCCESS);
		try {
			result.setData(admin.hpbSyncing().send());
		} catch (IOException e) {
			result.exception(e);
		}
		return result;
	}

	@Override
	public Result<HpbMining> hpbMining() {
		Result<HpbMining> result = new Result<>(ResultCode.SUCCESS);
		try {
			result.setData(admin.hpbMining().send());
		} catch (IOException e) {
			result.exception(e);
		}
		return result;
	}

	@Override
	public Result<HpbHashrate> hpbHashrate() {
		Result<HpbHashrate> result = new Result<>(ResultCode.SUCCESS);
		try {
			result.setData(admin.hpbHashrate().send());
		} catch (IOException e) {
			result.exception(e);
		}
		return result;
	}

	@Override
	public Result<HpbGasPrice> hpbGasPrice() {
		Result<HpbGasPrice> result = new Result<>(ResultCode.SUCCESS);
		try {
			result.setData(admin.hpbGasPrice().send());
		} catch (IOException e) {
			result.exception(e);
		}
		return result;
	}

	@Override
	public Result<HpbBlockNumber> hpbBlockNumber() {
		Result<HpbBlockNumber> result = new Result<>(ResultCode.SUCCESS);
		try {
			result.setData(admin.hpbBlockNumber().send());
		} catch (IOException e) {
			result.exception(e);
		}
		return result;
	}
}
