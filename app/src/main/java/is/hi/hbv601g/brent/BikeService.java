package is.hi.hbv601g.brent;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BikeService {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "BikesActivity";

    public BikeService() { }

    public static List<Bike> getBikes() {
        final List<Bike> bikes = new ArrayList<>();
        final Task<QuerySnapshot> task = db.collection("bikes")
                .get();
                /*.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                bikes.add(jsonToEntity(document.getData()));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    bikes.add(jsonToEntity(document.getData()));
                    // Log.d(bikes.get(0).getName());
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.d(TAG, "error");
            }
        });


        /*while (!task.isComplete()) {
            Log.d(TAG, "Loading bikes");
        }*/

        return bikes;
    }

    private static Bike jsonToEntity(Map<String, Object> jsonBike) {
        Bike b = new Bike();
        try {
            b.setId(jsonBike.get("id").toString());
            b.setBrand(jsonBike.get("brand").toString());
            b.setName(jsonBike.get("name").toString());
            b.setSize(jsonBike.get("size").toString());
            b.setSerial(jsonBike.get("serial").toString());
            b.setPrice(Long.parseLong( jsonBike.get("ppd").toString()));
            return b;
        } catch (Exception e) {
            return null;
        }
    }
}
