package chord;

import java.util.HashMap;
import java.util.Map;

public class Helper {
    private static Map<Integer, Integer> powerOfTwo;

    public Helper() {
        powerOfTwo = new HashMap<>();
        int base = 1;
        for (int i = 0; i < 32; i++) {
            powerOfTwo.put(i, base);
            base *= 2;
        }
    }

    /**
     *
     * @param i
     * @return 2^i
     */
    public static Integer getPowerOfTwo(int i) {
        return powerOfTwo.get(i);
    }
}
