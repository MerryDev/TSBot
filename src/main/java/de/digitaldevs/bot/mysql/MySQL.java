package de.digitaldevs.bot.mysql;

import de.digitaldevs.bot.SecurityBot;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.Getter;

public class MySQL {

  @Getter public static String host, port, username, password;
  @Getter private static Connection connection;

  public static synchronized void openConnection() {
    if (!isConnected()) {
      try {
        connection =
            DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/test" + "?autoReconnect=true&useSSL=false",
                username,
                password);
        SecurityBot.getLOGGER().info("Verbindugn zur Datenbank erfolgreich hergestellt!");
      } catch (Exception exception) {
        exception.printStackTrace();
        SecurityBot.getLOGGER()
            .severe(
                "Die Verbindung zur Datenbank konnte nicht hergestellt werden! "
                    + exception.getMessage());
      }
    }
  }

  public static synchronized void initDatabase() {
    if (isConnected()) {
      try {
        connection
            .createStatement()
            .executeUpdate(
                "CREATE DATABASE IF NOT EXISTS teamspeak_bot CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI;;");

        connection
            .createStatement()
            .executeUpdate(
                "CREATE TABLE IF NOT EXISTS teamspeak_bot.user\n"
                    + "(\n"
                    + "    ID       INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,\n"
                    + "    username VARCHAR(255) NOT NULL UNIQUE,\n"
                    + "    password VARCHAR(255) NOT NULL\n"
                    + ") ENGINE = INNODB;");
      } catch (Exception exception) {
        SecurityBot.getLOGGER()
            .severe(
                "Ein Fehler bei der Interaktion mit der Datenbank ist aufgetreten! "
                    + exception.getMessage());
      }
    }
  }

  public static synchronized void update(String query) {
    if (isConnected()) {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
      } catch (Exception exception) {
        SecurityBot.getLOGGER()
            .severe(
                "Ein Fehler bei der Interaktion mit der Datenbank ist aufgetreten! "
                    + exception.getMessage());
      }
    }
  }

  public static synchronized ResultSet getResult(String query) {
    if (isConnected()) {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
      } catch (Exception exception) {
        SecurityBot.getLOGGER()
            .severe(
                "Ein Fehler bei der Interaktion mit der Datenbank ist aufgetreten! "
                    + exception.getMessage());
      }
    }
    return null;
  }

  public static boolean isConnected() {
    return connection != null;
  }
}
