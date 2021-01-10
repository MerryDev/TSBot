package de.digitaldevs.bot.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.cryptic.Cryptographer;
import de.digitaldevs.bot.cryptic.PasswordManager;
import de.digitaldevs.bot.cryptic.UserManager;
import de.digitaldevs.bot.utils.BBCode;
import de.digitaldevs.bot.utils.Color;

public class LoginCommand {

  private static final TS3Api API = SecurityBot.getAPI();
  private static final int BOT_ID = API.whoAmI().getId();

  public void register() {
    API.addTS3Listeners(
        new TS3EventAdapter() {
          @Override
          public void onTextMessage(TextMessageEvent e) {
            if (e.getTargetClientId() == BOT_ID) {
              int invokerID = e.getInvokerId();
              String message = e.getMessage();

              if (message.startsWith("!login")) {
                String[] args = message.split(" ");

                if (args.length == 3) {
                  String username = args[1];
                  String password = args[2];

                  if (UserManager.userExists(username)) {

                    Cryptographer cryptographer = new Cryptographer();
                    String passwordFromDatabase = PasswordManager.getPassword(username);
                    String decryptedPassword = cryptographer.decrypt(passwordFromDatabase);

                    if (!UserManager.isLoggedIn(invokerID)) {
                      if (password.equals(decryptedPassword)) {
                        UserManager.login(invokerID);
                        API.sendPrivateMessage(
                            invokerID,
                            Color.GREEN.getNAME()
                                + "Du hast dich erfolgreich eingeloggt!"
                                + Color.GREEN.getEND());
                      } else {
                        API.sendPrivateMessage(
                            invokerID,
                            Color.RED.getNAME()
                                + "Das Passowrt ist nicht korrekt!"
                                + Color.RED.getEND());
                      }
                    } else {
                      API.sendPrivateMessage(
                          invokerID,
                          Color.RED.getNAME() + "Du bist bereits eingeloggt!" + Color.RED.getEND());
                    }

                  } else {
                    API.sendPrivateMessage(
                        invokerID,
                        Color.RED.getNAME()
                            + "Der Benutzer "
                            + BBCode.BOLD.getOPENING_TAG()
                            + username
                            + BBCode.BOLD.getCLOSING_TAG()
                            + " existiert nicht!"
                            + Color.RED.getEND());
                  }

                } else {
                  API.sendPrivateMessage(
                      invokerID,
                      Color.RED.getNAME()
                          + "Bitte benutze "
                          + BBCode.BOLD.getOPENING_TAG()
                          + "!login <Name> <Passwort>"
                          + BBCode.BOLD.getCLOSING_TAG()
                          + "!"
                          + Color.RED.getEND());
                }
              }
            }
          }
        });
  }
}
