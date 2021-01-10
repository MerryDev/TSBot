package de.digitaldevs.bot.config;

import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.Var;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConfigProperties {

  private FileOutputStream outputStream = null;
  private FileInputStream inputStream = null;
  private File folder = new File(Var.APPLICATION_PATH + "/config/");
  private File file = new File(Var.APPLICATION_PATH + "/config/config.properties");

  public void create() {
    try {
      if (!folder.exists()) {
        folder.mkdirs();
      }
      if (!file.exists()) {
        file.createNewFile();
        outputStream = new FileOutputStream(file);
        Properties properties = new Properties();
        properties.setProperty("host", "localhost");
        properties.setProperty("port", "9987");
        properties.setProperty("server_id", "1");
        properties.setProperty("query_name", "security");
        properties.setProperty("password", "foo");
        properties.setProperty("teamrank_id", "33");
        properties.setProperty("nickname", "Security Bot");
        properties.store(outputStream, null);
        outputStream.close();
      }
    } catch (Exception exception) {
      SecurityBot.getLOGGER().warning(exception.getMessage());
    }
  }

  public void load() {
    try {
      if (file.exists()) {
        inputStream = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(inputStream);
        Var.host = properties.getProperty("host");
        Var.port = properties.getProperty("port");
        Var.serverID = properties.getProperty("server_id");
        Var.queryName = properties.getProperty("query_name");
        Var.password = properties.getProperty("password");
        Var.teamrankID = properties.getProperty("teamrank_id");
        Var.nickname = properties.getProperty("nickname");
        inputStream.close();
      }
    } catch (Exception exception) {
      SecurityBot.getLOGGER().warning(exception.getMessage());
    }
  }
}
