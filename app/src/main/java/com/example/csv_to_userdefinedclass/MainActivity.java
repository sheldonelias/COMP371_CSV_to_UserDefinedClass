/*
File for study for use to create app that can import a .CSV from the /res
Also, for study to create an app that can import a .TXT from internal
storage for the app in data/android.<packageName>.<appName>/App example for creating a user defined class to store CSV file date from /res/raw. Shows how store app data in internal device storage for app.

Use Device Explorer and find:
data/data/com.packageName.projectName

Project reads and writes to io_file.txt

 */

package com.example.csv_to_userdefinedclass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow;

//Maybe Used Imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

//Essential Imports
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //Great for stubs to creat a data source from a not yet connected subsystem
    static String[] strArr3 = {"hello", "there"};

    //Also great for stubs to creat a data source from a not yet connected subsystem
    static ArrayList<Integer> intArrList = new ArrayList<Integer>(5);

    //Used for storing data from a .CSV, in this case cities_data.csv
    List<City> cities = new ArrayList<City>();

    //Create for stub or testing to use in String[] tupArr
    //Strings in an array are much like what a BufferedReader gets
    String tuple1 = "line1-word1 line1-word2 line1-word3";
    String tuple2 = "line2-word1 line2-word2 line2-word3";

    //use for stub or testing to simulate a BufferedReader
    String[] tupArr = {tuple1, tuple2};
    //Create file name
    String MY_FILE_NAME = "io_file.txt";

    //Use for displaying rows within Table
    TableRow row1;
    TableRow row2;
    TextView text1;

    //Use for capturing text from user to save to a file
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);
        //Practice displaying rows using these below
        //The data transfer to rows must occur in method
        //Code here only assigns .XML objects to TableRow objects
        //TableRow row3 = findViewById(R.id.row3);
        //TableRow row4 = findViewById(R.id.row4);
        //TableRow row5 = findViewById(R.id.row5);
        //TableRow row6 = findViewById(R.id.row6);

        //Assigning the EditText UML object to a EditText Java object
        editText = findViewById(R.id.editText);

        Button button1 = findViewById(R.id.button1);
        //Button 1 click listener with anonynmous class
        button1.setOnClickListener(new View.OnClickListener()
                                   {   @Override
                                   public void onClick(View view)
                                   {
                                       //Need try/catch around method that has a throws
                                       try {
                                           readCityData();
                                           displayCityData();
                                       } catch (IOException e) {
                                           throw new RuntimeException(e);
                                       }
                                   }
                                   }
        );

        Button button2 = findViewById(R.id.button2);
        //Button 2 click listener with anonynmous class
        button2.setOnClickListener(new View.OnClickListener()
                                   {  @Override
                                   public void onClick(View view)
                                   {
                                       //Need try/catch around method that has a throws
                                       try {
                                           readInputFile();
                                       } catch (IOException e) {
                                           throw new RuntimeException(e);
                                       }
                                   }
                                   }
        );

        Button button3 = findViewById(R.id.button3);
        //Button 2 click listener with anonynmous class
        button3.setOnClickListener(new View.OnClickListener()
                                   {  @Override
                                   public void onClick(View view)
                                   {
                                       TextView textView = findViewById(R.id.textView1);
                                       textView.setText(editText.getText());

                                       //Need try/catch around method that has a throws
                                       try {
                                           writeOutputFile();
                                       } catch (IOException e) {
                                           throw new RuntimeException(e);
                                       }
                                   }
                                   }
        );
    }

    //Needs to use throws IOException because of try for in InputStream object
    private void readCityData() throws IOException {
        //Needs try/catch because always possible file is missing
        try (InputStream stream = getResources().openRawResource(R.raw.cities_data))
        {
            //Log.i("XXX", "Stream works.");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8)
            );

            //Using line counter to skip over header lines in .CSV
            int lineCounter = 0;
            String line;
            while((line = reader.readLine()) != null)
            {   //Start from line 2 because first two lines are column label headers
                if (lineCounter >= 2) {
                    //split line by delimiter
                    Log.i("XXX", line);
                    String[] words = line.split(",");
                    //load all data to class object
                    City city = new City();
                    city.setIdNumber(Integer.parseInt(words[0]));
                    city.setName(words[1]);
                    city.setCountry(words[2]);
                    city.setContinent(words[3]);
                    city.setPopulation(Integer.parseInt(words[4]));
                    city.setLatitude(Double.parseDouble(words[5]));
                    city.setLongitude(Double.parseDouble(words[6]));
                    city.setYearFounded(Integer.parseInt(words[7]));
                    city.setElevation(Integer.parseInt(words[8]));
                    //Add to the ArrayList cities
                    cities.add(city);
                    //Check content of each city object in Log
                    Log.i("XXX", city.toString());
                }
                lineCounter++;
            }
        }catch(IOException exception)
        {
            Log.wtf("XXX", "Error reading line in file.");
        }
    }

    public void displayCityData()
    {
        //Use as temporary string holder
        String tempStr;
        //Use to pass TextView object into row
        TextView text = new TextView(this);
        //Row 1 data loading by column
        //Row 1-Col 1 data loading
        tempStr = Integer.toString(cities.get(0).getIdNumber());
        text.append(tempStr + " ");
        row1.addView(text);
        //Row 1-Col 2 data loading
        text = new TextView(this);
        tempStr = cities.get(0).getName();
        text.append(tempStr + " ");
        row1.addView(text);
        //Row 1-Col 3 data loading
        text = new TextView(this);
        tempStr = cities.get(0).getCountry();
        text.append(tempStr + " ");
        row1.addView(text);
        //Row 1-Col 4 data loading
        text = new TextView(this);
        tempStr = cities.get(0).getContinent();
        text.append(tempStr + " ");
        row1.addView(text);
        //Row 1-Col 5 data loading
        text = new TextView(this);
        tempStr = Integer.toString(cities.get(0).getPopulation());
        text.append(tempStr + " ");
        row1.addView(text);
        //Row 1-Col 6 data loading
        text = new TextView(this);
        tempStr = Double.toString(Math.round(cities.get(0).getLongitude()));
        text.append(tempStr + " ");
        row1.addView(text);
        //Row 1-Col 6 data loading
        TextView text7 = new TextView(this);
        tempStr = Double.toString(Math.round(cities.get(0).getLatitude()));
        text7.append(tempStr + " ");
        row1.addView(text7);

        //Row 2
        //Row 2-Col 1 data loading
        text = new TextView(this);
        tempStr = Integer.toString(cities.get(1).getIdNumber());
        text.append(tempStr + " ");
        row2.addView(text);
        //Row 2-Col 2 data loading
        text = new TextView(this);
        tempStr = cities.get(1).getName();
        text.append(tempStr + " ");
        row2.addView(text);
        //Row 2-Col 3 data loading
        text = new TextView(this);
        tempStr = cities.get(1).getCountry();
        text.append(tempStr + " ");
        row2.addView(text);
        //Row 2-Col 4 data loading
        text = new TextView(this);
        tempStr = cities.get(1).getContinent();
        text.append(tempStr + " ");
        row2.addView(text);
        //Row 2-Col 5 data loading
        text = new TextView(this);
        tempStr = Integer.toString(cities.get(1).getPopulation());
        text.append(tempStr + " ");
        row2.addView(text);
        //Row 2-Col 6 data loading
        text = new TextView(this);
        tempStr = Double.toString(Math.round((Double)cities.get(1).getLongitude()));
        text.append(tempStr + " ");
        row2.addView(text);
        //Row 2-Col 7 data loading
        text7 = new TextView(this);
        tempStr = Double.toString(Math.round((Double)cities.get(1).getLatitude()));
        text7.append(tempStr + " ");
        row2.addView(text7);
    }

    public void readInputFile() throws IOException
    {
        MY_FILE_NAME = "io_file.txt";

        //Gets the internal path of the file
        File path = getApplicationContext().getFilesDir();
        //Converts the text file to a File object
        File readFrom = new File(path, MY_FILE_NAME);
        //Creates a byte[] array of the correct number of indices
        byte[] content = new byte[(int) readFrom.length()];

        try
        {
            //Creates a new file input stream using the file, readFrom
            FileInputStream fileis = new FileInputStream(readFrom);
            //Reads the fileis stream into the content byte[] array
            fileis.read(content);
            //Creates a string object from the byte[] array object
            String stringContent = new String(content);

            //Copy .XML TextView object to a Java TextView object
            text1 = findViewById(R.id.textView1);
            //Copy text content in string form to XML object
            text1.setText(stringContent);
            //Report to Toast
            Toast.makeText(getBaseContext(), stringContent,
                    Toast.LENGTH_LONG).show();

            //Use return if needed
            //return new String(content);

        } catch (Exception exception) {
            exception.printStackTrace();
            String msg = exception.toString();
            Toast.makeText(getApplicationContext(),"Trouble with IO with input." + msg,
                    Toast.LENGTH_SHORT).show();
            Log.wtf("XXX", "Trouble with IO with input." + msg);
            //Use return if needed
            //return e.toString();
        }
    }

    public void writeOutputFile() throws IOException
    {
        //Assign name of file to write
        MY_FILE_NAME = "io_file.txt";
        //Establish output path
        File path = getApplicationContext().getFilesDir();
        //Report to Toast the path
        Toast.makeText(getApplicationContext(),path.toString(), Toast.LENGTH_LONG).show();

        //Build continuous string with line breaks "\n" from Array
        StringBuilder output = new StringBuilder();
        String userInput = editText.getText().toString();
        //Append content of editText  object to output StringBuilder
        output.append(userInput);

        try
        {
            //Create a new file output file stream
            FileOutputStream writer = new FileOutputStream(new File(path,MY_FILE_NAME));
            //Write to file, but must do so by converting byte code
            writer.write(output.toString().getBytes());
            //Close file output file stream
            writer.close();
            //Report to Toast
            Toast.makeText(getApplicationContext(),"Wrote " +
                    MY_FILE_NAME, Toast.LENGTH_SHORT).show();
            //Report to Log
            Log.i("XXX", "Wrote " + MY_FILE_NAME);
        }catch(IOException exception)
        {
            //Report errors to Toast and Log
            exception.printStackTrace();
            String msg = exception.toString();
            Toast.makeText(getApplicationContext(),"Trouble with IO with output." + msg,
                    Toast.LENGTH_SHORT).show();
            Log.wtf("XXX", "Trouble with IO with output." + msg);
        }
    }
}