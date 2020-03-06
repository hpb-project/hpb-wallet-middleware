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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;


public class ObjectJsonHelper {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static ObjectMapper om = new ObjectMapper();

    static {
        om.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        om.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        om.setSerializationInclusion(Include.ALWAYS);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.setDateFormat(new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * @return String
     * @Title:serialize
     * @Description:把java对象序列化为字符串
     * @author:wanggengle
     * @date:2017-05-15 12:41:26
     */
    public static String serialize(Object o) throws JsonProcessingException {
        return om.writeValueAsString(o);
    }

    /**
     * @return T
     * @Title:deserialize
     * @Description:把字符串反序列化为java对象
     * @author:wanggengle
     * @date:2017-05-15 12:42:00
     */
    public static <T> T deserialize(String str, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return om.readValue(str.getBytes(), clazz);
    }
}
