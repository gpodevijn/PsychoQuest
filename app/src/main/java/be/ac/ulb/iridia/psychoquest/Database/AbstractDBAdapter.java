package be.ac.ulb.iridia.psychoquest.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gaetan on 20.05.14.
 */
public class AbstractDBAdapter {

    private static final String TAG = "AbstractDBAdapter";

    protected DatabaseHelper mDBHelper;
    protected SQLiteDatabase mDB;

    private static final String DATABASE_NAME = "DB_PSYCHOQUEST";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PARTICIPANT = "Participant";
    private static final String TABLE_EXPERIMENT = "Experiment";
    private static final String TABLE_SAM_EXP_RESULTS = "SAMExpResults";
    private static final String TABLE_NASATLX_EXP_RESULTS = "NASATLXExpResults";

    private static final String CREATE_TABLE_PARTICIPANT = "CREATE TABLE " +
            TABLE_PARTICIPANT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " Name TEXT NOT NULL UNIQUE " +
            ");";


    private static final String CREATE_TABLE_EXPERIMENT = "CREATE TABLE "+
            TABLE_EXPERIMENT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            " Name TEXT NOT NULL UNIQUE, " +
            " Date TEXT NOT NULL, " +
            " SessionNumber INTEGER NOT NULL, " +
            " NASATLX INTEGER DEFAULT 0, " + // does the experiment include a NASATLX questionnaire
            " SAM INTEGER DEFAULT 0 " +      // does the experiment include a SAM questionnaire
            ")";

    private static final String CREATE_TABLE_SAM_EXP_RESULTS = "CREATE TABLE " +
            TABLE_SAM_EXP_RESULTS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " ParticipantID INTEGER NOT NULL, " +
            " ExperimentID INTEGER NOT NULL, " +
            " SessionID INTEGER  NOT NULL, " +
            " ArousalScore INTEGER  NOT NULL, " +
            " ValenceScore INTEGER  NOT NULL, " +
            " FOREIGN KEY(ParticipantID) REFERENCES " + TABLE_PARTICIPANT  + " (_id) " +
            " FOREIGN KEY(ExperimentID) REFERENCES " + TABLE_EXPERIMENT  + " (_id) " +
            "ON DELETE CASCADE" +
            ");";

    private static final String CREATE_TABLE_NASATLX_EXP_RESULTS = "CREATE TABLE " +
            TABLE_NASATLX_EXP_RESULTS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " ParticipantID INTEGER NOT NULL, " +
            " ExperimentID INTEGER NOT NULL, " +
            " SessionID INTEGER  NOT NULL, " +
            " MentalScore INTEGER NOT NULL, " +
            " PhysicalScore INTEGER NOT NULL, " +
            " TemporalScore INTEGER NOT NULL, " +
            " PerformanceScore INTEGER NOT NULL, " +
            " EffortScore INTEGER NOT NULL, " +
            " FrustrationScore INTEGER NOT NULL, " +
            " FOREIGN KEY(ParticipantID) REFERENCES " + TABLE_PARTICIPANT  + " (_id) " +
            " FOREIGN KEY(ExperimentID) REFERENCES " + TABLE_EXPERIMENT  + " (_id) " +
            "ON DELETE CASCADE" +
            ");";

    protected Context mContext = null;

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_PARTICIPANT);
            sqLiteDatabase.execSQL(CREATE_TABLE_EXPERIMENT);
            sqLiteDatabase.execSQL(CREATE_TABLE_NASATLX_EXP_RESULTS);
            sqLiteDatabase.execSQL(CREATE_TABLE_SAM_EXP_RESULTS);
            Log.d(TAG, "Tables Participant, experiment, nasatlx and sam created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.w(TAG, "Updating database from version " + oldVersion + " to version " + newVersion);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ CREATE_TABLE_PARTICIPANT);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ CREATE_TABLE_EXPERIMENT);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_NASATLX_EXP_RESULTS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SAM_EXP_RESULTS);
        }
    }

    public AbstractDBAdapter(Context context) {
        mContext = context;
    }

    public AbstractDBAdapter open() {
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


}
