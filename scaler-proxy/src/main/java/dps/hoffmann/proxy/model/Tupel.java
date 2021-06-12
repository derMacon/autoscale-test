package dps.hoffmann.proxy.model;

import lombok.Value;

@Value
public class Tupel<E, T> {
    private final E e;
    private final T t;
}
