package dps.hoffmann.springconsumer.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoundtripStat {

    TOTAL_ROUNDTRIP("roundtrip.average.total.%s"),
    PROCESSING_DURATION("roundtrip.average.processing.%s"),
    RECEIVING_DURATION("roundtrip.average.receiving.%s");

    private final String format;

    public String getAverageName(LogicalServiceName serviceName) {
        return getName(serviceName) + ".average";
    }

    public String getCurrName(LogicalServiceName serviceName) {
        return getName(serviceName) + ".current";
    }

    private String getName(LogicalServiceName serviceName) {
        return String.format(format, serviceName.name().toLowerCase());
    }

}
