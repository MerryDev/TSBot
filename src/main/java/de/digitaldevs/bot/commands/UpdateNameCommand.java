package de.digitaldevs.bot.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.cryptic.PasswordManager;
import de.digitaldevs.bot.cryptic.UserManager;
import de.digitaldevs.bot.utils.BBCode;
import de.digitaldevs.bot.utils.Color;

public class UpdateNameCommand {

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

              if (message.startsWith("!updatename")) {
                String[] args = message.split(" ");

                if (args.length == 3) {
                  String oldName = args[1];
                  String newName = args[2];

                  if (UserManager.userExists(oldName)) {
                    PasswordManager.updateUsername(oldName, newName);
                    API.sendPrivateMessage(
                        invokerID,
                        Color.GREEN.getNAME()
                            + "Der Benutzer "
                            + BBCode.BOLD.getOPENING_TAG()
                            + Color.DEEP_SKY_BLUE.getNAME()
                            + oldName
                            + Color.DEEP_SKY_BLUE.getEND()
                            + BBCode.BOLD.getCLOSING_TAG()
                            + " wurde in "
                            + Color.ORANGE.getNAME()
                            + BBCode.BOLD.getOPENING_TAG()
                            + newName
                            + BBCode.BOLD.getCLOSING_TAG()
                            + Color.ORANGE.getEND()
                            + " umbenannt!"
                            + Color.GREEN.getEND());

                  } else {
                    API.sendPrivateMessage(
                        invokerID,
                        Color.RED.getNAME()
                            + "Der Benutzer "
                            + BBCode.BOLD.getOPENING_TAG()
                            + oldName
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
                          + "!updatename <Alter Name> <Neuer Name>"
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
