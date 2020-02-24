package com.hpb.bc.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import javax.annotation.Resource;

import com.hpb.bc.constant.TokenConstant;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.token.mapper.TxTransferRecordMapper;
import com.hpb.bc.vo.TokenTypeInfoVo;
import com.hpb.bc.example.ContractErcStandardInfoExample;
import com.hpb.bc.service.TransactionService;
import com.hpb.bc.token.mapper.ContractErcStandardInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.chain.mapper.TransactionHistoryInfoMapper;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.entity.TransactionHistoryInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.exception.ErrorException;
import com.hpb.bc.util.NumericUtil;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.Response.Error;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.HpbBlock.Block;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionCount;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;
import io.hpb.web3.protocol.core.methods.response.HpbTransaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;


@Service
public class TransactionServiceImpl extends AbstractBaseService implements TransactionService {

    @Resource(name = "hpbAdmin")
    private Admin admin;
    @Autowired
    private Token20ServiceImpl token20Service;
    @Autowired
    private Token721ServiceImpl token721Service;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private TransactionHistoryInfoMapper transactionHistoryInfoMapper;
    @Autowired
    private ContractErcStandardInfoMapper contractErcStandardInfoMapper;
    @Autowired
    private TxTransferRecordMapper txTransferRecordMapper;

    @Override
    public Result<io.hpb.web3.protocol.core.methods.response.Transaction> hpbGetTransactionByHash(List<String> reqStrList) {
        Result<io.hpb.web3.protocol.core.methods.response.Transaction> result = new Result<>(ResultCode.SUCCESS);
        if (reqStrList == null || reqStrList.size() < 2) {
            result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
            return result;
        }
        String transactionHash = reqStrList.get(1);
        if (!NumericUtil.isValidTxHash(transactionHash)) {
            result.result(ResultCode.FAIL.code(), ResultCode.INVALID_TXHASH.code());
            return result;
        }
        try {
            HpbTransaction hpbTransaction = admin.hpbGetTransactionByHash(transactionHash).send();
            Error error = hpbTransaction.getError();
            if (null != error) {
                result.error(error);
            } else {
                result.setData(hpbTransaction.getResult());
            }
        } catch (IOException e) {
            result.exception(e);
            log.error("processId:[" + reqStrList.get(0) + "]系统异常", e);
        }
        return result;
    }

    @Override
    public Result<BigInteger> hpbGetTransactionCount(List<String> reqStrList) {
        Result<BigInteger> result = new Result<>(ResultCode.SUCCESS);
        if (reqStrList == null || reqStrList.size() < 2) {
            result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
            return result;
        }
        String from = reqStrList.get(1);
        if (!NumericUtil.isValidAddress(from)) {
            result.result(ResultCode.FAIL.code(), ResultCode.INVALID_ADDRESS.code());
            return result;
        }
        try {
            result = getNonce(from, DefaultBlockParameterName.LATEST.name());
        } catch (IOException | ErrorException e) {
            result.exception(e);
            log.error("processId:[" + reqStrList.get(0) + "]系统异常", e);
        }
        return result;
    }

    @Override
    public Result<String> hpbSendTransaction(List<String> reqStrList) {
        Result<String> result = new Result<>(ResultCode.SUCCESS);

        try {
            Transaction transaction = createTransaction(reqStrList);
            HpbSendTransaction hpbSendTransaction = admin.hpbSendTransaction(transaction).send();
            Error error = hpbSendTransaction.getError();
            if (null != error) {
                result.error(error);
            } else {
                result.setData(hpbSendTransaction.getResult());
            }
        } catch (IOException e) {
            result.exception(e);
            log.error("processId:[" + reqStrList.get(0) + "]系统异常", e);
        }
        return result;
    }

    @Override
    public Result<String> hpbSendRawTransaction(List<String> reqStrList) {
        Result<String> result = new Result<>(ResultCode.SUCCESS);
        if (reqStrList == null || reqStrList.size() < 2) {
            result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
            return result;
        }
        String input = reqStrList.get(1);
        if (!NumericUtil.isValidRawTransaction(input)) {
            result.result(ResultCode.FAIL.code(), ResultCode.INVALID_ADDRESS.code());
            return result;
        }
        try {
            HpbSendTransaction hpbSendTransaction = admin.hpbSendRawTransaction(input).send();
            Error error = hpbSendTransaction.getError();
            if (null != error) {
                result.error(error);
            } else {
                result.setData(hpbSendTransaction.getResult());
            }
        } catch (IOException e) {
            result.exception(e);
            log.error("processId:[" + reqStrList.get(0) + "]系统异常", e);
        }
        return result;
    }

