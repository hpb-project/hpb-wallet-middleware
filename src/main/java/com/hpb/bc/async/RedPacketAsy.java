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

package com.hpb.bc.async;

import com.hpb.bc.cache.CachePacketConfiguration;
import com.hpb.bc.constant.RedPacketConstant;
import com.hpb.bc.contract.hpb.RedPacketToken;
import com.hpb.bc.entity.*;
import com.hpb.bc.entity.cache.CachePacketParam;
import com.hpb.bc.util.RedPacketHelper;
import com.hpb.bc.util.UUIDGeneratorUtil;
import com.hpb.bc.website.mapper.*;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.Response;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.tuples.generated.Tuple3;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

@Component
public class RedPacketAsy {
    private static final Logger log = LoggerFactory.getLogger(RedPacketAsy.class);
    @Autowired
    private Admin admin;
    @Autowired
    private RedPacketHelper redPacketHelper;
    @Autowired
    private RedPacketMapper redPacketMapper;
    @Autowired
    private RedPacketSendMapper redPacketSendMapper;
    @Autowired
    private RedPacketKeyMapper redPacketKeyMapper;
    @Autowired
    private RedPacketConfigMapper redPacketConfigMapper;
    @Autowired
    private RedPacketUseMapper redPacketUseMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CachePacketConfiguration cachePacketConfiguration;

    @Async
    public void updateTokenIdByWithSuccessHash(String packetNo, String hash) {
        try {
            List<RedPacketSend> redPacketSends = redPacketSendMapper.list(packetNo);
            if (null != redPacketSends && redPacketSends.size() > 0) {
                log.warn("发红包成功，不重复入库，packetNo:{},hash:{}", packetNo, hash);
                return;
            }
            HpbGetTransactionReceipt hpbGetTransactionReceipt = admin.hpbGetTransactionReceipt(hash).send();
            Response.Error error = hpbGetTransactionReceipt.getError();
            Map<String, Object> param = new HashMap<>();
            param.put("txHash", hash);
            param.put("redPacketNo", packetNo);
            String status = null;
            if (null != error) {
                log.error("发送红包签名数据交易失败：errorMsg {}", error.getMessage());
                status = BigInteger.ZERO.toString();
            } else {
                TransactionReceipt receipt = hpbGetTransactionReceipt.getResult();
                if (null != receipt) {
                    if (receipt.isStatusOK()) {
                        status = BigInteger.ONE.toString();
                        param.put("isOver", BigInteger.ONE.toString());
                        RedPacket redPacketCommon = redPacketMapper.queryByRedPacketNo(packetNo);
                        RedPacketToken redPacketToken = redPacketHelper.getRedPacketToken();
                        List<RedPacketToken.AddRedPacketTokenEventResponse> events = redPacketToken.getAddRedPacketTokenEvents(receipt);
                        if (null != events && events.size() > 0) {
                            List<BigInteger> tokenIds = new LinkedList<>();
                            for (RedPacketToken.AddRedPacketTokenEventResponse event : events) {
                                tokenIds.add(event.tokenId);
                            }
                            int totalNum = redPacketCommon.getTotalNum();
                            String sendAddress = redPacketCommon.getFromAddr();
                            String title = redPacketCommon.getTitle();
                            String titleEn = redPacketCommon.getTitleEn();
                            String type = redPacketCommon.getRedPacketType();
                            List<RedPacketSend> sendList = new ArrayList<>();
                            if (tokenIds.size() > 0) {
                                List<Long> tokenIdList = new ArrayList<>();
                                List<String> tokenValueList = new ArrayList<>();
                                for (int i = 0; i < totalNum; i++) {
                                    RedPacketSend rSend = new RedPacketSend();
                                    rSend.setRedPacketNo(packetNo);
                                    BigInteger tokenId = tokenIds.get(i);
                                    rSend.setTokenId(Long.valueOf(tokenId.toString()));
                                    Tuple3<String, String, BigInteger> send1 = redPacketToken.getRedPacketTokenByTokenId(tokenId).send();
                                    String tokenValue = send1.getValue3().toString();
                                    //todo
                                    if (tokenValue.contains("416661261171336203492429208591735466886152559183329360703868735848448")) {
                                        log.info("区块未确认，暂停20s 重新查询 packetNo：{}, hash:{},当前区块：{}", packetNo, hash, receipt.getBlockNumber());
                                        Thread.sleep(20000);
                                        updateTokenIdByWithSuccessHash(packetNo, hash);
                                        return;
                                    }

                                    rSend.setTokenValue(tokenValue);
                                    rSend.setFromAddr(sendAddress);
                                    rSend.setTxHash(hash);
                                    rSend.setStatus(BigInteger.ONE.toString());
                                    rSend.setIsUsed(BigInteger.ZERO.toString());
                                    rSend.setTitle(title);
                                    rSend.setTitleEn(titleEn);
                                    rSend.setRedPacketType(type);
                                    tokenIdList.add(rSend.getTokenId());
                                    tokenValueList.add(rSend.getTokenValue());
                                    sendList.add(rSend);
                                }
                                redPacketSendMapper.insertBatch(sendList);
                                generateKeys(totalNum, redPacketCommon.getEnterType(), packetNo, tokenIdList, tokenValueList);
                                insertRedPacketUse(redPacketCommon);
                            }
                            if (RedPacketConstant.ENTER_TYPE_2.equals(redPacketCommon.getEnterType())) {
                                redisTemplate.opsForValue().set("shakePacketNo", packetNo);
                            }
                            DefaultBlockParameter blockNumber = new DefaultBlockParameterNumber(receipt.getBlockNumber());
                            HpbBlock.Block block = admin.hpbGetBlockByNumber(blockNumber, false).send().getBlock();
                            param.put("startTime", null != block ? new Date(block.getTimestamp().multiply(new BigInteger("1000")).longValue()) : System.currentTimeMillis());
                        } else {
                            log.info("无event日志，errMsg packetNo:{},hash:{}", packetNo, hash);
                        }
                    } else {
                        status = BigInteger.ZERO.toString();
                        log.info("红包状态置为0， packetNo:{},hash:{},receipt.isStatusOK()：{}", packetNo, hash, receipt.isStatusOK());
                    }
                } else {
                    Thread.sleep(2000);
                    updateTokenIdByWithSuccessHash(packetNo, hash);
                    return;
                }
            }
            param.put("status", status);
            redPacketMapper.updateWithParam(param);
            log.info("=========更新红包状态成功 packetNo:{},hash:{},status：{}", packetNo, hash, status);
        } catch (Exception e) {
            log.error("更新资产ID和资产Value失败 errMsg:packetNo：{},hash：{},{},{}", packetNo, hash, e.getMessage(), e);
        }
    }

