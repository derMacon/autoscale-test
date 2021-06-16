package dps.hoffmann.producer.service;

import dps.hoffmann.producer.model.error.InvalidParserRequestException;
import dps.hoffmann.producer.model.instruction.BatchVisitor;
import dps.hoffmann.producer.model.instruction.Instruction;
import dps.hoffmann.producer.model.instruction.InstructionName;
import dps.hoffmann.producer.model.instruction.LogicalServiceName;
import dps.hoffmann.producer.model.instruction.ScalingInstruction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class RequestParserService {

    private static final String BATCH_REGEX_FORMAT = "%s\\{(.*)\\}";

    private static final String TEMP_DELIMITER = "_";



    private static final String BATCH_DELIMITER = ";";

    @Autowired
    private BenchmarkService benchmarkService;

    public void runRequest(String textMessage) {
        BatchVisitor visitor = new BatchVisitor(
                (scalingInstr) -> benchmarkService.benchmark(scalingInstr)
        );

        List<Instruction> instructions = parseTxtMsg(textMessage);
        System.out.println();
        for (Instruction instruction : instructions) {
            instruction.accept(visitor);
        }
    }

    private List<Instruction> parseTxtMsg(String textMsg) throws InvalidParserRequestException {
        if (textMsg == null || textMsg.isBlank() || textMsg.isEmpty()) {
            throw new InvalidParserRequestException("msg null or blank");
        }

        textMsg = textMsg.replaceAll("\n|\r|\t| |" + TEMP_DELIMITER, "");

        List<Instruction> instr = new ArrayList<>();
        for (String curr : textMsg.split(BATCH_DELIMITER)) {
            instr.addAll(parseBatch(curr));
        }

        return instr;
    }

    private List<Instruction> parseBatch(String txtMsg) {
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
