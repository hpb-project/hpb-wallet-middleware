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

package com.hpb.bc.util;

import java.math.BigDecimal;
import java.util.*;

public class RandomUtil {
    public static BigDecimal[] randomHandOutAlgorithm(BigDecimal totalAmount, int size, Integer scale, BigDecimal minAmount) {
        BigDecimal remainAmount = totalAmount.setScale(scale, BigDecimal.ROUND_DOWN);
        int remainSize = size;
        List<BigDecimal> out = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            BigDecimal random = BigDecimal.valueOf(Math.random());
            BigDecimal halfRemainSize = BigDecimal.valueOf(remainSize).divide(new BigDecimal(2), BigDecimal.ROUND_UP);
            BigDecimal max1 = remainAmount.divide(halfRemainSize, BigDecimal.ROUND_DOWN);
            BigDecimal minRemainAmount = minAmount.multiply(BigDecimal.valueOf(remainSize - 1)).setScale(scale, BigDecimal.ROUND_DOWN);
            BigDecimal max2 = remainAmount.subtract(minRemainAmount);
            BigDecimal max = (max1.compareTo(max2) < 0) ? max1 : max2;
            BigDecimal amount = random.multiply(max).setScale(scale, BigDecimal.ROUND_DOWN);
            if (amount.compareTo(minAmount) < 0) {
                amount = minAmount;
            }
            out.add(amount);
            remainAmount = remainAmount.subtract(amount).setScale(scale, BigDecimal.ROUND_DOWN);
            remainSize = remainSize - 1;
        }

        BigDecimal amount = remainAmount;
        out.add(amount);
        return out.toArray(new BigDecimal[size]);
    }
}
