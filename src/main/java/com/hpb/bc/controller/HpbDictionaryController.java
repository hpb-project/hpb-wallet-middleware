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
