package repository;

import dto.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserRepository {

  private static UserRepository instance;

  private Map<String, User> users;

  private UserRepository() {
    users = new HashMap<>();
  }

  public void addUser(User user) {
    this.users.put(user.userId(), user);
  }

  public Set<String> getAllUserIds(){
    return this.users.keySet();
  }

  public User getUserById(String id) {
    return this.users.get(id);
  }

  public List<User> getUserByName(String name) {
    return this.users.values().stream().filter(u -> u.name().equals(name)).toList();
  }

  public static UserRepository getInstance() {
    if (UserRepository.instance == null) {
      UserRepository.instance = new UserRepository();
    }
    return UserRepository.instance;
  }

}
