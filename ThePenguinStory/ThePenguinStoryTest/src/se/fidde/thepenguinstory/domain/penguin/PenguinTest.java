package se.fidde.thepenguinstory.domain.penguin;

import java.io.IOException;
import java.util.Collection;

import org.joda.time.DateTime;

import se.fidde.thepenguinstory.util.constants.IntegerConstants;
import android.content.Context;
import android.location.Location;
import android.test.ActivityTestCase;

public class PenguinTest extends ActivityTestCase {

    private Penguin penguin;
    private int BASE_HEALTH_AND_HAPPINESS = IntegerConstants.BASE_HEALTH_AND_HAPPINESS
            .get();
    private String URL = "www.google.com";
    private String invalidUrl = URL + "/test";
    private long ZERO = 0;
    private Location location = new Location(Context.LOCATION_SERVICE);
    private Location location2 = new Location(Context.LOCATION_SERVICE);

    @Override
    protected void setUp() throws Exception {
        penguin = new Penguin();
    }

    public void testGetPenguin() {
        assertNotNull("penguin not null", penguin);
    }

    public void testGetBirthdate() {
        DateTime birthdate = penguin.getBirthDate();
        assertNotNull("birthdate not null", birthdate);
    }

    public void testIsDead() {
        boolean dead = penguin.isDead();
        assertFalse("not dead", dead);
    }

    public void testGetBaseHealth() {
        int baseHealth = penguin.getBaseHealth();
        assertEquals("can get baseHealth", BASE_HEALTH_AND_HAPPINESS,
                baseHealth);
    }

    public void testGetBaseHappiness() {
        int baseHappiness = penguin.getBaseHappiness();
        assertEquals("can get base happiness", BASE_HEALTH_AND_HAPPINESS,
                baseHappiness);
    }

    public void testGetCurrentHealth() {
        int health = penguin.getCurrentHealth();
        assertEquals("can get currentHealth", BASE_HEALTH_AND_HAPPINESS, health);
    }

    public void testGetCurrentHappiness() {
        int happiness = penguin.getCurrentHappiness();
        assertEquals("can get currentHappiness", BASE_HEALTH_AND_HAPPINESS,
                happiness);
    }

    public void testGetMarkedUrlSet() {
        Collection<String> markedUrlSet = penguin.getMarkedUrls();
        assertNotNull("can get markedUrlSet", markedUrlSet);
    }

    public void testGetMarkedCoordinates() {
        Collection<PenguinLocation> markedCoordinates = penguin
                .getMarkedLocations();
        assertNotNull("can get markedCoordinates", markedCoordinates);
    }

    public void testIsHealthy() {
        boolean healthy = penguin.isHealthy();
        assertTrue("is healthy", healthy);
    }

    public void testIsClean() {
        boolean clean = penguin.isClean();
        assertTrue("is clean", clean);
    }

    public void testFeed() {
        penguin.feed();
        assertEquals("can feed", BASE_HEALTH_AND_HAPPINESS,
                penguin.getCurrentHealth());
    }

    public void testHeal() {
        penguin.heal();
        assertTrue("can heal", penguin.isHealthy());
    }

    public void testDance() {
        penguin.dance();
        assertEquals("can dance", BASE_HEALTH_AND_HAPPINESS,
                penguin.getCurrentHappiness());
    }

    public void testClean() {
        penguin.clean();
        assertTrue("can clean", penguin.isClean());
    }

    public void testMarkUrl() throws IOException {
        penguin.markUrl(URL);
        assertTrue("can mark url", penguin.getMarkedUrls().contains(URL));
    }

    public void testMarkUrl_fail() throws IOException {
        penguin.markUrl(invalidUrl);
        assertFalse("can not mark invalid url", penguin.getMarkedUrls()
                .contains(invalidUrl));
    }

    public void testGetMarkedTerritoryPoints() {
        int markedTerritoryPoints = penguin.getMarkedTerritoryPoints();
        assertEquals("get points", ZERO, markedTerritoryPoints);
    }

    public void testMarkLocation() {
        createMockLocations();

        penguin.markLocation(location);
        assertEquals("can add location", penguin.getMarkedLocations().size(), 1);
    }

    private void createMockLocations() {
        double lon = 1;
        double lat = 1;

        location.setLongitude(lon);
        location2.setLongitude(lon);

        location.setLatitude(lat);
        location2.setLatitude(lat);
    }

    public void testMarkLocation_fail() {
        createMockLocations();

        penguin.markLocation(location);
        penguin.markLocation(location);
        assertEquals("can not add location twice", 1, penguin
                .getMarkedLocations().size());
    }

    public void testMarkLocation_fail_markSameCoordinates() {
        createMockLocations();

        penguin.markLocation(location);
        penguin.markLocation(location2);
        assertEquals("can not add location twice", 1, penguin
                .getMarkedLocations().size());
    }

    public void testGetDaysAlive() {
        int daysAlive = penguin.getDaysAlive();
        assertEquals("can get days alive", ZERO, daysAlive);
    }
}
