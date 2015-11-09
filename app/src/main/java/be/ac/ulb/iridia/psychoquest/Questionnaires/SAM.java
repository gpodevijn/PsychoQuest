package be.ac.ulb.iridia.psychoquest.Questionnaires;

/**
 * Created by gaetan on 05.11.15.
 */
public class SAM {
    private static final String TAG = "SAM";

    private int mArousalScore;
    private int mValenceScore;

    public SAM() {
        mArousalScore = 0;
        mValenceScore = 0;
    }

    public SAM(int arousal, int valence) {
        mArousalScore = arousal;
        mValenceScore = valence;
    }

    public void setArousal(int arousal) {
        mArousalScore = arousal;
    }

    public void setValence(int valence) {
        mValenceScore = valence;
    }

    public int getValence() {
        return mValenceScore;
    }

    public int getArousal() {
        return mArousalScore;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Valence: " + mValenceScore + NEW_LINE);
        result.append(" Arousal: " + mArousalScore + NEW_LINE);
        result.append("}");

        return result.toString();
    }
}
