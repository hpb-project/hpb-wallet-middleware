package com.hpb.bc.util;

import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.abi.datatypes.generated.Uint8;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbCall;
import io.hpb.web3.utils.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Component
public class ERC20Util {
    private static final Logger log = LoggerFactory.getLogger(ERC721Util.class);
    public static final String FUNC_BALANCEOF = "balanceOf";
    @Autowired
    private Admin admin;
    private static final BigInteger GAS_PRICE = Convert.toWei("18", Convert.Unit.GWEI).toBigInteger();
    private static final BigInteger GAS_LIMIT = new BigInteger("9000000");
    private static final String publicAddr = "0xeb4153d38754233bb354b97a5bd318b4ea9b100e";

    public BigDecimal balanceOfAddr(String ownerAddr, String contractAddr){
        try {
            Address owner = new Address(ownerAddr);
            Function balanceOf = new Function(FUNC_BALANCEOF,
                    Arrays.<Type>asList(owner),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));

            String balanceOfStr = FunctionEncoder.encode(balanceOf);
            HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(publicAddr, contractAddr, balanceOfStr),
                    DefaultBlockParameterName.LATEST).send();
            List<Type> data = FunctionReturnDecoder.decode(hpbCall.getValue(), balanceOf.getOutputParameters());
            if(data != null && data.size()>0){
                return new BigDecimal(data.get(0).getValue().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Uint8 queryDecimals(String contractAddr){
        Function decimals = new Function("decimals",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        try {
            String decimalsStr = FunctionEncoder.encode(decimals);
            HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(publicAddr, contractAddr, decimalsStr),
                    DefaultBlockParameterName.LATEST).send();
            List<Type> data = FunctionReturnDecoder.decode(hpbCall.getValue(), decimals.getOutputParameters());
            if(data != null && data.size()>0){
                return (Uint8)data.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Uint8(0);
        }
        return new Uint8(0);

    }
}
