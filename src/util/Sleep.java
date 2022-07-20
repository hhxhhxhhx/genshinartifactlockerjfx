package util;

/**
 * Utility class to call Thread.sleep(100) without having to catch an exception every time.
 */
public class Sleep {
    public static void pause() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
