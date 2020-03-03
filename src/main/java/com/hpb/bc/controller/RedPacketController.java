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

import com.hpb.bc.entity.RedPacketConfig;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.impl.RedPacketServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/packet")
@Api(value = "红包")
@CrossOrigin
public class RedPacketController extends BaseController {
    @Autowired
    private RedPacketServiceImpl redPacketService;

    @ApiOperation(value = "准备发红包", notes = "[参数1:地址,参数2:红包总金额,参数3:红包总个数,参数4:红包入口,参数5:红包类型,参数6:红包主题]")
    @ApiImplicitParam(name = "reqStrList", value = "['address','totalCoin','totalNum','enterType':1-红包,'type':1-普通，2-拼手气,'title']", required = true, dataType = "List<String>")
    @PostMapping("/rawReady")
    public List<Object> rawReady(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = redPacketService.rawReady(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "发红包", notes = "[参数1:红包编号,参数2:签名信息(批量)]")
    @ApiImplicitParam(name = "reqStrList", value = "['redPacketNo','input']", required = true, dataType = "List<String>")
    @PostMapping("/sendRaw")
    public List<Object> generateRedPacket(@RequestBody List<String> reqStrList) {
        Result<String> result = redPacketService.sendRawTxAndUpdateSendInfo(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "刷新", notes = "[参数1:红包编号]")
    @ApiImplicitParam(name = "reqStrList", value = "['redPacketNo']", required = true, dataType = "List<String>")
    @PostMapping("/refresh")
    public List<Object> refreshSend(@RequestBody List<String> reqStrList) {
        Result<String> result = redPacketService.refreshSend(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "查询红包配置信息", notes = "[参数1:红包入口]")
    @ApiImplicitParam(name = "reqStrList", value = "['enterType':1-红包]", required = true, dataType = "List<String>")
    @PostMapping("/config")
    public List<Object> queryRedPacketConfig(@RequestBody List<String> reqStrList) {
        Result<RedPacketConfig> result = redPacketService.getRedPacketConfig(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "查询红包是否失效", notes = "[参数1:红包编号]")
    @ApiImplicitParam(name = "reqStrList", value = "['redPacketNo']", required = true, dataType = "List<String>")
    @PostMapping("/checkIsOver")
    public List<Object> checkPacketIsValid(@RequestBody List<String> reqStrList) {
        Result<String> result = redPacketService.checkPacketIsVaild(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "打开分享红包", notes = "[参数1:红包编号]")
    @ApiImplicitParam(name = "reqStrList", value = "['redPacketNo']", required = true, dataType = "List<String>")
    @PostMapping("/openShare")
    public List<Object> openShare(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = redPacketService.openShare(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "打开红包", notes = "[参数1:红包编号,参数2:起始页码]")
    @ApiImplicitParam(name = "reqStrList", value = "['redPacketNo','pageNum']", required = true, dataType = "List<String>")
    @PostMapping("/open")
    public List<Object> openRedPacket(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = redPacketService.openRedPacket(reqStrList, 0);
        return result.result();
    }

    @ApiOperation(value = "刷新打开红包", notes = "[参数1:红包编号,参数2:起始页码]")
    @ApiImplicitParam(name = "reqStrList", value = "['redPacketNo','pageNum']", required = true, dataType = "List<String>")
    @PostMapping("/refreshOpen")
    public List<Object> refreshOpenRedPacket(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = redPacketService.openRedPacket(reqStrList, 1);
        return result.result();
    }

    @ApiOperation(value = "领红包", notes = "[参数1:红包编号,参数2:钥匙,参数3:领红包地址,参数4：tokenId]")
    @ApiImplicitParam(name = "reqStrList", value = "['redPacketNo','packetKey','address']", required = true, dataType = "List<String>")
    @PostMapping("/draw")
    public List<Object> drawRedPacket(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = redPacketService.drawRedPacket(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "红包记录", notes = "[参数1:类型 1-发红包，0-收红包,参数2:地址,参数3:起始页码]")
    @ApiImplicitParam(name = "reqStrList", value = "['type','address','pageNum']", required = true, dataType = "List<String>")
    @PostMapping("/records")
    public List<Object> records(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        String type = reqStrList.get(1);
        if (null == reqStrList || reqStrList.size() < 4 && StringUtils.isBlank(type) && (BigInteger.ONE.toString().equals(type) || BigInteger.ZERO.toString().equals(type))) {
            result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
        } else {
            if (BigInteger.ONE.toString().equals(type)) {
                result = redPacketService.querySendRecords(reqStrList);
            } else if (BigInteger.ZERO.toString().equals(type)) {
                result = redPacketService.queryDrawRecords(reqStrList);
            }
        }
        return result.result();
    }

    @ApiOperation(value = "红包详情", notes = "[参数1:类型 1-发红包，0-收红包,参数2:红包编号,参数3:地址（刚进红包时值为钥匙），起始页码]")
    @ApiImplicitParam(name = "reqStrList", value = "['type','redPacketNo','address','pageNum']", required = true, dataType = "List<String>")
    @PostMapping("/detail")
    public List<Object> recordDetail(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = redPacketService.queryRecordDetail(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "领取红包校验", notes = "[参数1:红包编号,参数2:钥匙，参数3：起始页码,参数4：刷新时传领红包地址，其他传'']")
    @ApiImplicitParam(name = "reqStrList", value = "['redPacketNo','packetKey','pageNum']", required = true, dataType = "List<String>")
    @PostMapping("/beforeDrawCheck")
    public List<Object> beforeDrawCheck(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = redPacketService.beforeDrawCheck(reqStrList);
        return result.result();
    }
}
