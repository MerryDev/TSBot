package de.digitaldevs.bot.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import de.digitaldevs.bot.SecurityBot;
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
                            + username
                            + BBCode.BOLD.getCLOSING_TAG()
                            + " wurde gelöscht!"
                            + Color.GREEN.getEND());

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
                          + "!removeuser <Name>"
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
