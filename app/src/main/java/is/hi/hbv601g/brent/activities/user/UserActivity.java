package is.hi.hbv601g.brent.activities.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import is.hi.hbv601g.brent.R;
import is.hi.hbv601g.brent.activities.CurrentActivity;
import is.hi.hbv601g.brent.activities.MainActivity;

public class UserActivity extends CurrentActivity {

    Button mLogoutButton;

    TextView mDisplayname;
    TextView mEmailText;
    TextView mBookingText;
    View booking;

    ImageView imgUserPhoto;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    FirebaseAuth mAuth;
    FirebaseApp mApp;

    Uri pickedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.connected) {
            setUp();
        }
    }

    @Override
    public void setUp() {
        setContentView(R.layout.activity_user);
        super.setUp();

        mApp = FirebaseApp.getInstance();
        mAuth = FirebaseAuth.getInstance(mApp);

        // Get toolbar in layout (defined in xml file

        mLogoutButton = findViewById(R.id.logoutButton);
        mDisplayname = findViewById(R.id.user_name_text);
        mEmailText = findViewById(R.id.user_email_text);
        imgUserPhoto = findViewById(R.id.user_portrait);

        if (mAuth.getCurrentUser().getPhotoUrl() != null) {
            Uri image = mAuth.getCurrentUser().getPhotoUrl();
//            imgUserPhoto.setImageURI(image);
            Picasso.get().load(image).into(imgUserPhoto);
        }

        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });

        booking = findViewById(R.id.user_bookings);
        TextView bookingText = booking.findViewById(R.id.card_text_id);
        bookingText.setText("Bookings");
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bikesIntent = new Intent(getApplicationContext(), BookingsActivity.class);
                startActivity(bikesIntent);
            }
        });

        ImageView bookingImage = booking.findViewById(R.id.card_image_id);
        bookingImage.setImageResource(R.drawable.icon_list);

        mDisplayname.setText(mAuth.getCurrentUser().getDisplayName());
        mEmailText.setText(mAuth.getCurrentUser().getEmail());

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
    }

    private void updateUserInfo(Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());

        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Log.d("USERPROFILE >>", "Updated!");
                                            updateUI();
                                        }

                                    }
                                });

                    }
                });
            }
        });

    }

    private void updateUI() {

        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish();

    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(UserActivity.this, "Please accept required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else {
            openGallery();
        }

    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {

            pickedImgUri = data.getData();
            Log.d("UserAc", pickedImgUri.toString());
            imgUserPhoto.setImageURI(pickedImgUri);

            updateUserInfo(pickedImgUri, mAuth.getCurrentUser());

        }

    }
}