    private void insertRedPacketUse(RedPacket redPacket) {
        RedPacketUse use = new RedPacketUse();
        use.setRedPacketNo(redPacket.getRedPacketNo());
        use.setTotalNum(redPacket.getTotalNum());
        use.setUsedNum(0);
        use.setRefundNum(0);
        redPacketUseMapper.insert(use);
    }

    public void generateKeys(int totalNum, String enterType, String no, List<Long> tokenIdList, List<String> tokenValueList) {
        RedPacketConfig config = redPacketConfigMapper.selectByEnterType(enterType);
        if (config != null) {
            int multiple = config.getMultiple();
            int totalKeyNum = totalNum * multiple;
            List<RedPacketKey> records = new ArrayList<>();
            for (int i = 0; i < totalKeyNum; i++) {
                RedPacketKey key = new RedPacketKey();
                key.setRedPacketNo(no);
                String uuid = UUIDGeneratorUtil.generate(no).toLowerCase();
                key.setPacketKey(new HmacUtils(HmacAlgorithms.HMAC_SHA_1, uuid).hmacHex(uuid));
                key.setIsVaild(BigInteger.ZERO.toString());
                key.setTokenId(tokenIdList.get(i));
                key.setTokenValue(tokenValueList.get(i));
                records.add(key);
            }
            redPacketKeyMapper.insertBatch(records);
        }
    }

    public void updateDrawDetail(List<String> reqStrList, String from, long tokenId) {
        String packetNo = reqStrList.get(1);
        String drawAddress = reqStrList.get(3);
        CachePacketParam cache = new CachePacketParam();
        cache.setFrom(from);
        cache.setPacketNo(packetNo);
        cache.setTo(drawAddress);
        cache.setTokenId(tokenId);
        cache.setUid(UUIDGeneratorUtil.generate(from + packetNo + drawAddress + tokenId));
        appendCachePacketKeys(cache.getUid());
        cachePacketConfiguration.updateDrawValues(cache);
    }

    private void appendCachePacketKeys(String uid) {
        List<String> uids = cachePacketConfiguration.findAllPacketKey();
        if (null == uids) {
            uids = new ArrayList<>();
            uids.add(uid);
            cachePacketConfiguration.updateDrawKeys(uids);
        } else {
            if (!uids.contains(uid)) {
                uids.add(uid);
                cachePacketConfiguration.updateDrawKeys(uids);
            }
        }
    }
}
