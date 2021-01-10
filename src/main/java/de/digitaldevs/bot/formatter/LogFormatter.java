package de.digitaldevs.bot.formatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

  @Override
  public String format(LogRecord record) {
    Calendar calendar = new GregorianCalendar();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    return "[" + simpleDateFormat.format(calendar.getTime()) + "] "
        + record.getLevel() + ": "
        + record.getMessage() + "\n";
  }
}
