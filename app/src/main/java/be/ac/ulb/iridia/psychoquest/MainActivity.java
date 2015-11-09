package be.ac.ulb.iridia.psychoquest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import be.ac.ulb.iridia.psychoquest.Database.AbstractExperimentResultDBAdapter;
import be.ac.ulb.iridia.psychoquest.Database.ExperimentNASATLXResultsDBAdapter;
import be.ac.ulb.iridia.psychoquest.Database.ExperimentSAMResultsDBAdapter;
import be.ac.ulb.iridia.psychoquest.Questionnaires.NASATLX;
import be.ac.ulb.iridia.psychoquest.Questionnaires.SAM;

public class MainActivity extends AppCompatActivity
        implements StartExperimentFragment.OnStartExperimentSelectionListener,
        SAMSessionFragment.OnSAMSessionListener,
        NASATLXSessionFragment.OnNASATLXSessionListener,
        SessionTerminatedFragment.OnSessionTerminatedListener,
        DropboxFragment.OnDropboxIntegrationListener {

    private static final String TAG = "MainActivity";

    private String[] mDrawerElements;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    static private String APP_KEY = "26kt3406y37o37b";
    static private String APP_SECRET;
    public DropboxAPI<AndroidAuthSession> mDBApi;

    private Experiment mCurrentExperiment = new Experiment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavigationDrawer();
        if (savedInstanceState == null) {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.main_content_frame, homeFragment);
            fragmentTransaction.commit();
        }
    }

    private void initNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitle = mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mDrawerElements = getResources().getStringArray(R.array.nav_drawer_elements);
        mDrawerList = (ListView) findViewById(R.id.navigation_drawer);
        DrawerItemAdapter drawerItemAdapter = new DrawerItemAdapter(this, mDrawerElements);
        mDrawerList.setAdapter(drawerItemAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    public void onExperimentReadyToStart(Experiment experiment) {
        mCurrentExperiment = experiment;
        startNewSession();
    }

    @Override
    public void onSAMSessionFinished(SAM sam) {
        mCurrentExperiment.mSAM = sam;
        saveSession(new ExperimentSAMResultsDBAdapter(this));
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment sessionFragment;
        if (mCurrentExperiment.hasNASATLX()) {
            sessionFragment = new NASATLXSessionFragment();
        } else {
            sessionFragment = new SessionTerminatedFragment();
        }
        ft.replace(R.id.main_content_frame, sessionFragment);
        ft.commit();

    }

    @Override
    public void onNASATLXSessionFinished(NASATLX nasatlx) {
        mCurrentExperiment.mNASATLX = nasatlx;
        saveSession(new ExperimentNASATLXResultsDBAdapter(this));
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment sessionFragment = new SessionTerminatedFragment();
        ft.replace(R.id.main_content_frame, sessionFragment);
        ft.commit();
    }

    private void saveSession(AbstractExperimentResultDBAdapter dbAdapter) {
        dbAdapter.open();
        // first check if we shouldn't modify the row
        if (dbAdapter.doesParticipantIDExperimentIDSessionIDExist(mCurrentExperiment)) {
            // update
            Log.d(TAG, "*** update ***");
            dbAdapter.update(mCurrentExperiment);
        } else {
            Log.d(TAG, "*** insert ***");
            dbAdapter.insert(mCurrentExperiment);
        }
        dbAdapter.close();
    }

    @Override
    public void onSessionFinished() {
        if (mCurrentExperiment.getCurrentSession() < mCurrentExperiment.getSessionNumber()) {
            mCurrentExperiment.setNextCurrentSession();
            startNewSession();
        } else {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment experimentTerminated = new ExperimentTerminatedFragment();
            ft.replace(R.id.main_content_frame, experimentTerminated);
            ft.commit();
        }
    }

    private void startNewSession() {
        // if there's the SAM, we start with the SAM
        // if there's also the NASATLX, it'll run after the SAM
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment sessionFragment = null;
        if (mCurrentExperiment.hasSAM()) {
            sessionFragment = new SAMSessionFragment();
        }
        // if there's not hte sam, we start directly with the NASA
        else if (mCurrentExperiment.hasNASATLX()) {
            sessionFragment = new NASATLXSessionFragment();
        }
        ft.replace(R.id.main_content_frame, sessionFragment);
        ft.commit();
    }

    public Experiment getCurrentExperiment() {
        return mCurrentExperiment;
    }

    @Override
    public void onDropboxIntegrationEnabled() {
        initDropbox();
    }

    private class DrawerItemAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] mDrawerTextElements;
        private final TypedArray mDrawerImageElements;

        public DrawerItemAdapter(Context context, String[] mDrawerTextElements) {
            super(context, R.layout.drawer_list_item, mDrawerTextElements);
            this.context = context;
            this.mDrawerTextElements = mDrawerTextElements;
            this.mDrawerImageElements = getResources().obtainTypedArray(R.array.nav_drawer_elements_pic);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.text_item);
            textView.setText(mDrawerTextElements[position]);
            Drawable d = ResourcesCompat.getDrawable(getResources(), mDrawerImageElements.getResourceId(position, -1), null);
            int h = d.getIntrinsicHeight() / 2;
            int w = d.getIntrinsicWidth() / 2;
            d.setBounds(0, 0, w, h);
            textView.setCompoundDrawables(d, null, null, null);

            return rowView;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (position) {
                case 0: // start experiment
                    StartExperimentFragment startExperimentFragment = new StartExperimentFragment();
                    fragmentTransaction.replace(R.id.main_content_frame, startExperimentFragment);
                    fragmentTransaction.commit();
                    break;
                case 1: // add experiment
                    CreateExperimentFragment createExperimentFragment = new CreateExperimentFragment();
                    fragmentTransaction.replace(R.id.main_content_frame, createExperimentFragment);
                    fragmentTransaction.commit();
                    break;
                case 2: // add participant
                    CreateParticipantFragment createParticipantFragment = new CreateParticipantFragment();
                    fragmentTransaction.replace(R.id.main_content_frame, createParticipantFragment);
                    fragmentTransaction.commit();
                    break;
                case 3: // See results
                    SeeParticipantResultsFragment seeParticipantResultsFragment = new SeeParticipantResultsFragment();
                    fragmentTransaction.replace(R.id.main_content_frame, seeParticipantResultsFragment);
                    fragmentTransaction.commit();
                    break;
                case 4: // Backup
                    BackupResultsFragment backupResultsFragment = new BackupResultsFragment();
                    fragmentTransaction.replace(R.id.main_content_frame, backupResultsFragment);
                    fragmentTransaction.commit();
                    break;
                case 5: // Dropbox
                    DropboxFragment dropboxFragment = new DropboxFragment();
                    fragmentTransaction.replace(R.id.main_content_frame, dropboxFragment);
                    fragmentTransaction.commit();
                    break;

                default:
                    break;
            }
            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mDrawerElements[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) return true;
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void initDropbox() {
        APP_KEY = getResources().getString(R.string.dropbox_public_key);
        APP_SECRET = getResources().getString(R.string.dropbox_private_key);
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        SharedPreferences sharedPref = getSharedPreferences("dropbox", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        if (token != null) {
            session.setOAuth2AccessToken(token);
        } else {
            mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
        }
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences("dropbox", Context.MODE_PRIVATE);
        boolean isIntegrationEnabled = sharedPref.getBoolean("isIntegrationEnabled", false);

        if (!isIntegrationEnabled) {
            return;
        }
        APP_KEY = getResources().getString(R.string.dropbox_public_key);
        APP_SECRET = getResources().getString(R.string.dropbox_private_key);
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();
                String token = sharedPref.getString("token", null);
                if (token == null) {
                    String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                    sharedPref.edit().putString("token", accessToken).apply();
                }
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    public DropboxAPI<AndroidAuthSession> getDBApi() {
        return mDBApi;
    }
}
