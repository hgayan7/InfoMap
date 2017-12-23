package com.example.him.infomap;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mGoogleMap;
    //default coordinates
    String locationdata,latitude="26.144517",longitude="91.736237";
    EditText editText;
    Button search,searchAgain;
    TextView textView,condition,temptext,pressuretext;
    String sentLocation;
    RelativeLayout relativeLayout;
    String mainW,temp,pressure;
    private static final String TAG=MapActivity.class.getSimpleName();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        setContentView(R.layout.activity_map);
        final Geocoding geocoding=new Geocoding();
        final WeatherData weatherData=new WeatherData();
        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mSupportMapFragment.getMapAsync(this);
        relativeLayout=(RelativeLayout)findViewById(R.id.map_relative);
        condition=(TextView)findViewById(R.id.conditiontext);
        temptext=(TextView)findViewById(R.id.temptext);
        pressuretext=(TextView)findViewById(R.id.pressuretext);
        editText=(EditText)findViewById(R.id.search_edittext);
        search=(Button)findViewById(R.id.fire);
        searchAgain=(Button)findViewById(R.id.search_again);
        textView=(TextView)findViewById(R.id.search_text);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")) {
                    search.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);

                      sentLocation = editText.getText().toString().replaceAll("\\s+", "");
                      geocoding.execute("https://maps.googleapis.com/maps/api/geocode/json?address="
                              + sentLocation
                              + "&key="+"YOUR_GEOCODING_API_KEY");
                    try {
                        weatherData.execute("http://api.openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&units=metric" +
                                "&APPID="+"YOUR_OPENWEATHERMAP_API_KEY");
                    }catch (Exception e){
                        Toast.makeText(MapActivity.this, "okay", Toast.LENGTH_SHORT).show();
                    }
                      searchAgain.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(MapActivity.this, "Enter the location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
               startActivity(getIntent());

            }
        });

    }


    //class for geocoding
    public class Geocoding extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);
                int data=reader.read();
                while (data!=-1){
                    char current=(char)data;
                    result=result+current;
                    data=reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject firstObject=new JSONObject(s);
                String results=firstObject.getString("results");
                JSONArray firstArray=new JSONArray(results);
                for(int i=0;i<1;i++){
                    JSONObject secondObject=firstArray.getJSONObject(i);
                    locationdata=secondObject.getString("geometry");
                    JSONObject thirdObject=new JSONObject(locationdata);
                    String lcl=thirdObject.getString("location");
                    JSONObject fourthObject=new JSONObject(lcl);

                   latitude=fourthObject.getString("lat");
                   longitude=fourthObject.getString("lng");
                    onMapReady(mGoogleMap);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //class for weather data
    public class WeatherData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String wresult="";
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);
                int data=reader.read();
                while(data!=-1){
                    char current= (char)data;
                    wresult=wresult+current;
                    data=reader.read();

                }
                return wresult;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject firstO=new JSONObject(s);
                String firstS=firstO.getString("weather");
                String secondS=firstO.getString("main");
                JSONArray jsonArray=new JSONArray(firstS);
                for(int i=0;i<1;i++){
                   JSONObject second0=jsonArray.getJSONObject(i);
                   mainW=second0.getString("main");
                }
                JSONObject third0=new JSONObject(secondS);
                temp=third0.getString("temp");
                pressure=third0.getString("pressure");

                relativeLayout.setVisibility(View.VISIBLE);
                condition.setText("Condition:" + mainW);
                temptext.setText("Temperature:" + temp + " °C");
                pressuretext.setText("Pressure:" + pressure + " Pa");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
       try{
           boolean mapstyle=mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));
           if(!mapstyle){
               Log.d(MapActivity.class.getSimpleName(), "onMapReady: ");
           }
       }catch (Resources.NotFoundException r){
           Log.d(MapActivity.class.getSimpleName(), "onMapReady: ");
       }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude)))      // Sets the center of the map to Mountain View
                .zoom(8)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)// Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(8),7000,null);

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude))));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(MapActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
