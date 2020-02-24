package com.hpb.bc.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import javax.annotation.Resource;

import com.hpb.bc.constant.TokenConstant;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.entity.HpbInstantPrice;
import com.hpb.bc.vo.AddressSwitchVo;
import com.hpb.bc.vo.AssetVo;
import com.hpb.bc.vo.WalletTransferVo;
import io.hpb.web3.utils.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.entity.HpbDataDic;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.exception.ErrorException;
import com.hpb.bc.service.HpbDataDicService;
import com.hpb.bc.service.PersonalService;
import com.hpb.bc.util.NumericUtil;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionCount;
@Service
public class PersonalServiceImpl extends AbstractBaseService implements PersonalService {
	
	@Resource(name = "hpbAdmin")
	private Admin admin;
	@Autowired
	private HpbDataDicService hpbDataDicService;
	@Value("${web3.gasLimit}")
	private String gasLimit;
	@Value("${web3.gasPrice}")
	private String gasPrice;
	@Autowired
	private TokenManageServiceImpl tokenManageService;
	@Autowired
	private HpbInstantPriceServiceImpl hpbInstantPriceService;
	@Autowired
	private Token20ServiceImpl token20Service;

	@Override
	public Result<Map<String,Object>> walletTransfer(List<String> reqStrList) {
		Result<Map<String,Object>> result = new Result<>(ResultCode.SUCCESS);
		if(reqStrList==null || reqStrList.size()!=3) {
			result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
			return result;
		}
		String from = reqStrList.get(1);
		if(!NumericUtil.isValidAddress(from)) {
			result.result(ResultCode.FAIL.code(), ResultCode.INVALID_ADDRESS.code());
			return result;
		}
		List<WalletTransferVo> voList = new ArrayList<>();
		String type = reqStrList.get(2);
		try {
			if(TokenConstant.HPB.equalsIgnoreCase(type)){
				WalletTransferVo vo = new WalletTransferVo();
				vo.setType(TokenConstant.HPB);
				vo.setBalance(getBalance(from,DefaultBlockParameterName.LATEST.name()).toString());
				voList.add(vo);
			}

			if(TokenConstant.HRC20.equalsIgnoreCase(type)){
				voList = token20Service.query20TransferSymbolsAndBalance(from);
			}

			if(TokenConstant.HRC721.equalsIgnoreCase(type)){
				voList = tokenManageService.query721TransferStockNumByAddress(from);
			}
			Map<String, Object> resMap = new HashMap<>(1);
			resMap.put("list", voList);
			result.setData(resMap);
		} catch (Exception e) {
			result.exception(e);
			log.error("processId:["+reqStrList.get(0)+"]系统异常",e);
		}
		return result;
	}

	@Override
	public Result<String> hpbGetBalance(List<String> reqStrList) {
		Result<String> result = new Result<>(ResultCode.SUCCESS);
		if(reqStrList==null || reqStrList.size()<2) {
			result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
			return result;
		}
		String from = reqStrList.get(1);
		if(!NumericUtil.isValidAddress(from)) {
			result.result(ResultCode.FAIL.code(), ResultCode.INVALID_ADDRESS.code());
			return result;
		}
		try {
			BigInteger balance = getBalance(from,DefaultBlockParameterName.LATEST.name());
			result.setData(balance.toString());
		} catch (IOException e) {
			result.exception(e);
			log.error("processId:["+reqStrList.get(0)+"]系统异常",e);
		}
		return result;
	}

	@Override
	public Result<Map<String,Object>> listLegalBalances(List<String> reqStrList) {
		Result<Map<String,Object>> result = new Result<>(ResultCode.SUCCESS);
		try {
			if(reqStrList==null || reqStrList.size()<2) {
				result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
				return result;
			}
			Map<String,Object> data = new LinkedHashMap<>(3);
			List<AddressSwitchVo> vos = tokenManageService.queryAllTokenBalance(reqStrList);

			BigDecimal cnyTotalValue = BigDecimal.ZERO;
			BigDecimal usdTotalValue = BigDecimal.ZERO;
			for(AddressSwitchVo vo: vos){
				cnyTotalValue = cnyTotalValue.add(new BigDecimal(vo.getCnyTotalValue()));
				usdTotalValue = usdTotalValue.add(new BigDecimal(vo.getUsdTotalValue()));
			}
			data.put("cnyTotalValue", cnyTotalValue.toString());
			data.put("usdTotalValue", usdTotalValue.toString());
			data.put("list", vos);
			result.setData(data);
		} catch (Exception e) {
			result.result(ResultCode.FAIL.code(), ResultCode.INVALID_ADDRESS.code());
			log.error("processId:["+reqStrList.get(0)+"]系统异常",e);
		}
		return result;
	}

