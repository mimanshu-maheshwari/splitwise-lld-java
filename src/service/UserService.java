package service;

import dto.User;
import dto.expense.EqualExpense;
import dto.expense.ExactExpense;
import dto.expense.Expense;
import dto.expense.ExpenseType;
import dto.expense.PercentExpense;
import dto.split.EqualSplit;
import dto.split.ExactSplit;
import dto.split.PercentSplit;
import dto.split.Split;
import exception.CommandException;
import repository.BalanceSheetRepository;
import repository.ExpenseRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static utils.Constants.EXPENSE;
import static utils.Constants.SHOW;

public class UserService {

  private static UserService instance;

  private UserRepository userRepository;

  private ExpenseRepository expenseRepository;

  private BalanceSheetRepository balanceSheetRepository;

  private UserService() {
    this.userRepository = UserRepository.getInstance();
    this.expenseRepository = ExpenseRepository.getInstance();
    this.balanceSheetRepository = BalanceSheetRepository.getInstance();
  }

  public void addUser(User user) {
    this.userRepository.addUser(user);
    this.balanceSheetRepository.addUser(user.userId());
  }

  public void processCommand(String command) throws CommandException {
    // early return for invalid command
    if (null == command || command.trim().isEmpty()) {
      throw new CommandException("Empty Command");
    }
    if (command.startsWith(SHOW)) {
      processShowCommand(command);
    } else if (command.startsWith(EXPENSE)) {
      processExpenseCommand(command);
    } else {
      throw new CommandException("Invalid Command");
    }
  }

  private void processShowCommand(String command) throws CommandException {
    String[] inputs = command.split("\\s+");
    var printStatements = new HashSet<String>();
    if (inputs.length == 1) {
      showDetailsForAllUsers(printStatements);
    } else if (inputs.length == 2) {
      showDetailsForUser(printStatements, inputs[1]);
    } else {
      throw new CommandException("Invalid Show Command");
    }
    printStatements.forEach(System.out::println);
  }

  private void showDetailsForUser(Set<String> printStatements, String userId) {
    var balance = this.balanceSheetRepository.getBalanceForUserId(userId);
    if (balance.isEmpty()) {
      printStatements.add("No balances");
    } else {
      balance.forEach((uid, amount) -> {
        if (amount == 0) {
          return;
        }
        User paidBy, paidTo;
        if (amount > 0) {
          paidBy = getUserById(userId);
          paidTo = getUserById(uid);
        } else {
          paidTo = getUserById(userId);
          paidBy = getUserById(uid);
          amount = -amount;
        }
        printBalance(printStatements, paidBy, paidTo, amount);
      });
    }
  }

  public User getUserById(String userId) {
    return this.userRepository.getUserById(userId);
  }

  private void showDetailsForAllUsers(Set<String> printStatements) {
    var ids = this.userRepository.getAllUserIds();
    if (!ids.isEmpty()) {
      ids.forEach(id -> showDetailsForUser(printStatements, id));
    } else {
      printStatements.add("No balances");
    }
  }

  private void processExpenseCommand(String command) throws CommandException {
    var input = command.split("\\s+");
    if (input.length < 6) {
      throw new CommandException("Invalid expense");
    }
    var paidBy = input[1].trim();
    var paidAmount = Double.parseDouble(input[2].trim());
    int len = Integer.parseInt(input[3].trim());
    var expenseType = ExpenseType.valueOf(input[4 + len].trim().toUpperCase());
    int userPointer = 4, valuePointer = 5 + len;
    var splits = createSplits(input, expenseType, len, userPointer, valuePointer, paidAmount);
    var expense = createExpense(expenseType, this.userRepository.getUserById(paidBy), paidAmount, splits);
    this.expenseRepository.addExpense(expense);
    updateBalanceSheet(paidBy, expense, paidAmount);

  }

  private void updateBalanceSheet(String paidBy, Expense expense, Double paidAmount) {
    var payerBalancesheet = this.balanceSheetRepository.getBalanceForUserId(paidBy);
    for (var split : expense.getSplits()) {
      var paidTo = split.getUserId();
      payerBalancesheet.merge(paidTo, split.getAmount(), Double::sum);
      var loanerBalancesheet = this.balanceSheetRepository.getBalanceForUserId(paidTo);
      loanerBalancesheet.merge(paidBy, -split.getAmount(), Double::sum);
    }
  }

  private List<Split> createSplits(String[] input, ExpenseType expenseType, int len, int userPointer, int valuePointer, double paidAmount) {
    var splits = new ArrayList<Split>();
    for (int i = 0; i < len; ++i) {
      switch (expenseType) {
        case EQUAL -> splits.add(new EqualSplit(input[userPointer + i].trim(), paidAmount / len));
        case PERCENT ->
          splits.add(new PercentSplit(input[userPointer + i].trim(), Double.parseDouble(input[valuePointer + i])));
        case EXACT ->
          splits.add(new ExactSplit(input[userPointer + i].trim(), Double.parseDouble(input[valuePointer + i].trim())));
      }
    }
    return splits;
  }

  private Expense createExpense(ExpenseType type, User paidBy, Double paidAmount, List<Split> splits) throws CommandException {
    return switch (type) {
      case EXACT ->
        new ExactExpense("EXACT-" + this.expenseRepository.size() + 1, paidBy.userId(), paidAmount, splits, null);
      case EQUAL ->
        new EqualExpense("EQUAL-" + this.expenseRepository.size() + 1, paidBy.userId(), paidAmount, splits, null);
      case PERCENT -> {
        if (!validatePercentSplit(splits)) {
          throw new CommandException("Invalid percentage sum");
        }
        for (var split : splits) {
          PercentSplit percentSplit = (PercentSplit) split;
          split.setAmount(paidAmount * percentSplit.getPercent() / 100.00);
        }
        yield new PercentExpense("PERCENT-" + this.expenseRepository.size() + 1, paidBy.userId(), paidAmount, splits, null);
      }
    };
  }

  private boolean validatePercentSplit(List<Split> splits) {
    double percentage = 0.0;
    for (var split : splits) {
      var percentSplit = (PercentSplit) split;
      percentage += percentSplit.getPercent();
    }
    return ((percentage * 100.00) / 100.00) == 100.00;
  }

  private void printBalance(Set<String> printStatements, User paidBy, User paidTo, Double amount) {
    printStatements.add(String.format("%s owes %s: %.2f", paidTo.name(), paidBy.name(), amount));
  }

  public static UserService getInstance() {
    if (UserService.instance == null) {
      UserService.instance = new UserService();
    }
    return UserService.instance;
  }

}
