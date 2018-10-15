package rocks.electrodyne.birdlighttest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Utils {

    private boolean onRequestPermission = false;
    private GoogleApiClient googleApiClient;
    static ArrayList<String> ReceivedMessage;
    static ArrayList<String> SentMessage;
    static ArrayList<String> getSSID;
    static SpotsDialog blockingDialog = null;

    public interface onClickCallback {
        void callback(int arg);
    }

    /*
    Add procedure:
    1.) to paste the message on the (arg: edittext object , arraylist )
    2.) to insert data on the arraylist. this is based on the actions made by the devices. have listener on udp.
     */
    /*
    ARGS:
    @type => ["receive", "send"]

     */


    /*
     Algorithm:
     @receive:
     pagkareceive ng android app ng message galing sa wifi through UDP, ipupush yung message sa isang arrayList.
     ilalagay ung message sa huling index nung arrayList. pag puno na ung array list, buburahin ung nasa upper index then mag momove yung list upward,
     then ididisplay sya sa text box. pag pala naupdate ung textbox dapat automatic syang mag scroll sababa.

     instances when you update the display:
     1.) when button @ menu was pressed where communication_layout is involved.
     2.) when send button was pressed, transmit box will update and scroll down automatically.
     3.) when android received message, receive box will update and scroll down automatically.

     Events:
     1.)  OnCreateView of Comms Fragments.
     2.)  OnClickListener of Send Button.
     3.)  UDP broadcast receiver event bring to UI Thread.

     */
    static String getMessages (String type) {
        String result = "";
        String[] temp;
        ArrayList<String> tempArr;

        type = type.toLowerCase();

        if (type == "receive")
            tempArr = ReceivedMessage;
        else
            tempArr = SentMessage;

        try {
            temp = tempArr.toArray(new String[0]);
            for (int i = 0; i < temp.length; i++) {
                result += temp[i];
                if (i != (temp.length - 1))
                    result += "\r\n";
            }

            return result;
        }catch (Exception e) {
            return "";
        }
    }

    public void requestPermission(Activity context, String[] permission, int requestCode) {
        // Here, thisActivity is the current activity

        String[] unApprovedPermissions = new String[]{};
        ArrayList<String> unApprovedList = new ArrayList<>();
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permission[i]) != PackageManager.PERMISSION_GRANTED){
                onRequestPermission = true;
                unApprovedList.add(permission[i]);


                if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                        permission[i])) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                   // AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                   // alertBuilder.setCancelable(true);
                    //alertBuilder.setTitle("")

                    ActivityCompat.requestPermissions(context,permission, requestCode);
                    Toast.makeText(context, "Please Add this permission to make this application work.", Toast.LENGTH_SHORT).show();
                } else {
                    // No explanation needed; request the permission
                    if ( i == (permission.length - 1) )
                        ActivityCompat.requestPermissions(context,unApprovedList.toArray(unApprovedPermissions), requestCode);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }

            }
        }

    }

    public void activateGPS(final Activity context) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            final PendingResult result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient,builder.build());
            result.setResultCallback(new ResultCallback() {
                @Override
                public void onResult(@NonNull Result r) {
                    final Status status = r.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            //scan();
                            // Log.e(TAG, "onResult: SUCCESS" );
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(context, 1000);
                                // scan();
                                //  Log.e(TAG, "onResult: RESOLUTION_REQUIRED");
                            } catch (IntentSender.SendIntentException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //scan();
                            // Log.e(TAG, "onResult: SETTINGS_CHANGE_UNAVAILABLE");
                            break;

                    }
                }
            });
        }
        googleApiClient = new GoogleApiClient.Builder(context).addApi(AppIndex.API).build();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    static void Wifi_Scan(final Activity context, final BroadcastReceiver broadcastReceiver, final WifiManager wifiManager)
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub

                //wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                //receiverWifi = new WifiReceiver();
                if (blockingDialog != null ) // blocking is enabled.
                {
                    blockingDialog.show();
                }
                context.registerReceiver(broadcastReceiver, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                wifiManager.startScan();
                //wifiLoad.show();
                //Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show();
            }
        }, 20);

    }

    static class WifiReceiver extends BroadcastReceiver {

        private WifiManager mWifiManager;
        private List<ScanResult> wifiList;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView mRV;
        private Utils.onClickCallback mCB;
        private ArrayList<String> accessPoints = new ArrayList<>();
        private ArrayList<Integer> levelImage = new ArrayList<>();
        private String[] accessPt = {""};
        private Integer[] lvlImg = {};

        //Constructor
        public WifiReceiver( Utils.onClickCallback cb,  WifiManager wifiManager,  RecyclerView rv,  RecyclerView.Adapter recycler_view_adapter) {
            this.mAdapter = recycler_view_adapter;
            this.mWifiManager = wifiManager;
            this.mRV = rv;
            this.mCB = cb;
        }//pass on the function of the recycler view initializer.

        @Override
        public void onReceive(Context context, Intent intent) {

            context.unregisterReceiver(this);
            if (accessPoints.size() > 0)
                accessPoints.clear();

            wifiList = mWifiManager.getScanResults();
            for( int i = 0 ; i < wifiList.size() ; i++) {
                String currentSSID =  wifiList.get(i).SSID;
                boolean match = currentSSID.contains("LAGO-");

                if ( match ) {
                    accessPoints.add(wifiList.get(i).SSID);

                    switch (mWifiManager.calculateSignalLevel(wifiList.get(i).level, 5)) {
                        case 0 :
                            levelImage.add(R.drawable.wifi_level_0);
                            break;
                        case 1 :
                            levelImage.add(R.drawable.wifi_level_1);
                            break;
                        case 2 :
                            levelImage.add(R.drawable.wifi_level_2);
                            break;
                        case 3 :
                            levelImage.add(R.drawable.wifi_level_3);
                            break;
                        case 4 :
                            levelImage.add(R.drawable.wifi_level_4);
                            break;
                        default:
                            //levelImage.add(R.drawable.ic_launcher_background);
                            break;
                    }

                }
                //process currentSSID.


            }

            //addWifiList(accessPoints,levelImage);
           // wifiLoad.dismiss();
            if (blockingDialog != null)
            {
                blockingDialog.dismiss();
            }
            accessPt = accessPoints.toArray(new String[0]);
            getSSID = accessPoints;
            lvlImg =  levelImage.toArray(new Integer[0]);
            this.mAdapter = new MainViewAdapter(mCB,accessPt,lvlImg);
            mRV.setAdapter(this.mAdapter);
            mRV.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    static String generatePassword(int serialNumber) {

        /*
        new generate password algorithm: when the generate button / this function is called, run asynctask, @ asynctask, after calculation, call
        interface callback named : generatePasswordCallback. @ the function calling this ( ConnectFragment add this callback function, @ generate button,
        show it at the text box.

        Generate Password (BTN) -> generatePassword(void)[this] -> Run AsyncTask (generatePasswordAsync) @RunAsyc: Start Calculation -> @Done Calc Raise
        a Callback/interface from caller
         */
     //   if (isAsync == true) {
      //      Integer serialNumber_Integer = Integer.valueOf(serialNumber);
        //    new generatePasswordAsync().execute(serialNumber_Integer);
       // }
       // else {
            int numDigits = 51;
            int currentNumber;
            StringBuilder p = new StringBuilder();
            //int debugPnum = 0;

            for (int d = 11; d <= 50; d++ ) {
                currentNumber = serialNumber % d;
                if (currentNumber == 0){
                    numDigits = d;
                    break;
                }
            }
            if (numDigits == 51)
                numDigits = 25; //25 will be it's default length.
            while(p.length() < numDigits) {
                for ( int i = 127; i > 32; i-- ) {
                    int currentModulo = i;
                    while ( currentModulo > 32 ) {
                        currentModulo = serialNumber % currentModulo;
                        if (currentModulo == 44 || currentModulo == 34 || currentModulo == 58 || currentModulo == 59
                                || currentModulo == 92 || currentModulo == 38 || currentModulo == 37 || currentModulo == 43
                                || currentModulo == 39 || currentModulo == 60 || currentModulo == 62 || currentModulo == 63)
                            break;
                        if (currentModulo > 32)
                            p.append((char)currentModulo);
                    }

                    if (p.length() >= numDigits)
                        break;
                }
            }
            return p.toString();
      //  }


    }



    static class GeneratePasswordAsync extends AsyncTask<Integer, Void, String>{
        public interface AsyncResponse {
            void passwordGenerated(String password);
        }
        private AsyncResponse mDelegate = null;

        public GeneratePasswordAsync(AsyncResponse delegate) {
            this.mDelegate = delegate;
        }
        @Override
        protected String doInBackground(Integer... integers) {
            int numDigits = 51;
            Integer serialNumber = integers[0];
            int currentNumber;
            StringBuilder p = new StringBuilder();


            for (int d = 11; d <= 50; d++ ) {
                currentNumber = serialNumber % d;
                if (currentNumber == 0){
                    numDigits = d;
                    break;
                }
            }
            if (numDigits == 51)
                numDigits = 25; //25 will be it's default length.
            while(p.length() < numDigits) {
                for ( int i = 127; i > 32; i-- ) {
                    int currentModulo = i;
                    while ( currentModulo > 32 ) {
                        currentModulo = serialNumber % currentModulo;
                        if (currentModulo == 44 || currentModulo == 34 || currentModulo == 58 || currentModulo == 59
                                || currentModulo == 92 || currentModulo == 38 || currentModulo == 37 || currentModulo == 43
                                || currentModulo == 39 || currentModulo == 60 || currentModulo == 62 || currentModulo == 63)
                            break;
                        if (currentModulo > 32)
                            p.append((char)currentModulo);
                    }
                    if (p.length() >= numDigits)
                        break;

                }
            }
            return p.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            this.mDelegate.passwordGenerated(s);
        }
    }
}
