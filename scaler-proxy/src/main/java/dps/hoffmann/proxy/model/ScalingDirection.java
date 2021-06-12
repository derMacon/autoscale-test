package dps.hoffmann.proxy.model;

public enum ScalingDirection {
    UP, DOWN;

    private static final String requestJsonFormat = "{" +
            "\"groupLabels\": " +
            "{\"scale\": \"%s\", " +
            "\"service\": \"%s\"}" +
            "}";

    public String getRequestJson(String serviceName) {
        return String.format(requestJsonFormat, this.name().toLowerCase(), serviceName);
    }
}
