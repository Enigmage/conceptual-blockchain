package einschain;

import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Base64;

public class StringUtil {
  public static String getFromKey(PublicKey key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
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
