package dto.split;

public sealed abstract class Split permits ExactSplit, EqualSplit, PercentSplit {
  private String userId;
  private Double amount;
  public Split(String userId) {
    this.userId = userId;
  }


  public String getUserId() {
    return userId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "Split{" +
      "userId='" + userId + '\'' +
      ", amount=" + amount +
      '}';
  }

}
