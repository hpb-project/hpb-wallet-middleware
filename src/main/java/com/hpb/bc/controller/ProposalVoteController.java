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
import com.hpb.bc.service.impl.ProposalVoteServiceImpl;
import com.hpb.bc.vo.IssueVoteDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/proposal")
@Api(value = "题案投票")
@CrossOrigin
public class ProposalVoteController {
    @Autowired
    private ProposalVoteServiceImpl proposalVoteService;

    @ApiOperation(value = "题案列表", notes = "[参数1:起始页码]")
    @ApiImplicitParam(name = "reqStrList", value = "['pageNum']", required = true, dataType = "List<String>")
    @PostMapping("/list")
    public List<Object> list(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = proposalVoteService.list(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "题案详情", notes = "[参数1:题案编号，参数2:投票人地址]")
    @ApiImplicitParam(name = "reqStrList", value = "['pageNum','address']", required = true, dataType = "List<String>")
    @PostMapping("/detail")
    public List<Object> detail(@RequestBody List<String> reqStrList) {
        Result<IssueVoteDetailVo> result = proposalVoteService.detail(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "题案个人记录", notes = "[参数1:投票人地址]")
    @ApiImplicitParam(name = "reqStrList", value = "['address']", required = true, dataType = "List<String>")
    @PostMapping("/personalRecords")
    public List<Object> personalRecords(@RequestBody List<String> reqStrList) {
        Result<Map<String, Object>> result = proposalVoteService.personalRecords(reqStrList);
        return result.result();
    }

    @ApiOperation(value = "题案投票", notes = "[参数1:题案编号，参数2:投票人地址，参数3:选项，参数4：票数，参数5：签名数据]")
    @ApiImplicitParam(name = "reqStrList", value = "['no','address','1-支，0-不支持','voteNum','signData']", required = true, dataType = "List<String>")
    @PostMapping("/vote")
    public List<Object> vote(@RequestBody List<String> reqStrList) {
        Result<String> result = proposalVoteService.vote(reqStrList);
        return result.result();
    }
}
