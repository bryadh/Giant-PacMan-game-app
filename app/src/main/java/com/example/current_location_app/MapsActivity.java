package com.example.current_location_app;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "LOCATION DEBUG";

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        CheckUserPermsions();
        loadGhosts();

    }

    @SuppressLint("MissingPermission")
    public void runListener(){

        MyLocationListener myLocationListener = new MyLocationListener(this);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3, myLocationListener);

        MyThread myThread = new MyThread();
        myThread.start();

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

        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera

    }

    /* ********** Check permissions **********  */
    //access to permsions
    public void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

        runListener();// init the contact list

    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    runListener();// init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Location permission denied" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /* ********** Thread **********  */
    Location prevLocation;
    class MyThread extends Thread {

        public MyThread(){
            prevLocation = new Location("start");
            prevLocation.setLongitude(0);
            prevLocation.setLongitude(0);
        }

        public void run(){

            while (true){
                try {
                    //Thread.sleep(1000);

                    if(prevLocation.distanceTo(MyLocationListener.location) == 0){
                        continue;
                    }
                        prevLocation = MyLocationListener.location;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // Add a marker
                            if (MyLocationListener.location != null) {
                                mMap.clear();
                                LatLng currentLocation = new LatLng(MyLocationListener.location.getLatitude(), MyLocationListener.location.getLongitude());
                                mMap.addMarker(new MarkerOptions()
                                        .position(currentLocation)
                                        .title("PACMAN")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pacman))
                                        .snippet(MyLocationListener.location.getLatitude()+","+MyLocationListener.location.getLongitude()));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,14));

                                Log.d(TAG, "run: BEFORE entering ghost generation loop");
                                
                                for(Ghost g : ghostsList){

                                    Log.d(TAG, "run: INSIDE ghost generation loop");
                                    
                                    Location gLocation = new Location("provider");
                                    gLocation.setLatitude(currentLocation.latitude + (Math.random() * (100 - 10)));
                                    gLocation.setLongitude(currentLocation.longitude + (Math.random() * (100 - 10)));

                                    g.setLocation(gLocation);

                                    LatLng gLatLng = new LatLng(g.getLocation().getLatitude(), g.getLocation().getLongitude());
                                    mMap.addMarker(new MarkerOptions()
                                            .position(gLatLng)
                                            .title(g.getName())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ghost1)));
                                }

                                Log.d(TAG, "run: AFTER ghost generation loop");

                            }

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /* ********** Setting the ghosts **********  */
    List<Ghost> ghostsList = new ArrayList<>();
    public void loadGhosts(){

        ghostsList.add(new Ghost(R.drawable.ghost1,"ghost1", false, 0, 0 ));
        ghostsList.add(new Ghost(R.drawable.ghost2,"ghost2", false, 0 , 0 ));
        ghostsList.add(new Ghost(R.drawable.ghost3,"ghost3", false, 0, 0));
    }

}