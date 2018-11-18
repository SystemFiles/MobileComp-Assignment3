package com.example.liamstickney.assignment3.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by liamstickney on 2018-11-16.
 */

public class Score implements Parcelable, Comparable<Score> { //implements Compareable for compareTo function

    private int score; //two data fields
    private String name;

    public Score(String name, int score) { //initializer
        this.name = name;
        this.score = score;
    }

    protected Score (Parcel in) { //initializer for parcel
        name = in.readString();
        score = in.readInt();
    }

    public String toFile() {
        return this.name + ":" + this.score;
    } //toFile helper function



    public static final Creator<Score> CREATOR = new Creator<Score>() { //needed for Parcelable
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    public int getScore() {
        return score;
    } //setters and getters for score and name

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    //will be used for passing the ArrayList of Scores
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(score);
        parcel.writeString(name);
    }


    //compares two scores, only swaps when the current score is greater than the previous one
    @Override
    public int compareTo(@NonNull Score o) {
        if (this.getScore() > o.getScore()) {
            return 1;
        } else if (this.getScore() < o.getScore()) {
            return -1;
        } else {
            return 0;
        }
    }
}
