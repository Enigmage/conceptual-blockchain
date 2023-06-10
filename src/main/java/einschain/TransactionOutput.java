package einschain;

import java.security.PublicKey;

public class TransactionOutput {
  public String id;
  public PublicKey reciepient;
  public float amount;
  public String parentTransactionId;

  public TransactionOutput(PublicKey reciepient, float amount, String parentTransactionId) {
    this.reciepient = reciepient;
    this.amount = amount;
    this.parentTransactionId = parentTransactionId;
    this.id = StringUtil.applySha256(
        StringUtil.getStringFromKey(reciepient) + Float.toString(amount) + parentTransactionId);
  }

  public boolean isMyCoin(PublicKey pubKey) {
    return this.reciepient == pubKey;
  }
}
