package de.digitaldevs.bot.cryptic;

import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.mysql.MySQL;
import java.sql.ResultSet;

public class PasswordManager {

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

  public static void createUser(String username, String password) {
    MySQL.update(
        "INSERT INTO teamspeak_bot.user (username, password) VALUES ('"
            + username
            + "', '"
            + password
            + "');");
  }

  public static void deleteUser(String username) {
    MySQL.update("DELETE FROM teamspeak_bot.user WHERE username='" + username + "';");
  }

  public static void updateUsername(String currentName, String newName) {
    MySQL.update(
        "UPDATE teamspeak_bot.user SET username='"
            + newName
            + "' WHERE username='"
            + currentName
            + "';");
  }

  public static void updatePassword(String username, String newPassword) {
    MySQL.update(
        "UPDATE teamspeak_bot.user SET password='"
            + newPassword
            + "' WHERE username='"
            + username
            + "';");
  }

  public static Password getPassword(String username) {
    ResultSet resultSet =
        MySQL.getResult(
            "SELECT teamspeak_bot.user.password FROM teamspeak_bot.user WHERE username='"
                + username
                + "';");
    try {
      while (resultSet.next()) {
        return new Password(username, resultSet.getString("password"));
      }
    } catch (Exception exception) {
      SecurityBot.getLOGGER()
          .severe(
              "Ein Fehler bei der Interaktion mit der Datenbank ist aufgetreten! "
                  + exception.getMessage());
    }
    return null;
  }
}
