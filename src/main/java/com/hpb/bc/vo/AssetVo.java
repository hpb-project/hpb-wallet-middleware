package com.hpb.bc.vo;

import com.hpb.bc.entity.BaseEntity;


public class AssetVo extends BaseEntity {
    private String id;
    private String tokenSymbol;
    private String tokenSymbolImageUrl;
    private String tokenName;
    private String contractCreater;
    private String contractAddress;
    private String tokenTotalSupply;
    private String contractType;
    private Integer transfersNum;
    private String cnyPrice;
    private String usdPrice;
    private String cnyTotalValue;
    private String usdTotalValue;
    private String decimals;
    private String balanceOfToken;

    public String getBalanceOfToken() {
        return balanceOfToken;
    }

    public void setBalanceOfToken(String balanceOfToken) {
        this.balanceOfToken = balanceOfToken;
    }

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenSymbolImageUrl() {
        return tokenSymbolImageUrl;
    }

    public void setTokenSymbolImageUrl(String tokenSymbolImageUrl) {
        this.tokenSymbolImageUrl = tokenSymbolImageUrl;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getContractCreater() {
        return contractCreater;
    }

    public void setContractCreater(String contractCreater) {
        this.contractCreater = contractCreater;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getTokenTotalSupply() {
        return tokenTotalSupply;
    }

    public void setTokenTotalSupply(String tokenTotalSupply) {
        this.tokenTotalSupply = tokenTotalSupply;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Integer getTransfersNum() {
        return transfersNum;
    }

    public void setTransfersNum(Integer transfersNum) {
        this.transfersNum = transfersNum;
    }

    public String getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(String cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

    public String getUsdPrice() {
        return usdPrice;
    }

    public void setUsdPrice(String usdPrice) {
        this.usdPrice = usdPrice;
    }

    public String getCnyTotalValue() {
        return cnyTotalValue;
    }

    public void setCnyTotalValue(String cnyTotalValue) {
        this.cnyTotalValue = cnyTotalValue;
    }

    public String getUsdTotalValue() {
        return usdTotalValue;
    }

    public void setUsdTotalValue(String usdTotalValue) {
        this.usdTotalValue = usdTotalValue;
    }
}
