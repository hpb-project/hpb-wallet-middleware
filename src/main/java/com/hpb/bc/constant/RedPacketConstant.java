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

package com.hpb.bc.constant;

import java.math.BigDecimal;


public class RedPacketConstant {
    public static final String COIN_SYMBOL = "HPB";
    public static final String PREFIX = "PACKET";
    public static final int BATCH_NUM = 1000;
    public static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");
    public static final  String PER_MINER_FEE = "9000000000000000";
    public static final String ENTER_TYPE_2 = "2";
}
