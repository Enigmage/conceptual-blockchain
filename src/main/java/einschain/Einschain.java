package einschain;

import com.google.gson.GsonBuilder;
import java.util.ArrayList;

public class Einschain {
  public static ArrayList<Block> blockchain = new ArrayList<Block>();
  public static int difficulty = 5;

  public static Boolean isChainValid() {
    Block curr, prev;
    String hashTarget = new String(new char[difficulty]).replace('\0', '0');
    for (int i = 1; i < blockchain.size(); i++) {
      curr = blockchain.get(i);
      prev = blockchain.get(i - 1);

      if (!curr.hash.equals(curr.calculateHash())) return false;
      if (!curr.previousHash.equals(prev.hash)) return false;
      if (!curr.hash.substring(0, difficulty).equals(hashTarget)) return false;
    }
    return true;
  }

  public static void prettyPrint() {
    String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
    System.out.println(blockchainJson);
  }

  public static void main(String args[]) {
    blockchain.add(new Block("Genesis block", "0"));
    System.out.println("Mining block 1");
    blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
    blockchain.add(new Block("Second block", blockchain.get(blockchain.size() - 1).hash));
    System.out.println("Mining block 2");
    blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
    blockchain.add(new Block("Third block", blockchain.get(blockchain.size() - 1).hash));
    System.out.println("Mining block 3");
    blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
    // String blockchainJson = new
    // GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
    prettyPrint();
  }
}
