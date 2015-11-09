package be.ac.ulb.iridia.psychoquest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaetan on 20.05.14.
 */
public class ParticipantDBAdapter extends AbstractDBAdapter {

    private static final String TAG = "ParticipantDBAdapter";

    private  static  final String TABLE_PARTICIPANT = "Participant";

    public ParticipantDBAdapter(Context context) {
        super(context);
    }

    public long insert(String name) {
        ContentValues args = new ContentValues();
        args.put("Name", name);
        return mDB.insert(TABLE_PARTICIPANT, null, args);
    }

    public boolean isEmpty() {
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM " + TABLE_PARTICIPANT, null);
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

    public int getNumberOfParticipants() {
        Cursor cursor = mDB.rawQuery("SEMECT COUNT(*) FROM " + TABLE_PARTICIPANT, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    public int getParticipantID(String name) {
        int ret = -1;

        String query = "SELECT _id FROM " + TABLE_PARTICIPANT + " WHERE Name = ?";
        Cursor cursor = mDB.rawQuery(query, new String[] { name });

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret = cursor.getInt(0);
        }

        return ret;
    }

    public String getParticipantName(int id) {
        String ret = "";

        String query = "SELECT Name FROM " + TABLE_PARTICIPANT + " WHERE _id=" + id;
        Cursor cursor = mDB.rawQuery(query, null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ret = cursor.getString(0);
        }

        return ret;
    }

    public List<String> getAllParticipants() {
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTICIPANT;
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

    public void clean() {
        mDB.delete(TABLE_PARTICIPANT, null, null);
    }

}
