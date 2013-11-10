package tim.matura.processing.impl;

import tim.matura.processing.IBinaryReceiver;
import tim.matura.processing.ISoundReceiver;

/**
 * @author Tiim
 * @since 29.07.13 12:21
 */
public class SndToBinaryProcessor implements ISoundReceiver {

    private static final float SOUND_LIMIT = 1050;
    private boolean prevVal = false;
    private final IBinaryReceiver[] receivers;

    public SndToBinaryProcessor(IBinaryReceiver... receivers) {

        this.receivers = receivers;
    }

    @Override
    public void receive(float soundSample) {

        boolean isSound;

        // Debouncing the signal
        if (!prevVal) {
            isSound = soundSample > SOUND_LIMIT + 100;
        } else {
            isSound = soundSample >= SOUND_LIMIT - 100;
        }

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
