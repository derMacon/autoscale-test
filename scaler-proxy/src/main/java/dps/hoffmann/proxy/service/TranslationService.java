package dps.hoffmann.proxy.service;

import dps.hoffmann.proxy.exception.InvalidJsonException;
import dps.hoffmann.proxy.model.RequestType;
import dps.hoffmann.proxy.model.ScalingDirection;
import dps.hoffmann.proxy.model.ScalingInstruction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service that translates the a given json body generated by prometheus
 * or its alertmanager to a given request type enum member
 */
@Service
@Slf4j
public class TranslationService {

    @Value("${translation.fieldname}")
    private String fieldName;

    /**
     * translates the a given json body generated by prometheus or its
     * alertmanager to a given request type enum member
     * @param jsonBody request body comming from the prometheus / alertmanager
     * @return enum member representing request type
     */
    public List<ScalingInstruction> translateRequest(String jsonBody) {
        RequestType requestType = parseRequestType(jsonBody);
        return createInstructions(requestType);
    }

    private RequestType parseRequestType(String jsonBody) {
        // todo maybe use ObjectMapper to translate to map ???
        log.info("json body");
        String patternStr = ".*" + fieldName + "\":\"(.*?)\",.*";
        Pattern patternObj = Pattern.compile(patternStr);
        Matcher matcher = patternObj.matcher(jsonBody);

        if (!matcher.find() || matcher.groupCount() != 1) {
            String msg = "could not find key with name \"" + fieldName + "\" in json body";
            log.error(msg);
            throw new InvalidJsonException(msg);
        }

        String jsonValue = matcher.group(1);


        // todo maybe use streams
        RequestType requestType = null;
        for (RequestType type : RequestType.values()) {
            if (jsonValue.equalsIgnoreCase(type.getRequestName())) {
                requestType = type;
            }
        }

        if (requestType == null) {
            String error = "could not find type for json value: " + jsonValue;
            log.error(error);
            throw new InvalidJsonException(error);
        }

        return requestType;
    }

    private List<ScalingInstruction> createInstructions(RequestType requestType) {
        List<ScalingInstruction> out = new ArrayList<>();
        ScalingDirection scalingDir = requestType.getScalingDir();
        for (int i = 0; i < requestType.getScalingInterval(); i++) {
            out.add(new ScalingInstruction(scalingDir));
        }
        return out;
    }



}
