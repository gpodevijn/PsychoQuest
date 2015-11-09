package be.ac.ulb.iridia.psychoquest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

/**
 * Created by gaetan on 09.11.15.
 */
public class DropboxFragment extends Fragment {

    private static final String TAG = "DropboxFragment";
    public DropboxAPI<AndroidAuthSession> mDBApi;

    private OnDropboxIntegrationListener mCallback;

    public interface OnDropboxIntegrationListener {
        void onDropboxIntegrationEnabled();
    }

    public DropboxFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dropbox_fragment, container, false);

        mDBApi = ((MainActivity)getActivity()).getDBApi();

        initDropboxRadioButton(v);

        return v;
    }

    public void initDropboxRadioButton(View v) {
        final CheckBox dropboxCheckBox  = (CheckBox)v.findViewById(R.id.enable_dropbox_checkbox);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("dropbox", Context.MODE_PRIVATE);
        boolean isIntegrationEnabled = sharedPref.getBoolean("isIntegrationEnabled", false);
        if (isIntegrationEnabled) {
            dropboxCheckBox.setChecked(true);
        }
        dropboxCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableDropboxIntegration();
                } else {
                    disableDropboxIntegration();
                }
            }
        });
    }

    private void enableDropboxIntegration() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("dropbox", Context.MODE_PRIVATE);
        boolean isIntegrationEnabled = sharedPref.getBoolean("isIntegrationEnabled", false);
        if (!isIntegrationEnabled) {
            sharedPref.edit().putBoolean("isIntegrationEnabled", true).apply();
            mCallback.onDropboxIntegrationEnabled();
        }
    }

    private void disableDropboxIntegration() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("dropbox", Context.MODE_PRIVATE);
        boolean isIntegrationEnabled = sharedPref.getBoolean("isIntegrationEnabled", false);
        if (isIntegrationEnabled) {
            sharedPref.edit().putBoolean("isIntegrationEnabled", false).apply();
            sharedPref.edit().remove("token");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Activity a;
            if (context instanceof Activity){
                a=(Activity) context;
                mCallback = (OnDropboxIntegrationListener)a;

            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDropboxIntegrationListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnDropboxIntegrationListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDropboxIntegrationListener");
        }
    }
}
