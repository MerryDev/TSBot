package de.digitaldevs.bot.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.Var;
import de.digitaldevs.bot.cryptic.PasswordManager;
import de.digitaldevs.bot.cryptic.UserManager;
import de.digitaldevs.bot.utils.BBCode;
import de.digitaldevs.bot.utils.Color;

public class RemoveUserCommand {

  private static final TS3Api API = SecurityBot.getAPI();
  private static final int BOT_ID = API.whoAmI().getId();

  public void register() {
    API.addTS3Listeners(
        new TS3EventAdapter() {
          @Override
          public void onTextMessage(TextMessageEvent e) {
            if (e.getTargetClientId() == BOT_ID) {
              String message = e.getMessage();
              int invokerID = e.getInvokerId();

              if (message.startsWith("!removeuser")) {

                Client client = API.getClientByUId(e.getInvokerUniqueId());

                if(client.isInServerGroup(Integer.parseInt(Var.teamrankID))) {
                  String[] args = message.split(" ");

                  if (args.length == 2) {
                    String username = args[1];

                    if (UserManager.userExists(username)) {
                      PasswordManager.deleteUser(username);
                      API.sendPrivateMessage(
                          invokerID,
                          Color.GREEN.getNAME()
                              + "Der Benutzer "
                              + BBCode.BOLD.getOPENING_TAG()
                              + Color.DEEP_SKY_BLUE.getNAME()
                              + username
                              + Color.DEEP_SKY_BLUE.getEND()
                              + BBCode.BOLD.getCLOSING_TAG()
                              + " wurde gelöscht!"
                              + Color.GREEN.getEND());

                      SecurityBot.getLOGGER()
                          .info(e.getInvokerName() + " hat den Benutzer " + username + " gelöscht.");

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

                      SecurityBot.getLOGGER()
                          .warning(
                              e.getInvokerName()
                                  + " wollte einen nicht existierenden Benutzer löschen.");
                    }

                  } else {
                    API.sendPrivateMessage(
                        invokerID,
                        Color.RED.getNAME()
                            + "Bitte benutze "
                            + BBCode.BOLD.getOPENING_TAG()
                            + "!removeuser <Name>"
                            + BBCode.BOLD.getCLOSING_TAG()
                            + "!"
                            + Color.RED.getEND());
                  }

                }else {
                  SecurityBot.getLOGGER()
                      .warning(
                          e.getInvokerName()
                              + " hat versucht einen Nutzer zu löschen, besitzt aber nicht die nötigen Rechte!");
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
