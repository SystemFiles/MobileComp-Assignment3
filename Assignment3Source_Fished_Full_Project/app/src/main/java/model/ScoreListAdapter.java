package model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ca.sykesdev.assignment3.R;

/**
 * Used to list all the places in descending order
 */
public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreListViewHolder> {
    private ArrayList<Score> mScoresList;

    /**
     * Constructor for scores adapter
     * @param scores the ArrayList of score objects to be listed.
     */
    public ScoreListAdapter (ArrayList<Score> scores) {
        mScoresList = scores;
    }

    public static class ScoreListViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtScoreName, mTxtScoreAmount;

        public ScoreListViewHolder(@NonNull LinearLayout layProperties) {
            super(layProperties);
            mTxtScoreName = layProperties.findViewById(R.id.txtScoreName);
            mTxtScoreAmount = layProperties.findViewById(R.id.txtScoreAmount);
        }
    }


    @NonNull
    @Override
    public ScoreListAdapter.ScoreListViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                   int view) {
        // Create and inflate view for score data
        LinearLayout layProperties = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_item_view, parent, false);

        // Create and return the Viewholder for txtPlace using the passed layout
        return new ScoreListViewHolder(layProperties);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreListAdapter.ScoreListViewHolder scoreListViewHolder,
                                 int position) {
        scoreListViewHolder.mTxtScoreName.setText(mScoresList.get(position).getName());
        scoreListViewHolder.mTxtScoreAmount.setText(mScoresList.get(position).getDisplayScore());
    }

    @Override
    public int getItemCount() {
        return mScoresList.size();
    }
}
