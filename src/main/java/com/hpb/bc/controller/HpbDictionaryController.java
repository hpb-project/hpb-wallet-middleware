package com.hpb.bc.controller;

import com.hpb.bc.entity.HpbDataDic;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.HpbDataDicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dictionary")
@CrossOrigin
public class HpbDictionaryController extends BaseController {

    @Autowired
    private HpbDataDicService hpbDataDicService;

    @ApiOperation(value = "查询关键字test ", notes = "参数1：字典key：(0  安卓或1 IOS);参数2:language_type(0:中文或   1:英文);")
    @PostMapping(value = "/test")
    public List<Object> version(@RequestBody List<String> reqStrList) {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        HpbDataDic data = hpbDataDicService.selectFromDataDic(444, "test");
        result.setData(data.getDataName());
        return result.SUCCESS();
    }
}
