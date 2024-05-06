package com.topnetwork.core.netparams;

import com.topnetwork.core.helper.CoinEnum;

import org.bitcoinj.params.AbstractBitcoinNetParams;
import org.bitcoinj.params.MainNetParams;

public class NetParamsService {

    /**
     * The signature concatenation transaction object uses the current network version
     *
     * @param chinType
     * @return
     */
    public static AbstractBitcoinNetParams getNetworkParameters(String chinType) {
        CoinEnum coinEnum = CoinEnum.getCoinEnum(chinType);
        if (coinEnum != null) {
            return coinEnum.getNetworkParameters();
        }
        // Should do processing, this is not reasonable above CoinEnum must not be null
        return MainNetParams.get();
    }

}
