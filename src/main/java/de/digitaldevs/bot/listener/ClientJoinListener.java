package de.digitaldevs.bot.listener;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.Var;
import de.digitaldevs.bot.utils.BBCode;
import lombok.Data;

@Data
public class ClientJoinListener {

  private static final TS3Api API = SecurityBot.getAPI();

  public void addListener() {
    API.addTS3Listeners(
        new TS3EventAdapter() {
          @Override
          public void onClientJoin(ClientJoinEvent e) {
            API.sendPrivateMessage(
                e.getClientId(),
                "Hey "
                    + BBCode.BOLD.getOPENING_TAG()
                    + e.getClientNickname()
                    + BBCode.BOLD.getCLOSING_TAG()
                    + "!"
                    + " Bitte gebe dein persönliches Passwort ein, um deine Rechte zu bekommen!");
            Var.users.put(e.getClientId(), false);
            Var.names.put(e.getClientId(), e.getClientNickname());
            Var.dbIDs.put(e.getClientId(), e.getClientDatabaseId());
          }
        });
  }
}
