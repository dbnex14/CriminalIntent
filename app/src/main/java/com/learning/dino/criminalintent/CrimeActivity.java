package com.learning.dino.criminalintent;

import android.support.v4.app.Fragment;

import java.util.UUID;


public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        //return new CrimeFragment();
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

// This code was used when this class was extending FragmentActivity (android.support.v4.app.FragmentActivity).
// But we want to make this code generic so we can use it to create any fragment rather specific to just
// creating new CrimeFragment().  So, we created an abstract class SingleFragmentActivity with a CreateFragment()
// method that must be defined by its ancestors.
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
//
//        android.support.v4.app.FragmentManager fm;
//        fm = getSupportFragmentManager();
//        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
//
//        if (fragment == null){
//            fragment = new CrimeFragment();
//            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
//        }
//    }
}