	@Override
	public Result<Map<String,Object>> getlegalTenderBalance(List<String> reqStrList) {
		Result<Map<String,Object>> result = new Result<>(ResultCode.SUCCESS);
		Map<String,Object>  resMap = new LinkedHashMap<>();
		if(reqStrList==null || reqStrList.size()<2) {
			result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
			return result;
		}
		String from = reqStrList.get(1);
		if(!NumericUtil.isValidAddress(from)) {
			result.result(ResultCode.FAIL.code(), ResultCode.INVALID_ADDRESS.code());
			return result;
		}
		try {
			result.setData(getlegalTenderBalanceMap(reqStrList));
		} catch (Exception e) {
			result.exception(e);
			log.error("processId:["+reqStrList.get(0)+"]系统异常",e);
		}
		return result;
	}

	private Map<String,Object> getlegalTenderBalanceMap(List<String> reqStrList){
		Map<String,Object>  resMap = new LinkedHashMap<>();
		AssetVo info = buildHpbStandardInfo(reqStrList.get(1));
		List<AssetVo> assetsList = new LinkedList<>();
		Map<String,Object> manage = tokenManageService.tokenManage(reqStrList).getData();
		List<ContractErcStandardInfo> manageList = (List<ContractErcStandardInfo>)manage.get("list");
		assetsList.add(info);
		if(null != manageList && manageList.size()>0){
			List<AssetVo> manages = convert2AssetVoList(manageList);
			assetsList.addAll(manages);
		}
		BigDecimal  pTotalCnyValue = BigDecimal.ZERO;
		BigDecimal  pTotalUsdValue = BigDecimal.ZERO;
		for(AssetVo assetsInfo: assetsList){
			pTotalCnyValue = pTotalCnyValue.add(new BigDecimal(assetsInfo.getCnyTotalValue()));
			pTotalUsdValue =pTotalUsdValue.add(new BigDecimal(assetsInfo.getUsdTotalValue()));
		}
		resMap.put("hpbBalance", info.getTokenTotalSupply());
		resMap.put("ptCnyValue", pTotalCnyValue.toString());
		resMap.put("ptUsdValue", pTotalUsdValue.toString());
		resMap.put("list", assetsList);
		return resMap;
	}

	private List<AssetVo> convert2AssetVoList(List<ContractErcStandardInfo> list){
		List<AssetVo> vos = new LinkedList<>();
		for (ContractErcStandardInfo info: list){
			AssetVo vo = new AssetVo();
			vo.setId(info.getId());
			vo.setTokenSymbol(info.getTokenSymbol());
			vo.setTokenSymbolImageUrl(info.getTokenSymbolImageUrl());
			vo.setTokenName(info.getTokenName());
			vo.setContractCreater(info.getContractCreater());
			vo.setContractAddress(info.getContractAddress());
			vo.setTokenTotalSupply(info.getTokenTotalSupply().toString());
			vo.setContractType(info.getContractType());
			vo.setUsdPrice(info.getUsdPrice());
			vo.setCnyPrice(info.getCnyPrice());
			vo.setCnyTotalValue(null == info.getCnyTotalValue()?  "0":info.getCnyTotalValue());
			vo.setUsdTotalValue(null == info.getUsdTotalValue()?  "0":info.getCnyTotalValue());
			vo.setDecimals(info.getDecimals().toString());
			vo.setBalanceOfToken(info.getBalanceOfToken());
			vos.add(vo);
		}
		return vos;
	}

