package thosekids.com.finder_sohacks4;

import java.util.Date;

/**
 * Created by drewneely on 8/5/17.
 */

public class LocationUpdate {

    public double longitude;
    public double latitude;
    public long time;

    public LocationUpdate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        time = (new Date()).getTime();
    }
}
