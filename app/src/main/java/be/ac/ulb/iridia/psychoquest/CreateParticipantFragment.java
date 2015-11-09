package be.ac.ulb.iridia.psychoquest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import be.ac.ulb.iridia.psychoquest.Database.ParticipantDBAdapter;

/**
 * Created by gaetan on 05.11.15.
 */
public class CreateParticipantFragment extends Fragment {
    private final static String TAG = "CreateParticipant";
    private EditText mParticipantNameEditView;

    public CreateParticipantFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.create_participant_fragment, container, false);
        initEditText(v);
        initValidateButton(v);
        return v;
    }

    private void initEditText(View v) {
        mParticipantNameEditView = (EditText) v.findViewById(R.id.participant_name);
    }

    private void initValidateButton(View v) {
        Button addParticipant = (Button)v.findViewById(R.id.add_participant_button);

        addParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mParticipantNameEditView.getText() != null) {
                    String participantName = mParticipantNameEditView.getText().toString();
                    createParticipant(participantName);
                    HomeFragment homeFragment = new HomeFragment();
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content_frame, homeFragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private void createParticipant(String participantName) {
        ParticipantDBAdapter participantDBAdapter = new ParticipantDBAdapter(getActivity());
        participantDBAdapter.open();
        participantDBAdapter.insert(participantName);
        participantDBAdapter.close();
    }
}
