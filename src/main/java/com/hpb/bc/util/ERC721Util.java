package com.hpb.bc.util;

import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Component
public class ERC721Util {
    @Autowired
    private Admin admin;
    private static final String publicAddr = "0xeb4153d38754233bb354b97a5bd318b4ea9b100e";

    public Utf8String querySymbol(String contractAddr){
        Function symbol = new Function("symbol",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        try {
            String symbolStr = FunctionEncoder.encode(symbol);
            HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(publicAddr, contractAddr, symbolStr),
                    DefaultBlockParameterName.LATEST).send();
            List<Type> data = FunctionReturnDecoder.decode(hpbCall.getValue(), symbol.getOutputParameters());
            if(null != data && data.size()>0){
                return  (Utf8String)data.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Utf8String("");
        }
        return new Utf8String("");
    }

    public BigDecimal balanceOfAddr(String ownerAddr, String contractAddr){
        try {
            Address owner = new Address(ownerAddr);
            Function balanceOf = new Function("balanceOf",
                    Arrays.<Type>asList(owner),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));

            String balanceOfStr = FunctionEncoder.encode(balanceOf);
            HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(publicAddr, contractAddr, balanceOfStr),
                    DefaultBlockParameterName.LATEST).send();
            List<Type> data = FunctionReturnDecoder.decode(hpbCall.getValue(), balanceOf.getOutputParameters());
            if(null != data && data.size()>0){
                return  new BigDecimal(data.get(0).getValue().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new BigDecimal(0);
        }
        return new BigDecimal(0);
    }

    public Uint256 rpcTokenOfOwnerByIndex(Address address, Uint256 uint256, String contractAddress) {
        Function function = new Function("tokenOfOwnerByIndex",
                Arrays.<Type>asList(address, uint256),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));

        List<Type> typeList = new ArrayList<>();
        try {
            String encodedFunction = FunctionEncoder.encode(function);
            HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
            String value = hpbCall.getValue();
            typeList = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            if (null!=typeList && typeList.size()>0){
                return (Uint256) typeList.get(0);
            }
            return  new Uint256(0);
        } catch (IOException e) {
            e.printStackTrace();
            return  new Uint256(0);
        }


    }

    public Utf8String tokenURI(Uint256 tokenId, String contractAddress) {
        final Function function = new Function("tokenURI",
                Arrays.<Type>asList(tokenId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));


        List<Type> typeList = new ArrayList<>();
        try {
            String encodedFunction = FunctionEncoder.encode(function);
            HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
            String value = hpbCall.getValue();
            typeList = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            if(null!=typeList && typeList.size()>0){
                return (Utf8String) typeList.get(0);
            }
            return  new Utf8String("");
        } catch (IOException e) {
            e.printStackTrace();
            return new Utf8String("");
        }
    }

    public Uint256  balanceOf(Address owner,String contractAddress) {
        final Function function = new Function("balanceOf",
                Arrays.<Type>asList(owner),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        List<Type> typeList = new ArrayList<>();
        try {
            String encodedFunction = FunctionEncoder.encode(function);
            HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
            List<Type> data = FunctionReturnDecoder.decode(hpbCall.getValue(), function.getOutputParameters());
            if(null != data && data.size()>0){
                return (Uint256) data.get(0);
            }
            return  new Uint256(0);
        } catch (IOException e) {
            e.printStackTrace();
            return new Uint256(0);
        }
    }
}
