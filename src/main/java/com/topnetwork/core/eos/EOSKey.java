package com.topnetwork.core.eos;

import com.topnetwork.core.utils.ByteUtil;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

import java.util.Arrays;

public class EOSKey  {

  private final int version = 128;
  protected byte[] bytes;

  private EOSKey(byte[] bytes) {
   this.bytes = bytes;
  }

  public static EOSKey fromPrivate(byte[] prvKey) {
       return new EOSKey(prvKey);
  }

  /**
   * Get the EOS public Key
   * @return
   */
  public String getPublicKeyAsHex() {
    ECKey ecKey = ECKey.fromPrivate(bytes);
    byte[] pubKeyData = ecKey.getPubKey();
    RIPEMD160Digest digest = new RIPEMD160Digest();
    digest.update(pubKeyData, 0, pubKeyData.length);
    byte[] out = new byte[20];
    digest.doFinal(out, 0);
    byte[] checksumBytes = Arrays.copyOfRange(out, 0, 4);
    pubKeyData = ByteUtil.concat(pubKeyData, checksumBytes);
    return "EOS" + Base58.encode(pubKeyData);
  }

  /**
   * Get the EOS private key
   * @return
   */
  public String getPrivateKey(){
    byte[] addressBytes = new byte[1 + bytes.length + 4];
    addressBytes[0] = (byte) version;
    System.arraycopy(bytes, 0, addressBytes, 1, bytes.length);
    byte[] checksum = Sha256Hash.hashTwice(addressBytes, 0, bytes.length + 1);
    System.arraycopy(checksum, 0, addressBytes, bytes.length + 1, 4);
    return Base58.encode(addressBytes);
  }

  public ECKey getECKey() {
    return ECKey.fromPrivate(bytes, true);
  }

}
