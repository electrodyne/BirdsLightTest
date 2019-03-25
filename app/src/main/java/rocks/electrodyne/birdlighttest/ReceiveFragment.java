package rocks.electrodyne.birdlighttest;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class ReceiveFragment extends Fragment {

    private boolean isListening = false;
    Intent serviceIntent;
    Button startReceiveBtn;
    Utils.UDPBroadcastReceiver udpBroadcastReceiver;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (Utils.isConnected == false){
            //show dialog first.
            //insertDialog Here "OK" option.

            //When "OK is pressed, go back to mainFragment
            Toast.makeText(getActivity(), "Connect to a WiFi Network First!", Toast.LENGTH_SHORT).show();
            android.app.FragmentManager fragmentManager = getFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MainFragment mainFragment = new MainFragment();
            fragmentTransaction.replace(R.id.fragment_container, mainFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

        View v;
        v = inflater.inflate(R.layout.receive_layout,container,false);

        startReceiveBtn = v.findViewById(R.id.receive_startReceiveBtn);
        final EditText portNumET = v.findViewById(R.id.receive_portNumET);
        //FOR AESTETHICS ONLY: CHANGE THE BTN_TEXT TO "STOP RECEIVE SERVICE" WHEN BROADCAST RECEIVER AND SERVICE WAS STOPPED.
        udpBroadcastReceiver = new Utils.UDPBroadcastReceiver();
        getActivity().registerReceiver(udpBroadcastReceiver, new IntentFilter(UDPListenerService.UDP_BROADCAST));

        startReceiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListening == false) {
                    serviceIntent = new Intent(getActivity(), UDPListenerService.class);
                    serviceIntent.putExtra(UDPListenerService.UDP_PORT, Integer.parseInt(portNumET.getText().toString()));
                    getActivity().startService(serviceIntent);
                    startReceiveBtn.setText("STOP RECEIVE SERVICE");
                    isListening = true;

                } else {
                    getActivity().stopService(serviceIntent);
                    startReceiveBtn.setText("START RECEIVE SERVICE");
                    isListening = false;
                }

            }
        });


        return v;
    }

    @Override
    public void onStop() {
        //stop service.
        Log.e(TAG, "onStop: " );
        getActivity().stopService(serviceIntent);
        startReceiveBtn.setText("START RECEIVE SERVICE");
        super.onStop();
    }
}
