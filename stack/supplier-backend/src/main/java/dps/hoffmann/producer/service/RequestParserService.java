package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.error.InvalidParserRequestException;
import dps.hoffmann.producer.model.instruction.BatchVisitor;
import dps.hoffmann.producer.model.instruction.Instruction;
import dps.hoffmann.producer.model.instruction.InstructionName;
import dps.hoffmann.producer.model.instruction.LogicalServiceName;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Takes in a user request provided by the api endpoint, generates the parsed instruction out of
 * the dataset and starts a benchmark test
 */
@Service
@Slf4j
public class RequestParserService {

    private static final String TEMP_DELIMITER = "_";
    private static final String BATCH_DELIMITER = ";";

    @Autowired
    private BenchmarkService benchmarkService;

    @Autowired
    private Semaphore semaphore;

    /**
     * Runs a benchmark test for the given user input. By putting it inside a critical section
     * (semaphores) the benchmark requests will be executed after one another.
     *
     * @param textMessage user input following the specified grammar
     */
    @SneakyThrows
    public void runRequest(String textMessage) {
        semaphore.acquire();
        log.info("new txt msg: {}", textMessage);

        BatchVisitor visitor = new BatchVisitor(
                (scalingInstr) -> benchmarkService.benchmark(scalingInstr)
        );

        List<List<Instruction>> batches = parseTxtMsg(textMessage);
        for (List<Instruction> batch : batches) {
            for (Instruction instruction : batch) {
                instruction.accept(visitor);
            }
            benchmarkService.waitTillQueuesEmpty();
        }

        semaphore.release();
    }

    private List<List<Instruction>> parseTxtMsg(String textMsg) throws InvalidParserRequestException {
        if (textMsg == null || textMsg.isBlank() || textMsg.isEmpty()) {
            throw new InvalidParserRequestException("msg null or blank");
        }

        textMsg = textMsg.replaceAll("\n|\r|\t| |" + TEMP_DELIMITER, "");

        List<List<Instruction>> instr = new ArrayList<>();
        for (String curr : textMsg.split(BATCH_DELIMITER)) {
            instr.add(parseBatch(curr));
        }

        return instr;
    }

    private List<Instruction> parseBatch(String txtMsg) {
        log.info("parsing batch: {}", txtMsg);
        List<Instruction> out = null;

        for (LogicalServiceName serviceName : LogicalServiceName.values()) {
            if (serviceName.matches(txtMsg)) {
                log.info("matcher matched: {} -> {}", txtMsg, serviceName.name());
                out = parseAllBatchInstructions(serviceName, serviceName.getArgBlock(txtMsg));
            }
        }

        if (out == null) {
            throw new InvalidParserRequestException("Service name not available: " + txtMsg);
        }

        return out;
    }

    private List<Instruction> parseAllBatchInstructions(LogicalServiceName serviceName,
                                                        String msg) {
        List<Instruction> instructions = new ArrayList<>();

        msg = msg.replaceAll("\\),", ")" + TEMP_DELIMITER);

        for (String txtInstr : msg.split(TEMP_DELIMITER)) {
            Instruction newElem = parseSingleInstruction(serviceName, txtInstr);
            instructions.add(newElem);
        }
        return instructions;
    }

    private Instruction parseSingleInstruction(LogicalServiceName serviceName, String msg) {
        Iterator<InstructionName> it = Arrays.stream(InstructionName.values()).iterator();
        Instruction instruction = null;

        while (instruction == null && it.hasNext()) {
            instruction = it.next().create(serviceName, msg);
        }

        if (instruction == null) {
            throw new InvalidParserRequestException("cannot parse instr: " + msg);
        }

        return instruction;
    }


}
