package be.ac.ulb.iridia.psychoquest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import be.ac.ulb.iridia.psychoquest.Database.ExperimentDBAdapter;
import be.ac.ulb.iridia.psychoquest.Database.ParticipantDBAdapter;

/**
 * Created by gaetan on 05.11.15.
 */
public class StartExperimentFragment extends Fragment {
    private OnStartExperimentSelectionListener mCallback;
    private Spinner mExperimentSpinner = null;
    private Spinner mParticipantSpinner = null;


    public StartExperimentFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface OnStartExperimentSelectionListener {
        void onExperimentReadyToStart(Experiment experiment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_experiment_fragment, container, false);

        InitExperimentSpinner(v);
        InitParticipantSpinner(v);

        Button startExperimentButton = (Button) v.findViewById(R.id.start_experiment);
        startExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExperimentDBAdapter experimentDBAdapter = new ExperimentDBAdapter(getActivity());
                experimentDBAdapter.open();
                String expName = mExperimentSpinner.getSelectedItem().toString();
                int experimentID = experimentDBAdapter.getExperimentID(expName);
                int numberOfSessions = experimentDBAdapter.getNumberOfSessions(experimentID);
                boolean hasSAM = experimentDBAdapter.doesIncludeSAM(experimentID);
                boolean hasNASATLX = experimentDBAdapter.doesIncludeNASATLX(experimentID);
                experimentDBAdapter.close();


                ParticipantDBAdapter participantDBAdapter = new ParticipantDBAdapter(getActivity());
                participantDBAdapter.open();
                String partName = mParticipantSpinner.getSelectedItem().toString();
                int participantID = participantDBAdapter.getParticipantID(partName);
                participantDBAdapter.close();

                Experiment experiment = new Experiment();
                experiment.setCurrentSession(1);
                experiment.setSessionNumber(numberOfSessions);
                experiment.setParticipantID(participantID);
                experiment.setExperimentID(experimentID);
                experiment.setHasSAM(hasSAM);
                experiment.setHasNASATLX(hasNASATLX);

                mCallback.onExperimentReadyToStart(experiment);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Activity a;
            if (context instanceof Activity){
                a=(Activity) context;
                mCallback = (OnStartExperimentSelectionListener)a;

            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStartExperimentSelectionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnStartExperimentSelectionListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStartExperimentSelectionListener");
        }
    }

    private void InitExperimentSpinner(View f) {
        mExperimentSpinner = (Spinner) f.findViewById(R.id.select_experiment_spinner);
        ExperimentDBAdapter experimentDBAdapter = new ExperimentDBAdapter(getActivity());
        experimentDBAdapter.open();
        List<String> experiments = experimentDBAdapter.getAllExperimentNames();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, experiments);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExperimentSpinner.setAdapter(dataAdapter);
        experimentDBAdapter.close();
    }

    private void InitParticipantSpinner(View f) {
        mParticipantSpinner = (Spinner) f.findViewById(R.id.select_participant_spinner);
        ParticipantDBAdapter participantDBAdapter = new ParticipantDBAdapter(getActivity());
        participantDBAdapter.open();
        List<String> participants = participantDBAdapter.getAllParticipants();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, participants);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mParticipantSpinner.setAdapter(dataAdapter);
        participantDBAdapter.close();
    }
}
