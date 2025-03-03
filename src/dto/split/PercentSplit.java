package dto.split;

public final class PercentSplit extends Split {
  private double percent;
  public PercentSplit(String userId, Double percent){
    super(userId);
    this.percent = percent;
  }

  public double getPercent() {
    return percent;
  }

  public void setPercent(double percent) {
    this.percent = percent;
  }

}
