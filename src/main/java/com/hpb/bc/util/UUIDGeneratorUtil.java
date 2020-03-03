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
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;


public class UUIDGeneratorUtil {
	private static SecureRandom seederStatic = null;
	private static byte[] addr = null;
	private static String midValueStatic = null;
	private String midValue = null;
	private SecureRandom seeder = null;

	public UUIDGeneratorUtil() {
		StringBuffer buffer = new StringBuffer(16);
		buffer.append(midValueStatic);
		buffer.append(toHex(System.identityHashCode(this), 8));
		this.midValue = buffer.toString();
		this.seeder = new SecureRandom();
		this.seeder.nextInt();
	}

	public static String generate(Object obj) {
		StringBuffer uid = new StringBuffer(32);

		long currentTimeMillis = System.currentTimeMillis();
		uid.append(toHex((int) (currentTimeMillis & 0xFFFFFFFF), 8));

		uid.append(midValueStatic);

		uid.append(toHex(System.identityHashCode(obj), 8));

		uid.append(toHex(getRandom(), 8));

		return uid.toString();
	}
	public static String get32UUID() {
		return generate(System.nanoTime());
	}
	public String generate() {
		StringBuffer uid = new StringBuffer(32);

		long currentTimeMillis = System.currentTimeMillis();
		uid.append(toHex((int) (currentTimeMillis & 0xFFFFFFFF), 8));

		uid.append(this.midValue);

		uid.append(toHex(this.seeder.nextInt(), 8));

		return uid.toString();
	}

	private static String toHex(int value, int length) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buffer = new StringBuffer(length);
		int shift = length - 1 << 2;
		int i = -1;
		while (true) {
			i++;
			if (i >= length)
				break;
			buffer.append(hexDigits[(value >> shift & 0xF)]);
			value <<= 4;
		}

		return buffer.toString();
	}

	private static int toInt(byte[] bytes) {
		int value = 0;
		int i = -1;
		while (true) {
			i++;
			if (i >= bytes.length)
				break;
			value <<= 8;
			value |= (bytes[i]& 0xff);
		}

		return value;
	}

	private static synchronized int getRandom() {
		return seederStatic.nextInt();
	}

	static {
		try {
			addr = RandomStringUtils.randomAlphanumeric(12).getBytes(StandardCharsets.UTF_8);
			StringBuffer buffer = new StringBuffer(8);
			buffer.append(toHex(toInt(addr), 8));
			midValueStatic = buffer.toString();
			seederStatic = new SecureRandom();
			seederStatic.nextInt();
		} catch (Exception ex) {
		}
	}
}
