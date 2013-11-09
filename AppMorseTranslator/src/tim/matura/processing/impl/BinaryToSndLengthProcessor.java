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
        //TODO: Something is weird with these lenghts..
        if (sound != last) {
            float sndLength = (float) duration / (float) samplesPerSecond;
            for (ISoundLengthReceiver rec : receivers) {
                rec.setSoundLength(sndLength, last);
            }
            Logging.d("Length: " + duration + " == " + sndLength + " -> " + last);
            last = sound;
            duration = 0;
        }
    }


    public void setSamplePerSecond(int x) {
        samplesPerSecond = x;
    }
}
