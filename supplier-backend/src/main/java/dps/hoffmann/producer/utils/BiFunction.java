package dps.hoffmann.producer.utils;

public interface BiFunction<E, T, O> {
    O process(E e, T t);
}
