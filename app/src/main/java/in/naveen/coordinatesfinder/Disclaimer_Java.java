package in.naveen.coordinatesfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by naveen on 29/6/16.
 */
public class Disclaimer_Java extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disclaimer);


        //Get OpenSourceSoftwareLicenseInfo
        String s= GoogleApiAvailability.getInstance().getOpenSourceSoftwareLicenseInfo(this);
       TextView t= (TextView) findViewById(R.id.discm_text);

     t.setText(s);





}

}
