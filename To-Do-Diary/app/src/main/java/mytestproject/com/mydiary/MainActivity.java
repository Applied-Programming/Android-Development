package mytestproject.com.mydiary;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends ActionBarActivity {

    private EditText enterText;
    private Button saveButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterText = (EditText) findViewById(R.id.editText);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (!enterText.getText().toString().equals("")){

                     String data = enterText.getText().toString();

                     writeToFile(data);
                 }else {
                     Toast.makeText(getApplicationContext(), "Please enter text!", Toast.LENGTH_LONG).show();
                 }

            }
        });

        if (readFromFile() != null){
            enterText.setText(readFromFile());
        }else {

        }

    }


    private void writeToFile(String myData){

         try{

             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("myThoughts.txt", Context.MODE_PRIVATE));
             outputStreamWriter.write(myData);
             outputStreamWriter.close(); // always close your streams!


         }catch(IOException e){
             Log.v("MyActivity", e.toString());
         }
    }


    private String readFromFile(){

        String result = "";

        try{

            InputStream inputStream = openFileInput("myThoughts.txt");

             if ( inputStream != null){

                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                 String tempString = "";
                 StringBuilder stringBuilder = new StringBuilder();

                 while ( (tempString = bufferedReader.readLine()) != null){

                     stringBuilder.append(tempString);

                 }

                 inputStream.close();

                 result = stringBuilder.toString();

             }


        }catch (FileNotFoundException e){
            Log.v("MyActivity", "File not found" + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;

    }



}
