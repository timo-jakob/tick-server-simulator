package tickserver;

import java.time.Instant;
import java.util.Random;

public class TickFactory {

  private static final Random random = new Random();
  private static final String alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase();

  public static Tick generateNewTick() {
    double nextChance;
    int nextVolume;

    nextChance = random.nextDouble();
    if (nextChance < 0.1) {
      nextVolume = random.nextInt(100) + 401;
    } else {
      nextVolume = random.nextInt(301) + 100;
    }

    return new Tick(
        Instant.now(),
        random.nextInt(201) + 100,
        nextVolume,
        Character.toString(alphabet.charAt(random.nextInt(26)))
    );
  }
}
