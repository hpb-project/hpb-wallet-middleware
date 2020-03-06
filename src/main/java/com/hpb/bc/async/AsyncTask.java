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

package com.hpb.bc.async;

import com.hpb.bc.contract.hpb.HpbContractProxy;
import com.hpb.bc.entity.Token721StockDetailInfo;
import com.hpb.bc.example.TxTransferRecordExample;
import com.hpb.bc.token.mapper.TxTransferRecordMapper;
import com.hpb.bc.util.ERC721Util;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;


@Component
public class AsyncTask {
    @Autowired
    private TxTransferRecordMapper txTransferRecordMapper;
    @Autowired
    private ERC721Util erc721Util;

    @Async
    public void queryCountByTokenId(CountDownLatch countDownLatch,
                                    Address address,
                                    int i, String contractAddress, Token721StockDetailInfo token721) {
        Uint256 tokenId1 = erc721Util.rpcTokenOfOwnerByIndex(address, new Uint256(i), contractAddress);
        String tokenURI = erc721Util.tokenURI(tokenId1, contractAddress).toString().trim();
        if (tokenId1 != null) {
            String tokenId = tokenId1.getValue().toString();
            token721.setTokenId(tokenId);
            TxTransferRecordExample example = new TxTransferRecordExample();
            example.createCriteria().andContractAddressEqualTo(contractAddress).andTokenIdEqualTo(Long.valueOf(token721.getTokenId()));
            Long count = txTransferRecordMapper.countByExample(example);
            if (count != null) {
                token721.setCount(count);
            }
        }
        if (tokenURI != null) {
            token721.setTokenURI(tokenURI);
        }
        countDownLatch.countDown();
    }
}
