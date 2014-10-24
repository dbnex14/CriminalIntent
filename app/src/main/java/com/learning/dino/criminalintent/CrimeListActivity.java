package com.learning.dino.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by dbulj on 28/09/2014.
 */
public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }

    @Override
    public int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    //Callbacks method that must be implemented from CrimeListFragment.Callbacks
    public void onCrimeSelected(Crime crime){
        if (findViewById(R.id.detailFragmentContainer) == null){
            // Start an instance of CrimePagerActivity since this is small screen, so single fragment
            Intent i = new Intent(this, CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivity(i);
        } else {
            // Otherwise, we have master-detail view
            FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            if (oldDetail != null){
                ft.remove(oldDetail);
            }

            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();
        }
    }

    //Callback method that must be implemented from CrimeFragment.Callbacks
    public void onCrimeUpdated(Crime crime){
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment)fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }
}
