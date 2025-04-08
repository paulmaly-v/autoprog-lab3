import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.SenderService;
import services.SendingConfirmationService;
import services.SendingConfirmationServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyConsensusTest {

    static SendingConfirmationService confirmationService;
    static final String RESULTS_FILE = "src/test/resources/my_results_backwards.json";
    static final String LOG_PATH = "src/test/resources/myConsensusTest.log";

    @BeforeAll
    static void init() {
        confirmationService = new SendingConfirmationServiceImpl();
    }

    @Test
    public void testCustomId() {
        boolean consensusReached = confirmationService.sendForConfirmationCustomId(
                new MyCustomSenderService(),
                RESULTS_FILE,
                1121322245);

        assertTrue(consensusReached);
    }

    static class MyCustomSenderService implements SenderService {
        private final Map<String, Double> orgProbabilities = new HashMap<>();
        private final Random random = new Random();

        public MyCustomSenderService() {
            orgProbabilities.put("Org1", 0.95);
            orgProbabilities.put("Org2", 0.92);
            orgProbabilities.put("Org3", 0.88);
            orgProbabilities.put("Org4", 0.90);
        }

        @Override
        public int getReply(String orgName, int transactionId) {
            Double probability = orgProbabilities.getOrDefault(orgName, 0.5);
            
            double randomValue = random.nextDouble();
            
            return randomValue < probability ? 1 : 0;
        }

        @Override
        public int getMaxRequestNum() {
            return 10;
        }

        @Override
        public int getMaxRequestTotalNum() {
            return 50;
        }

        @Override
        public long getTimeoutSec() {
            return 0;
        }

        @Override
        public long getWaitingTimeSec() {
            return 0;
        }

        @Override
        public String getLogPath() {
            return LOG_PATH;
        }
    }
}
