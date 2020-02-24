package com.hpb.bc.service.impl;

import com.hpb.bc.entity.HpbInstantPrice;
import com.hpb.bc.service.HpbInstantPriceService;
import com.hpb.bc.website.mapper.HpbInstantPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HpbInstantPriceServiceImpl extends AbstractBaseService implements HpbInstantPriceService {
    @Autowired
    private HpbInstantPriceMapper hpbInstantPriceMapper;
    @Override
    public HpbInstantPrice queryHpbPrice(String coinSymbol) {
        return  hpbInstantPriceMapper.selectByCoinSymbol(coinSymbol);
    }
}
