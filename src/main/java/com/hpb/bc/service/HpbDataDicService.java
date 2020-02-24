package com.hpb.bc.service;

import java.util.List;

import com.hpb.bc.entity.HpbDataDic;
import com.hpb.bc.example.HpbDataDicExample;

public interface HpbDataDicService {

	HpbDataDic selectFromDataDic(Integer ethDataTypeNo, String dataNo);

	List<HpbDataDic> selectByExample(HpbDataDicExample example);
	
}
