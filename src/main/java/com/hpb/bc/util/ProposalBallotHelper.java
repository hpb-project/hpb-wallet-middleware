package com.hpb.bc.util;

import com.hpb.bc.contract.hpb.ProposalBallot;
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
public class ProposalBallotHelper {
    private static final Logger log = LoggerFactory.getLogger(ProposalBallotHelper.class);
    @Value("${issue.gasLimit}")
    private BigInteger gasLimit;
    @Value("${issue.contract.address}")
    private String contractAddress;
    @Value("${issue.privateKey}")
    private String issuePrivateKey;
    @Resource(name="hpbAdmin")
    private Admin admin;

    private static final ThreadLocal<ProposalBallot> proposalBallotHolder = new ThreadLocal<>();
    private static final ThreadLocal<Credentials> credentialsProposalBallotHolder = new ThreadLocal<>();
    private static final ThreadLocal<RawTransactionManager> transactionManagerHolder = new ThreadLocal<RawTransactionManager>();

    private Credentials getCredentials() throws Exception {
        Credentials credentials = credentialsProposalBallotHolder.get();
        if (credentials == null) {
            credentials = Credentials.create(issuePrivateKey);
            credentialsProposalBallotHolder.set(credentials);
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

    public ProposalBallot getProposalBallot() throws Exception{
        ProposalBallot proposalBallot = proposalBallotHolder.get();
        if (proposalBallot == null) {
            StaticGasProvider staticGasProvider=
                    new StaticGasProvider(Convert.toWei("18", Convert.Unit.GWEI),  new BigDecimal(gasLimit));
            proposalBallot = ProposalBallot.load(contractAddress, admin, getTransactionManager(),staticGasProvider);
            proposalBallotHolder.set(proposalBallot);
            log.info("setProposalBallotHolder[{}]", proposalBallot.hashCode());
        }
        return proposalBallot;
    }

}
