package repository;

import dto.expense.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {

  private static ExpenseRepository instance;
  private List<Expense> expenses;

  private ExpenseRepository() {
    this.expenses = new ArrayList<>();
  }

  public void addExpense(Expense expense){
    this.expenses.add(expense);
  }

  public int size(){
    return this.expenses.size();
  }

  public static ExpenseRepository getInstance() {
    if (instance == null) {
      instance = new ExpenseRepository();
    }
    return instance;
  }

}
