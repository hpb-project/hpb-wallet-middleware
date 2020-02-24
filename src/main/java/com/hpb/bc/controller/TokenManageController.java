package com.hpb.bc.controller;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.service.impl.TokenManageServiceImpl;
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
@RequestMapping("/token")
public class TokenManageController {
    @Autowired
    private TokenManageServiceImpl tokenManageService;

    @ApiOperation(value = "代币管理", notes = "[参数1:当前地址]")
    @ApiImplicitParam(name = "reqStrList", value = "['address']", required = true, dataType = "List<String>")
    @PostMapping("/manage")
    public List<Object> list(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = tokenManageService.tokenManage(reqStrList);
        return result.result();
    }
}
