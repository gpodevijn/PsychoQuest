package be.ac.ulb.iridia.psychoquest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.ac.ulb.iridia.psychoquest.Questionnaires.SAM;

/**
 * Created by gaetan on 06.11.15.
 */
public class SAMSessionFragment extends Fragment {
    private static final String TAG = "SAMSession";
    private OnSAMSessionListener mCallback;

    private List<Button> mValenceButtons = new ArrayList<Button>();
    private List<Button> mArousalButtons = new ArrayList<Button>();

    private SAM mSAM = new SAM();

    public SAMSessionFragment() {}

    public interface OnSAMSessionListener {
        void onSAMSessionFinished(SAM sam);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.samsession_fragment, container, false);

        InitNextButton(v);
        InitGridView(v);

        return v;
    }

    private void InitGridView(View v) {
        Button button;
        for (int i=1; i<=9; ++i) {
            int id = getResources().getIdentifier("valence_" + i + "_button", "id", getActivity().getPackageName());
            final int score = i;
            button = (Button)v.findViewById(id);
            mValenceButtons.add(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.isSelected()) {
                        view.setSelected(false);
                    } else {
                        for (Button b : mValenceButtons) {
                            if (b.isSelected())
                                b.setSelected(false);
                        }
                        view.setSelected(true);
                        mSAM.setValence(score);

                    }
                }
            });
            id = getResources().getIdentifier("arousal_" + i + "_button", "id", getActivity().getPackageName());
            button = (Button)v.findViewById(id);
            mArousalButtons.add(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.isSelected()) {
                        view.setSelected(false);
                    }
                    else {
                        for (Button b : mArousalButtons) {
                            if (b.isSelected())
                                b.setSelected(false);
                        }
                        view.setSelected(true);
                        mSAM.setArousal(score);
                    }
                }
            });
        }

    }

    private void InitNextButton(View v) {
        Button nextButton = (Button)v.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bArousalSelected = false;
                for (Button s : mArousalButtons) {
                    if (s.isSelected()) {
                        bArousalSelected = true;
                        break;
                    }
                }

                boolean bValenceSelected = false;
                for (Button s : mValenceButtons) {
                    if (s.isSelected()) {
                        bValenceSelected = true;
                        break;
                    }
                }

                if (bArousalSelected && bValenceSelected) {
                    mCallback.onSAMSessionFinished(mSAM);
                } else {
                    Context context = getActivity();
                    CharSequence text = getResources().getString(R.string.err_msg_select_arousal_valence);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
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
                mCallback = (OnSAMSessionListener)a;

            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSAMSessionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnSAMSessionListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSAMSessionListener");
        }
    }
}
