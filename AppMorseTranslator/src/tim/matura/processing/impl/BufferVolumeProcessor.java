package tim.matura.processing.impl;

import tim.matura.processing.ISampleReceiver;
import tim.matura.processing.ISoundReceiver;
import tim.matura.utils.RingBuffer;

/**
 * @author Tim
 * @since 09.11.13 09:45
 */
public class BufferVolumeProcessor implements ISampleReceiver {

    private RingBuffer buffer;

    private final ISoundReceiver[] receivers;

    public BufferVolumeProcessor(ISoundReceiver... receivers) {
        buffer = new RingBuffer(256); // Temporary buffer to prevent NPEs
        this.receivers = receivers;
    }


    @Override
    public void setSample(int sample) {
        buffer.insert(Math.abs(sample));
        for (ISoundReceiver r : receivers) {
            r.receive(buffer.getAverage());
        }
    }

    @Override
    public void setSamplePerSecond(int i) {
        buffer = new RingBuffer(i / 30);
        for (ISoundReceiver r : receivers) {
            r.setSamplePerSecond(i);
        }
    }
}
