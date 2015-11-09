package be.ac.ulb.iridia.psychoquest;

import be.ac.ulb.iridia.psychoquest.Questionnaires.NASATLX;
import be.ac.ulb.iridia.psychoquest.Questionnaires.SAM;

/**
 * Created by gaetan on 05.11.15.
 */
public class Experiment {
    private String mExperimentName;
    private int mExperimentID;
    private int mSessionNumber;
    private int mCurrentSession;
    private int mParticipantID;

    public SAM mSAM = new SAM();
    public NASATLX mNASATLX = new NASATLX();

    private boolean mHasSAM = false;
    private boolean mHasNASATLX = false;

    public void setExperimentName(String name) {
        mExperimentName = name;
    }

    public void setExperimentID(int experimentID) {
        mExperimentID = experimentID;
    }

    public void setSessionNumber(int sessionNumber) {
        mSessionNumber = sessionNumber;
    }

    public void setCurrentSession(int currentSession) {
        mCurrentSession = currentSession;
    }

    public void setNextCurrentSession() { mCurrentSession++; }

    public void setParticipantID(int participantID) {
        mParticipantID = participantID;
    }

    public void setHasSAM(boolean hasSAM) {
        mHasSAM = hasSAM;
    }

    public void setHasNASATLX(boolean hasNASATLX) {
        mHasNASATLX = hasNASATLX;
    }

    public int getExperimentID() {
        return mExperimentID;
    }

    public int getSessionNumber() {
        return mSessionNumber;
    }

    public int getCurrentSession() {
        return mCurrentSession;
    }

    public int getParticipantID() {
        return mParticipantID;
    }

    public String getExperimentName() {
        return mExperimentName;
    }

    public boolean hasSAM() {
        return mHasSAM;
    }

    public boolean hasNASATLX() {
        return mHasNASATLX;
    }
}
