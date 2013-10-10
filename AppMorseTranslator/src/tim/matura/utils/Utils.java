package tim.matura.utils;

/**
 * @author Tiim
 * @since 31.07.13 13:51
 */
public class Utils {

    public static boolean isApproxEqual(float x, float y) {
        return isApproxEqual(x, y, 15);
    }

    public static boolean isApproxEqual(float x, float y, int percentage) {
        float diff = (Math.abs(x) * percentage) / 100f;
        return y < x + diff && y > x - diff;
    }

    public static float average(float... ints) {
        int sum = 0;
        for (float i : ints) {
            sum += i;
        }
        return sum / ((float) ints.length);
    }

    public static float absAverage(int length, int... ints) {
        float sum = 0;
        for (int i = 0; i < length; i++) {
            sum += Math.abs(ints[i]);
        }
        return sum / (float) length;
    }

    public static int max(int[] array) {
        int max = array[0];
        for (int i : array) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

}
