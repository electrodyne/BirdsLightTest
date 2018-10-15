package rocks.electrodyne.birdlighttest;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;
import static rocks.electrodyne.birdlighttest.Utils.blockingDialog;
import static rocks.electrodyne.birdlighttest.Utils.getSSID;

public class ConnectFragment extends Fragment implements Utils.onClickCallback {
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




    private final String[] permissions = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        v = inflater.inflate(R.layout.connect_layout,container,false);

       // wifiLoad = new SpotsDialog(getActivity(), R.style.SpotsDialog);
       // wifiLoad.setCancelable(false);
        //DECLARATION FOR BLOCKING DIALOG. THIS IS NEEDED IF YOU ARE GONNA USE THE BLOCKING DIALOG WINDOW.
        Utils.blockingDialog = new SpotsDialog(getActivity(), R.style.SpotsDialog);
        blockingDialog.setCancelable(false);

        Setup_Wireless_Connectivity();
        scanBtn = v.findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.Wifi_Scan(getActivity(),receiver,wifiManager);
            }
        });

        genPass = v.findViewById(R.id.genPassBtn);
        ssidCode = v.findViewById(R.id.ssidCodeTxt);
        genPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                String ssid_code = ssidCode.getText().toString();
                String password = Utils.generatePassword(Integer.parseInt(ssid_code));
                Snackbar.make(v, password, Snackbar.LENGTH_INDEFINITE).show();
/*
                Utils.GeneratePasswordAsync genAsync = new Utils.GeneratePasswordAsync(new Utils.GeneratePasswordAsync.AsyncResponse() {
                    @Override
                    public void passwordGenerated(String pass) {
                        //post process here.
                        Snackbar.make(v, pass, Snackbar.LENGTH_INDEFINITE).show();
                    }
                });
                genAsync.execute(Integer.parseInt(ssid_code));*/
            }

        });

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
        final int mPosition = position;
        Utils.GeneratePasswordAsync genAsync = new Utils.GeneratePasswordAsync(new Utils.GeneratePasswordAsync.AsyncResponse() {
            @Override
            public void passwordGenerated(String password) {
                WifiConfiguration wc = new WifiConfiguration();
                wc.SSID = String.format("\"%s\"", Utils.getSSID.get(mPosition));
                wc.preSharedKey = String.format("\"%s\"",Utils.generatePassword(Integer.parseInt(Utils.getSSID.get(mPosition).substring(5))));
                wifiManager.disconnect();
                wifiManager.enableNetwork(wifiManager.addNetwork(wc),true);
                wifiManager.reconnect();

            }
        });
        genAsync.execute(Integer.parseInt(Utils.getSSID.get(position).substring(5)));

    }

    private void Setup_Wireless_Connectivity() {
        //get recycler view by ID
        mRecyclerView = v.findViewById(R.id.connect_recycler);
        //get wifi manager from main activity to local wifiManager
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        utils.requestPermission(getActivity(),permissions,0); //Request Persmission for WiFi and Fine Location
        receiver = new Utils.WifiReceiver(this,wifiManager,mRecyclerView,mAdapter);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            utils.activateGPS(getActivity());
        getActivity().registerReceiver(receiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if ( !wifiManager.isWifiEnabled() ) {
            Toast.makeText(getActivity().getApplicationContext(), "Automatically Turning On WiFi..", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.isLocationEnabled(getActivity()))
                Utils.Wifi_Scan(getActivity(),receiver,wifiManager);
            else
                Toast.makeText(getActivity(), "Press Scan Button once you activated the Location.", Toast.LENGTH_SHORT).show();
        } else {
            Utils.Wifi_Scan(getActivity(),receiver,wifiManager);
        }



    }



}
