package be.ac.ulb.iridia.psychoquest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import be.ac.ulb.iridia.psychoquest.Experiment;

/**
 * Created by gaetan on 20.05.14.
 */
public class ExperimentSAMResultsDBAdapter extends AbstractExperimentResultDBAdapter {
    private static final String TAG = "ExperimentResultsDBAdapter";

    private static final String TABLE_SAM_EXP_RESULTS = "SAMExpResults";

    public ExperimentSAMResultsDBAdapter(Context context) {
        super(context);
    }

    @Override
    public boolean insert(Experiment currentExperiment) {
        ContentValues args = new ContentValues();
        args.put("ParticipantID", currentExperiment.getParticipantID());
        args.put("ExperimentID", currentExperiment.getExperimentID());
        args.put("SessionID", currentExperiment.getCurrentSession());
        args.put("ArousalScore", currentExperiment.mSAM.getArousal());
        args.put("ValenceScore", currentExperiment.mSAM.getValence());
        mDB.insert(TABLE_SAM_EXP_RESULTS, null, args);
        return true;
    }

    @Override
    public void update(Experiment currentExperiment) {
        ContentValues args = new ContentValues();
        args.put("ParticipantID", currentExperiment.getParticipantID());
        args.put("ExperimentID", currentExperiment.getExperimentID());
        args.put("SessionID", currentExperiment.getCurrentSession());
        args.put("ArousalScore", currentExperiment.mSAM.getArousal());
        args.put("ValenceScore", currentExperiment.mSAM.getValence());
        mDB.update(TABLE_SAM_EXP_RESULTS, args, "ParticipantID = " +
                currentExperiment.getParticipantID() +
                " AND SessionID=" + currentExperiment.getCurrentSession() +
                " AND ExperimentID=" + currentExperiment.getExperimentID(), null);
    }

    @Override
    public boolean doesParticipantIDExperimentIDSessionIDExist(Experiment currentExperiment) {
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM " + TABLE_SAM_EXP_RESULTS +
                " WHERE ParticipantID= " + currentExperiment.getParticipantID() +
                " AND SessionID= "+ currentExperiment.getCurrentSession() +
                " AND ExperimentID="+currentExperiment.getExperimentID(), null);
        cursor.moveToFirst();
        if (cursor.getInt(0) == 0) {
            cursor.close();
            return false;
        }
        else {
            cursor.close();
            return true;
        }
    }

    public boolean isEmpty() {
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM " + TABLE_SAM_EXP_RESULTS, null);
        cursor.moveToFirst();
        if (cursor.getInt(0) == 0) {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }

    public int getArousalScore(Experiment currentExperiment, int sessionID) {
        return getScore(currentExperiment, sessionID, "ArousalScore");
    }
    public int getValenceScore(Experiment currentExperiment, int sessionID) {
        return getScore(currentExperiment, sessionID, "ValenceScore");
    }

    public List<Integer> getAllParticipantsInExperiment(int experimentID) {
        List<Integer> participantIDs = new ArrayList<Integer>();
        String query = "SELECT DISTINCT ParticipantID FROM " + TABLE_SAM_EXP_RESULTS +
                " WHERE ExperimentID=" +experimentID;
        Cursor cursor = mDB.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                participantIDs.add(cursor.getInt(0));
            }while (cursor.moveToNext());
        }
        return participantIDs;
    }
    public void clean() {
        mDB.delete(TABLE_SAM_EXP_RESULTS, null, null);
    }

    private int getScore(Experiment currentExperiment, int sessionID, String score) {
        int ret = -1;

        String query = "SELECT " + score + " FROM " + TABLE_SAM_EXP_RESULTS +
                " WHERE ParticipantID =" + currentExperiment.getParticipantID() +
                " AND SessionID = " + sessionID +
                " AND ExperimentID="+currentExperiment.getExperimentID();

        Log.d(TAG, "SQL query " + query);
        Cursor cursor = mDB.rawQuery(query,null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret = cursor.getInt(0);
        }

        return ret;
    }
}
