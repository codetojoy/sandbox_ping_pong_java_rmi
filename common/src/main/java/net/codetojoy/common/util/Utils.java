
package net.codetojoy.common.util;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public int getRandomId(int min, int max) {
        final int result = ThreadLocalRandom.current().nextInt(min, max + 1);
        return result;
    }
}
