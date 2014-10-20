package com.learning.dino.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dbulj on 18/10/2014.
 */
public class Photo {
    //Orientations as defined by Android:
    //LANDSCAPE=2
    //PORTRAIT=1
    //SQUARE=3
    //UNINDENTIFIED=0

    private static final String JSON_FILENAME = "filename";
    private static final String JSON_ORIENTATION = "orientation"; //CHALLANGE Ch.20-Image Orientation

    private String mFilename;
    private int mOrientation; //CHALLANGE Ch.20-Image Orientation

    public Photo(String filename, int orientation){
        mFilename = filename;
        mOrientation = orientation; //CHALLANGE Ch.20-Image Orientation
    }

    public  Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
        mOrientation = json.getInt(JSON_ORIENTATION); //CHALLANGE Ch.20-Image Orientation
    }

    public String getFilename(){
        return mFilename;
    }

    //CHALLANGE Ch.20-Image Orientation
    public int getOrientation(){
        return  mOrientation;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        json.put(JSON_ORIENTATION, mOrientation); //CHALLANGE Ch.20-Image Orientation
        return json;
    }
}
