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

package com.hpb.bc.service.impl;

import com.hpb.bc.cms.mapper.HpbDataDicMapper;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.entity.HpbDataDic;
import com.hpb.bc.example.HpbDataDicExample;
import com.hpb.bc.service.HpbDataDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class HpbDataDicServiceImpl extends AbstractBaseService implements HpbDataDicService {

    @Autowired
    private HpbDataDicMapper hpbDataDicMapper;

    @Override
    public HpbDataDic selectFromDataDic(Integer dataTypeNo, String dataNo) {
        if (dataTypeNo == null || !StringUtils.hasText(dataNo)) {
            return null;
        }
        HpbDataDicExample ethExample = new HpbDataDicExample();
        ethExample.createCriteria().andDataTypeNoEqualTo(dataTypeNo)
                .andDataNoEqualTo(dataNo)
                .andStateEqualTo(BcConstant.STATE_OPEN);
        List<HpbDataDic> selectByExample = hpbDataDicMapper.selectByExample(ethExample);
        if (selectByExample == null || selectByExample.size() == 0) {
            return null;
        }
        return selectByExample.get(0);
    }

    @Override
    public List<HpbDataDic> selectByExample(HpbDataDicExample example) {
        return hpbDataDicMapper.selectByExample(example);
    }

}