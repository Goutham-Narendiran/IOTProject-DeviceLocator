package com.example.devicelocator;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Initialize variables
    private GoogleMap mMap;
    MqttAndroidClient client;
    //if no communication from device simulations, default lat and long coordinates
    String d1loc = "43.95&-78.90";
    String d2loc = "43.8926535&-78.940705";
    String d3loc = "43.876243&-79.043361";
    String d4loc = "43.844288&-79.104954";
    //declare each device for coordinates
    LatLng device1, device2, device3, device4;
    //the area the map will start displaying by default
    LatLng zone = new LatLng(43.95, -78.90);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        //Broker for MQTT
        //HiveMQ
        String host1 = "tcp://broker.hivemq.com:1883";
        String clientId = MqttClient.generateClientId();
        //Establish connection
        client = new MqttAndroidClient(MapsActivity.this, host1, clientId);
        MqttConnectOptions options = new MqttConnectOptions();

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Display connected message upon establishing a successful connection
                    Toast.makeText(MapsActivity.this , "Connected", Toast.LENGTH_SHORT).show();
                    subscription();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Display message of error,connection timeout or firewall problems
                    Toast.makeText(MapsActivity.this , "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                //Print to console
                System.out.println("TOPIC: " +topic);


                //filter update prompt by topic
                if (topic.equalsIgnoreCase("deviceloc/latlong/device1")){
                    System.out.println("DEVICE 1 UPDATE");
                    d1loc = new String(message.getPayload());
                    updateMap(1);
                }
                else if (topic.equalsIgnoreCase("deviceloc/latlong/device2")){
                    System.out.println("DEVICE 2 UPDATE");
                    d2loc = new String(message.getPayload());
                    updateMap(2);
                }
                else if (topic.equalsIgnoreCase("deviceloc/latlong/device3")){
                    System.out.println("DEVICE 3 UPDATE");
                    d3loc = new String(message.getPayload());
                    updateMap(3);
                }
                else if (topic.equalsIgnoreCase("deviceloc/latlong/device4")){
                    System.out.println("DEVICE 4 UPDATE");
                    d4loc = new String(message.getPayload());
                    updateMap(4);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

  initMap();
}

//initialize the map
public void initMap(){

        //set Lat and Long for all devices
    //Lat and Long will be seperated by "&" when input
        String [] latlong = d1loc.split("&", 2);
        double lat = Double.parseDouble(latlong[0]);
        double lng = Double.parseDouble(latlong[1]);
        device1 = new LatLng (lat, lng);

        String [] latlong2 = d2loc.split("&", 2);
        double lat2 = Double.parseDouble(latlong2[0]);
        double lng2 = Double.parseDouble(latlong2[1]);
        device2 = new LatLng (lat2, lng2);

        String [] latlong3 = d3loc.split("&", 2);
        double lat3  = Double.parseDouble(latlong3[0]);
        double lng3 = Double.parseDouble(latlong3[1]);
        device3 = new LatLng (lat3, lng3);

        String [] latlong4 = d4loc.split("&", 2);
        double lat4 = Double.parseDouble(latlong4[0]);
        double lng4 = Double.parseDouble(latlong4[1]);
        device4 = new LatLng (lat4, lng4);



        //Call Map Ready Thread
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
}
//update map when new coordinates are in
public void updateMap(int device){


        //filter update by device
    //update Lat and Long for respective device only
    //other devices will remain the same until new coordinates available for respective device
        if (device == 1){

            String [] latlong = d1loc.split("&", 2);
            double lat = Double.parseDouble(latlong[0]);
            double lng = Double.parseDouble(latlong[1]);
             device1 = new LatLng (lat, lng);



        }
        else if (device == 2){
            String [] latlong = d2loc.split("&", 2);
            double lat = Double.parseDouble(latlong[0]);
            double lng = Double.parseDouble(latlong[1]);
             device2 = new LatLng (lat, lng);

        }
        else if (device == 3){
            String [] latlong = d3loc.split("&", 2);
            double lat = Double.parseDouble(latlong[0]);
            double lng = Double.parseDouble(latlong[1]);
             device3 = new LatLng (lat, lng);

        }
        else if (device == 4){
            String [] latlong = d4loc.split("&", 2);
            double lat = Double.parseDouble(latlong[0]);
             double lng = Double.parseDouble(latlong[1]);
             device4 = new LatLng (lat, lng);
        }


    mMap.clear();//clear all markers

    // Add new markers
    mMap.addMarker(new MarkerOptions().position(device1).title("Device 1 Location"));


    mMap.addMarker(new MarkerOptions().position(device2).title("Device 2 Location"));
    mMap.addMarker(new MarkerOptions().position(device3).title("Device 3 Location"));
    mMap.addMarker(new MarkerOptions().position(device4).title("Device 4 Location"));

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
        mMap.clear();

        // Add a markers for each device
       mMap.addMarker(new MarkerOptions().position(device1).title("Device 1 Location"));
        mMap.addMarker(new MarkerOptions().position(device2).title("Device 2 Location"));
        mMap.addMarker(new MarkerOptions().position(device3).title("Device 3 Location"));
        mMap.addMarker(new MarkerOptions().position(device4).title("Device 4 Location"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(zone, 10));
    }
//Function to subscribe using MQTT to the topic
private void subscription (){
    String topic = "deviceloc/latlong/#"; //subscribe to all topics in "deviceloc/latlong"
    int qos = 1; //msg level
    try {
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards

            }
        });
    } catch (MqttException e) {
        e.printStackTrace();
    }
    }
}


