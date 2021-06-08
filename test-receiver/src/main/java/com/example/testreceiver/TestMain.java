package com.example.testreceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {

//    public static void main(String[] args) {
//        System.out.println("test main");
//
//        String test = "{\"receiver\":\"slack\",\"status\":\"firing\"," +
//                "\"alerts\":[{\"status\":\"firing\",\"labels\":{\"alertname\":\"TooFewConsumers\",\"brokerName\":\"localhost\",\"instance\":\"activemq:8080\",\"job\":\"services\"},\"annotations\":{},\"startsAt\":\"2021-06-08T19:30:28.104721216Z\",\"endsAt\":\"0001-01-01T00:00:00Z\",\"generatorURL\":\"http://1a1a5836c061:9090/graph?g0.expr=org_apache_activemq_Broker_TotalMessageCount+%3E+1\\u0026g0.tab=1\",\"fingerprint\":\"7dca81cd71f86ee2\"}],\"groupLabels\":{},\"commonLabels\":{\"alertname\":\"TooFewConsumers\",\"brokerName\":\"localhost\",\"instance\":\"activemq:8080\",\"job\":\"services\"},\"commonAnnotations\":{},\"externalURL\":\"http://localhost:9093\",\"version\":\"4\",\"groupKey\":\"{}:{}\"}";
//
//        Pattern pattern = Pattern.compile(".*alertname\":(.*?),.*");
//        Matcher matcher = pattern.matcher(test);
//        while (matcher.find()) {
//            System.out.println("group 1: " + matcher.group(1));
//        }
//
//
//    }


}
