package de.digitaldevs.bot.cryptic;

import de.digitaldevs.bot.SecurityBot;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Cryptographer {

  public String encrypt(Password password) {
    try {
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, generateKeySpec());
      byte[] encrypted = cipher.doFinal(password.getVALUE().getBytes());
      BASE64Encoder encoder = new BASE64Encoder();
      return encoder.encode(encrypted);
    } catch (Exception exception) {
      SecurityBot.getLOGGER()
          .severe("Das Passwort konnte nicht verschlüsselt werden! " + exception.getMessage());
    }
    return null;
  }

  public String decrypt(String encrypted) {
    try {
      BASE64Decoder decoder = new BASE64Decoder();
      byte[] decrypted = decoder.decodeBuffer(encrypted);
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, generateKeySpec());
      return new String(cipher.doFinal(decrypted));
    } catch (Exception exception) {
      SecurityBot.getLOGGER()
          .severe("Das Passwort konnte nicht entschlüsselt werden! " + exception.getMessage());
    }
    return null;
  }

  private SecretKeySpec generateKeySpec() {
    byte[] key = "DigitalDevs.de".getBytes(StandardCharsets.UTF_8);
    try {
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      key = sha.digest(key);
      key = Arrays.copyOf(key, 16);
      return new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException exception) {
      SecurityBot.getLOGGER()
          .severe("Es konnte kein Schlüssel generiert werden! " + exception.getMessage());
    }
    return null;
  }
}
