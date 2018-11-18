package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Score class to store scores and the names of people who are associated with them
 */
public class Score implements Parcelable, Comparable<Score> { //implements Compareable for compareTo function

    // Data fields for Score
    private int score;
    private String name;

    /**
     * Base constructor for Score
     * @param name The name of the person to get the score
     * @param score The score amount
     */
    public Score (String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Parcel constructor
     * @param in The input buffer for parcelable
     */
    protected Score(Parcel in) {
        score = in.readInt();
        name = in.readString();
    }

    /**
     * Creator for parcelable object, Score.
     */
    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    public String getDisplayScore() {
        return String.format("Score: %d",
                this.score);
    }


    /**
     * compares two scores, only swaps when the current score is greater than the previous one
     * @param o Other score object to be compared with this score object
     * @return Which Score object is bigger
     */
    @Override
    public int compareTo(Score o) {
        if (this.getScore() > o.getScore()) {
            return 1;
        } else if (this.getScore() < o.getScore()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Helper function for file parsing/writing
     * @return Formatted string for file input/output
     */
    public String toFile() {
        return this.name + ":" + this.score;
    }

    /**
     * @return The score amount
     */
    public int getScore() {
        return score;
    }

    /**
     * @return The name of the associated score
     */
    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method to write to the parcel
     * @param dest Destination Parcel
     * @param flags flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score);
        dest.writeString(name);
    }
}
