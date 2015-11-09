package be.ac.ulb.iridia.psychoquest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import be.ac.ulb.iridia.psychoquest.Database.ExperimentDBAdapter;

/**
 * Created by gaetan on 05.11.15.
 */
public class CreateExperimentFragment extends Fragment {
    private final static String TAG = "CreateExperiment";

    private boolean mIncludeSAM = false;
    private boolean mIncludeNASATLX = false;

    private EditText mExperimentNameEditText;
    private EditText mExperimentNoSessionEditText;

    public CreateExperimentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.create_experiment_fragment, container, false);

        initEditTexts(v);
        initQuestionnairesCheckBoxes(v);
        initValidateButton(v);

        return v;
    }

    private void initEditTexts(View v) {
        mExperimentNameEditText = (EditText) v.findViewById(R.id.experiment_name);
        mExperimentNoSessionEditText = (EditText) v.findViewById(R.id.number_sessions);
    }
    private void initQuestionnairesCheckBoxes(View v) {
        LinearLayout checkBoxLL = (LinearLayout)v.findViewById(R.id.questionnaires_checkbox_list);
        CheckBox samCheckbox = new CheckBox(getActivity().getApplicationContext());
        samCheckbox.setText(R.string.sam_title);
        samCheckbox.setTextColor(Color.BLACK);
        CheckBox nasatlxCheckbox = new CheckBox(getActivity().getApplicationContext());
        nasatlxCheckbox.setText(R.string.nasatlx_title);
        nasatlxCheckbox.setTextColor(Color.BLACK);

        samCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncludeSAM = !mIncludeSAM;
            }
        });
        nasatlxCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncludeNASATLX = !mIncludeNASATLX;
            }
        });

        checkBoxLL.addView(samCheckbox);
        checkBoxLL.addView(nasatlxCheckbox);
    }

    private void initValidateButton(View v) {
        Button validateButton = (Button)v.findViewById(R.id.validate_button);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mExperimentNameEditText.getText() != null &&
                        mExperimentNoSessionEditText.getText() != null &&
                        (mIncludeNASATLX || mIncludeSAM)) {
                    String experimentName = mExperimentNameEditText.getText().toString();
                    int sessionNumber = Integer.parseInt(mExperimentNoSessionEditText.getText().toString());

                    CreateExperiment(experimentName, sessionNumber);

                    HomeFragment homeFragment = new HomeFragment();
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content_frame, homeFragment);
                    fragmentTransaction.commit();

                } else {
                    Context context = getActivity();
                    CharSequence text = getResources().getString(R.string.err_msg_create_experiment);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }
    public void CreateExperiment(String experimentName, int sessionNumber) {
        ExperimentDBAdapter experimentDBAdapter = new ExperimentDBAdapter(getActivity());
        experimentDBAdapter.open();
        experimentDBAdapter.insert(experimentName, sessionNumber, mIncludeNASATLX, mIncludeSAM);
        experimentDBAdapter.close();
    }
}
