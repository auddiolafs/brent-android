package is.hi.hbv601g.brent;

import android.util.Log;

import java.util.Map;

public class EntityParser {

    private static final String TAG = "ProfileActivity";

    public static Bike bikeToEntity(String bikeId, Map<String, Object> bikeData) {
        Bike b = new Bike();
        try {
            b.setId(bikeId);
            b.setBrand(bikeData.get("brand").toString());
            b.setName(bikeData.get("name").toString());
            b.setSize(bikeData.get("size").toString());
            b.setSerial(bikeData.get("serial").toString());
            b.setPrice(Long.parseLong(bikeData.get("ppd").toString()));
            return b;
        } catch (Exception e) {
            Log.d(TAG, "Error parsing bike");
            return null;
        }
    }

    public static Accessory accessoryToEntity(String id, Map<String, Object> data) {
        Accessory a = new Accessory();
        try {
            a.setId(id);
            a.setName(data.get("name").toString());
            a.setPrice(Long.parseLong(data.get("price").toString()));
            a.setType(data.get("type").toString());
            return a;
        } catch (Exception e) {
            Log.d(TAG, "Error parsing accessory");
            return null;
        }
    }

    public static Tour tourToEntity(String tourId, Map<String, Object> tourData) {
        Tour t = new Tour();
        try {
            t.setId(tourId);
            t.setName(tourData.get("name").toString());
            t.setLocation(tourData.get("location").toString());
            t.setPrice(Long.parseLong(tourData.get("price").toString()));
            // TODO: set dates
            return t;
        } catch (Exception e) {
            Log.d(TAG, "Error parsing tour");
            return null;
        }
    }

    public static Booking bookingToEntity(String bookingId, Map<String, Object> bookingData) {
        Booking t = new Booking();
        try {
            t.setId(bookingId);
            t.setPickupLocation(bookingData.get("pickupLocation").toString());
            // TODO: set dates and more
            return t;
        } catch (Exception e) {
            Log.d(TAG, "error");
            return null;
        }
    }
}
