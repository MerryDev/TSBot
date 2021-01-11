package de.digitaldevs.bot.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.Var;
import de.digitaldevs.bot.cryptic.Cryptographer;
import de.digitaldevs.bot.cryptic.Password;
import de.digitaldevs.bot.cryptic.PasswordManager;
import de.digitaldevs.bot.cryptic.UserManager;
import de.digitaldevs.bot.utils.BBCode;
import de.digitaldevs.bot.utils.Color;

public class AddUserCommand {

  private static final TS3Api API = SecurityBot.getAPI();
  private static final int BOT_ID = API.whoAmI().getId();

  public void register() {
    API.addTS3Listeners(
        new TS3EventAdapter() {
          @Override
          public void onTextMessage(TextMessageEvent e) {
            int invokerID = e.getInvokerId();

            if (e.getTargetClientId() == BOT_ID) {
              String message = e.getMessage();

              if (message.startsWith("!adduser")) {

                Client client = API.getClientByUId(e.getInvokerUniqueId());

                if (client.isInServerGroup(Integer.parseInt(Var.teamrankID))) {
                  String[] args = message.split(" ");
                  if (args.length == 3) {
                    String username = args[1];
                    String password = args[2];

                    if (!UserManager.userExists(username)) {
                      Cryptographer cryptographer = new Cryptographer();
                      String encryptedPassword =
                          cryptographer.encrypt(new Password(username, password));

                      PasswordManager.createUser(username, encryptedPassword);
                      API.sendPrivateMessage(
                          invokerID,
                          Color.GREEN.getNAME()
                              + "Du hast den Benutzer "
                              + BBCode.BOLD.getOPENING_TAG()
                              + Color.DEEP_SKY_BLUE.getNAME()
                              + username
                              + Color.DEEP_SKY_BLUE.getEND()
                              + BBCode.BOLD.getCLOSING_TAG()
                              + " erfolgreich angelegt!"
                              + Color.GREEN.getEND());

                      SecurityBot.getLOGGER()
                          .info(
                              e.getInvokerName() + " hat den Benutzer " + username + " angelegt.");

                    } else {
                      API.sendPrivateMessage(
                          invokerID,
                          Color.RED.getNAME()
                              + "Der Nutzername "
                              + BBCode.BOLD.getOPENING_TAG()
                              + username
                              + BBCode.BOLD.getCLOSING_TAG()
                              + " ist bereits vergeben!"
                              + Color.RED.getEND());
                    }

                  } else {
                    API.sendPrivateMessage(
                        invokerID,
                        Color.RED.getNAME()
                            + "Bitte benutze "
                            + BBCode.BOLD.getOPENING_TAG()
                            + "!adduser <Name> <Passwort>"
                            + BBCode.BOLD.getCLOSING_TAG()
                            + "!"
                            + Color.RED.getEND());
                  }
                } else {
                  SecurityBot.getLOGGER()
                      .warning(
                          e.getInvokerName()
                              + " hat versucht einen Nutzer zu erstellen, besitzt aber nicht die nötigen Rechte!");
                  API.sendPrivateMessage(
                      invokerID,
                      Color.RED.getNAME()
                          + " Du musst ein Teammitgleid sein, um diesen Befehl nutzen zu können!"
                          + Color.RED.getEND());
                }
              }
            }
          }
        });
  }
}
