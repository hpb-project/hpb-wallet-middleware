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

package com.hpb.bc.service.impl;

import com.hpb.bc.constant.TokenConstant;
import com.hpb.bc.entity.CoinConfig;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.entity.HpbInstantPrice;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.example.ContractErcStandardInfoExample;
import com.hpb.bc.service.TokenManageService;
import com.hpb.bc.token.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.util.ERC20Util;
import com.hpb.bc.util.ERC721Util;
import com.hpb.bc.util.NumericUtil;
import com.hpb.bc.vo.AddressSwitchVo;
import com.hpb.bc.vo.WalletTransferVo;
import com.hpb.bc.website.mapper.CoinConfigMapper;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.utils.Convert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


@Service
public class TokenManageServiceImpl extends AbstractBaseService implements TokenManageService {
    @Autowired
    private ContractErcStandardInfoMapper contractErcStandardInfoMapper;
    @Autowired
    private CoinConfigMapper coinConfigMapper;
    @Autowired
    private ERC721Util erc721Util;
    @Autowired
    private ERC20Util erc20Util;
    @Autowired
    private PersonalServiceImpl personalService;
    @Autowired
    private HpbInstantPriceServiceImpl hpbInstantPriceService;

    @Override
    public Result<Map<String, Object>> tokenManage(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            String addr = reqStrList.get(1);
            if (null == reqStrList || reqStrList.size() != 2 || !NumericUtil.isValidAddress(addr)) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }

