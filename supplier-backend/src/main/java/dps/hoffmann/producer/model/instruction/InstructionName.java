package dps.hoffmann.producer.model.instruction;

import dps.hoffmann.producer.utils.BiFunction;
import dps.hoffmann.producer.utils.InstructionFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
public enum InstructionName {
    BENCHMARK(
            "BENCHMARK\\(([0-9]+),([0-9]+)\\)",
            InstructionFactory::createScalingInstruction
    ),
    WAIT(
            "WAIT\\(([0-9]+)\\)",
            InstructionFactory::createWaitInstruction
    );

    private final String formatRegex;
    private final BiFunction<LogicalServiceName, Integer[], Instruction> func;

    public Instruction create(LogicalServiceName serviceName, String txtMsg) {
        Instruction out = null;
        if (matches(txtMsg)) {
            out = func.process(serviceName, getArgs(txtMsg));
        }
        return out;
    }

    private boolean matches(String txtMsg) {
        return getMatcher(txtMsg).matches();
    }

    private Integer[] getArgs(String txtMsg) {
        Matcher m = getMatcher(txtMsg);
        Integer[] out = new Integer[0];

        if (m.find()) {
            out = new Integer[m.groupCount()];
            for (int i = 0; i < m.groupCount(); i++) {
                out[i] = Integer.parseInt(m.group(i + 1));
            }
        }

        return out;
    }

    private Matcher getMatcher(String txtMsg) {
        Pattern pattern = Pattern.compile(formatRegex);
        return pattern.matcher(txtMsg);
    }
}
