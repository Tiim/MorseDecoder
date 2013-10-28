package tim.matura.processing.impl;

import tim.matura.morse.MorseCharacter;
import tim.matura.processing.IMorseReceiver;
import tim.matura.processing.ISoundLengthReceiver;
import tim.matura.utils.Logging;
import tim.matura.utils.Utils;

/**
 * @author Tim
 * @since 21.10.13
 */
public class LengthToMorse2 implements ISoundLengthReceiver {

    private final IMorseReceiver[] receivers;

    //TODO: Make that changable in the gui.
    private float morseTickLength = 0.240f; //s     == 5 WPM == 25 BPM


    public LengthToMorse2(IMorseReceiver... receivers) {

        this.receivers = receivers;
    }

    @Override
    public void setSoundLength(float length, boolean isSound) {
        if (Utils.isApproxEqual(length, MorseCharacter.DIT.getLenght() * morseTickLength) && isSound) {
            send(MorseCharacter.DIT);
        } else if (Utils.isApproxEqual(length, MorseCharacter.DAH.getLenght() * morseTickLength) && isSound) {
            send(MorseCharacter.DAH);
        } else if (Utils.isApproxEqual(length, MorseCharacter.PAUSE_LONG.getLenght() * morseTickLength) && !isSound) {
            send(MorseCharacter.PAUSE_LONG);
        } else if (Utils.isApproxEqual(length, MorseCharacter.PAUSE_SHORT.getLenght() * morseTickLength) && !isSound) {
            send(MorseCharacter.PAUSE_SHORT);
        } else if (Utils.isApproxEqual(length, MorseCharacter.PAUSE_BETWEEN_SYMBOLS.getLenght() * morseTickLength) && !isSound) {
            send(MorseCharacter.PAUSE_BETWEEN_SYMBOLS);
        } else {
            send(MorseCharacter.UNKNOWN);
        }

    }

    private void send(MorseCharacter c) {
        if (c != MorseCharacter.PAUSE_BETWEEN_SYMBOLS) {
            for (IMorseReceiver r : receivers) {
                r.setMorseChar(c);
            }
        }
    }

}
