package repository;

import java.util.HashMap;
import java.util.Map;

public class BalanceSheetRepository {

  private static BalanceSheetRepository instance;

  private Map<String, Map<String, Double>> balanceSheet;

  private BalanceSheetRepository() {
    this.balanceSheet = new HashMap<>();
  }

  public Map<String, Double> getBalanceForUserId(String userId){
    return this.balanceSheet.get(userId);
  }

  public void addUser(String id) {
    this.balanceSheet.put(id, new HashMap<>());
  }

  public static BalanceSheetRepository getInstance() {
    if (instance == null) {
      instance = new BalanceSheetRepository();
    }
    return instance;
  }

}
