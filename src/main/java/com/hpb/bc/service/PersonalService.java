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
