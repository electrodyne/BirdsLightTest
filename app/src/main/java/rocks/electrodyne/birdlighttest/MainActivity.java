package rocks.electrodyne.birdlighttest;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {



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

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) return;

            // Create a new Fragment to be placed in the activity layout
            MainFragment landingFragment = new MainFragment();

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
}
