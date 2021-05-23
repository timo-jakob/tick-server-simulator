package tickserver;

import io.grpc.stub.StreamObserver;
import java.util.logging.Logger;
import tickserver.TickerSimulatorServiceGrpc.TickerSimulatorServiceImplBase;

public class TickServerImpl extends TickerSimulatorServiceImplBase {

  private final Logger logger = Logger.getLogger(TickServerImpl.class.getName());

  @Override
  public void startTicker(tickserver.TickerSimulatorRequest request, StreamObserver<tickserver.TickerSimulatorResponse> responseObserver) {
    try {
      if (request.getActive()) {
        for (var i = 0; i < 10000; i++) {

          var tick = TickFactory.generateNewTick();

          tickserver.TickerSimulatorResponse response =
              tickserver.TickerSimulatorResponse.newBuilder()
                  .setPrice(tick.price())
                  .setSymbol(tick.symbol())
                  .setVolume(tick.volume())
                  .setTs(tick.timeStamp().getEpochSecond())
                  .build();

          responseObserver.onNext(response);
          Thread.sleep(10L);
       }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    } finally{
      responseObserver.onCompleted();
      logger.info("Stream ended");
    }
  }
}
