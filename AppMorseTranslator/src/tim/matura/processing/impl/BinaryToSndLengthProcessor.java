package tim.matura.processing.impl;

import tim.matura.processing.IBinaryReceiver;
import tim.matura.processing.ISoundLengthReceiver;
import tim.matura.utils.Logging;

/**
 * @author Tiim
 * @since 29.07.13 12:30
 */
public class BinaryToSndLengthProcessor implements IBinaryReceiver {

    private final ISoundLengthReceiver[] receivers;
    private boolean last = false;
    private int duration = 0;
    private int samplesPerSecond = 0;

    public BinaryToSndLengthProcessor(ISoundLengthReceiver... receiver) {

        this.receivers = receiver;
    }


    @Override
    public void setSound(boolean sound) {
        duration++;
        if (sound != last) {
            for (ISoundLengthReceiver rec : receivers) {
                rec.setSoundLength((float) duration / (float) samplesPerSecond, last);
            }
            Logging.d("Length: " + duration + " -> " + last);
            last = sound;
            duration = 0;
        }
    }


    public void setSamplePerSecond(int x) {
        samplesPerSecond = x;
    }
}
