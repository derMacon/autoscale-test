package dps.hoffmann.proxy.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.Message;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ConversionUtils {

    // for some reason quarkus amq api puts all the irrelevant header
    // information in the message itself... so in order to seperate those
    // from the actual content body, seperate them using the delimiter
    // - the msg. looks like this: [some weird unencoded stuff]$[actual content]
    private static final String HEADER_DELIMITER = "$";

    @SneakyThrows
    public static byte[] integersToBytes(AtomicInteger[] values) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for (int i = 0; i < values.length; ++i) {
            dos.writeInt(values[i].intValue());
        }

        return baos.toByteArray();
    }

    @SneakyThrows
    public static String translateActveMqByteMessage(Message msg) {
        String out = null;
        if (msg instanceof ActiveMQBytesMessage) {
            ActiveMQBytesMessage byteMsg = (ActiveMQBytesMessage) msg;
            byte[] bytes = new byte[(int) byteMsg.getBodyLength()];
            byteMsg.readBytes(bytes);

            out = new String(bytes);
            log.info("translated bytes: {}", out);
            // for some reason smallrye in the quarkus project appends a prefix
            // with the queue name to the json, this gets deleted by the following
            // query
            out = out.substring(out.indexOf(HEADER_DELIMITER) + 1);
        }
        return out;
    }


}
