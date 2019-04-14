package is.hi.hbv601g.brent.services;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import is.hi.hbv601g.brent.activities.model.AccessoriesActivity;
import is.hi.hbv601g.brent.models.Accessory;

public class AccessoryService {

    private AccessoriesActivity accessoriesActivity;
    private FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    private static final String mTAG = "AccessoryService";
    private static ArrayList<Accessory> mAccessories;

    public AccessoryService(AccessoriesActivity accessoriesActivity) {
        this.accessoriesActivity = accessoriesActivity;
    }

    public ArrayList<Accessory> getAccessories() {
        return mAccessories;
    }

    /**
     * Fetches all accessories from Firestore db, to be displayed in the accessories list.
     */
    public void fetchAccessories() {
        final ArrayList<Accessory> accessories = new ArrayList<>();
        final Task<QuerySnapshot> task = mDB.collection("accessories").get();

        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    accessories.add(Accessory.toEntity(document.getId(), document.getData()));
                }

                mAccessories = accessories;
                accessoriesActivity.setIsDataFetched(true);
                accessoriesActivity.setUp();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                accessoriesActivity.setUp();
                Log.d(mTAG, "error");
            }
        });
    }
}
