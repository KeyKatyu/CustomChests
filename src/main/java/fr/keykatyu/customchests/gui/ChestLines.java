package fr.keykatyu.customchests.gui;

public enum ChestLines {
    ONE("§a9 slots", 1, 9),
    TWO("§a18 slots", 2, 18),
    THREE("§a27 slots", 3, 27),
    FOUR("§a36 slots", 4, 36),
    FIVE("§a45 slots", 5, 45),
    SIX("§a54 slots", 6, 54);

    private String displayString;
    private int rows;
    private int slots;

    ChestLines(String displayString, int number, int slots) {
        this.displayString = displayString;
        this.rows = number;
        this.slots = slots;
    }

    public String getDisplayString() {
        return displayString;
    }

    public int getRows() {
        return rows;
    }

    public int getSlots() {
        return slots;
    }

    static
    public final ChestLines[] values = values();

    public ChestLines prev() {
        return values[(ordinal() - 1  + values.length) % values.length];
    }

    public ChestLines next() {
        return values[(ordinal() + 1) % values.length];
    }
}
