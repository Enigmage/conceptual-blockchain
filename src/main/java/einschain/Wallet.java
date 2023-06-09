package einschain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
  public PrivateKey privateKey;
  public PublicKey publicKey;

  public Wallet() {
    generateKeyPair();
  }

  public void generateKeyPair() {
    try {
      KeyPairGenerator keygen = KeyPairGenerator.getInstance("ECDSA", "BC");
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      ECGenParameterSpec espec = new ECGenParameterSpec("prime192v1");
      keygen.initialize(espec, random);
      KeyPair keypair = keygen.generateKeyPair();
      privateKey = keypair.getPrivate();
      publicKey = keypair.getPublic();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