    @Override
    public Result<TransactionReceipt> hpbGetTransactionReceipt(List<String> reqStrList) {
        Result<TransactionReceipt> result = new Result<>(ResultCode.SUCCESS);
        if (reqStrList == null || reqStrList.size() < 2) {
            result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
            return result;
        }
        String transactionHash = reqStrList.get(1);
        if (!NumericUtil.isValidTxHash(transactionHash)) {
            result.result(ResultCode.FAIL.code(), ResultCode.INVALID_TXHASH.code());
            return result;
        }
        try {
            HpbGetTransactionReceipt hpbGetTransactionReceipt = admin.hpbGetTransactionReceipt(transactionHash).send();
            Error error = hpbGetTransactionReceipt.getError();
            if (null != error) {
                result.error(error);
            } else {
                result.setData(hpbGetTransactionReceipt.getResult());
            }
        } catch (IOException e) {
            result.exception(e);
            log.error("processId:[" + reqStrList.get(0) + "]系统异常", e);
        }
        return result;
    }

    private Transaction createTransaction(List<String> reqStrList) {
        if (reqStrList == null || reqStrList.size() < 7) {
            throw new ErrorException(new Error());
        } else {
            String from = reqStrList.get(1);
            BigInteger nonce = new BigInteger(reqStrList.get(2));
            BigInteger gasPrice = new BigInteger(reqStrList.get(3));
            BigInteger gasLimit = new BigInteger(reqStrList.get(4));
            String to = reqStrList.get(5);
            BigInteger value = new BigInteger(reqStrList.get(6));
            String data = reqStrList.get(7);

            if (reqStrList.size() > 7) {
                value = new BigInteger(reqStrList.get(8));
            }
            return Transaction.createFunctionCallTransaction(from, nonce, gasPrice, gasLimit, to, value, data);
        }
    }

    public Result<BigInteger> getNonce(String address, String name) throws IOException {
        Result<BigInteger> result = new Result<>(ResultCode.SUCCESS);
        HpbGetTransactionCount transactionCount = admin.hpbGetTransactionCount(address, DefaultBlockParameterName.fromString(name)).send();
        Error error = transactionCount.getError();
        if (error != null) {
            result.error(error);
        } else {
            result.setData(transactionCount.getTransactionCount());
        }
        return result;

    }

    public static final String TX_TYPE_ALL = "0";
    public static final String TX_TYPE_FROM = "1";
    public static final String TX_TYPE_TO = "2";

    @Override
    public Result<PageInfo<TransactionHistoryInfo>> getTransactionHistory(List<String> reqStrList) {
        Result<PageInfo<TransactionHistoryInfo>> result =  new Result<>(ResultCode.SUCCESS);
        try {
            if(reqStrList.size()<2 || StringUtils.isBlank(reqStrList.get(1))) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String address = reqStrList.get(1);
            if(!NumericUtil.isValidAddress(address)) {
                result.result(ResultCode.FAIL.code(), ResultCode.INVALID_ADDRESS.code());
                return result;
            }
            String type = reqStrList.get(2);
            int pageNum = Integer.parseInt(reqStrList.get(3));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;
            if(TX_TYPE_ALL.equals(type)) {
                result.setData(selectTransactionHistoryInfoByAll(address,pageNum,pageSize));
            }else
            if(TX_TYPE_FROM.equals(type)) {
                result.setData(selectTransactionHistoryInfoByPage(address,null,pageNum,pageSize));
            }else
            if(TX_TYPE_TO.equals(type)) {
                result.setData(selectTransactionHistoryInfoByPage(null,address,pageNum,pageSize));
            }
        } catch (IOException e) {
            result.exception(e);
            log.error("processId:["+reqStrList.get(0)+"]系统异常",e);
        }
        return result;
    }

