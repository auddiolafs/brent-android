package is.hi.hbv601g.brent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.models.Tour;

public class TourActivity extends CurrentActivity {

    TextView mTitle;
    TextView mLocation;
    // TextView mLength;
    // TextView mLikes;
    // TextView mDescription;
    TextView mPrice;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_tour);
        // Get toolbar in layout (defined in xml file)
        super.setUp();
        loadData();
    }

    private void loadData() {
        Intent toursActivity_intent = getIntent();
        final Tour tour = toursActivity_intent.getParcelableExtra("tour");
        mTitle = findViewById(R.id.place_name);
        mLocation = findViewById(R.id.place_info_1);
        mPrice = findViewById(R.id.place_info_2);
        // mLength = findViewById(R.id.place_info_1);
        // mLikes = findViewById(R.id.place_info_2);
        // mDescription = findViewById(R.id.place_description);
        mImage = findViewById(R.id.tour_image);
        mTitle.setText(tour.getName());
        mLocation.setText(tour.getLocation());
        mPrice.setText(tour.getPrice().toString());
        // mLength.setText(route.getLength() + " km");
        // mDescription.setText(route.getDescription());
        // mLikes.setText(route.getLikes() + " likes");
        Picasso.get().load(tour.getImage()).into(mImage);
    }
}
