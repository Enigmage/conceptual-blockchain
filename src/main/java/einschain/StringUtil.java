package einschain;

import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class StringUtil {

  public static String getStringFromKey(Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  public static String getFromKey(PublicKey key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  public static byte[] applyECDSASig(PrivateKey pkey, String input) {
    Signature dsa;
    byte[] output = new byte[0];
    try {
      dsa = Signature.getInstance("ECDSA", "BC");
      dsa.initSign(pkey);
      byte[] strBytes = input.getBytes();
      dsa.update(strBytes);
      byte[] finalSign = dsa.sign();
      output = finalSign;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return output;
  }

  public static boolean verifyECDSASig(PublicKey pubKey, String data, byte[] signature) {
    try {
      Signature verify = Signature.getInstance("ECDSA", "BC");
      verify.initVerify(pubKey);
      verify.update(data.getBytes());
      return verify.verify(signature);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String applySha256(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes("UTF-8"));
      // StringBuffer hexString = new StringBuffer();

      // StringBuilder is better for use in a single thread.
      // for multiple threads use StringBuffer
      StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if (hex.length() == 1)
          hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
