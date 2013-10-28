package tim.matura.morse.util;

import tim.matura.morse.MorseCharacter;
import tim.matura.morse.MorseSequence;
import tim.matura.utils.Logging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Tim
 * @since 10.10.13
 */
public class MorseToAudio {

    public static final int SAMPLE_RATE = 10_000;

    public static final int DIT_LENGTH = 240; //ms

    private final MorseSequence seq;

    private int index = 0;

    public MorseToAudio(MorseSequence seq) {
        this.seq = seq;
    }

    public byte[] getSound() {
        //TODO: Smooth the sound a bit. Remove click noises
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while (hasNext()) {
            byte[] b = nextCharacter();
            os.write(b, 0, b.length);
        }
        return os.toByteArray();
    }

    public byte[] nextCharacter() {
        MorseCharacter c = seq.getCharAt(index++);
        switch (c) {
            case DIT:
                return getSineWave(DIT_LENGTH * MorseCharacter.DIT.getLenght(), false);
            case DAH:
                return getSineWave(DIT_LENGTH * MorseCharacter.DAH.getLenght(), false);
            case PAUSE_SHORT:
                return getSineWave(DIT_LENGTH * MorseCharacter.PAUSE_SHORT.getLenght(), true);
            case PAUSE_LONG:
                return getSineWave(DIT_LENGTH * MorseCharacter.PAUSE_LONG.getLenght(), true);
            default:
                return new byte[0];
        }
    }

    public boolean hasNext() {
        return index < seq.getLenght();
    }

    private byte[] getSineWave(int duration, boolean quiet) {
        final int sampleCount = (duration * SAMPLE_RATE / 1000) + (SAMPLE_RATE / 1000);
        final byte[] b = new byte[sampleCount * 2];
        if (!quiet) {
            final double[] samples = new double[sampleCount];
            final int freq = 440; //hz
            for (int i = 0; i < (duration * SAMPLE_RATE / 1000); ++i) {
                samples[i] = Math.sin(2 * Math.PI * i / (SAMPLE_RATE / freq));
            }
            for (int i = duration * SAMPLE_RATE / 100; i < sampleCount; i++) {
                samples[i] = 0;
            }
            int idx = 0;
            for (double dVal : samples) {
                short val = (short) (dVal * 32767);
                b[idx++] = (byte) (val & 0x00ff);
                b[idx++] = (byte) ((val & 0xff00) >>> 8);
            }
        } else {
            for (int i = 0; i < b.length; i++) {
                b[i] = 0;
            }
        }
        return b;
    }
}
