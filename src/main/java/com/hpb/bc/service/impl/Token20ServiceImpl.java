package com.hpb.bc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.constant.TokenConstant;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.entity.TransactionHistoryInfo;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.example.ContractErcStandardInfoExample;
import com.hpb.bc.service.Token20Service;
import com.hpb.bc.token.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.token.mapper.TxTransferRecordMapper;
import com.hpb.bc.util.ERC20Util;
import com.hpb.bc.vo.WalletTransferVo;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class Token20ServiceImpl extends AbstractBaseService implements Token20Service {
    @Autowired
    private TxTransferRecordMapper txTransferRecordMapper;
    @Autowired
    private ContractErcStandardInfoMapper contractErcStandardInfoMapper;
    @Autowired
    private TokenManageServiceImpl tokenManageService;
    @Autowired
    private ERC20Util erc20Util;
    @Autowired
    private Admin admin;

    @Override
    public PageInfo<TransactionHistoryInfo> list(List<String> reqStrList) {
        PageInfo<TransactionHistoryInfo> data = null;
        try{
            int pageNum = Integer.parseInt(reqStrList.get(5));
            int pageSize = BcConstant.PAGESIZE_DEFAULT;

            Map<String, String> example = new HashMap<>();
            example.put("address", reqStrList.get(1));
            example.put("contractAddress",reqStrList.get(3));
            PageHelper.startPage(pageNum,pageSize);
            data = selectTxTransferRecordByAllByPage(example, pageNum, pageSize);
            return data;
        } catch (Exception e) {
            log.error("ERC20交易记录查询失败 errMsg:{},{}", e.getMessage(),e);
            return data;
        }
    }

    private PageInfo<TransactionHistoryInfo> selectTxTransferRecordByAllByPage(Map<String, String> param,int pageNum, int pageSize){
        PageInfo<TransactionHistoryInfo> data = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<TxTransferRecord> txTransferRecords = txTransferRecordMapper.selectTxTransferRecordByAll(param);

            PageInfo<TxTransferRecord> dataPage = new PageInfo<>(txTransferRecords);
            List<TxTransferRecord> pageTransfers = dataPage.getList();

            List<TransactionHistoryInfo> historyInfoList = new ArrayList<>();
            for(TxTransferRecord record: pageTransfers){
                TransactionHistoryInfo historyInfo = new TransactionHistoryInfo();
                historyInfo.setTokenSymbol(record.getTokenType());
                historyInfo.setFromAccount(record.getFromAddress());
                historyInfo.setToAccount(record.getToAddress());
                historyInfo.setBlockNumber(record.getBlockNumber());
                historyInfo.setTokenType(TokenConstant.HRC20);
                historyInfo.setDecimals(erc20Util.queryDecimals(record.getContractAddress()).getValue().toString());
                historyInfo.settValue(record.getQuantity());

                String hash = record.getTxHash();
                historyInfo.setTransactionHash(hash);
                HpbGetTransactionReceipt hpbGetTransactionReceipt = admin.hpbGetTransactionReceipt(hash).send();
                if(!hpbGetTransactionReceipt.hasError()) {
                    TransactionReceipt transactionReceipt = hpbGetTransactionReceipt.getResult();
                    if(null != transactionReceipt){
                        historyInfo.setStatus(transactionReceipt.getStatus());
                        historyInfo.setGasUsed(transactionReceipt.getGasUsed().toString());

                        String blockHash = record.getBlockHash();
                        HpbBlock hpbBlock = admin.hpbGetBlockByHash(blockHash, false).send();
                        if(!hpbBlock.hasError()) {
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
        } catch (IOException e) {
            log.error("token 链上查询交易回执失败 errMsg:{} {}", e.getMessage(),e);
            data = new PageInfo<>();
            return data;
        }
    }

    public List<WalletTransferVo> query20TransferSymbolsAndBalance(String address){
        List<String> manageContracts = tokenManageService.contractAddrManage(TokenConstant.HRC20);

        List<WalletTransferVo> checkResultList = new ArrayList<>();
        List<ContractErcStandardInfo> infos = new ArrayList<>();
        if(manageContracts.size()>0) {
            ContractErcStandardInfoExample example = new ContractErcStandardInfoExample();
            example.setOrderByClause("create_timestamp desc");
            ContractErcStandardInfoExample.Criteria criteria = example.createCriteria();
            if(manageContracts.size()>0){
                criteria.andContractAddressIn(manageContracts);
                criteria.andContractTypeEqualTo(TokenConstant.ERC20);
                infos = contractErcStandardInfoMapper.selectByExample(example);
            }

            for(ContractErcStandardInfo info: infos){
                BigDecimal balance = erc20Util.balanceOfAddr(address, info.getContractAddress());
                if(balance.compareTo(BigDecimal.ZERO) >0){
                    WalletTransferVo vo = new WalletTransferVo();
                    vo.setContractAddress(info.getContractAddress());
                    vo.setType(TokenConstant.HRC20);
                    vo.setTokenNum(balance.toString());
                    vo.setSymbol(info.getTokenSymbol());
                    vo.setDecimals(erc20Util.queryDecimals(info.getContractAddress()).getValue().toString());
                    checkResultList.add(vo);
                }
            }
        }
        return checkResultList;
    }
}
