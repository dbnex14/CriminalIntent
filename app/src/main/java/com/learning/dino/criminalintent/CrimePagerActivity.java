package com.learning.dino.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by dbulj on 02/10/2014.
 * It does not uses any layout from xml but definces it in code since the view hierarchy is very
 * simple, it has just a single view.
 */
public class CrimePagerActivity extends FragmentActivity{

    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int pos) {
                Crime crime = mCrimes.get(pos);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            public void onPageScrollStateChanged(int state){}
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels){}

            public void onPageSelected(int pos){
                Crime crime = mCrimes.get(pos);
                if (crime.getTitle() != null){
                    setTitle(crime.getTitle());
                }
            }
        });

        //Set ViewPager to show selected item
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for (int i = 0; i < mCrimes.size(); i++){
            if (mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
