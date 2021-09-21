package dps.hoffmann.producer.model.instruction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
public enum LogicalServiceName {
    NODE("NODE\\{(.*?)\\}", "nodequeue"),
    SPRING("SPRING\\{(.*?)\\}", "springqueue"),
    QUARKUS("QUARKUS\\{(.*?)\\}", "quarkusqueue");

    private final String batchFormat;
    private final String queueDestination;


    public boolean matches(String txtMsg) {
        return getMatcher(txtMsg).matches();
    }

    public String getArgBlock(String txtMsg) {
        Matcher m = getMatcher(txtMsg);
        String out = "";
        if (m.find()) {
            out = m.group(1);
        }
        return out;
    }

    private Matcher getMatcher(String txtMsg) {
        Pattern pattern = Pattern.compile(batchFormat);
        return pattern.matcher(txtMsg);
    }

}
