package dps.hoffmann.proxy.properties;

import dps.hoffmann.proxy.model.Tupel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Getter
@Setter
@Component
@ConfigurationProperties(ignoreUnknownFields = false, prefix = "scaling")
public class ScalingProperties implements Iterable<Tupel<Integer, Integer>> {

    public static final int TIER_CNT = 3;


    private int qb0;
    private int qb1;
    private int qb2;
    private int cb0;
    private int cb1;
    private int cb2;
    private int cb3;

    public int getHighestContainerBound() {
        return cb3;
    }

    // general: tupel explanation
    // fst: lower bound exclusive
    // snd: upper bound inclusive

    private Tupel<Integer, Integer> getLowerTier() {
        return new Tupel<>(cb0, cb1);
    }

    private Tupel<Integer, Integer> getMiddleTier() {
        return new Tupel<>(cb1, cb2);
    }

    private Tupel<Integer, Integer> getHighTier() {
        return new Tupel<>(cb2, cb3);
    }

    @Override
    public Iterator<Tupel<Integer, Integer>> iterator() {
        return new Iterator<>() {
            private int idx = 0;

            @Override
            public boolean hasNext() {
                return idx < TIER_CNT;
            }

            @Override
            public Tupel<Integer, Integer> next() {
                Tupel<Integer, Integer> out = null;
                switch (idx++) {
                    case 0:
                        out = getLowerTier();
                        break;
                    case 1:
                        out = getMiddleTier();
                        break;
                    case 2:
                        out = getHighTier();
                        break;
                }
                return out;
            }
        };
    }
}
