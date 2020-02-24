package com.hpb.bc.service;

import com.hpb.bc.entity.result.Result;
import io.hpb.web3.protocol.core.methods.response.HpbBlockNumber;
import io.hpb.web3.protocol.core.methods.response.HpbGasPrice;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import io.hpb.web3.protocol.core.methods.response.HpbHashrate;
import io.hpb.web3.protocol.core.methods.response.HpbMining;
import io.hpb.web3.protocol.core.methods.response.HpbSyncing;

public interface GlobalService {

	Result<HpbSyncing> hpbSyncing();

	Result<HpbMining> hpbMining();

	Result<HpbHashrate> hpbHashrate();

	Result<HpbGasPrice> hpbGasPrice();

	Result<HpbBlockNumber> hpbBlockNumber();

	Result<HpbGetBalance> hpbGetBalance(String address, String name);
    
    
}
