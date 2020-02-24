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