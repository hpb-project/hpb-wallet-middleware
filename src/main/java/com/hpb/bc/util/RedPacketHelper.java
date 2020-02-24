package com.hpb.bc.util;

import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.contract.hpb.RedPacketToken;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.tx.ChainIdLong;
import io.hpb.web3.tx.RawTransactionManager;
import io.hpb.web3.tx.gas.StaticGasProvider;
import io.hpb.web3.utils.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;


@Component
public class RedPacketHelper {
    private static final Logger log = LoggerFactory.getLogger(RedPacketHelper.class);
    @Value("${redpacket.gasLimit}")
    private BigInteger gasLimit;
    @Value("${redpacket.contract.address}")
    private String contractAddress;
    @Value("${redpacket.privateKey}")
    private String packetPrivateKey;
    @Resource(name="hpbAdmin")
    private Admin admin;

    private static final ThreadLocal<RedPacketToken> redPacketTokenHolder = new ThreadLocal<>();
    private static final ThreadLocal<Credentials> credentialsRedPacketHolder = new ThreadLocal<>();
    private static final ThreadLocal<RawTransactionManager> transactionManagerHolder = new ThreadLocal<RawTransactionManager>();

    private Credentials getCredentials() throws Exception {
        Credentials credentials = credentialsRedPacketHolder.get();
        if (credentials == null) {
            credentials = Credentials.create(packetPrivateKey);
            credentialsRedPacketHolder.set(credentials);
            log.info("setCredentialsRedPacket[{}]", credentials.hashCode());
        }
        return credentials;
    }

    private RawTransactionManager getTransactionManager() throws Exception {
        RawTransactionManager transactionManager = transactionManagerHolder.get();
        if (transactionManager == null) {
            transactionManager = new RawTransactionManager(admin, getCredentials(), ChainIdLong.MAINNET);
            transactionManagerHolder.set(transactionManager);
            log.info("setTransactionManagerHolder[{}]", transactionManager.hashCode());
        }
        return transactionManager;
    }

    public RedPacketToken getRedPacketToken() throws Exception{
        RedPacketToken redPacketToken = redPacketTokenHolder.get();
        if (redPacketToken == null) {
            StaticGasProvider staticGasProvider=
                    new StaticGasProvider(Convert.toWei("18", Convert.Unit.GWEI),  new BigDecimal(gasLimit));
            redPacketToken = RedPacketToken.load(contractAddress, admin, getTransactionManager(),staticGasProvider);
            redPacketTokenHolder.set(redPacketToken);
            log.info("setRedPacketToken[{}]", redPacketToken.hashCode());
        }
        return redPacketToken;
    }

}
