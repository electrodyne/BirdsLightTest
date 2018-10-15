package rocks.electrodyne.birdlighttest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment implements Utils.onClickCallback {

    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;


    private String[] dataset = {
            "Connect to Device",
            "Send Data to Device",
            "Receive Data from Device",
            "Synchronous Transmission"
    };
    private Integer[] images = {
            R.drawable.ic_speaker_phone_black_24dp,
            R.drawable.ic_call_made_black_24dp,
            R.drawable.ic_call_received_black_24dp,
            R.drawable.ic_import_export_black_24dp

    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.main_layout,container,false);

        mRecyclerView = v.findViewById(R.id.recycler_layout);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(v.getContext() );
        mRecyclerView.setLayoutManager(mLayoutManager);


        //recycler view adapter below
        mAdapter = new MainViewAdapter(this,dataset,images);
        mRecyclerView.setAdapter(mAdapter);


        return v;
    }

    @Override
    public void callback(int position) {

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {
            case 0:
                ConnectFragment connectFragment = new ConnectFragment();
                fragmentTransaction.replace(R.id.fragment_container, connectFragment);
                break;
            case 1:
             //   TransmitFragment transmitFragment = new TransmitFragment();
             //   fragmentTransaction.replace(R.id.fragment_container, transmitFragment);
                break;
            case 2:
             //   ReceiveFragment receiveFragment = new ReceiveFragment();
             //   fragmentTransaction.replace(R.id.fragment_container, receiveFragment);
                break;
            case 3:
             //   TranceiveFragment tranceiveFragment = new TranceiveFragment();
             //   fragmentTransaction.replace(R.id.fragment_container, tranceiveFragment);
                break;
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
