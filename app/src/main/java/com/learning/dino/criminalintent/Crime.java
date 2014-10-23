package com.learning.dino.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dbulj on 25/09/2014.
 */
public class Crime {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_SUSPECT = "suspect";
    private static final String JSON_SUSPECT_PHONE = "suspect_phone";

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private Photo mPhoto;
    private String mSuspect;
    private String mSuspectPhone;

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDateString(){
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(mDate);
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public Crime(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE)){
            mTitle = json.getString(JSON_TITLE);
        }
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
        //mDate = new Date(json.getString(JSON_DATE));

        if (json.has(JSON_PHOTO)){
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }

        if (json.has(JSON_SUSPECT)){
            mSuspect = json.getString(JSON_SUSPECT);
        }

        if (json.has(JSON_SUSPECT_PHONE)){
            mSuspectPhone = json.getString(JSON_SUSPECT_PHONE);
        }
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_DATE, mDate.getTime());
        if (mPhoto != null){
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }
        json.put(JSON_SUSPECT, mSuspect);
        json.put(JSON_SUSPECT_PHONE, mSuspectPhone);
        return json;
    }

    public Photo getPhoto(){
        return mPhoto;
    }

    public void setPhoto(Photo p){
        mPhoto = p;
    }

    public String getSuspect(){
        return mSuspect;
    }

    public void setSuspect(String suspect){
        mSuspect = suspect;
    }

    public String getSuspectPhone(){
        return mSuspectPhone;
    }

    public void setSuspectPhone(String suspectPhone){
        mSuspectPhone = suspectPhone;
    }

    @Override
    public String toString(){
        return mTitle;
    }
}
