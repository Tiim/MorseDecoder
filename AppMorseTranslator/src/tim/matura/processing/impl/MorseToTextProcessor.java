package tim.matura.processing.impl;

import tim.matura.morse.MorseCharacter;
import tim.matura.morse.MorseSequence;
import tim.matura.morse.Translator;
import tim.matura.processing.IMorseReceiver;
import tim.matura.processing.ITextReceiver;

/**
 * @author Tiim
 * @since 31.07.13 10:45
 */
public class MorseToTextProcessor implements IMorseReceiver {

    private final ITextReceiver receiver;
    private MorseSequence morseSequence = new MorseSequence();

    public MorseToTextProcessor(ITextReceiver receiver) {

        this.receiver = receiver;
    }

    @Override
    public void setMorseChar(MorseCharacter ch) {
        morseSequence.append(ch);

        if (ch == MorseCharacter.PAUSE_SHORT) {
            receiver.setText(new Translator(morseSequence).getString());
            morseSequence = new MorseSequence();
        }
    }

}
