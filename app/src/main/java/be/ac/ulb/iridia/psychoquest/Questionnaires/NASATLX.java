package be.ac.ulb.iridia.psychoquest.Questionnaires;

/**
 * Created by gaetan on 05.11.15.
 */
public class NASATLX {
    private static final String TAG = "NASATLX";
    
    private int mMentalScore;
    private int mPhysicalScore;
    private int mTemporalScore;
    private int mPerformanceScore;
    private int mEffortScore;
    private int mFrustrationScore;

    public NASATLX() {
        mMentalScore = 0;
        mPhysicalScore = 0;
        mTemporalScore = 0;
        mPerformanceScore = 0;
        mEffortScore = 0;
        mFrustrationScore = 0;
    }

    public NASATLX(int mental, int physical, int temporal, int performance, int effort, int frustration) {
        mMentalScore = mental;
        mPhysicalScore = physical;
        mTemporalScore = temporal;
        mPerformanceScore = performance;
        mEffortScore = effort;
        mFrustrationScore = frustration;
    }
    
    public void setMental(int mental) {
        mMentalScore = mental;
    }
    
    public void setPhysical(int physical) {
        mPhysicalScore = physical;
    }
    
    public void setTemporal(int temporal) {
        mTemporalScore = temporal;
    }
    
    public void setPerformance(int performance) {
        mPerformanceScore = performance;
    }
    
    public void setEffort(int effort) {
        mEffortScore = effort;
    }
    
    public void setFrustration(int frustration) {
        mFrustrationScore = frustration;
    }

    public int getMental() {
        return mMentalScore;
    }

    public int getPhysical() {
        return mPhysicalScore;
    }

    public int getTemporal() {
        return mTemporalScore;
    }

    public int getPerformance() {
        return mPerformanceScore;
    }

    public int getEffort() {
        return mEffortScore;
    }

    public int getFrustration() {
        return mFrustrationScore;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Mental: " + mMentalScore + NEW_LINE);
        result.append(" Physical: " + mPhysicalScore + NEW_LINE);
        result.append(" Temporal: " + mTemporalScore + NEW_LINE);
        result.append(" Performance: " + mPerformanceScore + NEW_LINE);
        result.append(" Effort: " + mEffortScore + NEW_LINE);
        result.append(" Frustration: " + mFrustrationScore + NEW_LINE);
        result.append("}");

        return result.toString();
    }
}
