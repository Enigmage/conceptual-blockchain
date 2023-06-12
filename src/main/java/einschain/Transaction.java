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

    if (getInputsValue() < Einschain.minimumTransaction) {
      System.out.println("#Transaction inputs too small " + getInputsValue());
      System.out.println("Please enter amount greater than " + Einschain.minimumTransaction);
      return false;
    }

    float leftOver = getInputsValue() - amount;
    transactionId = computeHash();

    outputs.add(new TransactionOutput(this.receiver, amount, transactionId));
    outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

    for (TransactionOutput o : outputs)
      Einschain.UTXOs.put(o.id, o);

    for (TransactionInput i : inputs) {
      if (i.UTXO != null)
        Einschain.UTXOs.remove(i.UTXO.id);
    }

    return true;
  }

  public float getInputsValue() {
    float total = 0;
    for (TransactionInput i : inputs) {
      if (i.UTXO == null)
        continue;
      total += i.UTXO.amount;
    }
    return total;
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
