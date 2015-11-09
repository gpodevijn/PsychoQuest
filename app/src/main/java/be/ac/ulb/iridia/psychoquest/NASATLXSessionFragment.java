package be.ac.ulb.iridia.psychoquest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import be.ac.ulb.iridia.psychoquest.Questionnaires.NASATLX;

/**
 * Created by gaetan on 06.11.15.
 */
public class NASATLXSessionFragment extends Fragment {
    private OnNASATLXSessionListener mCallback;

    private NASATLX mNASATLX = new NASATLX();

    public interface OnNASATLXSessionListener {
        void onNASATLXSessionFinished(NASATLX nasatlx);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.nasatlxsession_fragment, container, false);
        InitNextButton(v);
        InitNASAScales(v);
        return v;
    }

    private void InitNextButton(View v) {
        Button nextButton = (Button)v.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onNASATLXSessionFinished(mNASATLX);
            }
        });
    }



    private void InitNASAScales(View v) {
        SeekBar mentalSeekBar = (SeekBar) v.findViewById(R.id.nasa_mental_scale_seekbar);
        SeekBar physicalSeekBar = (SeekBar) v.findViewById(R.id.nasa_physical_scale_seekbar);
        SeekBar temporalSeekBar = (SeekBar) v.findViewById(R.id.nasa_temporal_scale_seekbar);
        SeekBar performanceSeekBar = (SeekBar) v.findViewById(R.id.nasa_performance_scale_seekbar);
        SeekBar effortSeekBar = (SeekBar) v.findViewById(R.id.nasa_effort_scale_seekbar);
        SeekBar frustrationSeekBar = (SeekBar) v.findViewById(R.id.nasa_frustration_scale_seekbar);

        mentalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mNASATLX.setMental(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        physicalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mNASATLX.setPhysical(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        temporalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mNASATLX.setTemporal(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        performanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mNASATLX.setPerformance(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        effortSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mNASATLX.setEffort(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        frustrationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mNASATLX.setFrustration(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Activity a;
            if (context instanceof Activity){
                a=(Activity) context;
                mCallback = (OnNASATLXSessionListener)a;

            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnNASATLXSessionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnNASATLXSessionListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNASATLXSessionListener");
        }
    }
}
