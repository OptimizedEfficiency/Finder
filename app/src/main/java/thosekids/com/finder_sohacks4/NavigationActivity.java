package thosekids.com.finder_sohacks4;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.hardware.SensorEventListener;
import android.view.animation.*;
import android.location.*;
import android.hardware.*;
import android.content.*;

public class NavigationActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    private Location location = new Location("A");
    private Location target = new Location("B");
    private LocationManager locationManager;
    GeomagneticField geoField = new GeomagneticField(
            (float) target.getLatitude(),
            (float) target.getLongitude(),
            (float) target.getAltitude(),
            System.currentTimeMillis());
    //test comment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.compass);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        location.setLatitude(29.464093);
        location.setLongitude(-98.483757);

        target.setLatitude(29.463362);
        target.setLongitude(-98.484409);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this); // to stop the listener and save battery
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        degree += geoField.getDeclination();

        float bearing = location.bearingTo(target);
        degree = (bearing - degree) * -1;
        degree = normalizeDegree(degree);

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    private float normalizeDegree(float value) {
        if (value >= 0.0f && value <= 180.0f) {
            return value;
        } else {
            return 180 + (180 + value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}