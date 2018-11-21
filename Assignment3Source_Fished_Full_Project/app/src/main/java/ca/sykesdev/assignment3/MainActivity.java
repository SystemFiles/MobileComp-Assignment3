package ca.sykesdev.assignment3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import model.Score;

public class MainActivity extends Activity {

    // Activity Constants
    private final String TAG = "MAIN_ACTIVITY";
    private static final String FILE_NAME = "scores.txt";
    public static final String SCORES_KEY = "scores";

    // Declaring all of the controls
    private EditText edtScore, edtName;
    private TextView txtName, txtScore;
    private Button btnSubmit, btnViewScores, btnReset;
    private FileInputStream fis;
    private FileOutputStream fos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init. Controls
        edtScore = findViewById(R.id.edtScore);
        edtName = findViewById(R.id.edtName);
        txtScore = findViewById(R.id.txtScore);
        txtName = findViewById(R.id.txtName);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnViewScores = findViewById(R.id.btnView);
        btnReset = findViewById(R.id.btnReset);


        // Read the file..
        readForScoresDisplay();

        // Setup UI
        setupUI();

    }

    /**
     * Performs all the need setups for controls on the UI
     */
    private void setupUI() {
        Log.i(TAG, "setupUI: Setting up user interface...");
        btnSubmit.setOnClickListener(new View.OnClickListener() { //onClick for submit button
            @Override
            public void onClick(View view) {
                String nameText = edtName.getText().toString().trim();
                String scoreText = edtScore.getText().toString().trim();
                ArrayList<Score> listOfScores; //create ArrayList

                try {
                    // Creates new file object for scores.txt
                    File file = new File(getFilesDir(), FILE_NAME);

                    //if the file doesn't exist when we try to write to it, we create one
                    // automatically
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    // Opens save file for appending
                    fos = openFileOutput(FILE_NAME, MODE_APPEND);

                    //checks for empty data fields
                    if (nameText.isEmpty() || scoreText.isEmpty()) {
                        // Then decide which element to trigger error message.
                        if (nameText.isEmpty()) {
                            edtName.setError(getString(R.string.err_name_required_text));
                        }

                        if (scoreText.isEmpty()) {
                            edtScore.setError(getString(R.string.err_score_required_text));
                        }
                    } else {
                        saveScoreToFile();
                    }
                } catch (IOException e) { //catch input/output exceptions
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.err_could_not_read_file),
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onCreate: Error handling file interaction...");
                }

            }
        });

        // Delete the file and reset the input fields
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile();
            }
        });

        // Starts new activity and passes the arrayList in the intent (needs to be implemented...)
        btnViewScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fis = openFileInput(FILE_NAME);
                } catch (IOException e) {
                    Log.e(TAG, "onCreate: Error handling file interaction...");
                }

                if (fis != null) {
                    ArrayList<Score> listOfScores;
                    listOfScores = readFile(fis);


                    // Send scores to displayActivity
                    if (!listOfScores.isEmpty()) {
                        Intent scoreIntent = new Intent(getApplicationContext(), DisplayActivity.class);
                        scoreIntent.putExtra(SCORES_KEY, listOfScores);
                        startActivity(scoreIntent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.err_no_scores_to_list_text),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Tell user no scores exist
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.err_no_scores_to_list_from_file_text),
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onCreate: No scores from intent");
                }
            }
        });
    }

    /**
     * Saves scores to a the save file (scores.txt)
     */
    private void saveScoreToFile() {
        ArrayList<Score> scores;
        Score score = new Score(edtName.getText().toString(),
                Integer.parseInt(edtScore.getText().toString()));

        // The writeFile function is called
        // toFile() simply returns "Score.getName():Score.getScore()", which is written to the file
        writeFile(score.toFile(), fos);


        Log.i(TAG, "saveScoreToFile: Saving scores to file...");
        // Open file for reading after writing
        try {
            fis = openFileInput(FILE_NAME);
            scores = readFile(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //if the score just added is now the top score in the arrayList, we update the high score
        if (scores.get(0).getScore() == score.getScore()) {
            txtName.setText(String.format(getString(R.string.txt_high_score_name_withscore_text),
                    score.getName()));
            txtScore.setText(String.format(getString(R.string.txt_high_score_withscore_text),
                    score.getScore()));
        }

        // Reset input fields
        edtScore.setText(null);
        edtName.setText(null);
    }

    /**
     * Reads file for scores and sets them
     */
    private void readForScoresDisplay() {
        Log.i(TAG, "readForScoresDisplay: Reading scores to display");
        try {
            File file = new File(getFilesDir(), FILE_NAME); // Creates new file object (based on scores.txt)
            ArrayList<Score> listOfScores; // Arraylist created when opening the app to store scores

            if (file.exists()) { //will only read file if it exists, else nothing is read
                fis = openFileInput(FILE_NAME); //opens the file for reading
                listOfScores = readFile(fis); //initializes the arraylist based
                // on the list returned by readFile


                /*
                   The score and name textfields will display the first value of the listOfScores
                   arraylist. the first field will always be the highest based on the compareTo
                   function of the Score objects (provided the file exists)
                */
                txtScore.setText(String.format(getString(R.string.txt_high_score_withscore_text),
                        listOfScores.get(0).getScore()));
                txtName.setText(String.format(getString(R.string.txt_high_score_name_withscore_text),
                        listOfScores.get(0).getName()));
            }

        } catch (IOException e) { //catch all file input/output exceptions
            Log.e(TAG, "onCreate: Error handling file interaction...");
        }
    }

    /**
     * Writes scores to file
     *
     * @param toFile The file
     * @param fos    The output stream
     */
    public void writeFile(String toFile, FileOutputStream fos) {
        PrintWriter writer = new PrintWriter(fos);
        writer.println(toFile);
        writer.close();
    }

    /**
     * Read file helper function
     * NOTE: readFile returns the arrayList automatically sorted, instead of a String
     * I figured since we typically need to read a file for the list, just create it while reading from it
     * However, this means that every time we read from the file, we are just creating a new ArrayList instead
     * of appending the old one...
     *
     * @param fis The file input-stream.
     * @return A list of score objects.
     */
    public ArrayList<Score> readFile(FileInputStream fis) {
        ArrayList<Score> listOfScores = new ArrayList<>();
        Scanner scanner;
        scanner = new Scanner(fis);
        while (scanner.hasNextLine()) {
            String[] currentScore = scanner.nextLine().split(":"); //creates a String array for every line (array contains name and score)
            Score score = new Score(currentScore[0], Integer.parseInt(currentScore[1])); //creates a new Score object for every line based on each line's string array
            listOfScores.add(score); //adds the new object to the ArrayList
        }
        scanner.close();

        for (int i = 0; i < listOfScores.size(); i++) { //bubble sort algorithm that will go through the arrayList and swap each Score if the score is greater than the pervious one
            for (int j = i; j > 0; j--) {
                if (listOfScores.get(j).compareTo(listOfScores.get(j - 1)) > 0) {
                    Collections.swap(listOfScores, j, j - 1);
                }
            }
        }
        return listOfScores; //return the ArrayList fully sorted
    }

    /**
     * Delete the file
     */
    public void deleteFile() { //creates the file object based on the scores.txt file
        File file = new File(getFilesDir(), FILE_NAME);
        file.delete(); //deletes the file and resets the text fields
        resetFields();
    }

    /**
     * Resets all fields
     */
    public void resetFields() { //resets the data fields
        txtName.setText(getString(R.string.txt_high_score_name_default));
        txtScore.setText(getString(R.string.txt_high_score_default));
        edtName.setText(null);
        edtScore.setText(null);
    }
}
