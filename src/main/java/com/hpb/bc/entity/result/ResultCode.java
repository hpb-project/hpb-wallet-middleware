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

package com.hpb.bc.entity.result;

public enum ResultCode {
    SUCCESS("000000", "成功"),

    FAIL("999999", "失败"),

    WARN("-1", "网络异常，请稍后重试"),

    NOT_LOGIN("400", "没有登录"),

    EXCEPTION("401", "发生异常"),

    ERROR_EXCEPTION("40101", "获取区块链信息异常"),

    SYS_ERROR("402", "系统错误"),

    PARAMS_ERROR("403", "参数错误 "),

    RESPONSE_EMPTY("404", "响应结果为空"),

    RESPONSE_NULL("404", "响应结果为空"),

    NOT_SUPPORTED("410", "不支持或已经废弃"),

    INVALID_AUTHCODE("444", "无效的AuthCode"),

    TOO_FREQUENT("445", "太频繁的调用"),

    UNKNOWN_ERROR("499", "未知错误"),

    PARAMETER_ERROR("10101", "参数错误"),

    INVALID_ADDRESS("10104", "不合法 钱包地址"),

    INVALID_TXHASH("10105", "不合法 交易HASH"),

    INVALID_BLOCKHASH("10106", "不合法 区块HASH");

    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
