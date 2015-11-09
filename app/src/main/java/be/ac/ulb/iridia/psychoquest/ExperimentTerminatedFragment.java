package be.ac.ulb.iridia.psychoquest;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import be.ac.ulb.iridia.psychoquest.Database.ExperimentDBAdapter;
import be.ac.ulb.iridia.psychoquest.Database.ExperimentNASATLXResultsDBAdapter;
import be.ac.ulb.iridia.psychoquest.Database.ExperimentSAMResultsDBAdapter;
import be.ac.ulb.iridia.psychoquest.Database.ParticipantDBAdapter;

/**
 * Created by gaetan on 09.11.15.
 */
public class ExperimentTerminatedFragment extends Fragment {
    private static final String TAG = "ExperimentTerminated";
    private String mParticipantName;
    private String mExperimentName;
    private String mParticipantResults = "";
    private TextView mSaveSuccessfull;
    Experiment mCurrentExperiment;

    public ExperimentTerminatedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.experiment_terminated_fragment, container, false);
        mSaveSuccessfull = (TextView)v.findViewById(R.id.save_successfull_textview);

        mCurrentExperiment = ((MainActivity)getActivity()).getCurrentExperiment();
        prepareExperimentResult();
        initButton(v);
        displayResults(v);
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
                    mSaveSuccessfull.setText(getString(R.string.saved_dropbox_successfull));
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

    private void prepareExperimentResult() {
        ParticipantDBAdapter participantDBAdapter = new ParticipantDBAdapter(getActivity());
        participantDBAdapter.open();
        mParticipantName = participantDBAdapter.getParticipantName(mCurrentExperiment.getParticipantID());
        participantDBAdapter.close();

        ExperimentDBAdapter experimentDBAdapter = new ExperimentDBAdapter(getActivity());
        experimentDBAdapter.open();
        mExperimentName = experimentDBAdapter.getExperimentName(mCurrentExperiment.getExperimentID());
        experimentDBAdapter.close();

        mParticipantResults += "Session";
        if (mCurrentExperiment.hasSAM() && mCurrentExperiment.hasNASATLX()) {
            mParticipantResults += "\t Valence \t Arousal \t Mental \t Physical \t Temporal \t Performance \t Effort \t Frustration\n";
            retrieveSAMNASATLXResult();
        }
        else if (mCurrentExperiment.hasSAM()) {
            mParticipantResults += "\t Valence \t Arousal\n";
            retrieveSAMResult();
        }
        else if (mCurrentExperiment.hasNASATLX()) {
            mParticipantResults += "\t Mental \t Physical \t Temporal \t Performance \t Effort \t Frustration\n";
            retrieveNASATLXResult();
        }
    }

    private void retrieveSAMNASATLXResult() {
        ExperimentSAMResultsDBAdapter experimentSAMResultsDBAdapter = new ExperimentSAMResultsDBAdapter(getActivity());
        experimentSAMResultsDBAdapter.open();
        ExperimentNASATLXResultsDBAdapter experimentNASATLXResultsDBAdapter = new ExperimentNASATLXResultsDBAdapter(getActivity());
        experimentNASATLXResultsDBAdapter.open();

        for (int i = 1; i <= mCurrentExperiment.getSessionNumber(); ++i) {
            mParticipantResults += i + " \t " +
                    experimentSAMResultsDBAdapter.getValenceScore(mCurrentExperiment, i) + " \t " +
                    experimentSAMResultsDBAdapter.getArousalScore(mCurrentExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getMentalScore(mCurrentExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getPhysicalScore(mCurrentExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getTemporalScore(mCurrentExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getPerformanceScore(mCurrentExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getEffortScore(mCurrentExperiment, i) + " \t " +
                    experimentNASATLXResultsDBAdapter.getFrustrationScore(mCurrentExperiment, i) + " \t\n ";
        }
        experimentSAMResultsDBAdapter.close();
        experimentNASATLXResultsDBAdapter.close();
    }

    private void retrieveSAMResult() {
        ExperimentSAMResultsDBAdapter experimentResultsDBAdapter = new ExperimentSAMResultsDBAdapter(getActivity());
        experimentResultsDBAdapter.open();

        for (int i = 1; i <= mCurrentExperiment.getSessionNumber(); ++i) {
            mParticipantResults += i + " \t " +
                    experimentResultsDBAdapter.getValenceScore(mCurrentExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getArousalScore(mCurrentExperiment, i) + " \t " + " \n ";
        }
        experimentResultsDBAdapter.close();
    }
    private void retrieveNASATLXResult() {
        ExperimentNASATLXResultsDBAdapter experimentResultsDBAdapter = new ExperimentNASATLXResultsDBAdapter(getActivity());
        experimentResultsDBAdapter.open();

        for (int i = 1; i <= mCurrentExperiment.getSessionNumber(); ++i) {
            mParticipantResults += i + " \t " +
                    experimentResultsDBAdapter.getMentalScore(mCurrentExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getPhysicalScore(mCurrentExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getTemporalScore(mCurrentExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getPerformanceScore(mCurrentExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getEffortScore(mCurrentExperiment, i) + " \t " +
                    experimentResultsDBAdapter.getFrustrationScore(mCurrentExperiment, i) + " \t \n";
        }
        experimentResultsDBAdapter.close();
    }

    private void displayResults(View v) {
        ((TextView)v.findViewById(R.id.experiment_info)).setText("(" + mParticipantName + " - " + mExperimentName + ")");
        ((TextView)v.findViewById(R.id.experiment_result)).setText(mParticipantResults);
    }

    private void saveOnSD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());
        String resultFilePath = Environment.getExternalStorageDirectory() +
                "/PsychoQuest/experiment_results/" +
                mExperimentName + "/" +
                currentDateAndTime+"_"+mParticipantName+"_"+".txt";
        Utils.saveOnSD(resultFilePath, mParticipantResults);

        mSaveSuccessfull.setText(getString(R.string.saved_sc_successfull));
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());

        String resultFilePath = Environment.getExternalStorageDirectory() +
                "/PsychoQuest/experiment_results/" +
                mExperimentName + "/" +
                currentDateAndTime+"_"+mParticipantName+"_"+".txt";
        File file = new File(resultFilePath);
        file.getParentFile().mkdirs();

        PrintWriter out = null;
        try {
            out = new PrintWriter(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (out != null)
            out.println(mParticipantResults);

        mSaveSuccessfull.setText(getString(R.string.saved_sc_successfull));*/
    }

    private void saveOnDropbox() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());

        Utils.saveOnDropbox(((MainActivity)getActivity()).getDBApi(),
                mParticipantResults,
                "/participant_results_backup/"+
                        mExperimentName+"/" +
                        currentDateAndTime+"_"+
                        mParticipantName+".txt");

        /*InputStream is = new ByteArrayInputStream(mParticipantResults.getBytes());

        DropboxAPI.Entry response = null;
        DropboxAPI<AndroidAuthSession> dbAPI = ((MainActivity)getActivity()).getDBApi();
        try {
            if (dbAPI == null) {
                Log.e(TAG, "Error mdbapi is null");
            }
            response = dbAPI.putFile("/participant_results_backup/"+
                            mExperimentName+"/" +
                            currentDateAndTime+"_"+
                            mParticipantName+".txt", is,
                    mParticipantResults.length(), null, null);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);*/
    }
}