	private AssetVo buildHpbStandardInfo(String from){
		AssetVo info = new AssetVo();
		try {
			info.setId(TokenConstant.HPB);
			info.setTokenSymbol(TokenConstant.HPB);
			info.setContractType(TokenConstant.HPB);
			info.setContractCreater(from);

			HpbInstantPrice hpbPrice = hpbInstantPriceService.queryHpbPrice(TokenConstant.HPB);
			BigInteger balance = getBalance(from,DefaultBlockParameterName.LATEST.name());
			info.setCnyPrice(hpbPrice.getCnyPrice());
			info.setUsdPrice(hpbPrice.getUsdPrice());
			info.setTokenTotalSupply(balance.toString());
			info.setDecimals("18");
			info.setTokenSymbolImageUrl("https://dapp-prod-fileserver01.oss-cn-hongkong.aliyuncs.com/wallet/HPB_logo.png");
			BigDecimal balanceDecimal = new BigDecimal(balance);
			info.setBalanceOfToken(balance.toString());
			balanceDecimal = Convert.fromWei(balanceDecimal, Convert.Unit.HPB);
			int intNum = 18;
			if(balanceDecimal.compareTo(BigDecimal.ZERO) == 0){
				intNum = 0;
			}
			info.setCnyTotalValue((balanceDecimal.multiply(new BigDecimal(hpbPrice.getCnyPrice())).setScale(intNum, BigDecimal.ROUND_HALF_EVEN)).toString());
			info.setUsdTotalValue((balanceDecimal.multiply(new BigDecimal(hpbPrice.getUsdPrice())).setScale(intNum, BigDecimal.ROUND_HALF_EVEN)).toString());
		} catch (Exception e) {
			log.info("查询HPB 币种基本信息错误 errMsg{}",e.getMessage(),e);
			return null;
		}
		return info;
	}

	@Override
	public Result<Map<String,String>> listBalance(List<String> reqStrList) {
		Result<Map<String,String>> result = new Result<>(ResultCode.SUCCESS);
		
		try {
			Map<String,String> data = new HashMap<>(reqStrList.size());
			for(int i=1;i<reqStrList.size();i++) {
				String from = reqStrList.get(i);
				if(!NumericUtil.isValidAddress(from)) {
					data.put(from, "INVALID_ADDRESS");
					continue;
				}
				data.put(from, getBalance(from,DefaultBlockParameterName.LATEST.name()).toString());
			}
			result.setData(data);
		} catch (IOException e) {
			result.exception(e);
			log.error("processId:["+reqStrList.get(0)+"]系统异常",e);
		}
		return result;
	}

	@Override
	public Result<Map<String,Object>> hpbGetNonce(List<String> reqStrList) {
		Result<Map<String,Object>> result = new Result<>(ResultCode.SUCCESS);
		if(reqStrList==null || reqStrList.size()<2) {
			result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
			return result;
		}
		String from = reqStrList.get(1);
		if(!NumericUtil.isValidAddress(from)) {
			result.result(ResultCode.FAIL.code(), ResultCode.INVALID_ADDRESS.code());
			return result;
		}
		try {
			Map<String,Object> data = new HashMap<>();
			data.put(BcConstant.NONCE, getNonce(reqStrList.get(1),DefaultBlockParameterName.LATEST));
			data.put(BcConstant.GAS_PRICE, selectGasPriceFromDataDic());
			data.put(BcConstant.GAS_LIMIT, selectGasLimitFromDataDic());
			result.setData(data);
		} catch (IOException |ErrorException e) {
			result.exception(e);
			log.error("processId:["+reqStrList.get(0)+"]系统异常",e);
		} 
		return result;
	}
	
	public BigInteger getBalance(String address,String name) throws IOException{
		HpbGetBalance hpbGetBalance = admin.hpbGetBalance(address, DefaultBlockParameterName.fromString(name)).send();
		return hpbGetBalance.getBalance(); 
	}
	
	public BigInteger getNonce(String address,DefaultBlockParameter parameter) throws IOException {
		HpbGetTransactionCount transactionCount = admin.hpbGetTransactionCount(address, parameter).send();
		return transactionCount.getTransactionCount();
	}
	
	public BigInteger getBalance(String address,DefaultBlockParameter parameter) throws IOException {
		HpbGetBalance hpbGetBalance = admin.hpbGetBalance(address, parameter).send();
		return hpbGetBalance.getBalance();
	}
	
	public String selectGasPriceFromDataDic() {
		HpbDataDic hpbDataDic = hpbDataDicService.selectFromDataDic(BcConstant.HPB_DATA_TYPE_NO,BcConstant.GAS_PRICE);
		if(hpbDataDic==null){
			return this.gasPrice;
		}
		String gasPrice = hpbDataDic.getDataName();
		if(StringUtils.hasText(gasPrice)) {
			return gasPrice;
		}
		return this.gasPrice;
	}
	
	public String selectGasLimitFromDataDic() {
		HpbDataDic hpbDataDic = hpbDataDicService.selectFromDataDic(BcConstant.HPB_DATA_TYPE_NO,BcConstant.GAS_LIMIT);
		if(hpbDataDic==null){
			return this.gasLimit;
		}
		String gasLimit = hpbDataDic.getDataName();
		if(StringUtils.hasText(gasLimit)) {
			return gasLimit;
		}
		return this.gasLimit;
	}
}
