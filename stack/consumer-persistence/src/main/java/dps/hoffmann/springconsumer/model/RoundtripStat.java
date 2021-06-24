package dps.hoffmann.springconsumer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoundtripStat {

    TOTAL_ROUNDTRIP("roundtrip.average.total.%s"),
    PROCESSING_DURATION("roundtrip.average.processing.%s"),
    RECEIVING_DURATION("roundtrip.average.receiving.%s");

    private final String format;

    public String getName(LogicalServiceName serviceName) {
        return String.format(format, serviceName.name().toLowerCase());
    }

}