    public Result<PageInfo<TransactionHistoryInfo>> getTransactionHistoryNew(List<String> reqStrList) {
        Result<PageInfo<TransactionHistoryInfo>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (reqStrList.size() < 2 || StringUtils.isBlank(reqStrList.get(1))) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            String address = reqStrList.get(1);
            if (!NumericUtil.isValidAddress(address)) {
                result.result(ResultCode.FAIL.code(), ResultCode.INVALID_ADDRESS.code());
                return result;
            }
            String type = reqStrList.get(6);
            int pageNum = Integer.parseInt(reqStrList.get(5));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;
            if (TX_TYPE_ALL.equals(type)) {
                result.setData(selectTransactionHistoryInfoByAll(address, pageNum, pageSize));
            } else if (TX_TYPE_FROM.equals(type)) {
                result.setData(selectTransactionHistoryInfoByPage(address, null, pageNum, pageSize));
            } else if (TX_TYPE_TO.equals(type)) {
                result.setData(selectTransactionHistoryInfoByPage(null, address, pageNum, pageSize));
            }
        } catch (IOException e) {
            result.exception(e);
            log.error("processId:[" + reqStrList.get(0) + "]系统异常", e);
        }
        return result;
    }

    private PageInfo<TransactionHistoryInfo> selectTransactionHistoryInfoByPage(String txFrom, String txTo, int pageNum, int pageSize) throws IOException {

        Map<String, Object> example = new HashMap<>();
        example.put("txFrom", txFrom);
        example.put("txTo", txTo);
        PageHelper.startPage(pageNum, pageSize);
        List<TransactionHistoryInfo> transactionHistoryInfoByPage = transactionHistoryInfoMapper.selectTransactionHistoryInfoByPage(example);
        PageInfo<TransactionHistoryInfo> data = new PageInfo<>(transactionHistoryInfoByPage);
        if (data.getSize() > 0) {
            List<TransactionHistoryInfo> list = data.getList();
            for (TransactionHistoryInfo transactionHistoryInfo : list) {
                String hash = transactionHistoryInfo.getTransactionHash();
                HpbGetTransactionReceipt hpbGetTransactionReceipt = admin.hpbGetTransactionReceipt(hash).send();
                if (!hpbGetTransactionReceipt.hasError()) {
                    TransactionReceipt transactionReceipt = hpbGetTransactionReceipt.getResult();
                    transactionHistoryInfo.setStatus(transactionReceipt.getStatus());
                    transactionHistoryInfo.setGasUsed(transactionReceipt.getGasUsed().toString());
                }
                String blockHash = transactionHistoryInfo.getBlockHash();
                HpbBlock hpbBlock = admin.hpbGetBlockByHash(blockHash, false).send();
                if (!hpbBlock.hasError()) {
                    Block block = hpbBlock.getBlock();
                    transactionHistoryInfo.settTimestap(block.getTimestamp().longValue());
                }
            }
            data.setList(list);
        }
        return data;
    }

    private PageInfo<TransactionHistoryInfo> selectTransactionHistoryInfoByAll(String address, int pageNum, int pageSize) throws IOException {
        Map<String, Object> example = new HashMap<>();
        example.put("address", address);
        PageHelper.startPage(pageNum, pageSize);
        List<TransactionHistoryInfo> transactionHistoryInfoByPage = transactionHistoryInfoMapper.selectTransactionHistoryInfoByAll(example);
        PageInfo<TransactionHistoryInfo> data = new PageInfo<>(transactionHistoryInfoByPage);
        if (data.getSize() > 0) {
            List<TransactionHistoryInfo> list = data.getList();
            for (TransactionHistoryInfo transactionHistoryInfo : list) {
                String hash = transactionHistoryInfo.getTransactionHash();
                HpbGetTransactionReceipt hpbGetTransactionReceipt = admin.hpbGetTransactionReceipt(hash).send();
                if (!hpbGetTransactionReceipt.hasError()) {
                    TransactionReceipt transactionReceipt = hpbGetTransactionReceipt.getResult();
                    if (transactionReceipt!=null){
                        transactionHistoryInfo.setStatus(transactionReceipt.getStatus());
                        transactionHistoryInfo.setGasUsed(transactionReceipt.getGasUsed().toString());

                        String blockHash = transactionReceipt.getBlockHash();
                        HpbBlock hpbBlock = admin.hpbGetBlockByHash(blockHash, false).send();
                        if (!hpbBlock.hasError()) {
                            Block block = hpbBlock.getBlock();
                            transactionHistoryInfo.settTimestap(block.getTimestamp().longValue());
                        }
                    }

                }
            }
            data.setList(list);
        }
        return data;
    }

    @Override
    public Result<TransactionHistoryInfo> getTransactionDetail(List<String> reqStrList) {
        Result<TransactionHistoryInfo> result = new Result<>(ResultCode.SUCCESS);
        try {
            String hash = reqStrList.get(1);
            Map<String, Object> example = new HashMap<>();
            example.put("hash", hash);
            TransactionHistoryInfo transactionHistoryInfo = transactionHistoryInfoMapper.selectTransactionHistoryInfoByHash(example);
            HpbGetTransactionReceipt hpbGetTransactionReceipt = admin.hpbGetTransactionReceipt(hash).send();
            if (hpbGetTransactionReceipt.hasError()) {
                result.error(hpbGetTransactionReceipt.getError());
                return result;
            }
            TransactionReceipt transactionReceipt = hpbGetTransactionReceipt.getResult();
            transactionHistoryInfo.setStatus(transactionReceipt.getStatus());
            transactionHistoryInfo.setGasUsed(transactionReceipt.getGasUsed().toString());
            String blockHash = transactionHistoryInfo.getBlockHash();
            HpbBlock hpbBlock = admin.hpbGetBlockByHash(blockHash, false).send();
            if (hpbBlock.hasError()) {
                result.error(hpbBlock.getError());
                return result;
            }
            Block block = hpbBlock.getBlock();
            transactionHistoryInfo.settTimestap(block.getTimestamp().longValue());
            result.setData(transactionHistoryInfo);
        } catch (IOException e) {
            result.exception(e);
            log.error("processId:[" + reqStrList.get(0) + "]系统异常", e);
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> historyList(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            String addr = reqStrList.get(1);
            String contractType = reqStrList.get(2);
            Map<String, Object> resMap = new HashMap<>();
            if (null == reqStrList || reqStrList.size() != 7 || !NumericUtil.isValidAddress(addr)) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            PageInfo<TransactionHistoryInfo> pageInfo = null;
            if (contractType.equals(TokenConstant.HRC20)) {
                pageInfo = token20Service.list(reqStrList);
            } else if (contractType.equals(TokenConstant.HRC721)) {
                pageInfo = token721Service.get721transferRecord(reqStrList);
            } else if (contractType.equals(TokenConstant.HPB)) {
                Result<PageInfo<TransactionHistoryInfo>> transactionHistory = transactionService.getTransactionHistoryNew(reqStrList);
                pageInfo = transactionHistory.getData();
            }
            resMap.put("total", pageInfo.getTotal());
            resMap.put("pageNum", pageInfo.getPageNum());
            resMap.put("pages", pageInfo.getPages());
            resMap.put("list", pageInfo.getList());
            result.setData(resMap);
            return result;
        } catch (Exception e) {
            log.error("【查询交易记录失败 reqParam:{} {} {} {} {}】", e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
    }

    @Override
    public Result<Map<String, Object>> getTypeList(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            String address = reqStrList.get(1);
            String contractType = reqStrList.get(2);
            if (null == reqStrList || reqStrList.size() != 3 ) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            contractType = TokenConstant.HRC20.equalsIgnoreCase(contractType)? TokenConstant.ERC20:TokenConstant.ERC721;

            List<String> recordContrscts = txTransferRecordMapper.selectContractAddrsByAddr(address);
            List<TokenTypeInfoVo> infoVoList = new ArrayList<>();
            if(null != recordContrscts && recordContrscts.size()>0){
                ContractErcStandardInfoExample example = new ContractErcStandardInfoExample();
                example.createCriteria().andContractTypeEqualTo(contractType).andContractAddressIn(recordContrscts);
                List<ContractErcStandardInfo> standardInfos = contractErcStandardInfoMapper.selectByExample(example);
                for(ContractErcStandardInfo info: standardInfos){
                    TokenTypeInfoVo tokenTypeInfo = new TokenTypeInfoVo();
                    tokenTypeInfo.setContractAddress(info.getContractAddress());
                    tokenTypeInfo.setTokenSymbol(info.getTokenSymbol());
                    infoVoList.add(tokenTypeInfo);
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", infoVoList);
            result.setData(map);
            return result;
        } catch (Exception e) {
            log.error("【代币管理查询失败 reqParam:{} {} 】", reqStrList.toArray(new String[reqStrList.size()]), e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
    }


}
