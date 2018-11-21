package ca.sykesdev.assignment3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import model.Score;
import model.ScoreListAdapter;

public class DisplayActivity extends Activity {

    // Activity Constants
    private final String TAG = "DISPLAY_ACTIVITY";

    // Declare the recycler view
    private RecyclerView rViewScoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Get intent from launching activity (MainActivity) && init the Recycler
        Intent scoreIntent = getIntent();
        rViewScoresList = findViewById(R.id.rViewScoresList);

        // Get list of scores from Intent
        ArrayList<Score> scoreList =
                scoreIntent.getParcelableArrayListExtra(MainActivity.SCORES_KEY);

        // List the scores in the view.
        Log.i(TAG, "onCreate: Listing scores...");
        listScoresInView(scoreList);
    }


    /**
     * Method called by event handler to load the list of data to display in RecyclerView...
     * AKA: PlacesRecyclerView
     */
    private void listScoresInView(ArrayList<Score> scores) {

        // For performance optimization we set each item to a fixed size..
        rViewScoresList.setHasFixedSize(true);

        // Set up a spacer for the list.. (line between rows)
        rViewScoresList.addItemDecoration(new DividerItemDecoration(
                rViewScoresList.getContext(), DividerItemDecoration.VERTICAL));

        // Create LinearLayour Manager and set it to vertical layout orientation
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        // Assign the layout manager to our recyclerview
        rViewScoresList.setLayoutManager(manager);

        // Create the adapter and assign it to the RecyclerView
        ScoreListAdapter adapter = new ScoreListAdapter(scores);
        rViewScoresList.setAdapter(adapter);
    }
}
