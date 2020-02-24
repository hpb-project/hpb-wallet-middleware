package com.hpb.bc.service;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.RedPacket;
import com.hpb.bc.entity.RedPacketConfig;
import com.hpb.bc.entity.RedPacketDetail;
import com.hpb.bc.entity.result.Result;

import java.util.List;
import java.util.Map;


public interface RedPacketService {
    /**签名前准备*/
    Result<Map<String, Object>> rawReady(List<String> reqStrList);

    /**发送签名数据*/
    Result<String> sendRawTxAndUpdateSendInfo(List<String> reqStrList);

    /**发送红包刷新*/
    Result<String> refreshSend(List<String> reqStrList);

    /**查询红包配置信息: 红包总个数，单个红包最大金额*/
    Result<RedPacketConfig> getRedPacketConfig(List<String> reqStrList);

    /**根据红包编号查询红包是否失效:1-进行中，2-结束*/
    Result<String> checkPacketIsVaild(List<String> reqStrList);

    /**打开分享红包*/
    Result<Map<String, Object>> openShare(List<String> reqStrList);

    /**打开红包*/
    Result<Map<String, Object>> openRedPacket(List<String> reqStrList,int flag);

    /**分页查询红包领取详情*/
    List<RedPacketDetail> queryRedPacketDetailsByPage(RedPacket redPacket, List<String> reqStrList);

    /**领红包*/
    Result<Map<String, Object>> drawRedPacket(List<String> reqStrList);

    /**查询领红包记录*/
    Result<Map<String, Object>> queryDrawRecords(List<String> reqStrList);

    /**查询发红包记录*/
    Result<Map<String, Object>> querySendRecords(List<String> reqStrList);

    /**查询红包详情*/
    Result<Map<String, Object>> queryRecordDetail(List<String> reqStrList);

    /**领取红包时校验*/
    Result<Map<String, Object>> beforeDrawCheck(List<String> reqStrList);
}
