package com.learning.dino.criminalintent;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by dbulj on 11/10/2014.
 */
public class CriminalIntentJSONSerializer {
    private static final String TAG = "JSONSERIALIZER";
    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context c, String f){
        mContext = c;
        mFilename = f;
    }

    public ArrayList<Crime> loadCrimes()
        throws  IOException, JSONException{

        ArrayList<Crime> crimes = new ArrayList<Crime>();

        //Check if SD card is mounted
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File sdCrimeFile = new File(mContext.getExternalFilesDir(null), mFilename);
        BufferedReader reader = null;

        if (isSDPresent){ // && sdCrimeFile.exists()){
            Log.i(TAG, "SD card is mounted.  Loading from SD card file.");
            try{
                //Open and read file from SD card
                FileInputStream in = new FileInputStream(sdCrimeFile);
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null){
                    jsonString.append(line);
                }

                //Parse JSON using JSONTokener
                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

                //Build array of crimes from JSONObjects
                for (int i = 0; i < array.length(); i++){
                    crimes.add(new Crime(array.getJSONObject(i)));
                }
            } catch (FileNotFoundException e){
                //Ignore.  This happens when no file has been previously stored on SD card.
            } finally {
                if (reader != null){
                    reader.close();
                }
            }
        } else{
            Log.i(TAG, "SD card is not mounted.  Loading from private storage file.");
            try{
                //Open and read file
                InputStream in = mContext.openFileInput(mFilename);
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null){
                    jsonString.append(line); //line breaks are ommited and irrelevant
                }

                //Parse JSON using JSONTokener
                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

                //Build array of crimes from JSONObjects
                for (int i = 0; i < array.length(); i++){
                    crimes.add(new Crime(array.getJSONObject(i)));
                }
            }catch(FileNotFoundException e) {
                //Ignore this, it happens when starting fresh
            } catch (JSONException e) {
                Log.e(TAG, "Error: " + e);
            }finally {
                if (reader != null){
                    reader.close();
                }
            }
        }

        return crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes)
        throws JSONException, IOException {

        //Check if SD card is mounted
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        //Build an array in JSON
        JSONArray array = new JSONArray();

        /*
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.i(TAG, "You would like to save to SD card.");
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.i(TAG, "You would like to save to private application area.");
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Where would you like to save crimes file?")
                .setPositiveButton("SD Card", dialogClickListener)
                .setNegativeButton("Private App Area", dialogClickListener)
                .setTitle("Choose location to save")
                .show();
        */

        if (isSDPresent){
            File sdFile = new File(mContext.getExternalFilesDir(null), mFilename);
            Log.i(TAG, "SD card is mounted, saving crimes to SD card at: " + sdFile.toString());

            for (Crime c: crimes){
                array.put(c.toJSON());
            }

            //Write file to disk
            Writer w = null;
            try{
                FileOutputStream out = new FileOutputStream(sdFile);
                w = new OutputStreamWriter(out);
                w.write(array.toString());
            }finally{
                if (w != null){
                    w.close();
                }
            }
        } else {
            Log.i(TAG, "No SD card found, saving crimes private application area.");
            for (Crime c: crimes){
                array.put(c.toJSON());
            }

            //Write file to disk
            Writer w = null;
            try{
                OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
                w = new OutputStreamWriter(out);
                w.write(array.toString());
            }finally{
                if (w != null){
                    w.close();
                }
            }
        }
    }
}
