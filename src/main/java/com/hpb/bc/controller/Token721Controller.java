package com.hpb.bc.controller;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.service.Token721Service;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/token721")
public class Token721Controller extends BaseController {
    @Autowired
    private Token721Service token721Service;

    @ApiOperation(value = "库存", notes = "[参数1:起始页码，参数2:合约地址,参数3:账户地址]")
    @ApiImplicitParam(name = "reqStrList", value = "['pageNum'，'contractAddress','address']", required = true, dataType = "List<String>")
    @PostMapping("/stock")
    public List<Object> get721StockDetail(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = token721Service.get721StockDetail(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "代币ID详情", notes = "[参数1:起始页码，参数2:代币id]")
    @ApiImplicitParam(name = "reqStrList", value = "['pageNum','tokenId']", required = true, dataType = "List<String>")
    @PostMapping("/idDetail")
    public List<Object> get721TokenIdDetail(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = token721Service.get721TokenIdDetail(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "转账-代币列表", notes = "[参数1：起始页码，参数2:合约地址,参数3:账户地址]")
    @ApiImplicitParam(name = "reqStrList", value = "['pageNum','contractAddress','address']", required = true, dataType = "List<String>")
    @PostMapping("/ids")
    public List<Object> getTokenId(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = token721Service.getTokenId(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "交易详情", notes = "[参数1:tokenid页码,参数2:账户地址,参数3:交易hash]")
    @ApiImplicitParam(name = "reqStrList", value = "['pageNum','contractAddress',address']", required = true, dataType = "List<String>")
    @PostMapping("/txDetail")
    public List<Object> get721TransferDetail(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = token721Service.get721TransferDetail(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "每笔交易下的代币id", notes = "[参数1:页码,参数2:交易hash,参数3:合约地址]")
    @ApiImplicitParam(name = "reqStrList", value = "['pageNum','hash']", required = true, dataType = "List<String>")
    @PostMapping("/idsByTxHash")
    public List<Object> getTokenIdsByTxHash(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = token721Service.getTokenIdsByTxHash(reqStrList);
        return result.result();
    }
}
