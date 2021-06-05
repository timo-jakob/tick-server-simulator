package tickserver;

import java.time.Instant;
import java.util.Random;
import java.util.function.Supplier;

class TickSupplier implements Supplier<Tick> {

  private static final Random random = new Random();
  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz".toUpperCase();

  @Override
  public Tick get() {

    double nextChance;
    int nextVolume;

    nextChance = random.nextDouble();
    nextVolume = nextChance < 0.1 ?
        random.nextInt(100) + 401 :
        random.nextInt(301) + 100;


    return new Tick(
        Instant.now(),
        random.nextInt(201) + 100,
        nextVolume,
        Character.toString(ALPHABET.charAt(random.nextInt(26)))
    );
  }
}
