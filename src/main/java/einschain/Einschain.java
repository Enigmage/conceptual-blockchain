package einschain;

import com.google.gson.GsonBuilder;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Einschain {
  public static ArrayList<Block> blockchain = new ArrayList<Block>();
  public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
  public static int difficulty = 5;
  public static float minimumTransaction = 0.1f;
  public static Wallet walletA;
  public static Wallet walletB;

  public static Boolean isChainValid() {
    Block curr, prev;
    final String hashTarget = "0".repeat(difficulty); // Java 11+
    for (int i = 1; i < blockchain.size(); ++i) {
      curr = blockchain.get(i);
      prev = blockchain.get(i - 1);

      if (!curr.hash.equals(curr.calculateHash()))
        return false;
      if (!curr.previousHash.equals(prev.hash))
        return false;
      if (!curr.hash.substring(0, difficulty).equals(hashTarget))
        return false;
    }
    return true;
  }

  public static String getJson() {
    final String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
    return blockchainJson;
  }

  public static void main(String args[]) {
    Security.addProvider(new BouncyCastleProvider());
    blockchain.add(new Block("Genesis block", "0"));
    System.out.println("Mining block 1");
    blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
    blockchain.add(new Block("Second block", blockchain.get(blockchain.size() - 1).hash));
    System.out.println("Mining block 2");
    blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
    blockchain.add(new Block("Third block", blockchain.get(blockchain.size() - 1).hash));
    System.out.println("Mining block 3");
    blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
    System.out.println(isChainValid());
    walletA = new Wallet();
    walletB = new Wallet();
    // Test public and private keys
    System.out.println("Private and public keys:");
    System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
    System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
    // Create a test transaction from WalletA to walletB
    final Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
    transaction.generateSignature(walletA.privateKey);
    // Verify the signature works and verify it from the public key
    System.out.println(
        "Signature is " + (transaction.verifySignature() ? "verified" : "not verified"));
  }
}
