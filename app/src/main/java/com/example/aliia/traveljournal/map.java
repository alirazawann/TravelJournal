package com.example.aliia.traveljournal;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class map extends Activity implements OnMapReadyCallback,LocationEngineListener,PermissionsListener,MapboxMap.OnMapClickListener
{

    private MapView mapView;
    private MapboxMap map;
   // private Button search;
    private PermissionsManager permissionManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef,databaseReference;
    DatabaseReference rootRef;
    DatabaseReference usersdRef;
    private List<LatLng> mUploads;

    private static final String TAG="MainActivity";
   // private EditText location_tf;
    public List<String> req,req2,req3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.activity_map);
        mapView=(MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        //mapView.getMapAsync(this);
        rootRef = FirebaseDatabase.getInstance().getReference();
        mUploads=new ArrayList<>();
        mStorage=FirebaseStorage.getInstance();

        databaseReference=FirebaseDatabase.getInstance().getReference();
        req= new ArrayList<>();
        req2= new ArrayList<>();
        req3= new ArrayList<>();

       // List<MarkerOptions> markers = new ArrayList<>();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                map=mapboxMap;
                enableLocation();
                up();
                for(int i=0;i<mUploads.size();i++) {

                }
            }
        });

    }
    public void up()
    {
        rootRef = FirebaseDatabase.getInstance().getReference();

        //mUploads.clear();



        usersdRef = rootRef.child("uploads");




        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.exists()) {

                        String name = ds.getKey();


                        req.add(name);
                        Log.d("Image TAG req 1", name);


                    }

                }



                for (int i = 0; i < req.size(); i++) {

                    mDatabaseRef = rootRef.child("uploads/" + req.get(i) + "/");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.d("Inside req 1", "inner");
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                Log.d("c",upload.getLati());
                                if(upload.getLati()!=null && upload.getLati().length()>0 ) {
                                    LatLng location = new LatLng(Double.parseDouble(upload.getLati()),Double.parseDouble( upload.getAlti()));
                                    mUploads.add(location);
                                    MarkerOptions options = new MarkerOptions();
                                    options.position(new LatLng(Double.parseDouble(upload.getLati()),Double.parseDouble( upload.getAlti())));
                                    //options.position(new LatLng(48.1386,11.51603));

                                    Log.d("indide loop","yyayy");
                                    map.addMarker(options);
                                }

                            }
                         //   mAdapter.notifyDataSetChanged();
                         //   mProgressCircle.setVisibility(View.INVISIBLE);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        usersdRef = rootRef.child("Outofcityuploads");

        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.exists()) {

                        String name = ds.getKey();


                        req2.add(name);


                    }

                }



                for (int i = 0; i < req2.size(); i++) {

                    mDatabaseRef = rootRef.child("Outofcityuploads/" + req2.get(i) + "/");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                Log.d("c",upload.getLati());
                                if(upload.getLati()!=null && upload.getLati().length()>0 ) {
                                    LatLng location = new LatLng(Double.parseDouble(upload.getLati()),Double.parseDouble( upload.getAlti()));
                                    mUploads.add(location);
                                    MarkerOptions options = new MarkerOptions();
                                    options.position(new LatLng(Double.parseDouble(upload.getLati()),Double.parseDouble( upload.getAlti())));
                                    //options.position(new LatLng(48.1386,11.51603));

                                    Log.d("indide loop","yyayy");
                                    map.addMarker(options);
                                }

                            }
                           // mAdapter.notifyDataSetChanged();

                           // mProgressCircle.setVisibility(View.INVISIBLE);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        usersdRef = rootRef.child("outofstateuploads");

        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.exists()) {

                        String name = ds.getKey();


                        req3.add(name);


                    }

                }



                for (int i = 0; i < req3.size(); i++) {

                    mDatabaseRef = rootRef.child("outofstateuploads/" + req3.get(i) + "/");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                Log.d("c",upload.getLati());
                                if(upload.getLati()!=null && upload.getLati().length()>0 ) {
                                    LatLng location = new LatLng(Double.parseDouble(upload.getLati()),Double.parseDouble( upload.getAlti()));
                                    mUploads.add(location);
                                    MarkerOptions options = new MarkerOptions();
                                    options.position(new LatLng(Double.parseDouble(upload.getLati()),Double.parseDouble( upload.getAlti())));
                                    //options.position(new LatLng(48.1386,11.51603));

                                    Log.d("indide loop","yyayy");
                                    map.addMarker(options);
                                }

                            }
                           // mAdapter.notifyDataSetChanged();

                           // mProgressCircle.setVisibility(View.INVISIBLE);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }









            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
     //   map=mapboxMap;
    //    enableLocation();

    }
    private void enableLocation()
    {
        if(PermissionsManager.areLocationPermissionsGranted(this))
        {
            initializeLocationEngine();
            initializeLocationLayer();
        }
        else

        {
            permissionManager=new PermissionsManager(this);
            permissionManager.requestLocationPermissions(this);
        }
    }
    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine()
    {
        locationEngine=new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation=locationEngine.getLastLocation();
        if(lastLocation!=null)
        {
            originLocation=lastLocation;
            setCameraPosition(lastLocation);
        }
        else

        {
            locationEngine.addLocationEngineListener(this);
        }
    }
    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer()
    {

        ///map.removeLayer("mapbox-location-shadow");
        //map.removeLayer("mapbox-location-layer");
        locationLayerPlugin=new LocationLayerPlugin(mapView,map,locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    private void setCameraPosition(Location location)
    {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),13.0));
    }

    @Override
    public void onMapClick(@NonNull LatLng point)
    {

        /*
        if(destinationMarker!=null)
        {
            map.removeMarker(destinationMarker);

        }
        destinationMarker=map.addMarker(new MarkerOptions().position(point));

        destinationPosition=Point.fromLngLat(point.getLongitude(),point.getLatitude());
        originPosition=Point.fromLngLat(originLocation.getLongitude(),originLocation.getLatitude());


        //getRoute(originPosition,destinationPosition);*/
    }
    private void getRoute(Point origin,Point destination)
    {
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                        if(response.body()==null)
                        {
                            Log.e(TAG,"No Royes found,check right uset and access token");
                            return;
                        }
                        else if(response.body().routes().size()==0)
                        {
                            Log.e(TAG,"No routes found");
                            return;
                        }
                        DirectionsRoute currentRoute=response.body().routes().get(0);
                        if(navigationMapRoute!=null)
                        {
                            navigationMapRoute.removeRoute();
                        }
                        else
                        {
                            navigationMapRoute=new NavigationMapRoute(null,mapView,map);


                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        Log.e(TAG,"Error"+t.getMessage());
                    }
                });

    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {

        locationEngine.requestLocationUpdates();

    }

    @Override
    public void onLocationChanged(Location location) {

        if(location!=null)
        {
            originLocation=location;
            setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {


    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted)
        {
            enableLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    @SuppressWarnings("MissingPermission")

    public void onStart()
    {

        super.onStart();
        if(locationEngine!=null)
        {
            locationEngine.requestLocationUpdates();

        }
        if(locationLayerPlugin!=null)
        {
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onStop()
    {
        super.onStop();
        if(locationEngine!=null)
        {
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin!=null)
        {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(locationEngine!=null)
        {
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }



    public void onsearch()
    {

        String location="Lahore";//=location_tf.getText().toString();

        if(location!=null || location.equals(""))
        {
            Geocoder geocoder=new Geocoder(this);
            int i=0;
            try
            {
                List<Address>addressList=geocoder.getFromLocationName("1600 Amphitheatre Parkway, Mountain View, CA",1);
                while (addressList.size()==0) {
                    addressList = geocoder.getFromLocationName("1600 Amphitheatre Parkway, Mountain View, CA", 1);

                    Log.d("loop","i");
                }
                if(addressList.size()>0)
                {
                    Address address=addressList.get(0);
                    LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());

                    Double l1=latLng.getLatitude();
                    Double l2=latLng.getAltitude();
                    Log.d("LATLNG",l1.toString());

                    Log.d("LATLNG",l2.toString());
                }
                else
                {
                    Log.d("Not Found","lora mera");
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            try {

            }
           catch (Exception e)
           {
               //Log.d("Not Found","cant get location");
           }

            //map.addMarker(new MarkerOptions().position(latLng).title("Maker"));
            //map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

}
