import de.dps.quarkusconsumer.service.StartupAckService;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {


    public static void main(String[] args) {
        Quarkus.run(IdMain.class, args);
    }

    public static class IdMain implements QuarkusApplication {

        private StartupAckService ackService;

        public IdMain(StartupAckService ackService) {
            this.ackService = ackService;
        }

        /**
         * rest call will be executed after Beans initialized
         * source (9:35): https://www.youtube.com/watch?v=FXzlgM162Zs
         */
        @Override
        public int run(String... args) {
            ackService.acknowledgeStartup();
            Quarkus.waitForExit();
            return 0;
        }
    }
}
