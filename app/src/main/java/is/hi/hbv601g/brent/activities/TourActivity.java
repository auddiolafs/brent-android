package is.hi.hbv601g.brent.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import is.hi.hbv601g.brent.Cart;
import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.models.Tour;

public class TourActivity extends CurrentActivity {

    private TextView mTitle;
    private TextView mLocation;
    private TextView mDuration;
    private TextView mDescription;
    private TextView mPrice;
    private TextView mDepartureTime;
    private ImageView mImage;
    private EditText mNumberOfTravelers;
    private final Calendar mDate = Calendar.getInstance();

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
        Intent toursActivity_intent = getIntent();
        final Tour tour = toursActivity_intent.getParcelableExtra("tour");
        loadData(tour);
        setDatePickers();
        setButtonOnClick(tour);
    }

    private void loadData(Tour tour) {
        mTitle = findViewById(R.id.place_name);
        mLocation = findViewById(R.id.place_info_1);
        mPrice = findViewById(R.id.place_info_2);
        mDuration = findViewById(R.id.place_info_3);
        mDepartureTime = findViewById(R.id.place_info_4);
        mDescription = findViewById(R.id.place_description);
        mNumberOfTravelers = findViewById(R.id.travelersEdit);
        mImage = findViewById(R.id.tour_image);
        mTitle.setText(tour.getName());
        mLocation.setText(tour.getLocation());
        mPrice.setText(tour.getPrice().toString() + " ISK");
        mDuration.setText(tour.getDuration().toString() + " hours");
        mDepartureTime.setText(tour.getDepartureTime());
        mDescription.setText(tour.getDescription());
        Picasso.get().load(tour.getImage()).into(mImage);
    }

    /**
     * Creates the datepicker.
     * Sets an onClickListener for the datepicker.
     */
    private void setDatePickers() {
        final EditText dateText = findViewById(R.id.dateText);
        final Calendar cldr = Calendar.getInstance();
        dateText.setInputType(InputType.TYPE_NULL);
        mDate.setTime(new Date());
        dateText.setText(cldr.get(Calendar.DAY_OF_MONTH) + "/" + cldr.get(Calendar.MONTH) + "/" +
                cldr.get(Calendar.YEAR));

        dateText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        DatePickerDialog startPicker = new DatePickerDialog(TourActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int day) {
                                        dateText.setText(day + "/" + (month + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        mDate.setTime(new Date(year, month, day));
                        startPicker.show();
                    }
                });
    }

    private void setButtonOnClick(final Tour tour) {
        TextView bookButton= findViewById(R.id.button_book_tour);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String travelers = mNumberOfTravelers.getText().toString();
                final Long numberOfTravelers;
                if (travelers.length() == 0) {
                    showMessage("Please add a number of travelers");
                } else {
                    numberOfTravelers = Long.parseLong(travelers);
                    Cart cart = Cart.getCart();
                    tour.setDate(mDate.getTime());
                    tour.setNumberOfTravelers(numberOfTravelers);
                    cart.addTourToCart(tour);
                    for (int i = 0; i < numberOfTravelers; i++) {
                        cart.setTotalPrice(cart.getTotalPrice() + tour.getPrice());
                    }
                    showMessage("Tour added to cart");
                    Intent toursIntent = new Intent(getApplicationContext(), ToursActivity.class);
                    startActivity(toursIntent);
                }
            }
        });
    }

    /**
     * Creates a toast message.
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
