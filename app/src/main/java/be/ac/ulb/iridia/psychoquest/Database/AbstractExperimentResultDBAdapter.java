package be.ac.ulb.iridia.psychoquest.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.ac.ulb.iridia.psychoquest.Experiment;

/**
 * Created by gaetan on 20.05.14.
 */
public class AbstractExperimentResultDBAdapter {

    private static final String TAG = "AbsExperimentResult";

    protected DatabaseHelper mDBHelper;
    protected SQLiteDatabase mDB;

    private static final String DATABASE_NAME = "DB_PSYCHOQUEST";
    private static final int DATABASE_VERSION = 1;

    protected Context mContext = null;

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }

    public AbstractExperimentResultDBAdapter(Context context) {
        mContext = context;
    }

    public AbstractExperimentResultDBAdapter open() {
        mDBHelper = new DatabaseHelper(mContext);
        mDB = mDBHelper.getWritableDatabase();
        if (!mDB.isReadOnly()) {
            mDB.execSQL("PRAGMA foreign_keys = ON;");
        }
        return this;
    }

    public void close() {
        mDBHelper.close();
    }

    public static void dropDB(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public  boolean insert(Experiment currentExperiment){return false;}
    public  void update(Experiment currentExperiment){}
    public  boolean doesParticipantIDExperimentIDSessionIDExist(Experiment currentExperiment){return false;}
}
