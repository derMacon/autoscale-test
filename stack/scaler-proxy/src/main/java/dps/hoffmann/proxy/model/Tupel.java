package dps.hoffmann.proxy.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode
@ToString
public class Tupel<E, T> {
    private final E fst;
    private final T snd;
}
