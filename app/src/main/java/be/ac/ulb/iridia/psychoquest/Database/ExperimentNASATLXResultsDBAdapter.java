package be.ac.ulb.iridia.psychoquest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import be.ac.ulb.iridia.psychoquest.Experiment;

/**
 * Created by gaetan on 20.05.14.
 */
public class ExperimentNASATLXResultsDBAdapter extends AbstractExperimentResultDBAdapter {
    private static final String TAG = "ExperimentResultsDBAdapter";

    private static final String TABLE_NASATLX_EXP_RESULTS = "NASATLXExpResults";

    public ExperimentNASATLXResultsDBAdapter(Context context) {
        super(context);
    }

    @Override
    public boolean insert(Experiment currentExperiment) {
        ContentValues args = new ContentValues();
        args.put("ParticipantID", currentExperiment.getParticipantID());
        args.put("ExperimentID", currentExperiment.getExperimentID());
        args.put("SessionID", currentExperiment.getCurrentSession());
        args.put("MentalScore", currentExperiment.mNASATLX.getMental());
        args.put("PhysicalScore", currentExperiment.mNASATLX.getPhysical());
        args.put("TemporalScore", currentExperiment.mNASATLX.getTemporal());
        args.put("PerformanceScore", currentExperiment.mNASATLX.getPerformance());
        args.put("EffortScore", currentExperiment.mNASATLX.getEffort());
        args.put("FrustrationScore", currentExperiment.mNASATLX.getFrustration());
        mDB.insert(TABLE_NASATLX_EXP_RESULTS, null, args);
        return true;
    }

    @Override
    public void update(Experiment currentExperiment) {
        ContentValues args = new ContentValues();
        args.put("ParticipantID", currentExperiment.getParticipantID());
        args.put("ExperimentID", currentExperiment.getExperimentID());
        args.put("SessionID", currentExperiment.getCurrentSession());
        args.put("MentalScore", currentExperiment.mNASATLX.getMental());
        args.put("PhysicalScore", currentExperiment.mNASATLX.getPhysical());
        args.put("TemporalScore", currentExperiment.mNASATLX.getTemporal());
        args.put("PerformanceScore", currentExperiment.mNASATLX.getPerformance());
        args.put("EffortScore", currentExperiment.mNASATLX.getEffort());
        args.put("FrustrationScore", currentExperiment.mNASATLX.getFrustration());
        mDB.update(TABLE_NASATLX_EXP_RESULTS, args, "ParticipantID = " +
                currentExperiment.getParticipantID() +
                " AND SessionID=" + currentExperiment.getCurrentSession() +
                " AND ExperimentID=" + currentExperiment.getExperimentID(), null);
    }

    @Override
    public boolean doesParticipantIDExperimentIDSessionIDExist(Experiment currentExperiment) {
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM " + TABLE_NASATLX_EXP_RESULTS +
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
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM " + TABLE_NASATLX_EXP_RESULTS, null);
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

    public int getMentalScore(Experiment currentExperiment, int sessionID) {
        return getScore(currentExperiment,sessionID, "PhysicalScore");
    }

    public int getPhysicalScore(Experiment currentExperiment, int sessionID) {
        return getScore(currentExperiment, sessionID,"PhysicalScore");

    }

    public int getPerformanceScore(Experiment currentExperiment, int sessionID) {
        return getScore(currentExperiment, sessionID,"PerformanceScore");
    }

    public int getTemporalScore(Experiment currentExperiment, int sessionID) {
        return getScore(currentExperiment, sessionID,"TemporalScore");
    }

    public int getEffortScore(Experiment currentExperiment, int sessionID) {
        return getScore(currentExperiment, sessionID,"EffortScore");
    }

    public int getFrustrationScore(Experiment currentExperiment, int sessionID) {
        return getScore(currentExperiment, sessionID,"FrustrationScore");
    }

    public List<Integer> getAllParticipantsInExperiment(int experimentID) {
        List<Integer> participantIDs = new ArrayList<Integer>();
        String query = "SELECT DISTINCT ParticipantID FROM " + TABLE_NASATLX_EXP_RESULTS +
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
        mDB.delete(TABLE_NASATLX_EXP_RESULTS, null, null);
    }

    private int getScore(Experiment currentExperiment, int sessionID, String score) {
        int ret = -1;

        String query = "SELECT " + score + " FROM " + TABLE_NASATLX_EXP_RESULTS +
                " WHERE ParticipantID =" + currentExperiment.getParticipantID() +
                " AND SessionID = " + sessionID +
                " AND ExperimentID="+currentExperiment.getExperimentID();
        Cursor cursor = mDB.rawQuery(query,null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret = cursor.getInt(0);
        }

        return ret;
    }
}
