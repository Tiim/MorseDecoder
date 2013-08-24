package tim.matura.processing.impl;

import tim.matura.processing.IBinaryReceiver;
import tim.matura.processing.ISoundReceiver;
import tim.matura.utils.Utils;

/**
 * @author Tiim
 * @since 29.07.13 12:21
 */
public class SndToBinaryProcessor implements ISoundReceiver {

    private float soundLimit = 2000;
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
        if (isSound) {
            soundLimit = Utils.average(soundLimit, soundSample);
        }
    }
}
