package tim.matura.processing.impl;

import tim.matura.morse.MorseCharacter;
import tim.matura.processing.ILengthUpdateListener;
import tim.matura.processing.IMorseReceiver;
import tim.matura.processing.ISoundLengthReceiver;
import tim.matura.utils.Logging;
import tim.matura.utils.Utils;

/**
 * @author Tim
 * @since 21.10.13
 */
public class LengthToMorse implements ISoundLengthReceiver {

    private final ILengthUpdateListener lengthUpdateListener;
    private final IMorseReceiver[] receivers;

    private float morseTickLength = 0.240f; //s     == 5 WPM == 25 BPM


    public LengthToMorse(ILengthUpdateListener l, IMorseReceiver... receivers) {
        lengthUpdateListener = l;

        this.receivers = receivers;
    }

    @Override
    public void setSoundLength(float length, boolean isSound) {
        if (Utils.isApproxEqual(length, MorseCharacter.DIT.getLenght() * morseTickLength) && isSound) {
            send(MorseCharacter.DIT);
            update(MorseCharacter.DIT, length);
        } else if (Utils.isApproxEqual(length, MorseCharacter.DAH.getLenght() * morseTickLength) && isSound) {
            send(MorseCharacter.DAH);
            update(MorseCharacter.DAH, length);
        } else if (Utils.isApproxEqual(length, MorseCharacter.PAUSE_LONG.getLenght() * morseTickLength) && !isSound) {
            send(MorseCharacter.PAUSE_LONG);
            update(MorseCharacter.PAUSE_LONG, length);
        } else if (Utils.isApproxEqual(length, MorseCharacter.PAUSE_SHORT.getLenght() * morseTickLength) && !isSound) {
            send(MorseCharacter.PAUSE_SHORT);
            update(MorseCharacter.PAUSE_SHORT, length);
        } else if (Utils.isApproxEqual(length, MorseCharacter.PAUSE_BETWEEN_SYMBOLS.getLenght() * morseTickLength) && !isSound) {
            send(MorseCharacter.PAUSE_BETWEEN_SYMBOLS);
            update(MorseCharacter.PAUSE_BETWEEN_SYMBOLS, length);
        } else {
            send(MorseCharacter.UNKNOWN);
        }

    }

    private void update(MorseCharacter character, float length) {
        length *= character.getLenght();
        float dummy = morseTickLength;
        morseTickLength = Utils.average(length, morseTickLength);
        if (morseTickLength != dummy) {
            lengthUpdateListener.lengthChanged(morseTickLength);
        }
    }

    private void send(MorseCharacter c) {
        if (c != MorseCharacter.PAUSE_BETWEEN_SYMBOLS) {
            for (IMorseReceiver r : receivers) {
                r.setMorseChar(c);
            }
            Logging.d(c.toString());
        }
    }

    public void setDitLength(int ditLength) {
        this.morseTickLength = ((float) ditLength) / 2000f;
        Logging.d("Expected Values:");
        Logging.d(MorseCharacter.DIT.getLenght() * morseTickLength + ": DIT");
        Logging.d(MorseCharacter.DAH.getLenght() * morseTickLength + ": DAH");
        Logging.d(MorseCharacter.PAUSE_SHORT.getLenght() * morseTickLength + ": Pause1");
        Logging.d(MorseCharacter.PAUSE_LONG.getLenght() * morseTickLength + ": Pause2");
    }
}
