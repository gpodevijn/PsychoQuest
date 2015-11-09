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

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import be.ac.ulb.iridia.psychoquest.Database.AbstractDBAdapter;

/**
 * Created by gaetan on 09.11.15.
 */
public class BackupResultsFragment extends Fragment {
    private final static String TAG = "BackupResultsFragment";
    private final static String DB_PATH = "/data/data/be.ac.ulb.iridia.psychoquest/databases/DB_PSYCHOQUEST";

    private DropboxAPI<AndroidAuthSession> mDBApi;

    private  static String BACKUP_PATH;
    private Button mBackupButton;
    private TextView mBackupTextView;
    public BackupResultsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mDBApi = ((MainActivity)getActivity()).mDBApi;
        View v = inflater.inflate(R.layout.backup_fragment, container, false);

        mBackupTextView = (TextView)v.findViewById(R.id.backup_textview);

        InitButton(v);

        return v;
    }


    private void InitButton(View v) {
        mBackupButton = (Button)v.findViewById(R.id.backup_db_button);
        mBackupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackupDB();
                mBackupTextView.setText(getResources().getString(R.string.backup_db_sucess) + BACKUP_PATH);
            }
        });

        final Button saveOnDropboxButton = (Button)v.findViewById(R.id.share_online);
        saveOnDropboxButton.setOnClickListener(new View.OnClickListener() {
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
                            SaveOnDropbox();
                            return null;
                        }
                    }.execute();
                }
            }
        });

        Button dropDB = (Button)v.findViewById(R.id.drop_db);
        dropDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbstractDBAdapter.dropDB(getActivity().getApplicationContext());
            }
        });
    }


    private void BackupDB() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());
        BACKUP_PATH = Environment.getExternalStorageDirectory() + "/PsychoQuest/backup_db/" + currentDateAndTime+".sqlite";

        File to = new File(BACKUP_PATH);
        File from = new File(DB_PATH);
        Utils.saveOnSD(Environment.getExternalStorageDirectory() + "/PsychoQuest/backup_db/",
                from, to);
      /*  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());

        BACKUP_PATH = Environment.getExternalStorageDirectory() + "/PsychoQuest/backup_db/" + currentDateAndTime+".sqlite";
        File to = new File(BACKUP_PATH);
        File from = new File(DB_PATH);

        File dir = new File(Environment.getExternalStorageDirectory() + "/PsychoQuest/backup_db/");
        if (!dir.exists()) {
            dir.mkdir();
        }

        if (!to.exists()) {
            try {
                Utils.copyFile(new FileInputStream(from), new FileOutputStream(to));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void SaveOnDropbox() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());

        Utils.saveOnDropbox(mDBApi, new File(DB_PATH),"/backup_db/"+currentDateAndTime+".sqlite");
        /*File db = new File(DB_PATH);

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(db);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DropboxAPI.Entry response = null;
        try {
            if (mDBApi == null) {
            }
            response = mDBApi.putFile("/backup_db/"+currentDateAndTime+".sqlite", inputStream,
                    db.length(), null, null);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);*/
    }
}
