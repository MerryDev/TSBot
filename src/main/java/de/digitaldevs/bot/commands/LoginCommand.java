package de.digitaldevs.bot.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.Var;
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

                        Client client = API.getClientByNameExact(username, true);
                        if (!client.isInServerGroup(Integer.parseInt(Var.teamrankID))) {
                          API.addClientToServerGroup(
                              Integer.parseInt(Var.teamrankID), client.getDatabaseId());

                          UserManager.login(invokerID);

                          SecurityBot.getLOGGER()
                              .info(
                                  client.getNickname()
                                      + " hat sich mit dem Benutzer "
                                      + username
                                      + " eingeloggt.");
                          API.sendPrivateMessage(
                              invokerID,
                              Color.GREEN.getNAME()
                                  + "Du hast dich erfolgreich eingeloggt!"
                                  + Color.GREEN.getEND());

                        } else {
                          SecurityBot.getLOGGER()
                              .severe(
                                  client.getNickname()
                                      + " versuchte sich einzuloggen. Die Rechte konnten vorher nicht entfernt werden!");
                          API.sendPrivateMessage(
                              invokerID,
                              Color.RED.getNAME()
                                  + "Du hast bereits deine Rechte!"
                                  + Color.RED.getEND());
                        }

                      } else {
                        SecurityBot.getLOGGER()
                            .warning(
                                e.getInvokerName()
                                    + " hat mit dem Benutzer "
                                    + username
                                    + " das falsche Passwort eingegeben!");
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
