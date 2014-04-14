package se.fidde.thepenguinstory.domain.service;

import java.io.IOException;

import org.joda.time.DateTime;

import se.fidde.thepenguinstory.domain.penguin.Penguin;
import se.fidde.thepenguinstory.util.constants.FloatConstants;
import se.fidde.thepenguinstory.util.constants.IntegerConstants;
import se.fidde.thepenguinstory.util.constants.UrlMarkRepsonce;
import se.fidde.thepenguinstory.util.factory.PenguinFactory;
import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class PenguinService extends IntentService {

    public static final String PENGUIN = "se.fidde.thepenguinstory.penguin";
    private static final String SERVICE = "penguinService";
    private static LocalBroadcastManager broadcastManager;
    private static Penguin penguin;
    private static boolean isRunning;
    private final Binder binder = new PenguinBinder();
    private String path;

    public PenguinService() {
        super(SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent arg0) {

        Log.d(SERVICE, "starting service");
        path = getFilesDir().getPath();
        isRunning = true;

        while (isRunning) {
            runService();
        }
        PenguinFactory.savePenguin(penguin, path);
    }

    private void runService() {
        try {
            updateState();
            PenguinFactory.savePenguin(penguin, path);
            broadCast(penguin);
            logPenguinStatus();
            sleep();

        } catch (InterruptedException e) {
            Log.e(SERVICE, "error, killing service");
            PenguinFactory.savePenguin(penguin, path);
            isRunning = false;
        }
    }

    private void loadPenguin() {
        if (penguin == null || penguin.isDead()) {
            Log.d(SERVICE, "penguin is dead, getting new penguin");
            penguin = PenguinFactory.loadPenguin(path);
        }
    }

    private void updateState() {
        loadPenguin();
        int timesToUpdate = getTimesToUpdateState();

        if (timesToUpdate > 0) {
            for (int i = 0; i < timesToUpdate; ++i) {
                updateHealth();
                updateHappiness();
                updateSickness();
                updateCleanliness();
                Log.d(SERVICE, "state updated: " + i);
            }
            long newTime = new DateTime().getMillis();
            penguin.setLastTimeUpdated(newTime);
        }
    }

    private int getTimesToUpdateState() {
        DateTime lastTimeChecked = penguin.getLastTimeUpdated();
        if (lastTimeChecked.getMillis() == 0)
            return 1;

        DateTime timeDifference = new DateTime().minus(lastTimeChecked
                .getMillis());
        int timeBeforeCheck = IntegerConstants.TIME_BEFORE_STATUS_UPDATE.get();
        int timesToUpdate = (int) (timeDifference.getMillis() / timeBeforeCheck);
        return timesToUpdate;
    }

    private void broadCast(Penguin penguin) {
        Intent intent = new Intent(PENGUIN);
        intent.putExtra("penguin", penguin);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(intent);
    }

    private void updateHealth() {
        int currentHealth = penguin.getCurrentHealth();

        int sicknessMultiplier = IntegerConstants.SICKNESS_MULTIPLIER.get();
        int updatedMultiplier = penguin.isHealthy() ? 1 : sicknessMultiplier;
        int decayRate = IntegerConstants.BASE_DECAYRATE.get();

        int decay = decayRate * updatedMultiplier;
        currentHealth = currentHealth - decay;

        String string = String.format("setting health: %d", currentHealth);
        Log.d(SERVICE, string);

        penguin.setCurrentHealth(currentHealth);
    }

    private void updateHappiness() {
        int currentHappiness = penguin.getCurrentHappiness();

        int multiplier = IntegerConstants.DIRTY_MULTIPLIER.get();
        int dirtyMultiplier = penguin.isClean() ? 1 : multiplier;
        int decayRate = IntegerConstants.BASE_DECAYRATE.get();

        int decay = decayRate * dirtyMultiplier;
        currentHappiness = currentHappiness - decay;

        String string = String
                .format("setting happiness: %d", currentHappiness);
        Log.d(SERVICE, string);

        penguin.setCurrentHappiness(currentHappiness);
    }

    private void updateSickness() {
        float chanceToGetSick = FloatConstants.CHANCE_TO_GET_SICK.get();

        if (Math.random() < chanceToGetSick) {
            penguin.setHealthy(false);

            String string = String.format("penguin set to unhealthy");
            Log.d(SERVICE, string);

        } else {
            Log.d(SERVICE, "penguin does not get sick");
        }
    }

    private void updateCleanliness() {
        float chanceToGetDirty = FloatConstants.CHANCE_TO_GET_DIRTY.get();

        if (Math.random() < chanceToGetDirty) {
            penguin.setClean(false);

            String string = String.format("penguin set to unclean");
            Log.d(SERVICE, string);

        } else {
            Log.d(SERVICE, "penguin does not get dirty");
        }
    }

    private void sleep() throws InterruptedException {
        Log.d(SERVICE, "serviceThread sleep");

        Thread.sleep(1000);

        Log.d(SERVICE, "serviceThread wakes");
    }

    private void logPenguinStatus() {
        int currentHealth = penguin.getCurrentHealth();
        int currentHappiness = penguin.getCurrentHappiness();
        int daysAlive = penguin.getDaysAlive();
        int markedTerritoryPoints = penguin.getMarkedTerritoryPoints();

        String string = String
                .format("penguin not dead, health: %s, happiness: %s, daysAlive: %s, points: %s",
                        currentHealth, currentHappiness, daysAlive,
                        markedTerritoryPoints);

        Log.d(SERVICE, string);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(SERVICE, "bind");
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(SERVICE, "service being destroyed");
        PenguinFactory.savePenguin(penguin, path);
    }

    public boolean markLocation(Location location) {
        return penguin.markLocation(location);
    }

    public UrlMarkRepsonce markUrl(String url) throws IOException {
        return penguin.markUrl(url);
    }

    public void feed() {
        penguin.feed();
    }

    public void dance() {
        penguin.dance();
    }

    public void clean() {
        penguin.clean();
    }

    public void heal() {
        penguin.heal();
    }

    public class PenguinBinder extends Binder {

        public PenguinService getPenguinService() {
            return PenguinService.this;
        }
    }
}