            Map<String, Object> resMap = tokenManageMap(reqStrList);
            resMap.put("list", resMap.get("list"));
            result.setData(resMap);
        } catch (Exception e) {
            log.error("【代币管理查询失败 reqParam:{} {} 】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    public Map<String, Object> tokenManageMap(List<String> reqStrList) {
        Map<String, Object> resMap = new HashMap<>(1);
        List<ContractErcStandardInfo> firstInfos = new LinkedList<>();
        try {
            String addr = reqStrList.get(1);
            List<CoinConfig> coinConfigs = coinConfigMapper.selectByStatus("1");
            List<String> manageSymbols = new ArrayList<>();
            List<String> manageContracts = new ArrayList<>();
            for (CoinConfig config : coinConfigs) {
                if (!manageSymbols.contains(config.getCoinSymbol())) {
                    manageSymbols.add(config.getCoinSymbol());
                }
                if (!manageContracts.contains(config.getContractAddr())) {
                    manageContracts.add(config.getContractAddr());
                }
            }

            List<ContractErcStandardInfo> contractErcStandardInfos = new ArrayList<>();
            if (manageSymbols.size() > 0) {
                ContractErcStandardInfoExample example = new ContractErcStandardInfoExample();
                ContractErcStandardInfoExample.Criteria criteria = example.createCriteria();
                example.setOrderByClause("create_timestamp desc");
                List<String> contractTypes = new ArrayList<>();
                contractTypes.add(TokenConstant.ERC20);
                contractTypes.add(TokenConstant.ERC721);
                criteria.andContractTypeIn(contractTypes).andTokenSymbolIn(manageSymbols).andContractAddressIn(manageContracts);

                contractErcStandardInfos = contractErcStandardInfoMapper.selectByExample(example);

                List<ContractErcStandardInfo> secondInfos = new LinkedList<>();
                List<String> standardSymbols = new ArrayList<>();
                for (ContractErcStandardInfo info : contractErcStandardInfos) {
                    info.setTokenTotalSupplys(info.getTokenTotalSupply().toString());
                    //ERC20 ERC721
                    if (!standardSymbols.contains(info.getTokenSymbol())) {
                        standardSymbols.add(info.getTokenSymbol());
                    }
                    BigDecimal balance = BigDecimal.ZERO;
                    int decimalInt = 0;
                    StringBuilder decimalSb = new StringBuilder("1");
                    if (TokenConstant.ERC721.equalsIgnoreCase(info.getContractType())) {
                        info.setContractType(TokenConstant.HRC721);
                        balance = erc721Util.balanceOfAddr(addr, info.getContractAddress());
                    } else if (TokenConstant.ERC20.equalsIgnoreCase(info.getContractType())) {
                        info.setContractType(TokenConstant.HRC20);
                        balance = erc20Util.balanceOfAddr(addr, info.getContractAddress());
                        decimalInt = Integer.parseInt(erc20Util.queryDecimals(info.getContractAddress()).getValue().toString());
                        info.setDecimals(Long.valueOf(decimalInt));
                        if (decimalInt > 0) {
                            for (int j = 0; j < decimalInt; j++) {
                                decimalSb.append("0");
                            }
                        }
                    }
                    HpbInstantPrice hpbInstantPrice = hpbInstantPriceService.queryHpbPrice(info.getTokenSymbol());
                    info.setCnyPrice(null == hpbInstantPrice ? null : hpbInstantPrice.getCnyPrice());
                    info.setUsdPrice(null == hpbInstantPrice ? null : hpbInstantPrice.getUsdPrice());
                    balance = (null == balance) ? BigDecimal.ZERO : balance;
                    info.setBalanceOfToken(balance.toString());
                    if (decimalInt > 0) {
                        balance = balance.divide(new BigDecimal(decimalSb.toString()));
                    }
                    if ("0".equals(info.getCnyPrice()) || BigDecimal.ZERO.compareTo(balance) == 0) {
                        decimalInt = 0;
                    }
                    BigDecimal cnyPrice = StringUtils.isBlank(info.getCnyPrice()) ? BigDecimal.ZERO : new BigDecimal(info.getCnyPrice());
                    BigDecimal usdPrice = StringUtils.isBlank(info.getUsdPrice()) ? BigDecimal.ZERO : new BigDecimal(info.getUsdPrice());

                    info.setCnyTotalValue((balance.multiply(cnyPrice).setScale(decimalInt, BigDecimal.ROUND_HALF_EVEN)).toString());
                    info.setUsdTotalValue((balance.multiply(usdPrice).setScale(decimalInt, BigDecimal.ROUND_HALF_EVEN)).toString());
                    for (CoinConfig coinConfig : coinConfigs) {
                        if (coinConfig.getCoinSymbol().equals(info.getTokenSymbol())) {
                            info.setOrderNum(coinConfig.getOrderNum());
                            if (coinConfig.getIsShow().equals("1")) {
                                firstInfos.add(info);
                            } else {
                                secondInfos.add(info);
                            }
                        }
                    }
                }
                firstInfos.addAll(secondInfos);
                for (String appendSymbol : standardSymbols) {
                    if (!manageSymbols.contains(appendSymbol)) {
                        ContractErcStandardInfoExample example2 = new ContractErcStandardInfoExample();
                        ContractErcStandardInfoExample.Criteria criteria2 = example.createCriteria();
                        criteria2.andTokenSymbolEqualTo(appendSymbol);

                        List<ContractErcStandardInfo> appendList = contractErcStandardInfoMapper.selectByExample(example2);
                        for (ContractErcStandardInfo info : appendList) {
                            info.setCnyTotalValue("0");
                            info.setUsdTotalValue("0");
                        }
                        firstInfos.addAll(appendList);
                    }
                }
            }
            Collections.sort(firstInfos, Comparator.comparing(ContractErcStandardInfo::getOrderNum).reversed());
            resMap.put("list", firstInfos);
            return resMap;
        } catch (Exception e) {
            log.error("----------tokenManageMap reqParam:{} {} ", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);

            return resMap;
        }
    }

    public List<AddressSwitchVo> queryAllTokenBalance(List<String> reqStrList) throws Exception {
        List<AddressSwitchVo> addressSwitchVos = new LinkedList<>();
        List<CoinConfig> coinConfigs = coinConfigMapper.selectByStatus("1");
        List<String> manageSymbols = new ArrayList<>();
        for (CoinConfig config : coinConfigs) {
            if (!manageSymbols.contains(config.getCoinSymbol())) {
                manageSymbols.add(config.getCoinSymbol());
            }
        }
        coinConfigs.sort(Comparator.comparing(CoinConfig::getOrderNum).reversed());

        try {
            for (int i = 1; i < reqStrList.size(); i++) {
                String from = reqStrList.get(i);
                if (!NumericUtil.isValidAddress(from)) {
                    continue;
                }
                Map<String, Object> manage = tokenManage(reqStrList).getData();
                List<ContractErcStandardInfo> contractErcStandardInfos = new ArrayList<>();

                if (null != manage) {
                    contractErcStandardInfos = (List<ContractErcStandardInfo>) manage.get("list");
                }

                HpbInstantPrice hpbPrice = hpbInstantPriceService.queryHpbPrice(TokenConstant.HPB);
                String balance = personalService.getBalance(from, DefaultBlockParameterName.LATEST.name()).toString();
                BigDecimal balanceDecimal = Convert.fromWei(balance, Convert.Unit.HPB);
                int intNum = 18;
                if (balanceDecimal.compareTo(BigDecimal.ZERO) == 0) {
                    intNum = 0;
                }
                BigDecimal cnyDecimal = balanceDecimal.multiply(new BigDecimal(hpbPrice.getCnyPrice())).setScale(intNum, BigDecimal.ROUND_HALF_EVEN);
                BigDecimal usdDecimal = balanceDecimal.multiply(new BigDecimal(hpbPrice.getUsdPrice())).setScale(intNum, BigDecimal.ROUND_HALF_EVEN);

                BigDecimal cnyValue = BigDecimal.ZERO;
                BigDecimal usdValue = BigDecimal.ZERO;
                for (ContractErcStandardInfo info : contractErcStandardInfos) {
                    cnyValue = cnyValue.add(new BigDecimal(info.getCnyTotalValue()));
                    usdValue = usdValue.add(new BigDecimal(info.getUsdTotalValue()));
                }
                AddressSwitchVo vo = new AddressSwitchVo();
                vo.setAddress(from);
                cnyValue = cnyValue.add(cnyDecimal);
                usdValue = usdValue.add(usdDecimal);
                vo.setCnyTotalValue(cnyValue.toString());
                vo.setUsdTotalValue(usdValue.toString());
                addressSwitchVos.add(vo);
            }
            return addressSwitchVos;
        } catch (Exception e) {
            log.error("地址切换查询余额失败 errMsg {},{},{}", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            return addressSwitchVos;
        }
    }

    public List<WalletTransferVo> query721TransferStockNumByAddress(String address) throws Exception {
        List<String> manageContracts = contractAddrManage(TokenConstant.HRC721);

        List<ContractErcStandardInfo> infos = new ArrayList<>();
        if (manageContracts.size() > 0) {
            ContractErcStandardInfoExample example = new ContractErcStandardInfoExample();
            example.setOrderByClause("create_timestamp desc");
            ContractErcStandardInfoExample.Criteria criteria = example.createCriteria();
            criteria.andContractAddressIn(manageContracts);
            criteria.andContractTypeEqualTo(TokenConstant.ERC721);
            infos = contractErcStandardInfoMapper.selectByExample(example);
        }

        List<WalletTransferVo> checkResultList = new ArrayList<>();
        for (ContractErcStandardInfo info : infos) {
            BigDecimal balance = erc721Util.balanceOfAddr(address, info.getContractAddress());
            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                WalletTransferVo vo = new WalletTransferVo();
                vo.setContractAddress(info.getContractAddress());
                vo.setType(TokenConstant.HRC721);
                vo.setTokenNum(balance.toString());
                vo.setSymbol(info.getTokenSymbol());
                checkResultList.add(vo);
            }
        }
        return checkResultList;
    }

    public List<String> contractAddrManage(String contractType) {
        Map<String, String> param = new HashMap<>();
        param.put("status", BigInteger.ONE.toString());
        param.put("coinType", contractType);
        List<CoinConfig> coinConfigs = coinConfigMapper.selectByStatusAndType(param);
        List<String> manageContracts = new ArrayList<>();

        for (CoinConfig config : coinConfigs) {
            if (!manageContracts.contains(config.getContractAddr())) {
                manageContracts.add(config.getContractAddr());
            }
        }
        return manageContracts;
    }

}
