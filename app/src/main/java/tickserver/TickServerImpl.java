package tickserver;

import io.grpc.stub.StreamObserver;
import java.time.Instant;
import java.util.Random;
import tickserver.TickerSimulatorServiceGrpc.TickerSimulatorServiceImplBase;

public class TickServerImpl extends TickerSimulatorServiceImplBase {

  private final Random random = new Random();

  @Override
  public void startTicker(tickserver.TickerSimulatorRequest request, StreamObserver<tickserver.TickerSimulatorResponse> responseObserver) {
    try {
      if (request.getActive()) {
        for (var i = 0; i < 10000; i++) {
          tickserver.TickerSimulatorResponse response =
              tickserver.TickerSimulatorResponse.newBuilder()
                  .setPrice(random.nextInt(200) + 100)
                  .setSymbol(Integer.toString(random.nextInt('Z' - 'A') + 'A'))
                  .setVolume(random.nextInt(400) + 100)
                  .setTs(Instant.now().getEpochSecond())
                  .build();

          responseObserver.onNext(response);
          Thread.sleep(10L);
       }
       responseObserver.onCompleted();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    } finally{
      responseObserver.onCompleted();
    }
  }
}
