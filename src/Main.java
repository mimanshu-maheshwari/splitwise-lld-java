import dto.User;
import exception.CommandException;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    var userService = UserService.getInstance();
    userService.addUser(new User("u1", "User1", "john.doe@host.com", "9876543210"));
    userService.addUser(new User("u2", "User2", "jane.doe@host.com", "9876543211"));
    userService.addUser(new User("u3", "User3", "johny.doe@host.com", "9876543212"));
    userService.addUser(new User("u4", "User4", "jinny.doe@host.com", "9876543213"));
    try (var inputStream = Main.class.getClassLoader().getResourceAsStream("input.txt")) {
      assert inputStream != null;
      try (var br = new BufferedReader(new InputStreamReader(inputStream))) {
        var command = "";
        while (null != (command = br.readLine()) && !command.isEmpty()) {
          System.out.println("[+]: " + command);
          userService.processCommand(command);
          System.out.println();
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "input stream failed %s", e.getMessage());
    } catch (CommandException e) {
      LOGGER.log(Level.WARNING, e.getMessage());
    }
  }

}