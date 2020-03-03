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

package com.hpb.bc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.constant.ProposalVoteConstant;
import com.hpb.bc.contract.hpb.ProposalBallot;
import com.hpb.bc.entity.IssueVote;
import com.hpb.bc.entity.IssueVoteDetail;
import com.hpb.bc.entity.IssueVoteResult;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.example.IssueVoteExample;
import com.hpb.bc.example.IssueVoteResultExample;
import com.hpb.bc.service.ProposalVoteService;
import com.hpb.bc.util.NumericUtil;
import com.hpb.bc.util.ProposalBallotHelper;
import com.hpb.bc.vo.IssueVoteDetailVo;
import com.hpb.bc.vo.IssueVotePersonalDetailVo;
import com.hpb.bc.website.mapper.IssueVoteDetailMapper;
import com.hpb.bc.website.mapper.IssueVoteMapper;
import com.hpb.bc.website.mapper.IssueVoteResultMapper;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.Response;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.tuples.generated.Tuple3;
import io.hpb.web3.tuples.generated.Tuple4;
import io.hpb.web3.tuples.generated.Tuple5;
import io.hpb.web3.utils.Convert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class ProposalVoteServiceImpl extends AbstractBaseService implements ProposalVoteService {
    @Autowired
    private IssueVoteMapper issueVoteMapper;
    @Autowired
    private IssueVoteDetailMapper issueVoteDetailMapper;
    @Autowired
    private IssueVoteResultMapper issueVoteResultMapper;
    @Resource(name = "hpbAdmin")
    private Admin admin;
    @Autowired
    private ProposalBallotHelper proposalBallotHelper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${issue.contract.address}")
    private String issurContractAddress;

    @Override
    public Result<String> vote(List<String> reqStrList) {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 6) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            if (!NumericUtil.isValidAddress(reqStrList.get(2))) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            if (!NumericUtil.isValidRawTransaction(reqStrList.get(5))) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String no = reqStrList.get(1);
            String address = reqStrList.get(2);
            String optionKey = reqStrList.get(3);
            String voteNum = reqStrList.get(4);
            IssueVote issueVote = issueVoteMapper.queryByNo(no);
            if (null == issueVote) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            Map<String, String> param = new HashMap<>();
            param.put("issueNo", no);
            param.put("voteAddr", address);
            param.put("optionKey", optionKey);
            IssueVoteDetail detail = issueVoteDetailMapper.queryByNoAddrOptionKey(param);
            if (null == detail) {
                detail = new IssueVoteDetail();
                detail.setVoteAddr(address);
                detail.setIssueNo(reqStrList.get(1));
                detail.setOptionKey(optionKey);
                detail.setOptionMenu(BigInteger.ONE.toString().equals(optionKey) ? issueVote.getValue1Zh() : issueVote.getValue2Zh());
                detail.setVoteNum(new BigDecimal(voteNum));
                issueVoteDetailMapper.insert(detail);
            } else {
                detail.setVoteNum(detail.getVoteNum().add(new BigDecimal(voteNum)));
                issueVoteDetailMapper.updateByPrimaryKey(detail);
            }
            HpbSendTransaction hpbSendTransaction = admin.hpbSendRawTransaction(reqStrList.get(5)).send();
            Response.Error error = hpbSendTransaction.getError();
            if (null != error) {
                result.error(error);
            } else {
                result.setData(hpbSendTransaction.getTransactionHash());
            }
        } catch (Exception e) {
            log.error("题案投票失败{},{}，{}", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.FAIL.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> list(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() < 2) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            int pageNum = Integer.parseInt(reqStrList.get(1));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;
            PageHelper.startPage(pageNum, pageSize);
            IssueVoteExample example = new IssueVoteExample();
            example.setOrderByClause("end_time desc");
            example.createCriteria().andIsVaildEqualTo("1").andVoteStatusNotEqualTo("0");
            List<IssueVote> votes = issueVoteMapper.selectByExample(example);
            Map<String, Object> resMap = new LinkedHashMap<>(4);

            if (null != votes && votes.size() > 0) {
                ProposalBallot proposalBallot = proposalBallotHelper.getProposalBallot();
                Tuple5<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>> tuple5
                        = proposalBallot.fetchAllProposal().send();
                int num = tuple5.getValue1().size();
                Map<String, String> allMap = new HashMap<>(num);
                for (int i = 0; i < num; i++) {
                    String no = new String(tuple5.getValue1().get(i), StandardCharsets.UTF_8);
                    allMap.put(no, tuple5.getValue4().get(i) + "_" + tuple5.getValue5().get(i));
                }

                for (IssueVote per : votes) {
                    if (BigInteger.ONE.toString().equals(per.getVoteStatus())) {
                        per.setCountDownTime(per.getEndTime().getTime() - per.getBeginTime().getTime());
                    }
                    String resultNum = allMap.get(per.getIssueNo());
                    BigInteger agreeNum = new BigInteger(resultNum.split("_")[0]);
                    BigInteger disagreeNum = new BigInteger(resultNum.split("_")[1]);
                    per.setValue1Num(agreeNum.toString());
                    per.setValue2Num(disagreeNum.toString());
                    BigInteger total = agreeNum.add(disagreeNum);
                    BigDecimal decimalTotal = Convert.fromWei(total.toString(), Convert.Unit.HPB);
                    if (decimalTotal.compareTo(BigDecimal.ONE) < 0) {
                        per.setValue1Rate(BigInteger.ZERO.toString());
                        per.setValue2Rate(BigInteger.ZERO.toString());
                    } else {
                        BigDecimal b1 = new BigDecimal(agreeNum).divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_EVEN);
                        BigDecimal b2 = new BigDecimal(1).subtract(b1);
                        per.setValue1Rate(b1.toString());
                        per.setValue2Rate(b2.toString());
                    }
                }
            }
            PageInfo<IssueVote> info = new PageInfo<>(votes);
            info.setList(votes);
            resMap.put("total", info.getTotal());
            resMap.put("pageNum", info.getPageNum());
            resMap.put("pages", info.getPages());
            resMap.put("list", info.getList());
            result.setData(resMap);
        } catch (Exception e) {
            log.error("【查询题案投票列表失败 reqParam:{} {} {}】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<IssueVoteDetailVo> detail(List<String> reqStrList) {
        Result<IssueVoteDetailVo> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() < 3) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            IssueVoteDetailVo vo = null;
            IssueVote per = issueVoteMapper.queryByNo(reqStrList.get(1));
            String fromStr = reqStrList.get(2);

            if (null != per) {
                vo = convertIssueVote2detailVo(per);
                ProposalBallot proposalBallot = proposalBallotHelper.getProposalBallot();
                Tuple4<BigInteger, BigInteger, BigInteger, BigInteger> send4 =
                        proposalBallot.fetchProposalVoteNumByNoandAddr(per.getIssueNo().getBytes(), fromStr).send();
                BigInteger agreeNum = null == send4.getValue1() ? BigInteger.ZERO : send4.getValue1();
                BigInteger disagreeNum = null == send4.getValue2() ? BigInteger.ZERO : send4.getValue2();
                BigInteger peragree = null == send4.getValue3() ? BigInteger.ZERO : send4.getValue3();
                BigInteger perdisagree = null == send4.getValue4() ? BigInteger.ZERO : send4.getValue4();
                vo.setPeragreeNum(peragree.toString());
                vo.setPerdisagreeNum(perdisagree.toString());
                vo.setValue1Num(agreeNum.toString());
                vo.setValue2Num(disagreeNum.toString());
                BigInteger total = agreeNum.add(disagreeNum);
                BigInteger perTotal = peragree.add(perdisagree);
                vo.setTotalNum(total.toString());
                vo.setIssurContractAddress(issurContractAddress);
                BigDecimal decimalTotal = Convert.fromWei(total.toString(), Convert.Unit.HPB);
                if (decimalTotal.compareTo(BigDecimal.ONE) < 0) {
                    vo.setValue1Rate(BigInteger.ZERO.toString());
                    vo.setValue2Rate(BigInteger.ZERO.toString());
                } else {
                    BigDecimal decimalAgreeNum = Convert.fromWei(agreeNum.toString(), Convert.Unit.HPB);
                    BigDecimal b1 = decimalAgreeNum.divide(decimalTotal, 2, BigDecimal.ROUND_HALF_EVEN);
                    BigDecimal b2 = new BigDecimal(1).subtract(b1);
                    vo.setValue1Rate(b1.toString());
                    vo.setValue2Rate(b2.toString());
                }
                if (BigInteger.ONE.toString().equals(vo.getVoteStatus())) {
                    vo.setCountDownTime(vo.getEndTime().getTime() - vo.getBeginTime().getTime());
                    String balance = admin.hpbGetBalance(fromStr, DefaultBlockParameterName.LATEST).send().getBalance().toString();
                    if (new BigDecimal(balance).compareTo(new BigDecimal(perTotal)) > 0) {
                        vo.setAviliableNum(new BigDecimal(balance).subtract(new BigDecimal(perTotal)).toString());
                    }
                }
            }
            result.setData(vo);
        } catch (Exception e) {
            log.error("【查询题案投票详情失败 reqParam:{} {} {}】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> personalRecords(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 2) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String fromStr = reqStrList.get(1);
            Map<String, Object> resMap = new LinkedHashMap<>();
            List<IssueVotePersonalDetailVo> voList = new ArrayList<>();
            ProposalBallot proposalBallot = proposalBallotHelper.getProposalBallot();
            Tuple3<List<byte[]>, List<BigInteger>, List<BigInteger>> send4
                    = proposalBallot.fetchProposalVoteNumByAddr(fromStr).send();
            int proposalNoNum = send4.getValue2().size();
            Map<String, String> filterMap = new HashMap<>(proposalNoNum);
            if (proposalNoNum > 0) {
                List<String> resetNos = new LinkedList<>();
                for (int n = 0; n < proposalNoNum; n++) {
                    if (send4.getValue2().get(n).compareTo(BigInteger.ZERO) > 0 ||
                            send4.getValue3().get(n).compareTo(BigInteger.ZERO) > 0) {
                        String no = new String(send4.getValue1().get(n), StandardCharsets.UTF_8);
                        resetNos.add(no);
                        filterMap.put(no, send4.getValue2().get(n) + "_" + send4.getValue3().get(n));
                    }
                }
                if (resetNos.size() > 0 && resetNos.size() <= 1000) {
                    IssueVoteExample example = new IssueVoteExample();
                    example.setOrderByClause("end_time desc");
                    example.createCriteria().andIssueNoIn(resetNos);
                    List<IssueVote> voteList = issueVoteMapper.selectByExample(example);
                    if (null != voteList && voteList.size() > 0) {
                        voList = convert2personalVo(voteList);
                        for (IssueVotePersonalDetailVo vo : voList) {
                            for (String issueNo : resetNos) {
                                if (issueNo.equals(vo.getIssueNo())) {
                                    if (BigInteger.ONE.toString().equals(vo.getVoteStatus())) {
                                        vo.setCountDownTime(vo.getEndTime().getTime() - vo.getBeginTime().getTime());
                                    }
                                    vo.setPeragreeNum(filterMap.get(issueNo).split("_")[0]);
                                    vo.setPerdisagreeNum(filterMap.get(issueNo).split("_")[1]);
                                }
                            }
                        }
                    }
                }
            }
            resMap.put("list", voList);
            result.setData(resMap);
        } catch (Exception e) {
            log.error("【查询个人题案投票失败 reqParam:{} {} {}】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    private IssueVoteDetailVo convertIssueVote2detailVo(IssueVote vote) {
        IssueVoteDetailVo vo = new IssueVoteDetailVo();
        vo.setIssueNo(vote.getIssueNo());
        vo.setVoteStatus(vote.getVoteStatus());
        vo.setNameZh(vote.getNameZh());
        vo.setNameEn(vote.getNameEn());
        vo.setDescZh(vote.getDescZh());
        vo.setDescEn(vote.getDescEn());
        vo.setValue1Zh(vote.getValue1Zh());
        vo.setValue2Zh(vote.getValue2Zh());
        vo.setValue1En(vote.getValue1En());
        vo.setValue2En(vote.getValue2En());
        vo.setFloorNum(vote.getFloorNum().toString());
        vo.setBeginTime(vote.getBeginTime());
        vo.setEndTime(vote.getEndTime());
        vo.setGmtCreate(vote.getGmtCreate());
        vo.setGmtModify(vote.getGmtModify());
        vo.setIsVaild(vote.getIsVaild());
        return vo;
    }

    private List<IssueVotePersonalDetailVo> convert2personalVo(List<IssueVote> voteList) {
        List<IssueVotePersonalDetailVo> vos = new LinkedList<>();
        for (IssueVote vote : voteList) {
            IssueVotePersonalDetailVo vo = new IssueVotePersonalDetailVo();
            vo.setIssueNo(vote.getIssueNo());
            vo.setVoteStatus(vote.getVoteStatus());
            vo.setNameZh(vote.getNameZh());
            vo.setNameEn(vote.getNameEn());
            vo.setDescZh(vote.getDescZh());
            vo.setDescEn(vote.getDescEn());
            vo.setValue1Zh(vote.getValue1Zh());
            vo.setValue2Zh(vote.getValue2Zh());
            vo.setValue1En(vote.getValue1En());
            vo.setValue2En(vote.getValue2En());
            vo.setBeginTime(vote.getBeginTime());
            vo.setEndTime(vote.getEndTime());
            vos.add(vo);
        }
        return vos;
    }

    private void executeIssueResult(IssueVote issueVote) {
        try {
            ProposalBallot contract = proposalBallotHelper.getProposalBallot();
            TransactionReceipt receipt = contract.updateProposal(issueVote.getIssueNo().getBytes(), BigInteger.valueOf(3)).send();
            if (receipt.isStatusOK()) {
                issueVote.setVoteStatus(ProposalVoteConstant.P_ANNOUNCE);
                issueVoteMapper.updateByPrimaryKey(issueVote);
                IssueVoteResultExample example = new IssueVoteResultExample();
                example.createCriteria().andIssueNoEqualTo(issueVote.getIssueNo());
                List<IssueVoteResult> resultList = issueVoteResultMapper.selectByExample(example);
                if (null == resultList || resultList.size() < 1) {
                    Tuple4<BigInteger, BigInteger, BigInteger, BigInteger> send =
                            contract.fetchProposalVoteNumByNo(issueVote.getIssueNo().getBytes()).send();
                    BigDecimal value1 = new BigDecimal(send.getValue3());
                    BigDecimal value2 = new BigDecimal(send.getValue4());
                    BigDecimal total = value1.add(value2);
                    BigDecimal actualTotal = Convert.fromWei(total, Convert.Unit.HPB);
                    String status = null;
                    if (actualTotal.compareTo(ProposalVoteConstant.CANCEL_POINT) < 0) {
                        status = ProposalVoteConstant.P_CANCEL;
                    } else {
                        status = ProposalVoteConstant.P_ACTIVE;
                    }
                    String value1Rate;
                    String value2Rate;
                    if (total.compareTo(BigDecimal.ZERO) == 0) {
                        value1Rate = BigInteger.ZERO.toString();
                        value2Rate = BigInteger.ZERO.toString();
                    } else {
                        BigDecimal b1 = value1.divide(total, 4, BigDecimal.ROUND_HALF_EVEN);
                        BigDecimal b2 = new BigDecimal(1).subtract(b1);
                        value1Rate = b1.multiply(new BigDecimal(100)).toString();
                        value2Rate = b2.multiply(new BigDecimal(100)).toString();
                    }
                    List<IssueVoteResult> issueVoteResults = new ArrayList<>();
                    IssueVoteResult result = new IssueVoteResult();
                    result.setIssueNo(issueVote.getIssueNo());
                    result.setOptionKey(BigInteger.ONE.toString());
                    result.setOptionValue(issueVote.getValue1Zh());
                    result.setVoteNum(value1);
                    result.setVoteRatio(new BigDecimal(value1Rate));
                    issueVoteResults.add(result);

                    IssueVoteResult result2 = new IssueVoteResult();
                    result2.setIssueNo(issueVote.getIssueNo());
                    result2.setOptionKey(BigInteger.ZERO.toString());
                    result2.setOptionValue(issueVote.getValue2Zh());
                    result2.setVoteNum(value2);
                    result2.setVoteRatio(new BigDecimal(value2Rate));
                    issueVoteResults.add(result2);
                    issueVoteResultMapper.insertBatch(issueVoteResults);

                    issueVote.setVoteStatus(status);
                    issueVoteMapper.updateByPrimaryKey(issueVote);
                    contract.updateProposal(issueVote.getIssueNo().getBytes(), BigInteger.valueOf(Long.valueOf(status))).send();
                    updateProposalVote(issueVote.getIssueNo() + "_" + issueVote.getEndTime().getTime() + "_" + status);
                }
            }
        } catch (Exception e) {
            log.error("处理题案结果失败 issueNo:{} errMsg:{} {}", issueVote.getIssueNo(), e.getMessage(), e);
        }
    }

    public void updateProposalVote(String value) {
        Object proposalVoteObj = redisTemplate.opsForValue().get(ProposalVoteConstant.ISSUE_REDIS_PREFIX);
        String[] arrStr = value.split("_");
        if (null == proposalVoteObj) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put(arrStr[0], arrStr[1] + "_" + arrStr[2]);
            redisTemplate.opsForValue().set(ProposalVoteConstant.ISSUE_REDIS_PREFIX, map);
        } else {
            Map<String, String> proposalVoteValues = (Map<String, String>) proposalVoteObj;
            proposalVoteValues.put(arrStr[0], arrStr[1] + "_" + arrStr[2]);
            redisTemplate.opsForValue().set(ProposalVoteConstant.ISSUE_REDIS_PREFIX, proposalVoteValues);
        }
    }

    private List<byte[]> genBydescEn(String encodeText) {
        int len = encodeText.length();
        List<String> list = new ArrayList<>();
        if (len <= 32) {
            list.add(encodeText);
        } else {
            int i = 0;
            for (i = 0; i < encodeText.length() - 1; i += 32) {
                list.add(encodeText.substring(i, i + 32));
            }
            if (StringUtils.isNotBlank(encodeText.substring(i))) {
                list.add(encodeText.substring(i));
            }
        }
        List<byte[]> resList = new ArrayList<>();
        for (int n = 0; n < list.size(); n++) {
            resList.add(list.get(n).getBytes());
        }
        return resList;
    }

}

