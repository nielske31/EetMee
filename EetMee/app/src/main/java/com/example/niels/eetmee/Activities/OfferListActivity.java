/*
EetMee
Niels van Opstal 11021519

This activity first requests the offers depending on where you came from. If the offers are received
the activity sets an adapter on the listview. If the user is viewing all offers, it can select a date
so that the offers can be filtered to that specific date.
 */
package com.example.niels.eetmee.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;


import com.example.niels.eetmee.Classes.Offer;
import com.example.niels.eetmee.Adapters.OfferAdapter;
import com.example.niels.eetmee.Requests.OfferRequest;
import com.example.niels.eetmee.Enums.OfferRequestType;
import com.example.niels.eetmee.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.GONE;
import static com.example.niels.eetmee.Activities.BaseActivity.myRefChecker;

public class OfferListActivity extends AppCompatActivity implements OfferRequest.Callback {


    private FusedLocationProviderClient mFusedLocationClient;
    private int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private double lat, lng;

    private OfferRequestType offerRequestType;

    private static String dateString;

    private static OfferRequest request;

    public static final int PERMISSION_NOT_GIVEN = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_list_activity);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//            If no permission
            if (checkLocationPermission()) {
                System.out.println("Locatie opvragen toegestaan");

            }
            else {

//                No persmission given so set lat and lng to 100 to imply error
                lat = PERMISSION_NOT_GIVEN;
                lng = PERMISSION_NOT_GIVEN;
            }
        }

//        If location was found succesfully
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                        else {

//                            If something went wrong in getting the locaiton
                            lat = PERMISSION_NOT_GIVEN;
                            lng = PERMISSION_NOT_GIVEN;
                        }
                    }
                });

//        Gets the OfferRequestType so that the correct data can be requested
        offerRequestType = (OfferRequestType) getIntent().getSerializableExtra("afkomst");

//        Creates new OfferRequest
        request = new OfferRequest();

//        Creates a dateString of the current date
        Date date = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd-M-yyyy");
        dateString = df.format(date);

        Button allOffersButton = findViewById(R.id.IkWilEtenButtonList);
        Button myOffersButton = findViewById(R.id.MyOffersButtonList);
        Button joinedOffersButton = findViewById(R.id.JoinedOffersButtonList);

//        Depending on the OfferRequestType received request the correct data
        switch (offerRequestType) {
            case ALLOFFERS:     request.getAllOffers(this, dateString);
                                allOffersButton.setVisibility(View.VISIBLE);
                                myOffersButton.setVisibility(GONE);
                                joinedOffersButton.setVisibility(GONE);
                                break;
            case MYOFFERS:      request.getMyOffers(this);
                                allOffersButton.setVisibility(GONE);
                                myOffersButton.setVisibility(View.VISIBLE);
                                joinedOffersButton.setVisibility(GONE);
                                break;
            case JOINEDOFFERS:  request.getJoinedOffers(this);
                                allOffersButton.setVisibility(GONE);
                                myOffersButton.setVisibility(GONE);
                                joinedOffersButton.setVisibility(View.VISIBLE);
                                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        Only creates possibility to pick date when user has requested all offers
            if (offerRequestType == OfferRequestType.ALLOFFERS) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.actionbar, menu);
                return true;
            }
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Show the datepicker when the datepicker icon is selected
        DialogFragment newDateFragment = new DatePickerFragment();
        newDateFragment.show(getFragmentManager(), "datePicker");
        return true;
    }



    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

//            Create dateString based on the picked data and request the offers on that date
            dateString = Integer.toString(dayOfMonth) + "-" + Integer.toString(month + 1) + "-" + Integer.toString(year);
            OfferRequest.Callback activity = (OfferListActivity) getActivity();
            request.getAllOffers(activity, dateString);

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

//            Set the date to current date when datepicker is created
            final Calendar c =Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


    }


    @Override
    public void gotOffers(ArrayList<Offer> offers) {

        ListView offerList = findViewById(R.id.OfferListView);
        if (offers.isEmpty()) {

//            If there are no offers received, show an message that says so
            findViewById(R.id.GotNoOffersTextView).setVisibility(View.VISIBLE);
            offerList.setAdapter(new OfferAdapter(this, 0, offers, lat, lng));
        } else {
            findViewById(R.id.GotNoOffersTextView).setVisibility(GONE);
//            Else, sets an adapter and an onItemClickListener on the listView

            offerList.setAdapter(new OfferAdapter(this, 0, offers, lat, lng));
            offerList.setOnItemClickListener(new onItemmClickListener());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        myRefChecker.checker();

//        Requests the data again to refresh the data showed
        switch (offerRequestType) {
            case ALLOFFERS:     request.getAllOffers(this, dateString);
                break;
            case MYOFFERS:      request.getMyOffers(this);
                break;
            case JOINEDOFFERS:  request.getJoinedOffers(this);
                break;
        }

    }


    @Override
    public void gotOffersError(String message) {
        Toast.makeText(OfferListActivity.this, "something went wrong. \n"
                + message, Toast.LENGTH_SHORT).show();
        Log.d("Error", message);
    }

    public class onItemmClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            If an item from the listview is clicked go to the details of that offer
            Offer offerClicked = (Offer) parent.getItemAtPosition(position);
            Intent intent = new Intent(OfferListActivity.this, DetailActivity.class);
            intent.putExtra("offer", offerClicked);
            startActivity(intent);
        }
    }
    private boolean checkLocationPermission() {
//        If permission is not granted to access fine location
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(OfferListActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

//                Create a dialog that asks the user for permission
                new AlertDialog.Builder(OfferListActivity.this)
                        .setTitle("Permission needed")
                        .setMessage("Allow permission to get your location")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

//                                Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions( OfferListActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(OfferListActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}

