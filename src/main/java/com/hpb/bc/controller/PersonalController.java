package com.hpb.bc.controller;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.PersonalService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personal")
public class PersonalController extends BaseController {
    @Autowired
    private PersonalService personalService;

    @ApiOperation(value = "钱包转账", notes = "参数1：address,参数二：币种类型(HPB,HRC-20,HRC-721)")
    @PostMapping("/walletTransfer")
    public List<Object> walletTransfer(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = personalService.walletTransfer(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "获取账户法币余额", notes = "参数1：address(0x开头的地址，42位hash)")
    @PostMapping("/getLegalTenderBalance")
    public List<Object> getLegalTenderBalance(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = personalService.getlegalTenderBalance(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "批量获取账户余额", notes = "参数1：address(0x开头的地址，42位hash)数组列表")
    @PostMapping("/listLegalBalances")
    public List<Object> listLegalBalances(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        if (reqStrList == null || reqStrList.size() < 2) {
            result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
            return result.result();
        }
        result = personalService.listLegalBalances(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "获取账户HPB余额", notes = "参数1：address(0x开头的地址，42位hash)")
    @PostMapping("/getBalance")
    public List<Object> getBalance(@RequestBody List<String> reqStrList) {
        Result<String> result = personalService.hpbGetBalance(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "批量获取账户余额", notes = "参数1：address(0x开头的地址，42位hash)数组列表")
    @PostMapping("/listBalance")
    public List<Object> listBalance(@RequestBody List<String> reqStrList) {
        Result<Map<String, String>> result = new Result<>(ResultCode.SUCCESS);
        if (reqStrList == null || reqStrList.size() < 2) {
            result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
            return result.result();
        }
        result = personalService.listBalance(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "获取nonce", notes = "参数1：address(0x开头的地址，42位hash)")
    @PostMapping("/getNonce")
    public List<Object> getNonce(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = personalService.hpbGetNonce(reqStrList);
        return result.result();
    }
}
