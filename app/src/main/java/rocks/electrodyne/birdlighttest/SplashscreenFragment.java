package rocks.electrodyne.birdlighttest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.avi.AVLoadingIndicatorView;

import static android.content.Context.MODE_PRIVATE;


/*
 * IAN:
 * As for the function of the splashscreen,
 * here This fragment will just show the title and the Loading
 * IAN1 Idea: How about the permissions? should we put them here?? -- NO! Just put them on ConnectFragment...
 * IAN2 Idea: How about we request to connect to internet? --NO!
 * IAN3 Idea: Have a fade effect on exit?? -- Sure..
 * Just Fade on Exit. Have 1s Delay..
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SplashscreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SplashscreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Strategy:
 * From Activity/Fragment, Pass in:
 * 1.   Activity/Fragment of the Caller of this activity.
 *
 * With no logic at all, after 1sec. go to next Fragment,
 */
public class SplashscreenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private Fragment mParam1;
    //private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public SplashscreenFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SplashscreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    //public static SplashscreenFragment newInstance(Fragment param1) {
    //    SplashscreenFragment fragment = new SplashscreenFragment();
    //    Bundle args = new Bundle();
    //    args.putString(ARG_PARAM1, param1);
    //    args.putString(ARG_PARAM2, param2);
    //    fragment.setArguments(args);
    //    return fragment;
    //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   SharedPreferences sharedPreference = getActivity().getSharedPreferences(NUMBER_FILE_NAME, MODE_PRIVATE);
     //   sharedPreference.edit().putInt(NUMBER_FILE_NAME, 0).apply();

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle("Permission Disabled")
                .setMessage("Please allow all permissions used by Birdslight to proceed.")
                .setCancelable(false)
                // .setView(R.layout.exit_dialog)
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        getActivity().startActivity(intent);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                    }
                });
        alertDialog = alertDialogBuilder.create();

        numPermissionGranted = getActivity().getSharedPreferences(NUMBER_FILE_NAME, MODE_PRIVATE).getInt(NUMBER_FILE_NAME, 0);
     //   if (getArguments() != null) {
     //       mParam1 = getArguments().getString(ARG_PARAM1);
     //       mParam2 = getArguments().getString(ARG_PARAM2);
     //   }
    }

    private final String[] permissions = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    Utils utils = new Utils();
    AlertDialog alertDialog;
    AlertDialog.Builder alertDialogBuilder;
    int numPermissionGranted = 0;
    View v;
    AVLoadingIndicatorView avLoadingIndicatorView;

    static final String NUMBER_FILE_NAME = "numPermissionGranted";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Run a 1 sec handler here.

        v = inflater.inflate(R.layout.fragment_splashscreen, container, false);

       // if (numPermissionGranted >= permissions.length){
      //      AVLoadingIndicatorView avLoadingIndicatorView = v.findViewById(R.id.avi);
       //     alertDialog.dismiss();
       //     avLoadingIndicatorView.show();
       // }
        avLoadingIndicatorView = v.findViewById(R.id.avi);
        avLoadingIndicatorView.hide();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    //public void onButtonPressed(Uri uri) {
    //    if (mListener != null) {
    //        mListener.onFragmentInteraction(uri);
    //    }
    //}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
     //   utils.requestPermission(getActivity(),permissions,1);
    //    if (context instanceof OnFragmentInteractionListener) {
    //        mListener = (OnFragmentInteractionListener) context;
    //    } else {
    //        throw new RuntimeException(context.toString()
    //                + " must implement OnFragmentInteractionListener");
    //    }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    //    mListener = null;
    }



    @Override
    public void onResume() {
        super.onResume();

        final Activity activity = getActivity();

        for (int i = 0; i < permissions.length; i++) {

            PermissionUtil.checkPermission(activity, permissions[i], new PermissionUtil.PermissionAskListener() {
                @Override
                public void onNeedPermission() {
                    //called only when the permission[i] needs permission request...

                    ActivityCompat.requestPermissions(activity,permissions,143);
                }

                @Override
                public void onPermissionPreviouslyDenied() {
                    if ( numPermissionGranted > 0)
                    numPermissionGranted--;
                    SharedPreferences sharedPreference = activity.getSharedPreferences(NUMBER_FILE_NAME, MODE_PRIVATE);
                    sharedPreference.edit().putInt(NUMBER_FILE_NAME, numPermissionGranted).apply();
                    ActivityCompat.requestPermissions(activity,permissions,143);
                }

                @Override
                public void onPermissionDisabled() {
                    //Show the path to approving permission settings..
                    alertDialog.show();
                }

                @Override
                public void onPermissionGranted() {
                    //increase only on first time...
                    if(numPermissionGranted < permissions.length) {
                        numPermissionGranted++;
                        SharedPreferences sharedPreference = activity.getSharedPreferences(NUMBER_FILE_NAME, MODE_PRIVATE);
                        sharedPreference.edit().putInt(NUMBER_FILE_NAME, numPermissionGranted).apply();
                    }
                }
            });
        }

        if (numPermissionGranted == permissions.length){
            alertDialog.dismiss();
            avLoadingIndicatorView.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //QRScanFragment qrScanFragment = new QRScanFragment();
                    //fragmentTransaction.replace(R.id.fragment_container, qrScanFragment );
                    ConnectFragment connectFragment = new ConnectFragment();
                    fragmentTransaction.replace(R.id.fragment_container,connectFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }, 1500);
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
  //  public interface OnFragmentInteractionListener {
  //      // TODO: Update argument type and name
  //      void onFragmentInteraction(Uri uri);
  //  }
}
