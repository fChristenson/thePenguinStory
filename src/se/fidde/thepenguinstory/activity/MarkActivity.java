package se.fidde.thepenguinstory.activity;

import java.io.IOException;

import se.fidde.thepenguinstory.R;
import se.fidde.thepenguinstory.domain.service.PenguinService;
import se.fidde.thepenguinstory.domain.service.PenguinService.PenguinBinder;
import se.fidde.thepenguinstory.util.constants.UrlMarkRepsonce;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MarkActivity extends Activity implements LocationListener {

    private final String ACTIVITY = "markActivity";
    private boolean isBound;
    private PenguinService penguinService;
    private LocationManager locationManager;

    public void markUrl(View view) throws IOException {
        EditText field = (EditText) findViewById(R.id.urlMarkingField);
        Editable text = field.getText();
        new UrlLoader().execute(text.toString());

        field.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.d(ACTIVITY, "markActivity created");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, PenguinService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        Log.d(ACTIVITY, "markActivity started");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        Log.d(ACTIVITY, "markActivity stopped");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String bestProvider = locationManager.getBestProvider(criteria, true);
        Log.d(ACTIVITY, "location provider found:" + bestProvider);

        if (bestProvider == null) {
            Toast makeText = Toast.makeText(this,
                    "No location provider was found", Toast.LENGTH_SHORT);
            makeText.show();
            return;
        }

        locationManager.requestLocationUpdates(bestProvider, 0, 0, this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PenguinBinder binder = (PenguinBinder) service;
            penguinService = binder.getPenguinService();
            isBound = true;

            Log.d(ACTIVITY, "handlerService bound");
        }
    };

    private class UrlLoader extends AsyncTask<String, Integer, UrlMarkRepsonce> {

        @Override
        protected UrlMarkRepsonce doInBackground(String... params) {
            try {
                Log.d(ACTIVITY, "connecting to: " + params[0]);
                return penguinService.markUrl(params[0]);

            } catch (IOException e) {
                return UrlMarkRepsonce.ERROR;
            }
        }

        @Override
        protected void onPostExecute(UrlMarkRepsonce result) {
            super.onPostExecute(result);

            Toast makeText = Toast.makeText(getApplicationContext(), "",
                    Toast.LENGTH_SHORT);

            switch (result) {
            case FOUND:
                makeText.setText("Website added");
                break;

            case IN_COLLECTION:
                makeText.setText("Website is in your collection!");
                break;

            default:
                makeText.setText("Website was not found");
                break;
            }
            makeText.show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        boolean markLocation = penguinService.markLocation(location);

        Toast makeText = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        if (markLocation)
            makeText.setText(R.string.location_marked);

        else
            makeText.setText(R.string.location_not_marked);

        makeText.show();
    }

    @Override
    public void onProviderDisabled(String arg0) {
    }

    @Override
    public void onProviderEnabled(String arg0) {
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }
}
