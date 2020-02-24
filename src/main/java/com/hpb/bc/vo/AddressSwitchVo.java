package com.hpb.bc.vo;

import com.hpb.bc.entity.BaseEntity;

public class AddressSwitchVo  extends BaseEntity {
    private String address;
    private String cnyTotalValue;
    private String usdTotalValue;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
