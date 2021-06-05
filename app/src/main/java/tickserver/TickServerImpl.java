package tickserver;

import io.grpc.stub.StreamObserver;
import java.util.stream.Stream;
import tickserver.TickerSimulatorServiceGrpc.TickerSimulatorServiceImplBase;

public class TickServerImpl extends TickerSimulatorServiceImplBase {

  @Override
  public void startTicker(tickserver.TickerSimulatorRequest request, StreamObserver<tickserver.TickerSimulatorResponse> responseObserver) {
    try {
      // we take the given chunk size and return the corresponding amount of ticks
      Stream.generate(new TickSupplier())
          .limit(request.getChunkSize())
          .forEach(tick -> {
            tickserver.TickerSimulatorResponse response =
                tickserver.TickerSimulatorResponse.newBuilder()
                    .setPrice(tick.price())
                    .setSymbol(tick.symbol())
                    .setVolume(tick.volume())
                    .setTs(tick.timeStamp().getEpochSecond())
                    .build();

            responseObserver.onNext(response);
          });
    } finally{
      responseObserver.onCompleted();
    }
  }
}
