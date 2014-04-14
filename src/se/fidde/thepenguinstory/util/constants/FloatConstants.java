package se.fidde.thepenguinstory.util.constants;

public enum FloatConstants {

    CHANCE_TO_GET_SICK(0.3f), CHANCE_TO_GET_DIRTY(0.3f);

    private final float number;

    private FloatConstants(float number) {
        this.number = number;
    }

    public float get() {
        return number;
    }

}
