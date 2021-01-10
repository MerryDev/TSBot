package de.digitaldevs.bot.cryptic;

import lombok.Data;
import lombok.Getter;

@Data
public class Password {

  @Getter private final String OWNER;
  @Getter private final String VALUE;
}
