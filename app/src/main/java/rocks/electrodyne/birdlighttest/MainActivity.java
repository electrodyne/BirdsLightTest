package rocks.electrodyne.birdlighttest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    public static Utils.WifiStateChangeReceiver wcreceiver;
    public boolean debug = false;



 //   AlertDialog ExitAlertDialog;
 //   AlertDialog.Builder ExitAlertDialogBuilder;
 //   Button ExitAgreeBtn;
 //   Button ExitCancelBtn;
 //   View.OnClickListener ExitDialogClickListener;

    TextView dialog_title;
    TextView dialog_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //INITIAL SETUP HERE.
        //1.) COMMAND LOGS AVAILABLE FOR THE DATALOG
        //2.) COMMAND ITSELF.
        Utils.ReceivedMessage = new ArrayList<>();
        Utils.SentMessage = new ArrayList<>();
        for (Integer i = 0 ; i < 10; i++) {
            Utils.ReceivedMessage.add(i.toString());
        }


        //Register Network Broadcast Receiver
        wcreceiver = new Utils.WifiStateChangeReceiver(this);


    /*    ExitDialogClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == ExitAgreeBtn)
                {
                    MainActivity.this.finish();
                }
                else if (v == ExitCancelBtn)
                {
                    ExitAlertDialog.dismiss();
                }

            }
        };
    */


        if ( debug ) {
            /*
             * Disabled during debug only.
             * but connect the WiFi network at debug.
             */
            Utils.isConnected = true;
            //this.registerReceiver(wcreceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } else
            this.registerReceiver(wcreceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));



        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) return;

            // Create a new Fragment to be placed in the activity layout
            //MainFragment landingFragment = new MainFragment();
            SplashscreenFragment landingFragment = new SplashscreenFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //Bundle args = new Bundle();
            //args.putString(MainFragment.ARGEX, "ASD");
            //landingFragment.setArguments(args);

            // Add the fragment to the 'fragment_container' FrameLayout
            android.app.FragmentManager fragmentManager = getFragmentManager();
            android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container,landingFragment);
            transaction.commit();

        }
    }



    @Override
    protected void onDestroy() {
        this.unregisterReceiver(wcreceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(this)
            //    .setTitle("Exit Birdslight?")
             //   .setMessage("Are you sure you want to exit Birdslight?")
                .setCancelable(true)
                .setView(R.layout.exit_dialog)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                   }
                })
                .setNegativeButton("Cancel", null)
                .show();



    }
}
