package de.digitaldevs.bot.config;

import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.Var;
import de.digitaldevs.bot.mysql.MySQL;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class MySQLProperties {

  private FileOutputStream outputStream = null;
  private FileInputStream inputStream = null;
  private File folder = new File(Var.APPLICATION_PATH + "/sql/");
  private File file = new File(Var.APPLICATION_PATH + "/sql/sql.properties");

  public void create() {
    try {
      if (!folder.exists()) {
        folder.mkdirs();
      }
      if (!file.exists()) {
        file.createNewFile();

        Properties properties = new Properties();
        outputStream = new FileOutputStream(file);
        properties.setProperty("host", "localhost");
        properties.setProperty("port", "3306");
        properties.setProperty("username", "root");
        properties.setProperty("password", "foo");
        properties.store(outputStream, null);
        outputStream.close();
      }
    } catch (Exception exception) {
      SecurityBot.LOGGER.severe(
          "Die Konfigurationsdatei für die Datenbank konnte nicht erstellt werden! "
              + exception.getMessage());
    }
  }

  public void load() {
    try {
      Properties properties = new Properties();
      inputStream = new FileInputStream(file);
      properties.load(inputStream);
      MySQL.host = properties.getProperty("host");
      MySQL.port = properties.getProperty("port");
      MySQL.username = properties.getProperty("username");
      MySQL.password = properties.getProperty("password");
      inputStream.close();
    } catch (Exception exception) {
      SecurityBot.LOGGER.severe(
          "Die Konfigurationsdatei für die Datenbank konnte nicht geladen werden! "
              + exception.getMessage());
    }
  }
}
