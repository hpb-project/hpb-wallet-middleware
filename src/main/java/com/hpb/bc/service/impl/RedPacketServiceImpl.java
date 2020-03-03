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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hpb.bc.async.RedPacketAsy;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.constant.RedPacketConstant;
import com.hpb.bc.entity.*;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.example.*;
import com.hpb.bc.service.RedPacketService;
import com.hpb.bc.util.NumericUtil;
import com.hpb.bc.util.RandomUtil;
import com.hpb.bc.util.UUIDGeneratorUtil;
import com.hpb.bc.website.mapper.*;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.Response;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;
import io.hpb.web3.utils.Convert;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Service
public class RedPacketServiceImpl extends AbstractBaseService implements RedPacketService {
    /**
     * 1-普通 2-拼手气
     */
    private static final String COMMON_RED = "1";
    private static final String SPELL_LUCK_RED = "2";
    /**
     * 红包结束
     */
    private static final String OVER = "2";
    /**
     * 领取红包，已领取
     */
    private static final String DRAW_EXIST = "3";
    /**
     * 领取红包，钥匙失效
     */
    private static final String KEY_INVAILD = "4";
    /**
     * 领取红包，确认中
     */
    private static final String DRAW_PACKET_CONFIRM = "5";
    /**
     * 发红包，默认确认中
     */
    private static final String INIT_PACKET_STATUS = "2";
    @Resource(name = "hpbAdmin")
    private Admin admin;
    @Value("${redpacket.contract.address}")
    private String contractAddr;
    @Value("${redpacket.from}")
    private String proxyAddr;
    @Autowired
    private RedPacketMapper redPacketMapper;
    @Autowired
    private RedPacketSendMapper redPacketSendMapper;
    @Autowired
    private RedPacketConfigMapper redPacketConfigMapper;
    public LoadingCache<String, String> cachePacketConfig = CacheBuilder.newBuilder()
            .refreshAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String enterType) {
                    RedPacketConfigExample example = new RedPacketConfigExample();
                    example.createCriteria().andEnterTypeEqualTo(enterType.split("_")[1]);
                    List<RedPacketConfig> configs = redPacketConfigMapper.selectByExample(example);
                    RedPacketConfig config = (null != configs && configs.size() > 0) ? configs.get(0) : null;
                    return null != config ? config.getEnterType() + "_" + config.getMaxNum() + "_" + config.getMinPerCoin() + "_" +
                            config.getMaxPerCoin() + "_" + config.getMultiple() + "_" + config.getTime() + "_" + config.getRate() : null;
                }
            });
    @Autowired
    private RedPacketKeyMapper redPacketKeyMapper;
    @Autowired
    private RedPacketUseMapper redPacketUseMapper;
    @Autowired
    private RedPacketDetailMapper redPacketDetailMapper;
    @Autowired
    private RedPacketAsy redPacketAsy;

    @Override
    public Result<Map<String, Object>> rawReady(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 7) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String enterType = reqStrList.get(4);
            String totalCoinStr = reqStrList.get(2);
            BigDecimal totalCoin = new BigDecimal(Convert.fromWei(totalCoinStr, Convert.Unit.HPB).toString());
            if (BigInteger.ONE.toString().equals(enterType)) {
                int totalNum = Integer.parseInt(reqStrList.get(3));
                String type = reqStrList.get(5);
                String[] resValues = new String[totalNum];
                BigDecimal[] values = new BigDecimal[totalNum];

                Map<String, String> mapObj = getPacketConfig(enterType);
                BigDecimal resMinAmount = new BigDecimal(mapObj.get("minAmount"));
                int scale = MapUtils.getInteger(mapObj, "scale");
                int maxNum = Integer.parseInt(cachePacketConfig.get(RedPacketConstant.PREFIX + "_" + enterType).split("_")[1]);
                if (totalNum <= maxNum && maxNum <= RedPacketConstant.BATCH_NUM) {
                    if (COMMON_RED.equals(type)) {
                        BigDecimal perValue = totalCoin.divide(new BigDecimal(totalNum)).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
                        for (int i = 0; i < totalNum; i++) {
                            values[i] = perValue;
                        }
                    } else if (SPELL_LUCK_RED.equals(type)) {
                        values = RandomUtil.randomHandOutAlgorithm(totalCoin, totalNum, scale, resMinAmount);
                    }
                    for (int i = 0; i < totalNum; i++) {
                        resValues[i] = Convert.toWei(values[i].toString(), Convert.Unit.HPB).toBigInteger().toString();
                    }
                    Map<String, Object> resMap = new HashMap<>(2);
                    resMap.put("packetNo", batchSend(reqStrList));
                    resMap.put("valuesArr", resValues);
                    resMap.put("contractAddr", contractAddr);
                    resMap.put("proxyAddr", proxyAddr);
                    result.setData(resMap);
                } else {
                    log.error("红包个数超限，请知悉 红包个数：{},maxNum：{}", totalNum, RedPacketConstant.BATCH_NUM);
                    result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                    return result;
                }
            } else {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
        } catch (Exception e) {
            log.error("【红包批量金额分配失败 reqParam:{} {} 】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    public Map<String, String> getPacketConfig(String enterType) {
        Map<String, String> resMap = new HashMap<>(2);
        try {
            String config = cachePacketConfig.get(RedPacketConstant.PREFIX + "_" + enterType);
            BigDecimal minAmount;
            if (StringUtils.isBlank(config)) {
                minAmount = RedPacketConstant.MIN_AMOUNT;
            } else {
                minAmount = new BigDecimal(config.split("_")[2]);
            }
            String minAmountStr = minAmount.toString();
            int scale = minAmountStr.substring(minAmountStr.indexOf(".") + 1, minAmountStr.indexOf("1") + 1).length();
            resMap.put("scale", String.valueOf(scale));
            resMap.put("minAmount", minAmount.toString());
        } catch (ExecutionException e) {
            log.error("读取红包配置失败 enterType:{}", enterType);
        }
        return resMap;
    }

    @Override
    public Result<String> sendRawTxAndUpdateSendInfo(List<String> reqStrList) {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        try {
            String packetNo = reqStrList.get(1);
            String input = reqStrList.get(2);
            if (reqStrList.size() != 3 || StringUtils.isBlank(packetNo) || StringUtils.isBlank(input)) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            HpbSendTransaction hpbSendTransaction = admin.hpbSendRawTransaction(input).send();
            Response.Error error = hpbSendTransaction.getError();
            if (null != error) {
                result.error(error);
            } else {
                String hash = hpbSendTransaction.getTransactionHash();
                result.setData(hash);
                redPacketAsy.updateTokenIdByWithSuccessHash(packetNo, hash);
            }
        } catch (Exception e) {
            log.error("【发送红包签名信息失败 reqParam:{} {} 】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<String> refreshSend(List<String> reqStrList) {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (reqStrList.size() != 2 || StringUtils.isBlank(reqStrList.get(1))) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            RedPacket redPacket = redPacketMapper.queryByRedPacketNo(reqStrList.get(1));
            if (null == redPacket) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            result.setData(redPacket.getStatus());
        } catch (Exception e) {
            log.error("刷新失败{},{}，{}", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> openShare(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 2 || StringUtils.isBlank(reqStrList.get(1))) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            RedPacket redPacket = redPacketMapper.queryByRedPacketNo(reqStrList.get(1));
            if (null == redPacket) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            Map<String, Object> resMap = new LinkedHashMap<>(3);
            resMap.put("utmostCoin", getutmostCoin(redPacket));
            resMap.put("from", redPacket.getFromAddr());
            resMap.put("title", redPacket.getTitle());
            result.setData(resMap);
        } catch (Exception e) {
            log.error("打开分享页失败{},{}，{}", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.FAIL.code());
            return result;
        }
        return result;
    }

    private String getutmostCoin(RedPacket redPacket) {
        String redPacketType = redPacket.getRedPacketType();
        String totalCoin = redPacket.getTotalCoin();
        BigDecimal totalCoinHpb = Convert.fromWei(redPacket.getTotalCoin(), Convert.Unit.HPB);

        String utmostCoinStr;
        if (totalCoinHpb.compareTo(RedPacketConstant.MIN_AMOUNT) < 1) {
            utmostCoinStr = Convert.toWei(RedPacketConstant.MIN_AMOUNT, Convert.Unit.HPB).toBigInteger().toString();
        } else {
            int totalNum = redPacket.getTotalNum();
            if (BigInteger.ONE.toString().equals(redPacketType)) {
                utmostCoinStr = Convert.toWei(totalCoinHpb.divide(new BigDecimal(totalNum)), Convert.Unit.HPB).toBigInteger().toString();
            } else {
                if (totalNum == 1) {
                    utmostCoinStr = totalCoin;
                } else {
                    Map<String, String> mapObj = getPacketConfig(redPacket.getEnterType());
                    utmostCoinStr = Convert.toWei(totalCoinHpb.divide(new BigDecimal(2), MapUtils.getInteger(mapObj, "scale"), RoundingMode.HALF_UP), Convert.Unit.HPB).toBigInteger().toString();
                }
            }
        }
        return utmostCoinStr;
    }

    public String batchSend(List<String> reqStrList) {
        try {
            int totalNum = Integer.parseInt(reqStrList.get(3));
            String type = reqStrList.get(5);
            String totalCoin = reqStrList.get(2);
            String sendAddress = reqStrList.get(1);
            String title = reqStrList.get(6);
            BigInteger totalMinerFee = BigInteger.valueOf(Long.parseLong(RedPacketConstant.PER_MINER_FEE)).multiply(BigInteger.valueOf(Long.parseLong(reqStrList.get(3))));

            String no = UUIDGeneratorUtil.generate(UUIDGeneratorUtil.get32UUID()).toLowerCase();
            RedPacket redPacket = new RedPacket();
            redPacket.setRedPacketNo(no);
            redPacket.setCoinSymbol(RedPacketConstant.COIN_SYMBOL);
            redPacket.setTotalNum(totalNum);
            redPacket.setFromAddr(sendAddress);
            redPacket.setRedPacketType(type);
            redPacket.setEnterType(reqStrList.get(4));
            redPacket.setTotalCoin(totalCoin);
            redPacket.setMinerFee(totalMinerFee.toString());
            redPacket.setStatus(INIT_PACKET_STATUS);
            redPacket.setTitle(title);
            redPacket.setIsOverTime(BigInteger.ZERO.toString());
            redPacketMapper.insert(redPacket);
            return no;
        } catch (Exception e) {
            log.error("红包初始数据落库失败{},{}，{}", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Result<String> checkPacketIsVaild(List<String> reqStrList) {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        try {
            RedPacket redPacket = redPacketMapper.queryByRedPacketNo(reqStrList.get(1));
            if (null == redPacket) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            result.setData(redPacket.getIsOver());
        } catch (Exception e) {
            log.error("【校验红包是否结束失败 reqParam:{} {} 】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<RedPacketConfig> getRedPacketConfig(List<String> reqStrList) {
        Result<RedPacketConfig> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (reqStrList.size() < 2) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            result.setData(redPacketConfigMapper.selectByEnterType(reqStrList.get(1)));
        } catch (Exception e) {
            log.error("【查询红包配置信息失败 reqParam:{} {} 】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> openRedPacket(List<String> reqStrList, int flag) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 3) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String redPacketNo = reqStrList.get(1);
            RedPacket redPacket = redPacketMapper.queryByRedPacketNo(reqStrList.get(1));
            if (null == redPacket) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }

            Map<String, Object> resMap = new LinkedHashMap<>(12);
            resMap.put("redPacketType", redPacket.getRedPacketType());
            resMap.put("from", redPacket.getFromAddr());
            resMap.put("title", redPacket.getTitle());
            resMap.put("isOver", redPacket.getIsOver());
            resMap.put("isOverTime", redPacket.getIsOverTime());
            resMap.put("maxCoin", getutmostCoin(redPacket));

            RedPacketUseExample example = new RedPacketUseExample();
            example.createCriteria().andRedPacketNoEqualTo(redPacketNo);
            resMap.put("totalPacketNum", redPacket.getTotalNum());

            Map<String, String> param = new HashMap<>(2);
            param.put("redPacketNo", redPacketNo);

            if (BigInteger.ONE.toString().equals(redPacket.getIsOver()) && flag == 0) {
                resMap.put("key", getPacketKey(redPacketNo, redPacket.getTotalNum()));
            } else if (BigInteger.ONE.toString().equals(redPacket.getIsOver()) && flag == 1) {
                resMap.put("key", null);
            }
            param.put("isVaild", BigInteger.ONE.toString());
            resMap.put("usedKeyNum", redPacketKeyMapper.selectUsedCount(param));
            int multiple = Integer.parseInt(cachePacketConfig.get(RedPacketConstant.PREFIX + "_" + redPacket.getEnterType()).split("_")[4]);
            resMap.put("totalKeyNum", redPacket.getTotalNum() * multiple);

            List<RedPacketDetail> detail = queryRedPacketDetailsByPage(redPacket, reqStrList);
            resMap.put("usedPacketNum", detail.size());
            resMap.put("total", redPacket.getTotalNum());
            resMap.put("list", detail);
            result.setData(resMap);
        } catch (Exception e) {
            log.error("【打开红包失败 reqParam:{} {} 】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    private String getPacketKey(String redPacketNo, int num) {
        RedPacketKey key = redPacketKeyMapper.selectUniqueKey(redPacketNo);
        while (null != key && BigInteger.ONE.toString().equals(key.getIsVaild()) && num > 0) {
            num--;
            key = redPacketKeyMapper.selectUniqueKey(redPacketNo);
        }
        if (null != key) {
            key.setIsVaild(BigInteger.ONE.toString());
            redPacketKeyMapper.updateByPrimaryKey(key);
            return key.getPacketKey();
        }
        return null;
    }

    @Override
    public List<RedPacketDetail> queryRedPacketDetailsByPage(RedPacket redPacket, List<String> reqStrList) {
        RedPacketDetailExample example = new RedPacketDetailExample();
        example.createCriteria().andRedPacketNoEqualTo(reqStrList.get(1)).andStatusEqualTo(BigInteger.ONE.toString());
        example.setOrderByClause("trade_time desc");
        List<RedPacketDetail> details = redPacketDetailMapper.selectByExample(example);
        details = updateLuckFlag(redPacket, details);
        return details;
    }

    private String subCheckKeyIsVaild(String packetNo, String packetKey) {
        String isVaild = null;
        RedPacketKeyExample example = new RedPacketKeyExample();
        example.createCriteria().andRedPacketNoEqualTo(packetNo).andPacketKeyEqualTo(packetKey);
        List<RedPacketKey> keys = redPacketKeyMapper.selectByExample(example);
        if (null == keys || keys.size() == 0) {
            isVaild = BigInteger.ONE.toString();
            return isVaild;
        }

        if (keys.size() == 1) {
            Map<String, String> param = new HashMap<>(2);
            param.put("packetKey", packetKey);
            param.put("status", BigInteger.ZERO.toString());
            param.put("redPacketNo", packetNo);
            RedPacketDetail detail = redPacketDetailMapper.selectByNoAndKeyAndNotErr(param);
            if (null != detail) {
                isVaild = BigInteger.ONE.toString();
            } else {
                isVaild = BigInteger.ZERO.toString();
            }
        }
        return isVaild;
    }

    @Override
    public Result<Map<String, Object>> drawRedPacket(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 5 || !NumericUtil.isValidAddress(reqStrList.get(3))) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String no = reqStrList.get(1);
            String key = reqStrList.get(2);
            Map<String, Object> resMap = new LinkedHashMap<>(1);
            RedPacket common = redPacketMapper.queryByRedPacketNo(no);

            String status;
            if (OVER.equals(common.getIsOver())) {
                if (BigInteger.ONE.toString().equals(common.getIsOverTime())) {
                    status = KEY_INVAILD;
                } else {
                    status = OVER;
                }
                resMap.put("status", status);
                result.setData(resMap);
                return result;
            }

            Map<String, String> param = new HashMap<>(5);
            param.put("redPacketNo", no);
            param.put("packetKey", key);
            RedPacketKey keyDo = redPacketKeyMapper.selectByParam(param);
            if (null == keyDo) {
                status = KEY_INVAILD;
                resMap.put("status", status);
                result.setData(resMap);
                return result;
            }
            long tokenId = keyDo.getTokenId();
            Map<String, Object> objParam = new HashMap<>(3);
            objParam.put("status", BigInteger.ZERO.toString());
            objParam.put("tokenId", tokenId);
            objParam.put("redPacketNo", no);
            RedPacketDetail detailWithTokenId = redPacketDetailMapper.selectByTokenIdAndStatus(objParam);
            if (null != detailWithTokenId) {
                status = KEY_INVAILD;
                resMap.put("status", status);
                result.setData(resMap);
                return result;
            }
            param.put("toAddr", reqStrList.get(3));
            param.put("status", BigInteger.ZERO.toString());
            RedPacketDetail existDetail = redPacketDetailMapper.selectByToAddrAndStatus(param);
            if (null == existDetail) {
                Map<String, String> contractMap = beforeCallDrawContract(reqStrList, common.getFromAddr(), tokenId);
                status = contractMap.get("status");
            } else {
                status = DRAW_EXIST;
            }
//            todo update red_packet_use
            resMap.put("coinValue", keyDo.getTokenValue());
            resMap.put("status", status);
            result.setData(resMap);
        } catch (Exception e) {
            log.error("【领红包失败 reqParam:{} {} {} 】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    private Map<String, String> beforeCallDrawContract(List<String> reqStrList, String from, long tokenId) {
        Map<String, String> resMap = new HashMap<>(2);
        try {
            String no = reqStrList.get(1);
            String key = reqStrList.get(2);
            String drawAddress = reqStrList.get(3);

            Map<String, String> param = new HashMap<>(4);
            param.put("redPacketNo", no);
            param.put("fromAddr", from);
            param.put("isUsed", BigInteger.ZERO.toString());

            Map<String, Object> queryParam = new HashMap<>(2);
            queryParam.put("redPacketNo", no);
            queryParam.put("tokenId", tokenId);
            RedPacketSend sendPacket = redPacketSendMapper.querySendWithtokenId(queryParam);

            RedPacketDetail detail = new RedPacketDetail();
            detail.setRedPacketNo(no);
            detail.setTokenId(tokenId);
            detail.setFromAddr(from);
            detail.setToAddr(drawAddress);
            detail.setPacketKey(key);
            detail.setStatus(DRAW_PACKET_CONFIRM);
            detail.setRedPacketType(sendPacket.getRedPacketType());
            detail.setTitle(sendPacket.getTitle());
            detail.setTitleEn(sendPacket.getTitleEn());
            detail.setCoinValue(sendPacket.getTokenValue());

            if (redPacketDetailMapper.insert(detail) > 0) {
                resMap.put("status", detail.getStatus());
                param.put("packetKey", key);
                redPacketKeyMapper.updateKeyUsed(param);
                redPacketAsy.updateDrawDetail(reqStrList, from, tokenId);
            }
        } catch (Exception e) {
            if ("Duplicate entry".contains(e.getMessage())) {
                resMap.put("status", DRAW_PACKET_CONFIRM);
                return resMap;
            }
            log.error("领红包发起失败 errMsg:{}，{}，{}", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
        }
        return resMap;
    }

    @Override
    public Result<Map<String, Object>> querySendRecords(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 4) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String address = reqStrList.get(2);
            int pageNum = Integer.parseInt(reqStrList.get(3));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;

            Map<String, Object> resMap = new LinkedHashMap<>(4);
            PageHelper.startPage(pageNum, pageSize);
            RedPacketExample packetExample = new RedPacketExample();
            packetExample.setOrderByClause("start_time desc");
            packetExample.createCriteria().andFromAddrEqualTo(address).andStatusNotEqualTo(BigInteger.ZERO.toString()).andTxHashIsNotNull();
            List<RedPacket> sends = redPacketMapper.selectByExample(packetExample);
            PageInfo<RedPacket> info = new PageInfo<>(sends);
            info.setList(sends);
            sends.forEach(send -> {
                RedPacketUse use = redPacketUseMapper.selectUsedInfo(send.getRedPacketNo());
                send.setUsedNum(use.getUsedNum());
                send.setRefundNum(use.getRefundNum());
            });
            resMap.put("total", info.getTotal());
            resMap.put("pageNum", info.getPageNum());
            resMap.put("pages", info.getPages());
            resMap.put("list", info.getList());
            result.setData(resMap);
        } catch (Exception e) {
            log.error("【查询发红包记录失败 reqParam:{} {} {}】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> queryDrawRecords(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (reqStrList.size() != 4) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String address = reqStrList.get(2);
            int pageNum = Integer.parseInt(reqStrList.get(3));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;

            Map<String, Object> resMap = new LinkedHashMap<>(4);
            PageHelper.startPage(pageNum, pageSize);
            RedPacketDetailExample receivesExample = new RedPacketDetailExample();
            receivesExample.setOrderByClause("trade_time desc");
            receivesExample.createCriteria().andToAddrEqualTo(address).andStatusNotEqualTo(BigInteger.ZERO.toString()).andTxHashIsNotNull();
            List<RedPacketDetail> receives = redPacketDetailMapper.selectByExample(receivesExample);
            PageInfo<RedPacketDetail> info = new PageInfo<>(receives);
            info.setList(receives);

            resMap.put("total", info.getTotal());
            resMap.put("pageNum", info.getPageNum());
            resMap.put("pages", info.getPages());
            resMap.put("list", info.getList());
            result.setData(resMap);
        } catch (Exception e) {
            log.error("【查询领红包记录失败 reqParam:{} {} {}】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.FAIL.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> queryRecordDetail(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 5) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            Map<String, Object> resMap = new LinkedHashMap<>(12);
            String type = reqStrList.get(1);
            String no = reqStrList.get(2);
            String address = reqStrList.get(3);
            int pageNum = Integer.parseInt(reqStrList.get(4));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;
            PageHelper.startPage(pageNum, pageSize);
            if (BigInteger.ONE.toString().equals(type)) {
                RedPacket redPacket = redPacketMapper.queryByRedPacketNo(no);
                RedPacketUse use = redPacketUseMapper.selectUsedInfo(no);
                RedPacketDetailExample receivesExample = new RedPacketDetailExample();
                receivesExample.setOrderByClause("trade_time desc");
                receivesExample.createCriteria().andFromAddrEqualTo(redPacket.getFromAddr()).andRedPacketNoEqualTo(no).andStatusEqualTo(BigInteger.ONE.toString());
                List<RedPacketDetail> details = redPacketDetailMapper.selectByExample(receivesExample);

                details = updateLuckFlag(redPacket, details);
                PageInfo<RedPacketDetail> info = new PageInfo<>(details);
                info.setList(details);
                resMap.put("packetStatus", redPacket.getStatus());
                resMap.put("isOver", redPacket.getIsOver());
                resMap.put("type", redPacket.getRedPacketType());
                resMap.put("from", redPacket.getFromAddr());

                resMap.put("usedNum", use.getUsedNum());
                resMap.put("totalPacketNum", redPacket.getTotalNum());
                resMap.put("title", redPacket.getTitle());
                resMap.put("totalCoin", redPacket.getTotalCoin());
                resMap.put("total", info.getTotal());
                resMap.put("pageNum", info.getPageNum());
                resMap.put("pages", info.getPages());
                resMap.put("list", info.getList());
            } else if (BigInteger.ZERO.toString().equals(type)) {
                RedPacketDetailExample receivesExample = new RedPacketDetailExample();
                receivesExample.setOrderByClause("trade_time desc");
                receivesExample.createCriteria().andToAddrEqualTo(address).andRedPacketNoEqualTo(no).andStatusEqualTo(BigInteger.ONE.toString());
                List<RedPacketDetail> details2 = redPacketDetailMapper.selectByExample(receivesExample);
                PageInfo<RedPacketDetail> info = new PageInfo<>(details2);
                info.setList(details2);
                resMap.put("total", info.getTotal());
                resMap.put("list", info.getList());
            }
            result.setData(resMap);
        } catch (Exception e) {
            log.error("【查询红包记录详情失败 reqParam:{} {} {}】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.FAIL.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> beforeDrawCheck(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 5) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String no = reqStrList.get(1);
            RedPacket redPacket = redPacketMapper.queryByRedPacketNo(no);
            if (null == redPacket) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }

            Map<String, Object> resMap = new LinkedHashMap<>(17);
            String packetKey = reqStrList.get(2);
            int pageNum = Integer.parseInt(reqStrList.get(3));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;
            PageHelper.startPage(pageNum, pageSize);

            RedPacketUse use = redPacketUseMapper.selectUsedInfo(no);
            RedPacketDetailExample receivesExample = new RedPacketDetailExample();
            receivesExample.setOrderByClause("trade_time desc");
            receivesExample.createCriteria().andRedPacketNoEqualTo(no).andStatusNotEqualTo(BigInteger.ZERO.toString());
            List<RedPacketDetail> details = redPacketDetailMapper.selectByExample(receivesExample);

            details = updateLuckFlag(redPacket, details);
            String keyIsVaild = subCheckKeyIsVaild(no, packetKey);
            resMap.put("keyIsVaild", keyIsVaild);

            Map<String, String> param = new HashMap<>(4);
            param.put("redPacketNo", no);
            param.put("packetKey", packetKey);

            RedPacketKey key = redPacketKeyMapper.selectByParam(param);
            String tokenId = key.getTokenId().toString();
            String tokenValue = key.getTokenValue();

            String drawAddress = reqStrList.get(4);
            if (StringUtils.isNotBlank(drawAddress) && NumericUtil.isValidAddress(drawAddress)) {
                param.put("packetKey", packetKey);
                param.put("toAddr", drawAddress);
                RedPacketDetail drawStatusDo = redPacketDetailMapper.selectByParam(param);
                resMap.put("drawStatus", drawStatusDo.getStatus());
            }
            resMap.put("tokenId", tokenId);
            resMap.put("tokenValue", tokenValue);

            PageInfo<RedPacketDetail> info = new PageInfo<>(details);
            info.setList(details);

            resMap.put("isOver", redPacket.getIsOver());
            resMap.put("type", redPacket.getRedPacketType());
            resMap.put("from", redPacket.getFromAddr());

            resMap.put("usedNum", use.getUsedNum());
            resMap.put("totalPacketNum", redPacket.getTotalNum());
            resMap.put("title", redPacket.getTitle());
            resMap.put("totalCoin", redPacket.getTotalCoin());

            resMap.put("total", info.getTotal());
            resMap.put("pageNum", info.getPageNum());
            resMap.put("pages", info.getPages());
            resMap.put("list", info.getList());
            result.setData(resMap);
        } catch (Exception e) {
            log.error("【领取红包校验失败 reqParam:{} {} {}】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.FAIL.code());
            return result;
        }
        return result;
    }

    private List<RedPacketDetail> updateLuckFlag(RedPacket redPacket, List<RedPacketDetail> details) {
        if (null != details && details.size() > 0) {
            if (!BigInteger.ONE.toString().equals(redPacket.getRedPacketType()) && OVER.equals(redPacket.getIsOver())) {
                String maxFlagCheck = null;
                for (RedPacketDetail detail : details) {
                    if (StringUtils.isNotBlank(detail.getMaxFlag()) && BigInteger.ONE.toString().equals(detail.getMaxFlag())) {
                        maxFlagCheck = detail.getMaxFlag();
                    }
                }
                if (null == maxFlagCheck) {
                    details.sort(Comparator.comparing(RedPacketDetail::getTradeTime));
                    Map<Long, BigDecimal> newMap = new LinkedHashMap<>(details.size());
                    for (RedPacketDetail detail : details) {
                        newMap.put(detail.getId(), new BigDecimal(detail.getCoinValue()));
                    }
                    Long maxId = newMap.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
                    redPacketDetailMapper.updateMaxFlagById(maxId);
                    details.stream().forEach(redPacketDetail -> {
                        if (maxId.equals(redPacketDetail.getId())) {
                            redPacketDetail.setMaxFlag(BigInteger.ONE.toString());
                        }
                    });
                    details.sort(Comparator.comparing(RedPacketDetail::getTradeTime).reversed());
                }
            }
        }

        return details;
    }

}


