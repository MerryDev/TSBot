package de.digitaldevs.bot.utils;

import lombok.Getter;

public enum Color {

  RED("[color=red]", "[/color]"),
  GREEN("[color=green]", "[/color]")
  ;

  @Getter private final String NAME;
  @Getter private final String END;

  Color(final String NAME, final String END) {
    this.NAME = NAME;
    this.END = END;
  }
}
