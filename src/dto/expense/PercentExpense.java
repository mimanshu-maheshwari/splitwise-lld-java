package dto.expense;

import dto.split.Split;

import java.util.List;

public final class PercentExpense extends Expense{

  public PercentExpense(String expenseId, String payedByUserId, Double amount, List<Split> splits, ExpenseMetadata expenseMetadata) {
    super(expenseId, payedByUserId, amount, splits, expenseMetadata);
  }

}
