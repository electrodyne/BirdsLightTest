package rocks.electrodyne.birdlighttest;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static android.content.ContentValues.TAG;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.ViewHolder> {
    private String[] mDataset;
    private Integer[] mImage;
    private Utils.onClickCallback mCallback;
    private Activity mActivity;
    public static int chosenPosition;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView dImage;
        TextView dText;
        RelativeLayout dLayoutParent;

        public ViewHolder(View v) {
            super(v);
            dImage = v.findViewById(R.id.list_image);
            dText = v.findViewById(R.id.list_text);
            dLayoutParent = v.findViewById(R.id.list_layout_parent);
        }
    }

    public MainViewAdapter(Activity activity,Utils.onClickCallback callback,String[] myDataset, Integer[] myImage){
        mDataset = myDataset;
        mImage = myImage;
        mCallback = callback;
        mActivity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_recyclable_view,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.dText.setText(mDataset[position]);
        holder.dImage.setImageResource(mImage[position]);

        //Note: let the main function calling this fill the onclick listener below.
        //get the position chosen, use keys1 and 2,

        if (QRScanFragment.fromQRScannerFragment == false && QRScanFragment.fromQRScannerFragment2 == true) //&& first time..
        {
            //Log.e(TAG, "SHOWCASE!! good!" );
            if (position == chosenPosition) {
                new MaterialShowcaseView.Builder(mActivity)
                        .setTarget(holder.dLayoutParent)
                        .setDismissOnTargetTouch(false)
                        .setTargetTouchable(true)
                        .setDismissText("GOT IT")
                        .setContentText(mActivity.getString(R.string.guide_wifi_hold_wifi_ssid))
                        .setDelay(500) // optional but starting animations immediately in onCreate can make them choppy
                        //         .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                        .withRectangleShape()
                        .show();

                holder.dLayoutParent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //Log.e(TAG, "onLongClick: " ); //continue here..
                        //Pop a dialog.
                        //Use dialog fragment...
                        DialogUtils dialogUtils = new DialogUtils();
                        dialogUtils.setCancelable(false);
                        dialogUtils.setLayoutResource(R.layout.ssid_rename_dialog,dialogUtils.getFragmentManager(),"sudo");

                        return false;
                    }
                });
            }
        }


        holder.dLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uses the fragment callback on the selected option.
                if (QRScanFragment.fromQRScannerFragment == false && QRScanFragment.fromQRScannerFragment2 == true)
                {

                }
                else {
                    chosenPosition = position;
                    mCallback.callback(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}
