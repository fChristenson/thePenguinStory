package se.fidde.thepenguinstory.util.constants;

public enum IntegerConstants {
    BASE_HEALTH_AND_HAPPINESS(24), BASE_DECAYRATE(1), SICKNESS_MULTIPLIER(2), DIRTY_MULTIPLIER(
            2), TIME_BEFORE_STATUS_UPDATE(1000 * 60 * 45), LOCATION_MULTIPLIER(
            10);

    private final int integer;

    private IntegerConstants(int number) {
        integer = number;
    }

    public int get() {
        return integer;
    }

}
