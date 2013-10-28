package tim.matura.morse;

/**
 * @author Tiim
 * @since 20.05.13 12:45
 */
public enum MorseCharacter {
    DIT(0, 1),
    DAH(1, 3),
    PAUSE_BETWEEN_SYMBOLS(2, 1),
    PAUSE_SHORT(3, 3),
    PAUSE_LONG(4, 7),
    UNKNOWN(-1, 0);

    private int lenght;
    private int id;

    private MorseCharacter(int id, int lenght) {
        this.lenght = lenght;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getLenght() {
        return lenght;
    }
}