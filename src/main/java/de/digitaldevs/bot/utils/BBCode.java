package de.digitaldevs.bot.utils;

import lombok.Getter;

public enum BBCode {
  BOLD("[B]", "[/B]");

  @Getter private final String OPENING_TAG;
  @Getter private final String CLOSING_TAG;

  BBCode(final String OPENING_TAG, final String CLOSING_TAG) {
    this.OPENING_TAG = OPENING_TAG;
    this.CLOSING_TAG = CLOSING_TAG;
  }
}
