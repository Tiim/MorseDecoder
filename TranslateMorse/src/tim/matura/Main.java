package tim.matura;

import tim.matura.morse.MorseSequence;
import tim.matura.morse.Translator;

import static tim.matura.morse.MorseCharacter.*;

/**
 * @author Tiim
 * @since 20.05.13 18:28
 */
public class Main {
    public static void main(String[] args) {
        Translator t = new Translator(new MorseSequence(DIT, DIT, DIT, DIT, PAUSE_SHORT, DIT, DAH));
        System.out.println(t.getString());
    }
}
