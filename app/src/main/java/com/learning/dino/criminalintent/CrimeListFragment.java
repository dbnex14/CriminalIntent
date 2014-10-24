package com.learning.dino.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private boolean mSubtitleVisible;
    private Button mAddCrimeButton;

    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks{
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mSubtitleVisible = false;
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        //we now use the custom adapter sublclass CrimeAdapter defined below
//        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(
//                getActivity()
//                , android.R.layout.simple_list_item_1
//                , mCrimes);

        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //<challange part - BEGIN>
        // View v = super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_item_list, parent, false);

        //Register list view of crimes for context menu.  android.R.id.list list resource Id is used
        //to retrieve the ListView managed by ListFragment within onCreateView().  Note that you can
        //not use ListFragment.getListView() within onCreateView() because getListView() will return
        //null until after onCreateView() returns.
        //On devices above Honeycomb, use contextual action instead.
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            //Use floating context menus on Froyo and GingerBread
            registerForContextMenu(listView);
        }else{
            //Use contextual action bar on Honeycomb and higher
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                }

                //ActionMode.Callback methods
                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    MenuInflater inflater = actionMode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
                            CrimeLab crimeLab = CrimeLab.get(getActivity());
                            for (int i = adapter.getCount() - 1; i >= 0; i--){
                                if (getListView().isItemChecked(i)){
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            actionMode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });
        }


        //Also set empty view, see fragment_item_list.xml for 'empty'
        listView.setEmptyView(v.findViewById(android.R.id.empty));

        mAddCrimeButton = (Button)v.findViewById(R.id.initialCrimeButton);
        mAddCrimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, 0);
            }
        });
        //</challenge part - END>


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        //we now use the custom adapter sublclass CrimeAdapter defined below
        //Crime c = (Crime)(getListAdapter().getItem(position));
        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position); //get the crime from adapter
        Log.d(TAG, c.getTitle() + " was clicked");

        //Start CrimeActivity to show crime clicked on in list.  Pass id of crime to show.
        //Intent i = new Intent(getActivity(), CrimeActivity.class);
        //Intent i = new Intent(getActivity(), CrimePagerActivity.class); //start an instance of CrimePagerActivity
        //i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        //startActivity(i);

        //call callbacks interface to delegate functionality back to activity so that
        //hosting activity does fragment bossing duties and layout dependent behavior
        mCallbacks.onCrimeSelected(c);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible && showSubtitle != null){
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                //Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                //i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                //startActivityForResult(i, 0);
                ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();

                //call callbacks interface to delegate functionality back to activity so that
                //hosting activity does fragment bossing duties and layout dependent behavior
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_item_show_subtitle:
                if (getActivity().getActionBar().getSubtitle() == null){
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
                } else{
                    getActivity().getActionBar().setSubtitle(null);
                    mSubtitleVisible = false;
                    item.setTitle(R.string.show_subtitle);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuinfo){
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
        Crime crime = adapter.getItem(position);

        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return  true;
        }

        return super.onContextItemSelected(item);
    }

    public void updateUI(){
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
