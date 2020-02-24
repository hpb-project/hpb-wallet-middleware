package com.hpb.bc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.async.AsyncTask;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.constant.TokenConstant;
import com.hpb.bc.entity.*;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.example.ContractErcStandardInfoExample;
import com.hpb.bc.example.TxTransferRecordExample;
import com.hpb.bc.service.Token721Service;
import com.hpb.bc.token.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.token.mapper.TxTransferRecordMapper;
import com.hpb.bc.util.ERC721Util;
import com.hpb.bc.util.NumericUtil;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class Token721ServiceImpl extends AbstractBaseService implements Token721Service {
    @Autowired
    private TxTransferRecordMapper txTransferRecordMapper;
    @Autowired
    private ERC721Util erc721Util;
    @Autowired
    private Admin admin;
    @Autowired
    private AsyncTask asyncTask;
    @Autowired
    private ContractErcStandardInfoMapper contractErcStandardInfoMapper;

    @Override
    public Result<Map<String, Object>> get721StockDetail(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        String contractAddress = reqStrList.get(2);
        String address = reqStrList.get(3);
        try {
            if (null == reqStrList || reqStrList.size() != 4 || !NumericUtil.isValidAddress(address) || !NumericUtil.isValidAddress(contractAddress)) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            int pageNum = Integer.parseInt(reqStrList.get(1));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;

            Map<String, Object> resMap = new HashMap<>();
            Uint256 balance = erc721Util.balanceOf(new Address(address), contractAddress);
            if (balance != null) {
                Integer value = balance.getValue().intValue();
                int end = value - (pageNum - 1) * pageSize;
                int start = value - (pageNum * pageSize) + 1;
                if (pageNum == (value / pageSize + 1)) {
                    start = 1;
                }
                List<Token721StockDetailInfo> list = new ArrayList<Token721StockDetailInfo>();
                CountDownLatch countDownLatch = new CountDownLatch(end - start + 1);
                Address address1 = new Address(address);
                for (int i = end - 1; i >= start - 1; i--) {
                    Token721StockDetailInfo token721StockDetailInfo = new Token721StockDetailInfo();
                    list.add(token721StockDetailInfo);
                    asyncTask.queryCountByTokenId(countDownLatch, address1, i, contractAddress, token721StockDetailInfo);
                }
                countDownLatch.await(60, TimeUnit.SECONDS);
                resMap.put("total", value.toString());
                resMap.put("pageNum", pageNum);
                resMap.put("pages", value % pageSize == 0 ? (value / pageSize) : (value / pageSize + 1));
                resMap.put("list", list);
                result.setData(resMap);
            }
        } catch (Exception e) {
            log.error("【查询721库存详情失败 reqParam:{} {} {}】", e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> get721TokenIdDetail(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        try {
            if (null == reqStrList || reqStrList.size() != 4) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }

            int pageNum = Integer.parseInt(reqStrList.get(1));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;
            PageHelper.startPage(pageNum, pageSize);
            Map<String, Object> resMap = new HashMap<>();
            String tokenId = reqStrList.get(2);
            String contractAddress = reqStrList.get(3);
            TxTransferRecordExample example = new TxTransferRecordExample();
            example.setOrderByClause("block_number desc ");
            example.createCriteria().andContractAddressEqualTo(contractAddress).andTokenIdEqualTo(new Long(tokenId));
            List<TxTransferRecord> txTransferRecords = txTransferRecordMapper.selectByExample(example);
            List<TxTransferRecordInfo> list = new ArrayList<>();

            if (txTransferRecords != null) {
                for (TxTransferRecord txTransferRecord : txTransferRecords) {
                    TxTransferRecordInfo txTransferRecordInfo = new TxTransferRecordInfo();
                    txTransferRecordInfo.setTokenId(txTransferRecord.getTokenId().toString());
                    BeanUtils.copyProperties(txTransferRecord, txTransferRecordInfo);
                    HpbBlock block = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(txTransferRecord.getBlockNumber()), false).send();
                    if (null != block) {
                        if (null != block.getBlock()) {
                            BigInteger timestamp = block.getBlock().getTimestamp();
                            txTransferRecordInfo.setBlockTimestamp(timestamp.longValue());
                            list.add(txTransferRecordInfo);
                        }
                    }
                }
                PageInfo<TxTransferRecordInfo> info = new PageInfo<>(list);
                PageInfo<TxTransferRecord> info1 = new PageInfo<>(txTransferRecords);
                info.setList(list);
                resMap.put("total", info1.getTotal());
                resMap.put("pageNum", info1.getPageNum());
                resMap.put("pages", info1.getPages());
                resMap.put("list", info.getList());
                result.setData(resMap);
            }
        } catch (Exception e) {
            log.error("【查询721TokenID详情失败 reqParam:{} {} {}】", e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public PageInfo<TransactionHistoryInfo> get721transferRecord(List<String> reqStrList) {
        PageInfo<TransactionHistoryInfo> data = null;
        String address = reqStrList.get(1);
        String contractAddress = reqStrList.get(3);
        try {
            int pageNum = Integer.parseInt(reqStrList.get(5));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;
            PageHelper.startPage(pageNum, pageSize);

            Map<String, String> map = new HashMap<>();
            map.put("address", address);
            map.put("contractAddress", contractAddress);
            List<TxTransferRecord> txTransferRecords = txTransferRecordMapper.selectTxTransferRecordByAddress(map);

            PageInfo<TxTransferRecord> dataPage = new PageInfo<>(txTransferRecords);
            ContractErcStandardInfoExample example = new ContractErcStandardInfoExample();

            List<TxTransferRecord> pageTransfers = dataPage.getList();

            List<TransactionHistoryInfo> historyInfoList = new ArrayList<>();
            for (TxTransferRecord record : pageTransfers) {
                TransactionHistoryInfo historyInfo = new TransactionHistoryInfo();
                historyInfo.setFromAccount(record.getFromAddress());
                historyInfo.setFlag(address.equalsIgnoreCase(record.getFromAddress()) ? "0" : "1");
                historyInfo.setToAccount(record.getToAddress());
                historyInfo.setBlockNumber(record.getBlockNumber());
                historyInfo.setTokenId(record.getTokenId().toString());
                historyInfo.setTokenType(TokenConstant.HRC721);
                historyInfo.setContractAddress(record.getContractAddress());
                String hash = record.getTxHash();
                historyInfo.setTransactionHash(hash);
                String tokenURI = erc721Util.tokenURI(new Uint256(record.getTokenId()), contractAddress).toString().trim();
                historyInfo.setTokenURI(tokenURI);
                example.createCriteria().andContractAddressEqualTo(record.getContractAddress());
                List<ContractErcStandardInfo> contractErcStandardInfos = contractErcStandardInfoMapper.selectByExample(example);
                for (ContractErcStandardInfo contractErcStandardInfo : contractErcStandardInfos) {
                    historyInfo.setTokenSymbol(contractErcStandardInfo.getTokenSymbol());
                }
                HpbGetTransactionReceipt hpbGetTransactionReceipt = admin.hpbGetTransactionReceipt(hash).send();
                if (!hpbGetTransactionReceipt.hasError()) {
                    TransactionReceipt transactionReceipt = hpbGetTransactionReceipt.getResult();
                    if (null != transactionReceipt) {
                        historyInfo.setStatus(transactionReceipt.getStatus());
                        historyInfo.setGasUsed(transactionReceipt.getGasUsed().toString());

                        String blockHash = record.getBlockHash();
                        HpbBlock hpbBlock = admin.hpbGetBlockByHash(blockHash, false).send();
                        if (!hpbBlock.hasError()) {
                            HpbBlock.Block block = hpbBlock.getBlock();
                            historyInfo.settTimestap(block.getTimestamp().longValue());
                        }
                    }
                }
                historyInfoList.add(historyInfo);
            }
            data = new PageInfo<>(historyInfoList);
            data.setList(historyInfoList);
            data.setPageNum(dataPage.getPageNum());
            data.setPages(dataPage.getPages());
            data.setTotal(dataPage.getTotal());
            return data;
        } catch (Exception e) {
            log.error("【查询721获取721交易记录 reqParam:{} {} {}】", e.getMessage(), e);
            return data;
        }
    }

    @Override
    public Result<Map<String, Object>> getTokenId(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        String contractAddress = reqStrList.get(2);
        String address = reqStrList.get(3);
        try {
            if (null == reqStrList || reqStrList.size() != 4 || !NumericUtil.isValidAddress(address) || !NumericUtil.isValidAddress(contractAddress)) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            int pageNum = Integer.parseInt(reqStrList.get(1));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;

            Map<String, Object> resMap = new HashMap<>();
            Uint256 balance = erc721Util.balanceOf(new Address(address), contractAddress);
            if (null != balance) {
                Integer value = balance.getValue().intValue();
                int end = value - (pageNum - 1) * pageSize;
                int start = value - (pageNum * pageSize) + 1;
                if (pageNum == (value / pageSize + 1)) {
                    start = 1;
                }

                List<Object> list = new ArrayList<>();
                if (null != value) {
                    CountDownLatch countDownLatch = new CountDownLatch(end - start + 1);
                    Address address1 = new Address(address);
                    for (int i = end - 1; i >= start - 1; i--) {
                        Token721StockDetailInfo token721StockDetailInfo = new Token721StockDetailInfo();
                        list.add(token721StockDetailInfo);
                        asyncTask.queryCountByTokenId(countDownLatch, address1, i, contractAddress, token721StockDetailInfo);
                    }
                    countDownLatch.await(60, TimeUnit.SECONDS);

                    resMap.put("total", value.toString());
                    resMap.put("pageNum", pageNum);
                    resMap.put("pages", value % pageSize == 0 ? (value / pageSize) : (value / pageSize + 1));
                    resMap.put("list", list);
                    result.setData(resMap);
                }
                return result;
            }
        } catch (Exception e) {
            log.error("【查询721代币id失败 reqParam:{} {} 】", e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> get721TransferDetail(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        String address = reqStrList.get(2);
        try {
            if (null == reqStrList || reqStrList.size() != 4 || !NumericUtil.isValidAddress(address)) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            Map<String, Object> resMap = new HashMap<>();
            String tradeHash = reqStrList.get(3);
            List<TxTransferRecord> transferRecords = new ArrayList<>();
            TxTransferRecordExample example = new TxTransferRecordExample();
            example.createCriteria().andTxHashEqualTo(tradeHash);
            transferRecords = txTransferRecordMapper.selectByExample(example);
            List<TxTransferRecord> list1 = new ArrayList<>();
            for (TxTransferRecord txTransferRecord : transferRecords) {
                TxTransferRecord txTransferRecord1 = new TxTransferRecord();
                HpbBlock block = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(txTransferRecord.getBlockNumber()), false).send();
                if (null != block) {
                    if (null != block.getBlock()) {
                        BigInteger timestamp = block.getBlock().getTimestamp();
                        txTransferRecord1.setBlockTimestamp(timestamp.longValue());
                    }
                }

                BeanUtils.copyProperties(txTransferRecord, txTransferRecord1);

                HpbGetTransactionReceipt receipt = admin.hpbGetTransactionReceipt(tradeHash).send();
                if (null != receipt) {
                    boolean status = receipt.getTransactionReceipt().get().isStatusOK();
                    txTransferRecord1.setStatus(status);
                    BigInteger gasUsed = receipt.getTransactionReceipt().get().getGasUsed();
                    txTransferRecord1.setGasUsed(gasUsed);
                }
                list1.add(txTransferRecord1);
                resMap.put("transferRecords", list1);
                result.setData(resMap);
            }
        } catch (Exception e) {
            log.error("【查询721获取721交易详情 reqParam:{} {} {}】", e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        return result;
    }

    @Override
    public Result<Map<String, Object>> getTokenIdsByTxHash(List<String> reqStrList) {
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS);
        Map<String, Object> resMap = new HashMap<>();
        List<Token721StockDetailInfo> list = new ArrayList<>();
        try {
            if (null == reqStrList || reqStrList.size() != 4) {
                result.result(ResultCode.FAIL.code(), ResultCode.PARAMETER_ERROR.code());
                return result;
            }
            int pageNum = Integer.parseInt(reqStrList.get(1));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;
            PageHelper.startPage(pageNum, pageSize);
            TxTransferRecordExample example = new TxTransferRecordExample();
            example.createCriteria().andTxHashEqualTo(reqStrList.get(2));
            String contractAddress = reqStrList.get(3);
            list = new ArrayList<>();
            List<TxTransferRecord> txTransferRecords = txTransferRecordMapper.selectByExample(example);
            for (TxTransferRecord txTransferRecord : txTransferRecords) {
                Token721StockDetailInfo info = new Token721StockDetailInfo();
                Uint256 tokenId = new Uint256(txTransferRecord.getTokenId());
                info.setTokenId(txTransferRecord.getTokenId().toString());
                String tokenURI = erc721Util.tokenURI(tokenId, contractAddress).toString();
                info.setTokenURI(tokenURI);
                list.add(info);
            }
        } catch (Exception e) {
            log.error("【查询721获取721代币id reqParam:{} {} {}】", e.getMessage(), e);
            result.result(ResultCode.FAIL.code(), ResultCode.EXCEPTION.code());
            return result;
        }
        PageInfo<Token721StockDetailInfo> info = new PageInfo<>(list);
        info.setList(list);
        resMap.put("total", info.getTotal());
        resMap.put("pageNum", info.getPageNum());
        resMap.put("pages", info.getPages());
        resMap.put("list", info.getList());
        result.setData(resMap);
        return result;
    }


}


