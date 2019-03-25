package rocks.electrodyne.birdlighttest;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import dmax.dialog.SpotsDialog;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.content.ContentValues.TAG;
import static rocks.electrodyne.birdlighttest.Utils.blockingDialog;
import static rocks.electrodyne.birdlighttest.Utils.getSSID;

public class ConnectFragment extends Fragment implements Utils.onClickCallback{
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public FloatingActionButton scanBtn;

    Utils utils = new Utils();
    private WifiManager wifiManager;
    private Utils.WifiReceiver receiver;
    private Button genPass ;
    private TextView ssidCode;
    View v;
    private final ConnectFragment thisFragment = this;

    /*
    Fire a broadcast receiver for checking the state of wifi
     */


    private final String[] permissions = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private final static String SHOWCASE_ID = "birdlightShowcaseID";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        v = inflater.inflate(R.layout.connect_layout,container,false);

       // wifiLoad = new SpotsDialog(getActivity(), R.style.SpotsDialog);
       // wifiLoad.setCancelable(false);
        //DECLARATION FOR BLOCKING DIALOG. THIS IS NEEDED IF YOU ARE GONNA USE THE BLOCKING DIALOG WINDOW.
       // Utils.blockingDialog = new SpotsDialog(getActivity(), R.style.SpotsDialog);
        Utils.blockingDialog = new SpotsDialog(getActivity(), R.style.Scan);

        blockingDialog.setCancelable(false);

        Setup_Wireless_Connectivity();
        scanBtn = v.findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                receiver = new Utils.WifiReceiver(getActivity(), thisFragment, wifiManager, mRecyclerView, mAdapter, new Utils.WifiReceiver.NoWifiAvailableListener() {
                    @Override
                    public void onNoWifiAvailable() {

                        new MaterialShowcaseView.Builder(getActivity())
                                .setTarget(scanBtn)
                                .setDismissOnTargetTouch(true)
                                .setTargetTouchable(true)
                                .setContentText(getString(R.string.guide_wifi_no_device_found))
                                .setDelay(500) // optional but starting animations immediately in onCreate can make them choppy
                                .show();


                    }
                });

