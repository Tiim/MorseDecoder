package tim.matura.morse.util;

import tim.matura.morse.MorseSequence;

import java.util.ArrayList;
import java.util.List;

import static tim.matura.morse.MorseCharacter.*;

/**
 * @author Tiim
 * @since 20.05.13 19:21
 */
public class MorseMap {

    public static final MorseMap INSTANCE = new MorseMap();

    private List<Character> c;
    private List<MorseSequence> m;

    private MorseMap() {
        this.c = new ArrayList<Character>();
        this.m = new ArrayList<MorseSequence>();
        put(' ', new ImmutableMorseSequence(PAUSE_LONG));
        put('a', new ImmutableMorseSequence(DIT, DAH));
        put('b', new ImmutableMorseSequence(DAH, DIT, DIT, DIT));
        put('c', new ImmutableMorseSequence(DAH, DIT, DAH, DIT));
        put('d', new ImmutableMorseSequence(DAH, DIT, DIT));
        put('e', new ImmutableMorseSequence(DIT));
        put('f', new ImmutableMorseSequence(DIT, DIT, DAH, DIT));
        put('g', new ImmutableMorseSequence(DAH, DAH, DIT));
        put('h', new ImmutableMorseSequence(DIT, DIT, DIT, DIT));
        put('i', new ImmutableMorseSequence(DIT, DIT));
        put('j', new ImmutableMorseSequence(DIT, DAH, DAH, DAH));
        put('k', new ImmutableMorseSequence(DAH, DIT, DAH));
        put('l', new ImmutableMorseSequence(DIT, DAH, DIT, DIT));
        put('m', new ImmutableMorseSequence(DAH, DAH));
        put('n', new ImmutableMorseSequence(DAH, DIT));
        put('o', new ImmutableMorseSequence(DAH, DAH, DAH));
        put('p', new ImmutableMorseSequence(DIT, DAH, DAH, DIT));
        put('q', new ImmutableMorseSequence(DAH, DAH, DIT, DAH));
        put('r', new ImmutableMorseSequence(DIT, DAH, DIT));
        put('s', new ImmutableMorseSequence(DIT, DIT, DIT));
        put('t', new ImmutableMorseSequence(DAH));
        put('u', new ImmutableMorseSequence(DIT, DIT, DAH));
        put('v', new ImmutableMorseSequence(DIT, DIT, DIT, DAH));
        put('w', new ImmutableMorseSequence(DIT, DAH, DAH));
        put('x', new ImmutableMorseSequence(DAH, DIT, DIT, DAH));
        put('y', new ImmutableMorseSequence(DAH, DIT, DAH, DAH));
        put('z', new ImmutableMorseSequence(DAH, DAH, DIT, DIT));
        put('0', new ImmutableMorseSequence(DAH, DAH, DAH, DAH, DAH));
        put('1', new ImmutableMorseSequence(DIT, DAH, DAH, DAH, DAH));
        put('2', new ImmutableMorseSequence(DIT, DIT, DAH, DAH, DAH));
        put('3', new ImmutableMorseSequence(DIT, DIT, DIT, DAH, DAH));
        put('4', new ImmutableMorseSequence(DIT, DIT, DIT, DIT, DAH));
        put('5', new ImmutableMorseSequence(DIT, DIT, DIT, DIT, DIT));
        put('6', new ImmutableMorseSequence(DAH, DIT, DIT, DIT, DIT));
        put('7', new ImmutableMorseSequence(DAH, DAH, DIT, DIT, DIT));
        put('8', new ImmutableMorseSequence(DAH, DAH, DAH, DIT, DIT));
        put('9', new ImmutableMorseSequence(DAH, DAH, DAH, DAH, DIT));
    }

    public void put(char c, MorseSequence m) {
        this.c.add(c);
        this.m.add(m);
    }

    public char getChar(MorseSequence m) {
        return c.get(this.m.indexOf(m));
    }

    public MorseSequence getMorse(char c) {
        return m.get(this.c.indexOf(c));
    }

    public boolean containsMorse(MorseSequence m) {
        return this.m.contains(m);
    }

    public boolean containsChar(char c) {
        return this.c.contains(new Character(c));
    }
}
