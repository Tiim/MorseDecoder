package tim.matura.utils;

/**
 * @author Tim
 * @since 09.11.13 09:46
 */
public class RingBuffer {

    private int i = 0;

    private boolean bufferFull = false;

    private int[] buffer;

    public RingBuffer(int size) {
        buffer = new int[size];
    }

    public void insert(int value) {
        buffer[i] = value;

        if (++i >= buffer.length) {
            i = 0;
            if (!bufferFull) {
                bufferFull = true;
            }
        }
    }

    public float getAverage() {
        long sum = 0;
        for (int j = 0; j < buffer.length; j++) {
            sum += buffer[j];

            if (!bufferFull && j >= i - 1) {
                break;
            }
        }
        return (float) sum / (float) buffer.length;
    }
}
