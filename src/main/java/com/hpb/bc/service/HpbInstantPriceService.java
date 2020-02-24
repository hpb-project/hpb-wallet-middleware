package com.hpb.bc.service;

import com.hpb.bc.entity.HpbInstantPrice;


public interface HpbInstantPriceService {
    HpbInstantPrice queryHpbPrice(String coinSymbol);
}
