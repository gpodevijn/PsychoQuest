package be.ac.ulb.iridia.psychoquest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by gaetan on 09.11.15.
 */
public class SessionTerminatedFragment extends Fragment {
    private OnSessionTerminatedListener mCallback;

    public interface OnSessionTerminatedListener {
        void onSessionFinished();
    }

    public SessionTerminatedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.session_terminated_fragment, container, false);
        initButton(v);
        return v;
    }

    private void initButton(View v) {
        Button nextSessionButton = (Button)v.findViewById(R.id.next_session_button);
        nextSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onSessionFinished();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Activity a;
            if (context instanceof Activity){
                a=(Activity) context;
                mCallback = (OnSessionTerminatedListener)a;

            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSessionTerminatedListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnSessionTerminatedListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSessionTerminatedListener");
        }
    }

}
