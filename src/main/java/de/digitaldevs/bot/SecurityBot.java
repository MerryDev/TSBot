package de.digitaldevs.bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import de.digitaldevs.bot.config.ConfigProperties;
import de.digitaldevs.bot.config.MySQLProperties;
import de.digitaldevs.bot.listener.ClientJoinListener;
import de.digitaldevs.bot.mysql.MySQL;
import de.digitaldevs.bot.formatter.LogFormatter;
import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import lombok.Getter;

public class SecurityBot {

  @Getter public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  @Getter public static final TS3Config CONFIG = new TS3Config();
  @Getter public static final TS3Query QUERY = new TS3Query(CONFIG);
  @Getter public static final TS3Api API = QUERY.getApi();

  public static void main(String[] args) {
    initLogger();
    initConfigurations();

    MySQL.openConnection();
    if (MySQL.isConnected()) {
      MySQL.initDatabase();

      CONFIG.setHost(Var.host);
      CONFIG.setFloodRate(FloodRate.UNLIMITED);
      CONFIG.setQueryPort(Integer.parseInt(Var.port));
      QUERY.connect();

      API.selectVirtualServerById(Integer.parseInt(Var.serverID));
      API.login(Var.queryName, Var.password);
      API.setNickname(Var.nickname);

      initEvents();

      LOGGER.info("Der Bot ist gestartet!");
    }
  }

  private static void initLogger() {
    LogManager.getLogManager().reset();
    ConsoleHandler consoleHandler = new ConsoleHandler();
    try {
      consoleHandler.setEncoding("UTF-8");
      consoleHandler.setLevel(Level.ALL);
      consoleHandler.setFormatter(new LogFormatter());
      LOGGER.addHandler(consoleHandler);
    } catch (Exception exception) {
      LOGGER.severe("Fehler beim Einrichten der Konsole! " + exception.getMessage());
    }
    try {
      LOGGER.setLevel(Level.ALL);
      File folder = new File(Var.APPLICATION_PATH + "/logs/");
      if (!folder.exists()) {
        folder.mkdir();
      }
      FileHandler fileHandler = new FileHandler(Var.APPLICATION_PATH + "/logs/log_%g.log", true);
      fileHandler.setFormatter(new LogFormatter());
      fileHandler.setLevel(Level.ALL);
      fileHandler.setEncoding("UTF-8");
      LOGGER.addHandler(fileHandler);
    } catch (Exception exception) {
      LOGGER.severe("Logging in eine Datei funktinoiert nicht! " + exception.getMessage());
    }
  }

  private static void initConfigurations() {
    LOGGER.info("Lade Bot...");
    ConfigProperties configProperties = new ConfigProperties();
    configProperties.create();
    configProperties.load();

    MySQLProperties mySQLProperties = new MySQLProperties();
    mySQLProperties.create();
    mySQLProperties.load();
  }

  private static void initEvents() {
    API.registerAllEvents();
    new ClientJoinListener().addListener();
    LOGGER.info("Lade Events...");
  }
}
