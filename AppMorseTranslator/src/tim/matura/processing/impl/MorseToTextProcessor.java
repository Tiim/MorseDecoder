package tim.matura.processing.impl;

import tim.matura.morse.MorseCharacter;
import tim.matura.morse.MorseSequence;
import tim.matura.morse.Translator;
import tim.matura.processing.IMorseReceiver;
import tim.matura.processing.ITextReceiver;
import tim.matura.utils.Logging;

import java.util.logging.Logger;

/**
 * @author Tiim
 * @since 31.07.13 10:45
 */
public class MorseToTextProcessor implements IMorseReceiver {

    private final ITextReceiver[] receivers;
    private MorseSequence morseSequence = new MorseSequence();

    public MorseToTextProcessor(ITextReceiver... receivers) {

        this.receivers = receivers;
    }

    @Override
    public void setMorseChar(MorseCharacter ch) {
        morseSequence.append(ch);

        if (ch == MorseCharacter.PAUSE_SHORT) {
            for (ITextReceiver rec : receivers) {
                String s = new Translator(morseSequence).getString();
                rec.setText(s);
                Logging.d(s);
            }
            morseSequence = new MorseSequence();
        }
    }

}
