package tickserver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;

class TickSupplierTest {

  @RepeatedTest(100000)
  void generateNewTick() {
    var tick = new TickSupplier().get();

    assertTrue(tick.price() >= 100 && tick.price() <= 300);
    assertEquals(1, tick.symbol().length());
    assertTrue(tick.timeStamp().getEpochSecond() > 0);
    assertTrue(tick.volume() >= 100 && tick.volume() <= 500);
  }
}