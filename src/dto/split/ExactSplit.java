package dto.split;

public non-sealed class ExactSplit extends Split{

  public ExactSplit(String userId, Double amount){
    super(userId);
    setAmount(amount);
  }

}
