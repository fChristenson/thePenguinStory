package se.fidde.thepenguinstory.domain.penguin;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import org.joda.time.DateTime;
import org.joda.time.Days;

import se.fidde.thepenguinstory.util.constants.IntegerConstants;
import se.fidde.thepenguinstory.util.constants.UrlMarkRepsonce;
import se.fidde.thepenguinstory.util.validation.UrlValidationHandler;
import android.location.Location;
import android.util.Log;

public class Penguin implements Serializable {

    private static final long serialVersionUID = 1L;
    private static long counter;
    private final String PENGUIN = "penguin";
    private final long BIRTH_DATE;
    private final int BASE_HEALTH_AND_HAPPINESS;

    private boolean isHealthy;
    private boolean isClean;
    private int currentHealth;
    private int currentHappiness;
    private long id;
    private long lastTimeChecked;

    private Collection<String> markedUrlCollection;
    private Collection<PenguinLocation> markedLocations;

    public Penguin() {
        ++counter;
        setId(counter);

        BIRTH_DATE = new DateTime().getMillis();
        BASE_HEALTH_AND_HAPPINESS = IntegerConstants.BASE_HEALTH_AND_HAPPINESS
                .get();

        setCurrentHappiness(BASE_HEALTH_AND_HAPPINESS);
        setCurrentHealth(BASE_HEALTH_AND_HAPPINESS);
        setClean(true);
        setHealthy(true);

        setMarkedUrlCollection(new HashSet<String>());
        setMarkedCoordinates(new HashSet<PenguinLocation>());
    }

    public boolean isDead() {
        return currentHealth <= 0 || currentHappiness <= 0;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public boolean isClean() {
        return isClean;
    }

    public void setHealthy(boolean isHealthy) {
        this.isHealthy = isHealthy;
    }

    public void setClean(boolean isClean) {
        this.isClean = isClean;
    }

    public void feed() {
        currentHealth = BASE_HEALTH_AND_HAPPINESS;
    }

    public void heal() {
        isHealthy = true;
    }

    public void dance() {
        currentHappiness = BASE_HEALTH_AND_HAPPINESS;
    }

    public void clean() {
        isClean = true;
    }

    public UrlMarkRepsonce markUrl(String url) throws IOException {
        Log.d(PENGUIN, "marking url");

        if (getMarkedUrls().contains(url)) {
            Log.d(PENGUIN, "url is in collection");
            return UrlMarkRepsonce.IN_COLLECTION;

        } else if (UrlValidationHandler.isValidUrl(url)) {
            getMarkedUrls().add(url);
            Log.d(PENGUIN, "url marked");

            return UrlMarkRepsonce.FOUND;
        }
        return UrlMarkRepsonce.ERROR;
    }

    public int getMarkedTerritoryPoints() {
        int locationPoints = markedLocations.size()
                * IntegerConstants.LOCATION_MULTIPLIER.get();

        return locationPoints + getMarkedUrls().size();
    }

    public boolean markLocation(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        PenguinLocation penguinLocation = new PenguinLocation(longitude,
                latitude);

        logCoordinates(penguinLocation);

        if (locationInCollection(penguinLocation)) {
            Log.d(PENGUIN, "location is in collection");
            return false;
        }

        markedLocations.add(penguinLocation);

        Log.d(PENGUIN, "location marked, locations in collection: "
                + markedLocations.size());

        return true;
    }

    private void logCoordinates(PenguinLocation penguinLocation) {
        double lon = penguinLocation.getLongitude();
        double lat = penguinLocation.getLatitude();

        String msg = String.format(
                "trying to mark longitude: %s, latitude: %s", lon, lat);

        Log.d(PENGUIN, msg);
    }

    private boolean locationInCollection(PenguinLocation penguinLocation) {
        for (PenguinLocation location : markedLocations)
            if (penguinLocation.equals(location))
                return true;

        return false;
    }

    public int getDaysAlive() {
        DateTime currentTime = new DateTime();
        Days days = Days.daysBetween(getBirthDate(), currentTime);
        return days.getDays();
    }

    public DateTime getBirthDate() {
        return new DateTime(BIRTH_DATE);
    }

    public int getBaseHealth() {
        return BASE_HEALTH_AND_HAPPINESS;
    }

    public int getBaseHappiness() {
        return BASE_HEALTH_AND_HAPPINESS;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getCurrentHappiness() {
        return currentHappiness;
    }

    public synchronized void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public synchronized void setCurrentHappiness(int currentHappiness) {
        this.currentHappiness = currentHappiness;
    }

    public Collection<PenguinLocation> getMarkedLocations() {
        return markedLocations;
    }

    public void setMarkedCoordinates(
            Collection<PenguinLocation> markedCoordinates) {
        this.markedLocations = markedCoordinates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Collection<String> getMarkedUrls() {
        return markedUrlCollection;
    }

    public void setMarkedUrlCollection(Collection<String> markedUrlCollection) {
        this.markedUrlCollection = markedUrlCollection;
    }

    public DateTime getLastTimeChecked() {
        return new DateTime(lastTimeChecked);
    }

    public void setLastTimeChecked(long lastTimeChecked) {
        this.lastTimeChecked = lastTimeChecked;
    }

    @Override
    public String toString() {
        return "Penguin [PENGUIN=" + PENGUIN + ", BIRTH_DATE=" + BIRTH_DATE
                + ", BASE_HEALTH_AND_HAPPINESS=" + BASE_HEALTH_AND_HAPPINESS
                + ", isHealthy=" + isHealthy + ", isClean=" + isClean
                + ", currentHealth=" + currentHealth + ", currentHappiness="
                + currentHappiness + ", id=" + id + ", lastTimeChecked="
                + lastTimeChecked + ", markedUrlCollection="
                + markedUrlCollection + ", markedLocations=" + markedLocations
                + "]";
    }

}
