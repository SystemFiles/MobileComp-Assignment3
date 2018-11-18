package com.example.liamstickney.assignment3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.liamstickney.assignment3.model.Score;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class MainActivity extends Activity {

    // Activity Constants
    private final String TAG = "MAIN_ACTIVITY";
    private static final String FILE_NAME = "scores.txt";

    //declaring all of the widgets
    private EditText edtScore;
    private EditText edtName;
    private TextView txtName;
    private TextView txtScore;
    private FileInputStream fis;
    private FileOutputStream fos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initializing widgets
        edtScore = findViewById(R.id.edtScore);
        edtName = findViewById(R.id.edtName);
        txtScore = findViewById(R.id.txtScore);
        txtName = findViewById(R.id.txtName);


        try {
            File file = new File(getFilesDir(), FILE_NAME); //creates new file object (based on scores.txt)
            ArrayList<Score> listOfScores; //arraylist created when opening the app

            if (file.exists()) { //will only read file if it exists, else nothing is read
                fis = openFileInput(FILE_NAME); //opens the file for reading
                listOfScores = readFile(fis); //initializes the arraylist based on the list returned by readFile


                /*
                   the score and name textfields will display the first value of the listOfScores
                   arraylist. the first field will always be the highest based on the compareTo
                   function of the Score objects (provided the file exists)
                */
                txtScore.setText("High Score: " + listOfScores.get(0).getScore());
                txtName.setText("by: " + listOfScores.get(0).getName()); //
            }

        } catch (IOException e) { //catch all file input/output exceptions
            Log.e(TAG, "onCreate: Error handling file interaction...");
        }

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() { //onClick for submit button
            @Override
            public void onClick(View view) {

                ArrayList<Score> listOfScores; //create ArrayList

                try {
                    File file = new File(getFilesDir(), FILE_NAME); //creates new file object for scores.txt

                    if (!file.exists()) { //if the file doesn't exist when we try to write to it, we create one automatically
                        file.createNewFile();
                    }

                    fos = openFileOutput(FILE_NAME, MODE_APPEND); //opens the file for appending

                    //creates a new Score object with the parameters from the user inputs

                    //checks for empty data fields (INEFFICIENT AS FRICK)
                    if (edtName.getText().toString().trim().isEmpty() && !edtScore.getText().toString().trim().isEmpty()) {
                        edtName.setError("Name is required");
                    }
                    else if (!edtName.getText().toString().trim().isEmpty() && edtScore.getText().toString().trim().isEmpty()) {
                        edtScore.setError("Score is required");
                    }
                    else if (edtName.getText().toString().trim().isEmpty() && edtScore.getText().toString().trim().isEmpty()) {
                        edtName.setError("Name is required");
                        edtScore.setError("Score is required");
                    }
                    else {
                        Score score = new Score(edtName.getText().toString(), Integer.parseInt(edtScore.getText().toString()));

                        //the writeFile function is called
                        //toFile() simply returns "Score.getName():Score.getScore()", which is written to the file
                        writeFile(score.toFile(), fos);


                        fis = openFileInput(FILE_NAME); //open file for reading after writing
                        listOfScores = readFile(fis); //sets the arrayList to the scores read from the file

                        //if the score just added is now the top score in the arrayList, we update the high score
                        if (listOfScores.get(0).getScore() == score.getScore()) {
                            txtName.setText("by: " + score.getName());
                            txtScore.setText("High Score: " + score.getScore());
                        }

                        edtScore.setText("");
                        edtName.setText("");
                    }

                } catch (IOException e) { //catch input/output exceptions
                    Log.e(TAG, "onCreate: Error handling file interaction...");
                }

            }
        });

        findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile(); //when the reset button is clicked, delete the file and reset the text fields
            }
        });

        //starts new activity and passes the arrayList in the intent (needs to be implemented...)
        findViewById(R.id.btnView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fis = openFileInput(FILE_NAME);
                } catch (IOException e) {
                    Log.e(TAG, "onCreate: Error handling file interaction...");
                }

                ArrayList<Score> listOfScores;
                listOfScores = readFile(fis);

                Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);

                intent.putExtra("scores", listOfScores);

                startActivity(intent);
            }
        });
    }





    //writeFile helper function
    public void writeFile(String toFile, FileOutputStream fos) {
        PrintWriter writer = new PrintWriter(fos);
        writer.println(toFile);
        writer.close();
    }

    /*
    read file helper function
    NOTE: readFile returns the arrayList automatically sorted, instead of a String
    I figured since we typically need to read a file for the list, just create it while reading from it
    However, this means that every time we read from the file, we are just creating a new ArrayList instead
    of appending the old one...
    */
    public ArrayList readFile(FileInputStream fis) {
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
                if(listOfScores.get(j).compareTo(listOfScores.get(j - 1)) > 0) {
                    Collections.swap(listOfScores, j, j -1);
                }
            }
        }
        return listOfScores; //return the ArrayList fully sorted
    }

    public void deleteFile() { //creates the file object based on the scores.txt file
        File file = new File(getFilesDir(), FILE_NAME);
        file.delete(); //deletes the file and resets the text fields
        reset();
    }

    public void reset() { //resets the data fields
        txtName.setText("by: ");
        txtScore.setText("High Score: ");
        edtName.setText("");
        edtScore.setText("");
    }
}
