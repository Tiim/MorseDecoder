package tim.matura.processing.impl;

import tim.matura.processing.IBinaryReceiver;
import tim.matura.processing.ISoundReceiver;

/**
 * @author Tiim
 * @since 29.07.13 12:21
 */
public class SndToBinaryProcessor implements ISoundReceiver {

    private int soundLimit = 20000;
    private final IBinaryReceiver receiver;

    public SndToBinaryProcessor(IBinaryReceiver receiver) {

        this.receiver = receiver;
    }

    @Override
    public void receive(int soundSample) {
        //Change this !!
        receiver.setSound(soundSample > soundLimit);
    }
}
