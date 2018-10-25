package com.itparkbynipun.floodspredictor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    LatLng place;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    Button dangerButton;
    Vector<getFloodData> floodDataItems = new Vector<>();
    ValueEventListener flooddatalistener;
    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dangerButton = (Button)(findViewById(R.id.dangerZoneBtn));
        mprogress = new ProgressDialog(this);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mAuth = FirebaseAuth.getInstance();
        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() == null) {
                    Intent login = new Intent(MapsActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthlistener);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("floodZones");




    }

    @Override
    public void onStart() {
        super.onStart();
        dangerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dangerButton.getText().toString().equals("Add Danger Zone")){
                    dangerButton.setText("Click to Add");
                    final String key = mDatabase.push().getKey().toString();
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            mDatabase.removeEventListener(flooddatalistener);
                            mDatabase.child(key).child("lat").setValue(latLng.latitude);
                            mDatabase.child(key).child("long").setValue(latLng.longitude);
                            mDatabase.child(key).child("tag").setValue("Flood Zone");
                            mDatabase.child(key).child("user").setValue("true");
//                            dangerButton.setText("Add Danger Zone");
                            mDatabase.addValueEventListener(flooddatalistener);

                        }
                    });
                }else{
                    mMap.setOnMapClickListener(null);
                    dangerButton.setText("Add Danger Zone");
                    Toast.makeText(MapsActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();


                }


            }
        });
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        mDatabase.removeEventListener(flooddatalistener);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mprogress.dismiss();
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        flooddatalistener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                floodDataItems.clear();

                for(DataSnapshot floodData : dataSnapshot.getChildren()){
                    getFloodData data = new getFloodData();
                    data.setLat(floodData.child("lat").getValue().toString());
                    data.setLongi(floodData.child("long").getValue().toString());
                    data.setTag(floodData.child("tag").getValue().toString());
                    data.setUser(floodData.child("user").getValue().toString());
                    floodDataItems.add(data);

                }

                mMap.clear();
                for(int i=0;i<floodDataItems.size();i++){

                    place = new LatLng(Double.parseDouble(floodDataItems.get(i).getLat().toString()),Double.parseDouble(floodDataItems.get(i).getLongi().toString()));

                    if(floodDataItems.get(i).getUser().toString().equals("true")){
                        mMap.addMarker(new MarkerOptions().position(place).title(floodDataItems.get(i).getTag().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.addCircle(new CircleOptions()
                                .center(place)
                                .radius(500.0).strokeWidth(3f)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.rgb(150,148,247)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(place));
                    }else{
                        mMap.addMarker(new MarkerOptions().position(place).title(floodDataItems.get(i).getTag().toString()));
                        mMap.addCircle(new CircleOptions()
                                .center(place)
                                .radius(500.0).strokeWidth(3f)
                                .strokeColor(Color.RED)
                                .fillColor(Color.rgb(255,84,101)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(place));
                    }


//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place,15),5000,null);

                }


//                mMap.moveCamera(CameraUpdateFactory.newLatLng(place));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

    }
}
