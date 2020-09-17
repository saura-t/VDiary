package com.amogomsau.vdiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddEntry extends AppCompatActivity {

    private EditText title, description;
    private Button add;
    private ImageView imageButton;
    private TextView location;
    private LocationManager locationManager;
    double latitude;
    double longitude;
    private static final int REQUEST_LOCATION = 1;
    private static final int Gallery_request_code = 123;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setImageBitmap(imageBitmap);
        } else if(requestCode == Gallery_request_code && resultCode == RESULT_OK && data != null) {
            Uri object = data.getData();
            imageButton.setImageURI(object);
        }
    }

    private boolean isLocationEnabled(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void gps_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wait, Location Seems Off. Turn On To Find Post Related To Your Location.")
                .setMessage("Please Select High Priority!")
                .setCancelable(false)
                .setPositiveButton("Sure",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("Whatever",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        add = findViewById(R.id.btnAddEntry);
        imageButton = findViewById(R.id.imageButton);
        location = findViewById(R.id.location);

        if (!isLocationEnabled()) {
            gps_dialog();
        } else {
            try {
                @SuppressLint("MissingPermission") Location locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationGPS != null) {
                    latitude = locationGPS.getLatitude();
                    longitude = locationGPS.getLongitude();
                    // Toast.makeText(getApplicationContext(), latitude + " : " + longitude, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Finding...", Toast.LENGTH_SHORT).show();
                }
                System.out.println(longitude + " : " + latitude);
                if (latitude != 0.0 && longitude != 0.0) {
                    Geocoder gcd = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    assert addresses != null;
                    if (addresses.size() > 0) {
                        location.setText(addresses.get(0).getLocality());
                    }
                }
            }
            catch (Exception e) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            }
        }

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), Gallery_request_code);
                } catch (Exception e) {

                }
            }
        });

        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    dispatchTakePictureIntent();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            

        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper mango = new DatabaseHelper(AddEntry.this);
                mango.addEntry(2, title.getText().toString().trim(), description.getText().toString().trim(), title.getText().toString().trim(), title.getText().toString().trim(), title.getText().toString().trim());
            }
        });
    }
}