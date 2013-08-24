package tim.matura.processing.impl;

import tim.matura.processing.ISampleReceiver;
import tim.matura.processing.ISoundReceiver;
import tim.matura.utils.Utils;

/**
 * @author Tiim
 * @since 24.08.13 10:53
 */
public class SampleToChunkProcessor implements ISampleReceiver {

    private final ISoundReceiver[] receivers;

    private static final float SECONDS = 0.05f;

    private int samplesPerSeconds = 0;

    private int[] samples;

    private int sampleNow = 0;

    public SampleToChunkProcessor(ISoundReceiver... receivers) {

        this.receivers = receivers;
    }

    @Override
    public void setSample(int sample) {
        samples[sampleNow++] = sample;

        if (samplesPerSeconds * SECONDS <= sampleNow) {
            int avr = (int) Utils.absAverage(sampleNow, samples);
            for (ISoundReceiver rec : receivers) {
                rec.receive(avr);
            }
//            Logging.d("Soundchuk: " + avr);
            sampleNow = 0;
        }
    }

    @Override
    public void setSamplePerSecond(int i) {
        samplesPerSeconds = i;
        samples = new int[(int) (samplesPerSeconds * SECONDS) + 1];
    }
}
