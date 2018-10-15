package rocks.electrodyne.birdlighttest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/*
    the goal is to transmit raw data and don't expect that something will return.

    --the goal in this is to check the functionality of the device.
       - to go to this you have to know what are the functionalities of the device.
       - or maybe we showcase only 1 action per release.

    how to test?
    -setup a wifi hotspot/access point and open a port at that access point.
 */
public class TransmitFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.communication_layout,container,false);
        //check if lago_wifi_ap was connected before the user can utilize this option.
        // : action: create a 1 bit checker for @IS_CONNECTED
        Button btn = v.findViewById(R.id.transmitBtn);
        final EditText editText = v.findViewById(R.id.receiveET);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x;
                x = Utils.getMessages("receive");
                editText.setText(x);

            }
        });
        return v;

    }
}
