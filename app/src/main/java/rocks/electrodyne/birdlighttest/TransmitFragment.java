package rocks.electrodyne.birdlighttest;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import static android.content.ContentValues.TAG;
import static android.content.Context.WIFI_SERVICE;

/*
    the goal is to transmit raw data and don't expect that something will return.

    --the goal in this is to check the functionality of the device.
       - to go to this you have to know what are the functionalities of the device.
       - or maybe we showcase only 1 action per release.

    how to test?
    -setup a wifi hotspot/access point and open a port at that access point.
 */
public class TransmitFragment extends Fragment {

    Utils.Ip ipUtil;
    EditText ipAdd;
    EditText portNum;
    EditText messageTxt;
    EditText receiveTxt;
    FloatingActionButton transmitInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Utils.isConnected = true; // Forced. Dbug mode.
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
        v = inflater.inflate(R.layout.trasmit_layout,container,false);
        //check if lago_wifi_ap was connected before the user can utilize this option.
        // : action: create a 1 bit checker for @Utils.isConnected


        ipAdd = v.findViewById(R.id.ipAddET);  //get ip address in NNN.NNN.NNN.HHH format
        portNum = v.findViewById(R.id.portNumET); //port number that will be used to train the "DOME"
        messageTxt = v.findViewById(R.id.transmit_message_txt);
        transmitInfo = v.findViewById(R.id.transmit_info_fab);
        receiveTxt = v.findViewById(R.id.receive_message_txt);
        Button sendRawABtn = v.findViewById(R.id.sendRawA);

        //HIDE FAB FOR A WHILE.
        transmitInfo.hide();

        //own ip address and broadcast address is now available.
        ipUtil = new Utils.Ip(getActivity().getApplicationContext());


        //Set Broadcast Address to IP address
        ipAdd.setText(ipUtil.getBroadcast().toString().substring(1));


        sendRawABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-> get the IP address of the dome.
                //-> Create a thread for writing UDP packet.
                //FYI: SocketAddress is your Address and Port Number combined.
                //transmit message

                if (ipAdd.getText().length() > 7 || portNum.getText().length() > 0) {
                    if (messageTxt.getText().length() > 0) {
                        Utils.sendUDP sudp = new Utils.sendUDP(messageTxt.getText().toString() + "\n",ipAdd.getText().toString(), Integer.parseInt(portNum.getText().toString()),receiveTxt);
                        sudp.execute();
                    } else
                        Toast.makeText(getActivity(), "Please Insert Message.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please Complete Receiver IP Address First", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return v;

    }



}
