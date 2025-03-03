package dto.expense;

import dto.split.Split;

import java.util.List;

public sealed abstract class Expense permits EqualExpense, ExactExpense, PercentExpense {

  private String expenseId;
  private String payedByUserId;
  private Double amount;
  private List<Split> splits;
  private ExpenseMetadata expenseMetadata;

  public Expense(String expenseId, String payedByUserId, Double amount, List<Split> splits, ExpenseMetadata expenseMetadata) {
    this.expenseId = expenseId;
    this.payedByUserId = payedByUserId;
    this.amount = amount;
    this.splits = splits;
    this.expenseMetadata = expenseMetadata;
  }

  public String getExpenseId() {
    return expenseId;
  }

  public void setExpenseId(String expenseId) {
    this.expenseId = expenseId;
  }

  public String getPayedByUserId() {
    return payedByUserId;
  }

  public void setPayedByUserId(String payedByUserId) {
    this.payedByUserId = payedByUserId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public List<Split> getSplits() {
    return splits;
  }

  public void setSplits(List<Split> splits) {
    this.splits = splits;
  }

  public ExpenseMetadata getExpenseMetadata() {
    return expenseMetadata;
  }

  public void setExpenseMetadata(ExpenseMetadata expenseMetadata) {
    this.expenseMetadata = expenseMetadata;
  }

  @Override
  public String toString() {
    return "Expense{" +
      "expenseId='" + expenseId + '\'' +
      ", payedByUserId='" + payedByUserId + '\'' +
      ", amount=" + amount +
      ", splits=" + splits +
      ", expenseMetadata=" + expenseMetadata +
      '}';
  }

}