                getActivity().registerReceiver(receiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                */
                Utils.Wifi_Scan(getActivity(),receiver,wifiManager);
            }
        });



        /*
        genPass = v.findViewById(R.id.genPassBtn);
        ssidCode = v.findViewById(R.id.ssidCodeTxt);
        genPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                String ssid_code = ssidCode.getText().toString();
                String password = Utils.generatePassword(Integer.parseInt(ssid_code));
                Log.e(TAG, "PASSWORD:" + password  + "length:" + password.length());
                Snackbar.make(v, password, Snackbar.LENGTH_INDEFINITE).show();
            }

        });
        */
        if (QRScanFragment.fromQRScannerFragment == false)
        {
            new MaterialShowcaseView.Builder(getActivity())
                    .setTarget(scanBtn)
                    .setDismissOnTargetTouch(true)
                    .setTargetTouchable(true)
                    .setDismissText("GOT IT")
                    .setContentText(getString(R.string.guide_scan_button))
                    .setDelay(500) // optional but starting animations immediately in onCreate can make them choppy
                    //         .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                    .show();
        }
        return v;
    }

    @Override
    public void callback(int position) {
        //do connection routine here..
        //get the SSID from dataset. @Utils.getSSID.get(position);
        /*
        this fuction is use and called when the user chose the wifi AP to connect to and this also generates the password for that AP.
         */
        //Log.e(TAG, "callback: " + Utils.generatePassword(Integer.parseInt(Utils.getSSID.get(position).substring(5))) );
        //create a generate password tab.
      //  Log.e(TAG, "MEEEE: ");

       //     blockingDialog = new SpotsDialog(getActivity(), R.style.Connect);
       //     blockingDialog.setCancelable(false);
       //     blockingDialog.show();


        final int mPosition = position;

        getActivity().unregisterReceiver(MainActivity.wcreceiver);
        //initiate QR scanner atop this fragment.

        Bundle bundle = new Bundle();
        bundle.putInt("ssid_position", mPosition);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        QRScanFragment qrScanFragment = new QRScanFragment();
        qrScanFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container, qrScanFragment);
        fragmentTransaction.addToBackStack("BS1");
        fragmentTransaction.commit();






        /*
        Utils.GeneratePasswordAsync genAsync = new Utils.GeneratePasswordAsync(new Utils.GeneratePasswordAsync.AsyncResponse() {
            @Override
            public void passwordGenerated(String password) {



                WifiConfiguration wc = new WifiConfiguration();
                wc.SSID = String.format("\"%s\"", Utils.getSSID.get(mPosition));
           //     wc.SSID = String.format("%s", Utils.getSSID.get(mPosition));
                wc.preSharedKey = String.format("\"%s\"",Utils.generatePassword(Integer.parseInt(Utils.getSSID.get(mPosition).substring(5))));
           //     wc.preSharedKey = String.format("%s",Utils.generatePassword(Integer.parseInt(Utils.getSSID.get(mPosition).substring(5))));
                wifiManager.disconnect();
                wifiManager.enableNetwork(wifiManager.addNetwork(wc),true);
                wifiManager.reconnect();
                Utils.isConnected = true; //flag that was checked if the android is connected to the Lago-Wifi.
               // Log.e(TAG, "I PASSED HERE");
                //Utils.WifiStateChangeReceiver wcreceiver = new Utils.WifiStateChangeReceiver(getActivity());
                //getActivity().registerReceiver(wcreceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                //check if a register is already registered.
                //getActivity().unregisterReceiver(wcreceiver);
            }
        });
        genAsync.execute(Integer.parseInt(Utils.getSSID.get(position).substring(5)));
        */
        //Reinitialize wcreceiver
        MainActivity.wcreceiver = new Utils.WifiStateChangeReceiver(getActivity());
        getActivity().registerReceiver(MainActivity.wcreceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


    }

    private void Setup_Wireless_Connectivity() {



        //get recycler view by ID
        mRecyclerView = v.findViewById(R.id.connect_recycler);



        //get wifi manager from main activity to local wifiManager
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);



        //Check and request user permission for GPS and WIFI
        utils.requestPermission(getActivity(),permissions,0); //Request Persmission for WiFi and Fine Location



        //Register Broadcast Receiver
        receiver = new Utils.WifiReceiver(getActivity(), this, wifiManager, mRecyclerView, mAdapter, new Utils.WifiReceiver.NoWifiAvailableListener() {
            @Override
            public void onNoWifiAvailable() {

                    new MaterialShowcaseView.Builder(getActivity())
                            .setTarget(scanBtn)
                            .setDismissOnTargetTouch(true)
                            .setTargetTouchable(true)
                            .setContentText(getString(R.string.guide_wifi_no_device_found))
                            .setDelay(500) // optional but starting animations immediately in onCreate can make them choppy
                            .show();


            }
        });




        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            utils.activateGPS(getActivity());




        // WIFI SCAN  Register Receiver
        getActivity().registerReceiver(receiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));




        if ( !wifiManager.isWifiEnabled() )
        {
            Toast.makeText(getActivity().getApplicationContext(), "Automatically Turning On WiFi..", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }

        /*
        //AUTO Scan function. uncomment if you want to scan on load.
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.isLocationEnabled(getActivity()))
                Utils.Wifi_Scan(getActivity(),receiver,wifiManager);
            else
                Toast.makeText(getActivity(), "Press Scan Button once you activated the Location.", Toast.LENGTH_SHORT).show();
        } else {
            Utils.Wifi_Scan(getActivity(),receiver,wifiManager);
        }

        */

    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e(TAG, "onResume: connectFragment.");
        if( QRScanFragment.fromQRScannerFragment == true )
        {
            try{


            }finally {
                QRScanFragment.fromQRScannerFragment = false;
            }
        }
    }
}
