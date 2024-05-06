package com.topnetwork.core.utils;

import com.google.common.collect.ImmutableList;

import org.bitcoinj.crypto.ChildNumber;

import java.util.ArrayList;
import java.util.List;

public class BIP44Util {
    // m / purpose'/ coin_type'/ account'/ change / address_index
    //public static final String ETH_PATH = "m/44'/60'/0'/0/0";

    public static ImmutableList<ChildNumber> generatePath(String path) {
        List<ChildNumber> list = new ArrayList<>();
        for (String p : path.split("/")) {
            if ("m".equalsIgnoreCase(p) || "".equals(p.trim())) {
                continue;
            } else if (p.charAt(p.length() - 1) == '\'') {
                list.add(new ChildNumber(Integer.parseInt(p.substring(0, p.length() - 1)), true));
            } else {
                list.add(new ChildNumber(Integer.parseInt(p), false));
            }
        }

        ImmutableList.Builder<ChildNumber> builder = ImmutableList.builder();
        return builder.addAll(list).build();
    }

}
