package com.hpb.bc.constant;

import java.math.BigDecimal;


public class ProposalVoteConstant {
    public static final String ISSUE_REDIS_PREFIX = "proposalvote";
    public static final String P_ANNOUNCE = "2";
    public static final String P_ACTIVE = "3";
    public static final String P_CANCEL = "5";
    public static final BigDecimal CANCEL_POINT = new BigDecimal("500000");
}
