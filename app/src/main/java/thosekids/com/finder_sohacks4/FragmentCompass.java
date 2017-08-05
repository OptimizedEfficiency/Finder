package thosekids.com.finder_sohacks4;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import static android.content.Context.SENSOR_SERVICE;

public class FragmentCompass extends Fragment implements SensorEventListener{

    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    private Location locationA = new Location("A");
    private Location target = new Location("B");
    private LocationManager locationManager;
    GeomagneticField geoField = new GeomagneticField(
            (float) target.getLatitude(),
            (float) target.getLongitude(),
            (float) target.getAltitude(),
            System.currentTimeMillis());
    //test comment
//    private LatLng latLng;



 /*   public void onLocationChanged(Location location) {
        if(location!=null){
            latLng = new LatLng(location.getLatitude(), location.getLongitude()); }
    }*/

    LocationManager lm;
    Location location;
    double longitude;
    double latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_fragmentcompass);
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);

        target.setLatitude(29.463362);
        target.setLongitude(-98.484409);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragmentcompass, container, false);
        image = (ImageView) view.findViewById(R.id.compass);

        return view;

    }


}