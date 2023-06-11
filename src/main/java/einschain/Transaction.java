package einschain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
  public static int transactionCount = 0;
  public String transactionId;
  public PublicKey sender;
  public PublicKey receiver;
  public float amount;

  public byte[] signature;
  public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

  public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

  public Transaction(
      PublicKey from, PublicKey to, float amount, ArrayList<TransactionInput> inputs) {
    this.sender = from;
    this.receiver = to;
    this.amount = amount;
    this.inputs = inputs;
  }

  public boolean processTransaction() {
    if (verifySignature() == false) {
      System.out.println("#Signature invalid");
      return false;
    }
    for (TransactionInput i : inputs)
      i.UTXO = Einschain.UTXOs.get(i.transactionOutputId);
    return true;
  }

  private String computeHash() {
    transactionCount++;
    return StringUtil.applySha256(
        StringUtil.getFromKey(sender)
            + StringUtil.getFromKey(receiver)
            + Float.toString(amount)
            + transactionCount);
  }

  public void generateSignature(PrivateKey privKey) {
    String data = StringUtil.getFromKey(sender) + StringUtil.getFromKey(receiver) + Float.toString(amount);
    signature = StringUtil.applyECDSASig(privKey, data);
  }

  public boolean verifySignature() {
    String data = StringUtil.getFromKey(sender) + StringUtil.getFromKey(receiver) + Float.toString(amount);
    return StringUtil.verifyECDSASig(sender, data, signature);
  }
}
