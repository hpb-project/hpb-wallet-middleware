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

import java.math.BigInteger;


public final class NumericUtil {
    public static final int ADDRESS_SIZE = 40;
    public static final int TX_HASH_SIZE = 64;

    private NumericUtil() {
    }

    public static boolean isValidRawTransaction(String input) {
        String cleanInput = cleanHexPrefix(input);

        try {
            toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidAddress(String input) {
        String cleanInput = cleanHexPrefix(input);

        try {
            toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == ADDRESS_SIZE;
    }

    public static boolean isValidTxHash(String input) {
        String cleanInput = cleanHexPrefix(input);

        try {
            toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == TX_HASH_SIZE;
    }

    public static String cleanHexPrefix(String input) {
        if (containsHexPrefix(input)) {
            return input.substring(2);
        } else {
            return input;
        }
    }

    public static boolean containsHexPrefix(String input) {
        return input.length() > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }

    public static BigInteger toBigIntNoPrefix(String hexValue) {
        return new BigInteger(hexValue, 16);
    }

}
