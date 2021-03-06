package tim.matura.processing.impl;

import tim.matura.processing.ISampleReceiver;
import tim.matura.processing.ISoundReceiver;

/**
 * @author Tim
 * @since 21.10.13
 */
@Deprecated
public class SampleSmoother implements ISampleReceiver {

    private final ISoundReceiver[] receivers;

    private float value = 0;

    public SampleSmoother(ISoundReceiver... receivers) {

        this.receivers = receivers;
    }

    @Override
    public void setSample(int sample) {
        sample = Math.abs(sample);
        value = value * 0.95f + sample * 0.01f; //oder andere Gewichte
        for (ISoundReceiver r : receivers) {
            r.receive((int) value);
        }
    }

    @Override
    public void setSamplePerSecond(int i) {
        for (ISoundReceiver r : receivers) {
            r.setSamplePerSecond(i);
        }
    }

}
