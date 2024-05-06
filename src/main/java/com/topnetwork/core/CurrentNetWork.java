package com.topnetwork.core;

public class CurrentNetWork {
    public static final String Mainnet = "Mainnet";
    public static final String Rinkeby = "Rinkeby";

    /**
     * Obtain the network node of the current eth
     * @return
     */
    public static String getEthCurrentNetWork(){
        return Mainnet;
    }

    /**
     * Gets the network node of the current UTXO
     * @return
     */
    public  static String getUTXOCurrentNetWork(){
        return Mainnet;
    }

}
