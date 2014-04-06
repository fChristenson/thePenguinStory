package se.fidde.thepenguinstory.activity;

import se.fidde.thepenguinstory.R;
import se.fidde.thepenguinstory.domain.penguin.Penguin;
import se.fidde.thepenguinstory.domain.service.PenguinService;
import se.fidde.thepenguinstory.domain.service.PenguinService.PenguinBinder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String ACTIVITY = "mainActivity";
    private ProgressBar happinessBar;
    private ProgressBar healthBar;
    private TextView daysAlive;
    private TextView points;
    private PenguinService penguinService;
    private boolean isBound;
    private Penguin penguin;
    private BroadcastReceiver broadcastReceiver;
    private ImageView penguinImage;
    private AnimationDrawable danceAnimationDrawable;

    public void feed(View view) {
        if (penguin == null || penguin.isDead())
            return;

        penguinService.feed();
        updateImage();
        healthBar.setProgress(penguin.getBaseHealth());
        Log.d(ACTIVITY, "feed");
    }

    public void heal(View view) {
        if (penguin == null || penguin.isDead())
            return;

        penguinService.heal();
        updateImage();
        Log.d(ACTIVITY, "heal");
    }

    public void dance(View view) {
        if (penguin == null || penguin.isDead())
            return;

        penguinService.dance();
        happinessBar.setProgress(penguin.getBaseHappiness());
        runDanceAnimation();
        Log.d(ACTIVITY, "dance");
    }

    private void runDanceAnimation() {

        int miliSeconds = 500;
        int imageId = R.drawable.penguin_dance1;
        int imageId2 = R.drawable.penguin_dance2;
        danceAnimationDrawable = new AnimationDrawable();

        danceAnimationDrawable.addFrame(getResources().getDrawable(imageId),
                miliSeconds);
        danceAnimationDrawable.addFrame(getResources().getDrawable(imageId2),
                miliSeconds);
        danceAnimationDrawable.addFrame(getResources().getDrawable(imageId),
                miliSeconds);
        danceAnimationDrawable.addFrame(getResources().getDrawable(imageId2),
                miliSeconds);

        imageId = updateImage();
        danceAnimationDrawable.addFrame(getResources().getDrawable(imageId), 1);

        penguinImage.setImageDrawable(danceAnimationDrawable);
        danceAnimationDrawable.start();
    }

    public void clean(View view) {
        if (penguin == null || penguin.isDead())
            return;

        penguinService.clean();
        updateImage();
        Log.d(ACTIVITY, "clean");
    }

    public void share(View view) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivity(intent);
        Log.d(ACTIVITY, "share");
    }

    public void markTerritory(View view) {
        if (penguin == null || penguin.isDead())
            return;

        Intent intent = new Intent(this, MarkActivity.class);
        startActivity(intent);
        Log.d(ACTIVITY, "mark territory");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(ACTIVITY, "create");
        getViews();
        startPenguinService();

        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                penguin = (Penguin) intent.getSerializableExtra("penguin");
                if (danceAnimationDrawable == null
                        || !danceAnimationDrawable.isRunning())
                    updateImage();

                updateViews();
            }
        };
    }

    public int updateImage() {
        if (penguin.isHealthy() && penguin.isClean()) {
            int result = R.drawable.penguin_normal;
            penguinImage.setImageResource(result);
            return result;

        } else if (!penguin.isHealthy() && !penguin.isClean()) {
            int result = R.drawable.penguin_sick_dirty;
            penguinImage.setImageResource(result);
            return result;

        } else if (penguin.isHealthy()) {
            int result = R.drawable.penguin_dirty;
            penguinImage.setImageResource(result);
            return result;

        } else {
            int result = R.drawable.penguin_sick;
            penguinImage.setImageResource(result);
            return result;
        }
    }

    private void updateViews() {
        healthBar.setProgress(penguin.getCurrentHealth());
        happinessBar.setProgress(penguin.getCurrentHappiness());
        daysAlive.setText(String.valueOf(penguin.getDaysAlive()));
        points.setText(String.valueOf(penguin.getMarkedTerritoryPoints()));
    }

    private void getViews() {
        penguinImage = (ImageView) findViewById(R.id.penguinImage);
        healthBar = (ProgressBar) findViewById(R.id.healthBar);
        happinessBar = (ProgressBar) findViewById(R.id.happinessBar);
        daysAlive = (TextView) findViewById(R.id.daysAliveNumberText);
        points = (TextView) findViewById(R.id.territoryPointsNumberText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(ACTIVITY, "start");
        IntentFilter filter = new IntentFilter(PenguinService.PENGUIN);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, filter);

        Intent service = new Intent(this, PenguinService.class);
        bindService(service, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                broadcastReceiver);

        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        Log.d(ACTIVITY, "stop");
    }

    private void startPenguinService() {
        Intent service = new Intent(this, PenguinService.class);
        startService(service);
        Log.d(ACTIVITY, "penguinService started");
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Log.d(ACTIVITY, "service disconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PenguinBinder binder = (PenguinBinder) service;
            penguinService = binder.getPenguinService();
            isBound = true;
            Log.d(ACTIVITY, "service connected");
        }
    };
}
