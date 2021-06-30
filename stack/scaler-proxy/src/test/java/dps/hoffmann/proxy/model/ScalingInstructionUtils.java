package dps.hoffmann.proxy.model;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static dps.hoffmann.proxy.utils.TimestampUtils.getDuration;
import static dps.hoffmann.proxy.utils.TimestampUtils.now;

/**
 * Factory generating test objects for unit tests
 */
public class ScalingInstructionUtils {

    public static Iterator<Tupel<Integer, Integer>> createScalePropsIt() {
        return new Iterator<>() {
            private int idx = 0;

            @Override
            public boolean hasNext() {
                return idx < 3;
            }

            @Override
            public Tupel<Integer, Integer> next() {
                Tupel<Integer, Integer> out = null;
                switch (idx++) {
                    case 0:
                        out = new Tupel<>(1, 5);
                        break;
                    case 1:
                        out = new Tupel<>(5, 10);
                        break;
                    case 2:
                        out = new Tupel<>(10, 30);
                        break;
                }
                return out;
            }
        };
    }

//    public static List<ScalingInstruction> createBatch() {
//        List<ScalingInstruction> instr = new LinkedList<>();
//
//        createInstr();
//    }

    public static ScalingInstruction createInstr(
            LogicalService service,
            String batchId,
            int secDelay) {

        Timestamp received = now();
        Timestamp scaled = new Timestamp(received.getTime() + secDelay * 1000L);

        return ScalingInstruction.builder()
                .logicalServiceName(service)
                .scalingBatchId(batchId)
                .containerId(RandomStringUtils.random(10))
                .swarmServiceName("swarmservicename")
                .receivedRequestTimestamp(received)
                .scaleAcknowledgementTimestamp(scaled)
                .build();
    }

    public static AtomicInteger[] genRandomArr(int size) {
        Random r = new Random();
        AtomicInteger[] out = new AtomicInteger[size];
        for (int i = 0; i < size; i++) {
            out[i] = new AtomicInteger(r.nextInt(500));
        }
        return out;
    }

}
