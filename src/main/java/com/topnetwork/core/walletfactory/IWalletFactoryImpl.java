package com.topnetwork.core.walletfactory;

import com.topnetwork.core.model.Wallet;

//Wallet creation factory
public interface IWalletFactoryImpl {
    Wallet getWallet(byte[] seeds, String password, int index);
}
