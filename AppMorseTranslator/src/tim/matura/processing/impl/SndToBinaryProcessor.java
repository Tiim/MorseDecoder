package tim.matura.processing.impl;

import tim.matura.processing.IBinaryReceiver;
import tim.matura.processing.ISoundReceiver;

/**
 * @author Tiim
 * @since 29.07.13 12:21
 */
public class SndToBinaryProcessor implements ISoundReceiver {

    private float soundLimit = 1000;
    private final IBinaryReceiver[] receivers;

    public SndToBinaryProcessor(IBinaryReceiver... receivers) {

        this.receivers = receivers;
    }

    @Override
    public void receive(int soundSample) {
        boolean isSound = soundSample > soundLimit;
        for (IBinaryReceiver rec : receivers) {
            rec.setSound(isSound);
        }
    }

    @Override
    public void setSamplePerSecond(int i) {
        for (IBinaryReceiver r : receivers) {
            r.setSamplePerSecond(i);
        }
    }
}
