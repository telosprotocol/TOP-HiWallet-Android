package com.topnetwork.core.model;

import com.topnetwork.core.helper.CoinEnum;

import wallet.core.jni.FilecoinAddressConverter;

public class WalletExtend {
    public static String requestAddress(Wallet wallet) {
        if (wallet.symbol.equals(CoinEnum.FIL_EVM.getSymbolName())) {
            //Convert to f4
            return FilecoinAddressConverter.convertFromEthereum(wallet.address);
        }
        return wallet.address;
    }

    public static String uiAddress(Wallet wallet) {
        return wallet.address;
    }

    public static String requestSymbol(Wallet wallet) {
        if (wallet.symbol.equals(CoinEnum.FIL_EVM.getSymbolName())) {
            return CoinEnum.FIL.getSymbolName();
        }
        return wallet.symbol;
    }

    public static String uiSymbol(Wallet wallet) {
        if (wallet.symbol.equals(CoinEnum.FIL_EVM.getSymbolName())) {
            return CoinEnum.FIL.getSymbolName();
        }
        return wallet.symbol;
    }

    public static String requestFullName(Wallet wallet) {
        if (wallet.symbol.equals(CoinEnum.FIL_EVM.getSymbolName())) {
            return CoinEnum.FIL.getFullName();
        }
        return wallet.fullName;
    }

    public static String uiFullName(Wallet wallet) {
        return wallet.fullName;
    }

    public static String requestChainType(Wallet wallet) {
        if (wallet.symbol.equals(CoinEnum.FIL_EVM.getSymbolName())) {
            return CoinEnum.FIL.getSymbolName();
        }
        return wallet.chainType;
    }

    public static String uiChainType(Wallet wallet) {
        if (wallet.symbol.equals(CoinEnum.FIL_EVM.getSymbolName())) {
            return CoinEnum.FIL.getSymbolName();
        }
        return wallet.chainType;
    }

    public static String chainType(Wallet wallet) {
        return wallet.chainType;
    }
}
