package in.naveen.coordinatesfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


EditText location_text, setLONG,setLAT;
    Button copy_lat,copy_lng;
    double lat,lng;
    String catchedStringfromgoogle;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        copy_lat = (Button) findViewById(R.id.lat_button);
        copy_lng = (Button) findViewById(R.id.long_button);


        location_text = (EditText) findViewById(R.id.location_text);
        setLAT= (EditText) findViewById(R.id.lat_text);
        setLONG = (EditText) findViewById(R.id.long_text);

        //on click respond for EditText

        location_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Respond to Done click event
                if(actionId== EditorInfo.IME_ACTION_DONE)
                {

                    String passed_value= location_text.getText().toString();



                    if (passed_value.matches(""))
                    {

                        Toast.makeText(getApplicationContext(),"Pass some value",Toast.LENGTH_SHORT).show();

                    }

                    else

                    {

                        Geocoder gc=new Geocoder(getApplication());

                        try {
                            List<android.location.Address> list=gc.getFromLocationName(passed_value,1);



                             android.location.Address address=list.get(0);
                            lat= address.getLatitude();
                            lng=address.getLongitude();
                            catchedStringfromgoogle= address.getLocality();
                            setLAT.setText(String.valueOf(lat));
                            setLONG.setText(String.valueOf(lng));

                            //clear previous markers
                            mMap.clear();

                            MarkerOptions mylocation = new MarkerOptions();
                            mylocation.title(catchedStringfromgoogle);
                            mylocation.position(new LatLng(lat, lng));
                            mylocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            mMap.addMarker(mylocation);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation.getPosition()));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),5));

                        } catch (IndexOutOfBoundsException e) {
                        // Handle if no value is present in "list" ,e.g when user passes "dcbvejvfewj" and no result is returned
                            Toast.makeText(getApplicationContext(),"Sorry but location seems to be incorrect",Toast.LENGTH_SHORT).show();
                         e.printStackTrace();

                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(),"No Accurate Place Catched",Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }


                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                //Handle the onclick of markers
                                if(catchedStringfromgoogle==null)
                                {
                                    Toast.makeText(getApplicationContext(),"No Accurate Place Catched",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),""+catchedStringfromgoogle,Toast.LENGTH_SHORT).show();

                                }
                                return false;
                            }
                        });


                    }


                    }






                  return false;
            }
        });
//Copy the text from "setLAT"

        copy_lat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cd = ClipData.newPlainText("COPY",setLAT.getText().toString());
                cm.setPrimaryClip(cd);




                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                String pasteData = "";

                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);


                pasteData = item.getText().toString();


                Toast.makeText(getApplicationContext(),pasteData+" Copied",Toast.LENGTH_SHORT).show();
            }
        });

//Copy the text from "setLONG"
        copy_lng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cd = ClipData.newPlainText("COPY",setLONG.getText().toString());
                cm.setPrimaryClip(cd);



                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                String pasteData = "";

                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);


                pasteData = item.getText().toString();


                Toast.makeText(getApplicationContext(),pasteData+" Copied",Toast.LENGTH_SHORT).show();
            }
        });

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

        //Default Location
        LatLng india = new LatLng(20.593684, 78.96288);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.glicence)
        {
            Intent i = new Intent(MapsActivity.this,Disclaimer_Java.class);
            startActivity(i);

          }


        if (item.getItemId()==R.id.dev)
        {

            Intent broswerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/snghnaveen/"));
            startActivity(broswerIntent);


        }



        if (item.getItemId()==R.id.exit)
        {

            this.finish();


        }

        return super.onOptionsItemSelected(item);
    }


}
