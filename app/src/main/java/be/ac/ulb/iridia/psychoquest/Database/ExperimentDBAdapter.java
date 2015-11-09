package be.ac.ulb.iridia.psychoquest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import be.ac.ulb.iridia.psychoquest.Experiment;

/**
 * Created by gaetan on 21/05/14.
 */
public class ExperimentDBAdapter extends AbstractDBAdapter {
    private static final String TAG = "ExperimentDBAdapter";

    private  static  final String TABLE_EXPERIMENT = "Experiment";

    public ExperimentDBAdapter(Context context) {
        super(context);
    }


    public long insert(String name, int nbSession, boolean NASATLX, boolean SAM) {
        Cursor cursor = mDB.rawQuery("SELECT date('now')", null);
        cursor.moveToFirst();
        ContentValues args = new ContentValues();
        args.put("Name", name);
        args.put("Date", cursor.getString(0));
        args.put("SessionNumber", nbSession);
        if (NASATLX)
            args.put("NASATLX", 1);
        if (SAM)
            args.put("SAM", 1);
        return mDB.insert(TABLE_EXPERIMENT, null, args);
    }


    public boolean isEmpty() {
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM " + TABLE_EXPERIMENT, null);
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

    public int getNumberOfSessions(int experimentID) {
        int ret = -1;
        String query = "SELECT SessionNumber FROM " + TABLE_EXPERIMENT + " WHERE _id="+experimentID;
        Cursor cursor = mDB.rawQuery(query,null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret = cursor.getInt(0);
        }
        cursor.close();

        return ret;
    }

    public int getExperimentID(String name) {
        int ret = -1;
        String query = "SELECT _id FROM " + TABLE_EXPERIMENT + " WHERE Name=?";
        Cursor cursor = mDB.rawQuery(query, new String[] { name });

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret = cursor.getInt(0);
        }

        cursor.close();
        return ret;
    }

    public Experiment getExperimentFromID(int experimentID) {
        Experiment ret = new Experiment();

        String query = "SELECT * FROM " + TABLE_EXPERIMENT + " WHERE _id =" + experimentID;
        Cursor cursor = mDB.rawQuery(query,null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret.setExperimentName(cursor.getString(1));
            Log.d(TAG, cursor.getString(1));
            ret.setSessionNumber(cursor.getInt(3));
            Log.d(TAG, cursor.getString(3));
            Log.d(TAG, "has nasa " +cursor.getInt(4));
            Log.d(TAG, "has sam " +cursor.getInt(5));

            if (cursor.getInt(4) == 1)
                ret.setHasNASATLX(true);
            else
                ret.setHasNASATLX(false);
            if (cursor.getInt(5) == 1)
                ret.setHasSAM(true);
            else
                ret.setHasSAM(false);
            ret.setExperimentID(experimentID);
        }
        cursor.close();
        return ret;
    }

    public String getExperimentName(int experimentID) {
        String ret = "";

        String query = "SELECT Name FROM " + TABLE_EXPERIMENT + " WHERE _id =" + experimentID;
        Cursor cursor = mDB.rawQuery(query,null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret = cursor.getString(0);
        }
        cursor.close();
        return ret;
    }

    public boolean doesIncludeSAM(int experimentID) {
        int ret = -1;
        String query = "SELECT SAM FROM " + TABLE_EXPERIMENT + " WHERE _id =" + experimentID;
        Cursor cursor = mDB.rawQuery(query,null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret = cursor.getInt(0);
        }

        cursor.close();
        return ret == 1;
    }

    public boolean doesIncludeNASATLX(int experimentID) {
        int ret = -1;
        String query = "SELECT NASATLX FROM " + TABLE_EXPERIMENT + " WHERE _id =" + experimentID;
        Cursor cursor = mDB.rawQuery(query,null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret = cursor.getInt(0);
        }

        cursor.close();
        return ret == 1;
    }

    public List<String> getAllExperimentNames() {
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_EXPERIMENT;
        Cursor cursor = mDB.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        mDB.close();

        return labels;
    }

    public void Clean() {
        mDB.delete(TABLE_EXPERIMENT, null, null);
    }
}
