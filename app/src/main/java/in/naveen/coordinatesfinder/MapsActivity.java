package in.naveen.coordinatesfinder;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


EditText location_text, setLONG,setLAT;
    Button copy_lat,copy_lng;
    String resultvalue,myurl;
    ProgressDialog pdialog;
    private GoogleMap mMap;

    public boolean isOnline()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }




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


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Internet Connection Problem.");
        builder.setTitle("Error");
        builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setIcon(null);


        pdialog = new ProgressDialog(MapsActivity.this);
        pdialog.setTitle("Processing");
        pdialog.setCancelable(false);



        //on click respond for EditText

        location_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //Respond to Done click event

                if(actionId== EditorInfo.IME_ACTION_DONE)
                {


                    String passed_value= location_text.getText().toString();
                    passed_value = passed_value.replaceAll(" ", "%20");
                    myurl="http://maps.google.com/maps/api/geocode/json?address="+passed_value+"&sensor=false";




                    if (passed_value.matches(""))
                    {

                        Toast.makeText(getApplicationContext(),"Pass some value",Toast.LENGTH_SHORT).show();

                    }
                   else if(isOnline()==false)

                    {

                        builder.show();

                     }
                    else
                    {


                        new JSONtask().execute(myurl);
                        pdialog.show();

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

//Copy the text from "setLONG

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



    public class JSONtask extends AsyncTask< String ,String, String >

    {


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "MalformedURLException e", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "IOException e", Toast.LENGTH_SHORT).show();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "IOException e", Toast.LENGTH_SHORT).show();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultvalue = result;
            readJSON();


        }
        void readJSON()
        {
            try {
                JSONObject jsonObject = new JSONObject(resultvalue);
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                JSONObject resultsobjects = resultsArray.getJSONObject(0);
                String name = resultsobjects.getString("geometry");
                JSONObject jsonObjectNAME = new JSONObject(name);
                String area_location = jsonObjectNAME.getString("location");
                JSONObject jsonObjectLOCATION= new JSONObject(area_location);
                String lat = jsonObjectLOCATION.getString("lat");
                String lng = jsonObjectLOCATION.getString("lng");
                setLAT.setText(String.valueOf(lat));
                setLONG.setText(String.valueOf(lng));

                mMap.clear();
                MarkerOptions mylocation = new MarkerOptions();
                mylocation.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                mylocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(mylocation);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation.getPosition()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 5));
                pdialog.dismiss();

            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No Location Found,try Again", Toast.LENGTH_SHORT).show();
                pdialog.dismiss();

            }

        }

    }
}



