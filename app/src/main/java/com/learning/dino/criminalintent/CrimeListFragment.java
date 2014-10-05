package com.learning.dino.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dbulj on 28/09/2014.
 */
public class CrimeListFragment extends ListFragment{

    private static final String TAG = "CrimeListFragment";
    private ArrayList<Crime> mCrimes;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        //we now use the custom adapter sublclass CrimeAdapter defined below
//        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(
//                getActivity()
//                , android.R.layout.simple_list_item_1
//                , mCrimes);

        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        //we now use the custom adapter sublclass CrimeAdapter defined below
        //Crime c = (Crime)(getListAdapter().getItem(position));

        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
        Log.d(TAG, c.getTitle() + " was clicked");

        //Start CrimeActivity to show crime clicked on in list.  Pass id of crime to show.
        //Intent i = new Intent(getActivity(), CrimeActivity.class);
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivity(i);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    //Custom adapter subclass
    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(ArrayList<Crime> crimes){
            super(getActivity(), 0, crimes); //pass 0 for layout ID since we are not using a pre-defined layout
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If we weren't given a view, inflate one
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }

            //Configure the view for this Crime
            Crime c = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            //dateTextView.setText(DateFormat.format("cccc, MMM dd, yyyy", c.getDate()));
            dateTextView.setText(c.getDateString());

            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }
    }
}
