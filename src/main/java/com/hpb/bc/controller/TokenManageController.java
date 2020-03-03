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
