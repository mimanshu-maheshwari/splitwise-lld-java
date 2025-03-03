package dto.split;

public final class EqualSplit extends Split {

  public EqualSplit(String userId, double amount) {
    super(userId);
    setAmount(amount);
  }

}
