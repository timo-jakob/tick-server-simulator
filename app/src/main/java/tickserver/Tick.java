package tickserver;

import java.time.Instant;

public record Tick(Instant timeStamp, Integer price, Integer volume, String symbol) { }
