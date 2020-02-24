package com.hpb.bc.controller;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.TransactionHistoryInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.service.TransactionService;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController extends BaseController {
    @Autowired
    private TransactionService transactionService;


    @ApiOperation(value = "查询交易", notes = "参数1：交易hash(0x开头的地址，64位hash)")
    @PostMapping(value = "/getTransactionByHash")
    public List<Object> getTransactionByHash(@RequestBody List<String> reqStrList) {
        Result<Transaction> result = transactionService.hpbGetTransactionByHash(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "查询个人交易数nonce", notes = "参数1：交易hash(0x开头的地址，64位hash)")
    @PostMapping(value = "/getTransactionCount")
    public List<Object> getTransactionCount(@RequestBody List<String> reqStrList) {
        Result<BigInteger> result = transactionService.hpbGetTransactionCount(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "查询交易回执", notes = "参数1：交易hash(0x开头的地址，64位hash)")
    @PostMapping("/getTransactionReceipt")
    public List<Object> getTransactionReceipt(@RequestBody List<String> reqStrList) {
        Result<TransactionReceipt> result = transactionService.hpbGetTransactionReceipt(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "查询交易详情", notes = "参数1：交易hash(0x开头的地址，64位hash)")
    @PostMapping("/getTransactionDetail")
    public List<Object> getTransactionDetail(@RequestBody List<String> reqStrList) {
        Result<TransactionHistoryInfo> result = transactionService.getTransactionDetail(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "发送交易", notes = "参数1：签名后hash数据(0x开头hash数据,16进制数据,测试异常情况)")
    @PostMapping("/sendRawTransaction")
    public List<Object> sendRawTransaction(@RequestBody List<String> reqStrList) throws IOException {
        Result<String> result = transactionService.hpbSendRawTransaction(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "查询交易历史", notes = "参数1：address(0x开头的地址,42位hash),参数2：交易类型(0-所有 1-发送 2-接收),参数3：页码数 >0")
    @PostMapping("/getTransactionHistory")
    public List<Object> getTransactionHistory(@RequestBody List<String> reqStrList) throws IOException {
        Result<PageInfo<TransactionHistoryInfo>> result = transactionService.getTransactionHistory(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "交易记录", notes = "[参数1：当前地址,参数2：合约类型,参数3:合约地址,参数4，代币简称,参数,6：页码数]")
    @PostMapping("/list")
    public List<Object> getTransaction(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = transactionService.historyList(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "合约类型列表", notes = "[参数1：当前地址，参数2:合约类型:HRC-20,HRC-721]")
    @ApiImplicitParam(name = "contractType", value = "['address','contractType']", required = true, dataType = "List<String>")
    @PostMapping("/typeList")
    public List<Object> getTypeList(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = transactionService.getTypeList(reqStrList);
        return result.result();
    }
}
