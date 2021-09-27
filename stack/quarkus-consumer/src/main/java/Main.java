import de.dps.quarkusconsumer.model.ContainerInfo;
import de.dps.quarkusconsumer.service.StartupAckService;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@QuarkusMain
public class Main {


    public static void main(String[] args) {
        Quarkus.run(IdMain.class, args);
    }

//    @AllArgsConstructor
    public static class IdMain implements QuarkusApplication {

//        @Inject
        private StartupAckService ackService;

        public IdMain(StartupAckService ackService) {
            this.ackService = ackService;
        }

        /**
         * rest call will be executed after Beans initialized
         * source (9:35): https://www.youtube.com/watch?v=FXzlgM162Zs
         */
        @Override
//        @Outgoing("ack-requests")
        public int run(String... args) throws Exception {
            System.out.println("before ack");
//            CompletableFuture<Void>.runAsync(() -> ackService.acknowledgeStartup());
//            CompletableFuture<Void>.runAsync(() -> System.out.println("test"));
            ackService.acknowledgeStartup();
            Quarkus.waitForExit();
//            Quarkus.blockingExit();
//            ackService.acknowledgeStartup();
            System.out.println("after ack");
            return 0;
        }
    }
}
