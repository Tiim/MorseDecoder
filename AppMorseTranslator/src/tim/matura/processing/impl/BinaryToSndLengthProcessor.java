package tim.matura.processing.impl;

import tim.matura.processing.IBinaryReceiver;
import tim.matura.processing.ISoundLengthReceiver;

/**
 * @author Tiim
 * @since 29.07.13 12:30
 */
public class BinaryToSndLengthProcessor implements IBinaryReceiver {

    private final ISoundLengthReceiver receiver;
    boolean last = false;
    int duration = 0;

    public BinaryToSndLengthProcessor(ISoundLengthReceiver receiver) {

        this.receiver = receiver;
    }


    @Override
    public void setSound(boolean sound) {
        duration++;
        if (sound != last) {
            receiver.setSoundLenght(duration, last);
            last = sound;
            duration = 0;
        }
    }
}
