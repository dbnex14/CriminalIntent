package com.learning.dino.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by dbulj on 28/09/2014.
 */
public class CrimeLab {

    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;

    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private CrimeLab(Context appContext){
        mAppContext = appContext;
        //mCrimes = new ArrayList<Crime>();
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);

        try{
            mCrimes = mSerializer.loadCrimes();
        }catch (Exception e){
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: " + e);
        }

        /*
        for (int i = 0; i < 100; i++){
            Crime c = new Crime();
            c.setTitle("Crime #" + i);
            c.setSolved(i % 2 == 0); //set every other one as solved
            mCrimes.add(c);
        }*/
    }

    public static CrimeLab get(Context c){
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for (Crime c : mCrimes){
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public boolean saveCrimes(){
        try{
            mSerializer.saveCrimes(mCrimes);
            //Toast.makeText(mAppContext, "Saved crime", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e){
            //Toast.makeText(mAppContext, "Error saving crimes: " + e, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error saving crimes: " + e);
            return false;
        }
    }

    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }
}
