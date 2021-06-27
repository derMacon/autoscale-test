package dps.hoffmann.springconsumer.utils;

public class MyMath {

    public static int floor(int input, int bound) {
        return input > bound
                ? input
                : bound;
    }
}
