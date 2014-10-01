package com.learning.dino.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by dbulj on 28/09/2014.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
