package de.digitaldevs.bot.cryptic;

import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.Var;
import de.digitaldevs.bot.mysql.MySQL;
import java.sql.ResultSet;

public class UserManager {

  public static boolean userExists(String username) {
    ResultSet resultSet =
        MySQL.getResult("SELECT * FROM teamspeak_bot.user WHERE username='" + username + "';");
    try {
      while (resultSet.next()) {
        return true;
      }
    } catch (Exception exception) {
      SecurityBot.getLOGGER()
          .severe(
              "Ein Fehler bei der Interaktion mit der Datenbank ist aufgetreten! "
                  + exception.getMessage());
    }
    return false;
  }

  public static boolean isLoggedIn(int clientID) {
    return Var.users.get(clientID);
  }

  public static void login(int clientID) {
    Var.users.replace(clientID, true);
  }

  public static void logout(int clientID) {
    Var.users.replace(clientID, false);
  }

}
