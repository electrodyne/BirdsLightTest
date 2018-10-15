package rocks.electrodyne.birdlighttest;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.ViewHolder> {
    private String[] mDataset;
    private Integer[] mImage;
    private Utils.onClickCallback mCallback;

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

    public MainViewAdapter(Utils.onClickCallback callback,String[] myDataset, Integer[] myImage){
        mDataset = myDataset;
        mImage = myImage;
        mCallback =callback;
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

        holder.dLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uses the fragment callback on the selected option.
                mCallback.callback(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}
