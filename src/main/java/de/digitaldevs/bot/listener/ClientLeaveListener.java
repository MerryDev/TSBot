package de.digitaldevs.bot.listener;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import de.digitaldevs.bot.SecurityBot;
import de.digitaldevs.bot.cryptic.UserManager;

public class ClientLeaveListener {

  private static final TS3Api API = SecurityBot.getAPI();

  public void addListener() {
    API.addTS3Listeners(new TS3EventAdapter() {
      @Override
      public void onClientLeave(ClientLeaveEvent e) {
        UserManager.logout(e.getClientId());
      }
    });
  }

}
