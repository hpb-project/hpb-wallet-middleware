package com.hpb.bc.service;

import com.hpb.bc.entity.IssueVote;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.vo.IssueVoteDetailVo;
import com.hpb.bc.vo.IssueVotePersonalDetailVo;

import java.util.List;
import java.util.Map;


public interface ProposalVoteService {
    Result<Map<String, Object>> list(List<String> reqStrList);
    Result<IssueVoteDetailVo> detail(List<String> reqStrList);
    Result<Map<String, Object>> personalRecords(List<String> reqStrList);
    Result<String> vote(List<String> reqStrList);

}
