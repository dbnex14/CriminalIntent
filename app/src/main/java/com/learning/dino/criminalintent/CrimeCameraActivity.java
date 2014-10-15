package com.learning.dino.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by dbulj on 14/10/2014.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new CrimeCameraFragment();
    }
}
