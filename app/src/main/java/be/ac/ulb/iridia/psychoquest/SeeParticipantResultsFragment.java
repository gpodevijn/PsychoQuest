package be.ac.ulb.iridia.psychoquest;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import be.ac.ulb.iridia.psychoquest.Database.ExperimentDBAdapter;
import be.ac.ulb.iridia.psychoquest.Database.ExperimentNASATLXResultsDBAdapter;
import be.ac.ulb.iridia.psychoquest.Database.ExperimentSAMResultsDBAdapter;
import be.ac.ulb.iridia.psychoquest.Database.ParticipantDBAdapter;

/**
 * Created by gaetan on 09.11.15.
 */
public class SeeParticipantResultsFragment extends Fragment {
    private static final String TAG = "SeeParticipantResults";

    private Spinner mExperimentSpinner = null;
    private Spinner mParticipantSpinner = null;
    private TextView mSaveSuccessful;

    private String mParticipantResults = "";
    private String mParticipantName;

    private Experiment mSelectedExperiment;

    public SeeParticipantResultsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.see_participant_result_fragment, container, false);
        mSaveSuccessful = (TextView)v.findViewById(R.id.save_successfull_textview);
        initButton(v);

        // participant spinner must be init before experiment's because it is update by experiment's
        InitParticipantSpinner(v);
        InitExperimentSpinner(v);

        return v;
    }

    private void initButton(View v) {
        Button saveDropbox = (Button)v.findViewById(R.id.save_participant_dropbox);
        saveDropbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("dropbox", Context.MODE_PRIVATE);
                boolean isIntegrationEnabled = sharedPref.getBoolean("isIntegrationEnabled", false);
                if (!isIntegrationEnabled) {
                    Context context = getActivity();
                    CharSequence text = getResources().getString(R.string.err_msg_dropbox_not_enabled);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            saveOnDropbox();
                            return null;
                        }
                    }.execute();
                    mSaveSuccessful.setText(getString(R.string.saved_dropbox_successfull));
                }
            }
        });

        Button saveSD = (Button)v.findViewById(R.id.save_participant_sd);
        saveSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOnSD();
            }
        });
    }

    private void InitExperimentSpinner(View v) {
        mExperimentSpinner = (Spinner)v.findViewById(R.id.experiment_spinner);

        ExperimentDBAdapter experimentDBAdapter = new ExperimentDBAdapter(getActivity());
        experimentDBAdapter.open();
        List<String> experiments = experimentDBAdapter.getAllExperimentNames();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, experiments);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mExperimentSpinner.setAdapter(dataAdapter);
        experimentDBAdapter.close();

        mExperimentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update participant spinner
                ExperimentDBAdapter experimentDBAdapter = new ExperimentDBAdapter(getActivity());
                experimentDBAdapter.open();
                int selectedExperimentID = experimentDBAdapter.getExperimentID(mExperimentSpinner.getSelectedItem().toString());
                mSelectedExperiment = experimentDBAdapter.getExperimentFromID(selectedExperimentID);
                experimentDBAdapter.close();

                // Get all participants in that experiment
                ParticipantDBAdapter participantDBAdapter = new ParticipantDBAdapter(getActivity());
                participantDBAdapter.open();
                List<String> participantNames = new ArrayList<String>();
                ArrayAdapter<String> dataAdapter;

                if (mSelectedExperiment.hasNASATLX()) {
                    ExperimentNASATLXResultsDBAdapter experimentNASATLXResultsDBAdapter = new ExperimentNASATLXResultsDBAdapter(getActivity());
                    experimentNASATLXResultsDBAdapter.open();
                    List<Integer> participantIDs = experimentNASATLXResultsDBAdapter.getAllParticipantsInExperiment(selectedExperimentID);
                    if (!participantIDs.isEmpty()) {
                        for (Integer participantID : participantIDs) {
                            String name = participantDBAdapter.getParticipantName(participantID);
                            participantNames.add(name);
                        }
                    }
                    experimentNASATLXResultsDBAdapter.close();
                }
                if (mSelectedExperiment.hasSAM()) {
                    ExperimentSAMResultsDBAdapter experimentSAMResultsDBAdapter = new ExperimentSAMResultsDBAdapter(getActivity());
                    experimentSAMResultsDBAdapter.open();
                    List<Integer> participantIDs = experimentSAMResultsDBAdapter.getAllParticipantsInExperiment(selectedExperimentID);

                    if (!participantIDs.isEmpty()) {
                        for (Integer participantID : participantIDs) {
                            String name = participantDBAdapter.getParticipantName(participantID);
                            participantNames.add(name);
                        }
                    }
                    experimentSAMResultsDBAdapter.close();
                }
                dataAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, participantNames);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                mParticipantSpinner.setAdapter(dataAdapter);
                participantDBAdapter.close();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.d(TAG, "onNothingSelected");
            }

        });
    }

    private void InitParticipantSpinner(final View v) {
        mParticipantSpinner = (Spinner)v.findViewById(R.id.participant_spinner);
        mParticipantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mParticipantName = mParticipantSpinner.getSelectedItem().toString();

                // retrieve participant id
                ParticipantDBAdapter participantDBAdapter = new ParticipantDBAdapter(getActivity());
                participantDBAdapter.open();
                int partID = participantDBAdapter.getParticipantID(mParticipantName);
                mSelectedExperiment.setParticipantID(partID);
                participantDBAdapter.close();

                mParticipantResults += "Session";
                if (mSelectedExperiment.hasSAM() && mSelectedExperiment.hasNASATLX()) {
                    mParticipantResults += "\t Valence \t Arousal \t Mental \t Physical \t Temporal \t Performance \t Effort \t Frustration\n";
                    retrieveSAMNASATLXResult();
                } else if (mSelectedExperiment.hasSAM()) {
                    mParticipantResults += "\t Valence \t Arousal\n";
                    retrieveSAMResult();
                } else if (mSelectedExperiment.hasNASATLX()) {
                    mParticipantResults += "\t Mental \t Physical \t Temporal \t Performance \t Effort \t Frustration\n";
                    retrieveNASATLXResult();
                }

                ((TextView) v.findViewById(R.id.result_textview)).setText(mParticipantResults);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.d(TAG, "onNothingSelected");
            }

        });
    }

    private void retrieveSAMNASATLXResult() {
        ExperimentSAMResultsDBAdapter experimentSAMResultsDBAdapter = new ExperimentSAMResultsDBAdapter(getActivity());
        experimentSAMResultsDBAdapter.open();
        ExperimentNASATLXResultsDBAdapter experimentNASATLXResultsDBAdapter = new ExperimentNASATLXResultsDBAdapter(getActivity());
        experimentNASATLXResultsDBAdapter.open();

        for (int i = 1; i <= mSelectedExperiment.getSessionNumber(); ++i) {
            mParticipantResults += i + " \t " +
                    experimentSAMResultsDBAdapter.getValenceScore(mSelectedExperiment, i) + " \t " +
                    experimentSAMResultsDBAdapter.getArousalScore(mSelectedExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getMentalScore(mSelectedExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getPhysicalScore(mSelectedExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getTemporalScore(mSelectedExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getPerformanceScore(mSelectedExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getEffortScore(mSelectedExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getFrustrationScore(mSelectedExperiment, i) + " \t\n ";
        }
        experimentSAMResultsDBAdapter.close();
        experimentNASATLXResultsDBAdapter.close();
    }

    private void retrieveSAMResult() {
        ExperimentSAMResultsDBAdapter experimentResultsDBAdapter = new ExperimentSAMResultsDBAdapter(getActivity());
        experimentResultsDBAdapter.open();

        for (int i = 1; i <= mSelectedExperiment.getSessionNumber(); ++i) {
            mParticipantResults += i + " \t " +
                    experimentResultsDBAdapter.getValenceScore(mSelectedExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getArousalScore(mSelectedExperiment, i) + " \t " + " \n ";
        }
        experimentResultsDBAdapter.close();
    }
    private void retrieveNASATLXResult() {
        ExperimentNASATLXResultsDBAdapter experimentResultsDBAdapter = new ExperimentNASATLXResultsDBAdapter(getActivity());
        experimentResultsDBAdapter.open();

        for (int i = 1; i <= mSelectedExperiment.getSessionNumber(); ++i) {
            mParticipantResults += i + " \t " +
                    experimentResultsDBAdapter.getMentalScore(mSelectedExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getPhysicalScore(mSelectedExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getTemporalScore(mSelectedExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getPerformanceScore(mSelectedExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getEffortScore(mSelectedExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getFrustrationScore(mSelectedExperiment, i) + " \t \n";
        }
        experimentResultsDBAdapter.close();
    }

    private void saveOnSD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());
        String resultFilePath = Environment.getExternalStorageDirectory() +
                "/PsychoQuest/experiment_results/" +
                mSelectedExperiment.getExperimentName() + "/" +
                currentDateAndTime+"_"+mParticipantName+".txt";
        Utils.saveOnSD(resultFilePath, mParticipantResults);

        mSaveSuccessful.setText(getString(R.string.saved_sc_successfull));
    }

    private void saveOnDropbox() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());

        Utils.saveOnDropbox(((MainActivity)getActivity()).getDBApi(),
                mParticipantResults,
                "/participant_results_backup/"+
                        mSelectedExperiment.getExperimentName()+"/" +
                        currentDateAndTime+"_"+
                        mParticipantName+".txt");

       /* SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());

        InputStream is = new ByteArrayInputStream(mParticipantResults.getBytes());

        DropboxAPI.Entry response = null;
        DropboxAPI<AndroidAuthSession> dbAPI = ((MainActivity)getActivity()).getDBApi();
        try {
            if (dbAPI == null) {
                Log.e(TAG, "Error mdbapi is null");
            }
            response = dbAPI.putFile("/participant_results_backup/"+
                            mSelectedExperiment.getExperimentName()+"/" +
                            currentDateAndTime+"_"+
                            mParticipantName+".txt", is,
                    mParticipantResults.length(), null, null);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);*/
    }

}
