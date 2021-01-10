package de.digitaldevs.bot;

import java.util.HashMap;

public class Var {

  public static final String APPLICATION_PATH = System.getProperty("user.dir");

  public static String host, port, serverID, queryName, password, teamrankID, nickname;

  public static HashMap<Integer, Boolean> users = new HashMap<>();

}
