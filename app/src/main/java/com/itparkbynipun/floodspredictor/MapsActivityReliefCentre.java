package com.itparkbynipun.floodspredictor;

import android.graphics.Color;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class MapsActivityReliefCentre extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ValueEventListener flooddatalistener;
    private DatabaseReference mDatabase;
    private Vector<getReliefData> reliefDataItems = new Vector<>();
    private LatLng place;
    private Button reliefButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_relief_centre);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("reliefCentres");
        reliefButton = (Button)(findViewById(R.id.reliefCenterBtn));


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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        flooddatalistener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reliefDataItems.clear();

                for(DataSnapshot floodData : dataSnapshot.getChildren()){
                    getReliefData data = new getReliefData();
                    data.setLat(floodData.child("lat").getValue().toString());
                    data.setLongi(floodData.child("long").getValue().toString());
                    data.setTag(floodData.child("tag").getValue().toString());
                    reliefDataItems.add(data);

                }

                mMap.clear();
                for(int i=0;i<reliefDataItems.size();i++){

                    place = new LatLng(Double.parseDouble(reliefDataItems.get(i).getLat().toString()),Double.parseDouble(reliefDataItems.get(i).getLongi().toString()));


                    mMap.addMarker(new MarkerOptions().position(place).title(reliefDataItems.get(i).getTag().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    mMap.addCircle(new CircleOptions()
                            .center(place)
                            .radius(500.0).strokeWidth(3f)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.rgb(150,148,247)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(place));

//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place,15),5000,null);

                }


//                mMap.moveCamera(CameraUpdateFactory.newLatLng(place));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        reliefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(reliefButton.getText().toString().equals("Add Relief Zone")){
                    reliefButton.setText("Click to Add");
                    final String key = mDatabase.push().getKey().toString();
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            mDatabase.removeEventListener(flooddatalistener);
                            mDatabase.child(key).child("lat").setValue(latLng.latitude);
                            mDatabase.child(key).child("long").setValue(latLng.longitude);
                            mDatabase.child(key).child("tag").setValue("Flood Zone"+latLng);
//                            dangerButton.setText("Add Danger Zone");
                            mDatabase.addValueEventListener(flooddatalistener);

                        }
                    });
                }else{
                    mMap.setOnMapClickListener(null);
                    reliefButton.setText("Add Relief Zone");
                    Toast.makeText(MapsActivityReliefCentre.this, "Successfully added", Toast.LENGTH_SHORT).show();


                }


            }
        });
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        mDatabase.removeEventListener(flooddatalistener);
    }

}
