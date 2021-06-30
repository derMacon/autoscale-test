package dps.hoffmann.proxy.utils;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class ConversionUtils {

    @SneakyThrows
    public static byte[] integersToBytes(AtomicInteger[] values) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for (int i = 0; i < values.length; ++i) {
            dos.writeInt(values[i].intValue());
        }

        return baos.toByteArray();
    }


}
