/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
