package tim.matura.processing.impl;

import android.util.Log;
import tim.matura.app.AppMorseTranslator.util.Ref;
import tim.matura.morse.MorseCharacter;
import tim.matura.processing.IMorseReceiver;
import tim.matura.processing.ISoundLengthReceiver;

/**
 * @author Tiim
 * @since 29.07.13 12:35
 */
public class LengthToMorseProcessor implements ISoundLengthReceiver {

    private final IMorseReceiver receiver;
    private int morseTickLength;


    public LengthToMorseProcessor(IMorseReceiver receiver) {

        this.receiver = receiver;
    }


    @Override
    public void setSoundLenght(int lenght, boolean isSound) {
        //Todo: Change this !!!
        Log.d(Ref.NAME, lenght + "  " + isSound);
        if (isSound) {
            if (lenght > 10) {
                receiver.setMorseChar(MorseCharacter.DAH);
            } else {
                receiver.setMorseChar(MorseCharacter.DIT);
            }
        } else {
            if (lenght > 20) {
                receiver.setMorseChar(MorseCharacter.PAUSE_LONG);
            } else {
                receiver.setMorseChar(MorseCharacter.PAUSE_SHORT);
            }
        }
    }
}
