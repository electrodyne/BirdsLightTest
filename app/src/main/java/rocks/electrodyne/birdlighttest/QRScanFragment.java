package rocks.electrodyne.birdlighttest;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;


/**
 * A simple {@link Fragment} subclass.
 */
public class QRScanFragment extends Fragment {


    public QRScanFragment() {
        // Required empty public constructor
    }

    private CodeScanner mCodeScanner;
    public static boolean fromQRScannerFragment = false;
    public static boolean fromQRScannerFragment2 = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity activity = getActivity();
        View v;
        v = inflater.inflate(R.layout.fragment_qrscan,container,false);
        final Fragment thisFragment = this;
        try {
            CodeScannerView scannerView = v.findViewById(R.id.scanner_view);
            mCodeScanner = new CodeScanner(activity, scannerView);




            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(final Result result) {

                    /**
                     *
                     * @param position
                     *
                     */

                    final int mPosition = thisFragment.getArguments().getInt("ssid_position");

                    WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = String.format("\"%s\"", Utils.getSSID.get(mPosition));
                    wc.preSharedKey = String.format("\"%s\"",result.getText());
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(wifiManager.addNetwork(wc),true);
                    wifiManager.reconnect();
                    //Utils.isConnected = true;

                    /*Utils.GeneratePasswordAsync genAsync = new Utils.GeneratePasswordAsync(new Utils.GeneratePasswordAsync.AsyncResponse() {
                        @Override
                        public void passwordGenerated(String password) {

                            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

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
                    genAsync.execute(Integer.parseInt(Utils.getSSID.get(mPosition).substring(5)));

                    */
                    //for debug only. shows the scanned Qr code.

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                            fromQRScannerFragment = true;
                            fromQRScannerFragment2 = true;
                            android.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.popBackStackImmediate();

                            //android.app.FragmentManager fragmentManager = getFragmentManager();
                            //fragmentManager.popBackStackImmediate();

                        }
                    });

                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCodeScanner.startPreview();
                }
            });
        } catch (RuntimeException re) {
           /* TextView dialog_title = v.findViewById(R.id.title_dialog);
            TextView dialog_message = v.findViewById(R.id.message_dialog);
            dialog_title.setText("Permission Failure");
            dialog_message.setText("Please approve 'Camera' permission to proceed.");
            new AlertDialog.Builder(activity)
                    //    .setTitle("Exit Birdslight?")
                    //   .setMessage("Are you sure you want to exit Birdslight?")
                    .setCancelable(false)
                    .setView(R.layout.exit_dialog)
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            activity.finish();
                        }
                    })

                    .show();*/
           // Toast.makeText(activity, "asdf", Toast.LENGTH_SHORT).show();
        }

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnQRScanListener) {
                    mListener = (OnQRScanListener) context;
                } else {
                    throw new RuntimeException(context.toString()
                            + " must implement OnQRScanListener");
                }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}
