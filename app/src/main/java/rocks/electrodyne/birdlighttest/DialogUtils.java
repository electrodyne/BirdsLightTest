package rocks.electrodyne.birdlighttest;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DialogUtils extends DialogFragment{

    int mLayoutResource;


    public DialogUtils() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(mLayoutResource, container,false);

        if (mLayoutResource == R.layout.ssid_rename_dialog)
        {
            // for rename dialog...

        }

        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    public void setLayoutResource(int layoutResource, FragmentManager fm, String tag) {
        mLayoutResource = layoutResource;
        //this.setCancelable(true);
        this.show(fm, tag);

    }
}
