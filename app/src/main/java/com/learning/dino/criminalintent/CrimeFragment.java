package com.learning.dino.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CrimeFragment extends Fragment {

    //We define extra keys in activity that retrieves them and uses them. Also, extras are
    //interfaces to this activity (incoming and outgoing extras) so if this activity has to return
    //extra, that outgoing extra would also be defined here.
    public static final String EXTRA_CRIME_ID = "com.learning.dino.criminalintent.crime_id";

    //constant for my DatePickerFragment class
    private static final String DIALOG_DATE = "date";

    //constant for my TimePickerFragment class
    private static final String DIALOG_TIME = "time";

    //constant for the request code to make CrimeFragment the target fragment of DatePickerFragment
    private static final int REQUEST_DATE = 0;

    //constant for the request code to make CrimeFragment the target fragment of TimePickerFragment
    private static final int REQUEST_TIME = 1;

    //constant for the request code to make CrimeFragment the target fragment of ChoiceDialogFragment
    private static final int REQUEST_CHOICE = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void updateDate(){
        //mDateButton.setText(DateFormat.format("cccc MMM dd, yyyy", mCrime.getDate()));
        mDateButton.setText(mCrime.getDateString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //mCrime = new Crime();

        //UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        //this tells FragmentManager that CrimeFragment will be implementing options
        //menu callbacks on behalf of the activity
    }

    @TargetApi(11) //annotate method to wave of Lint since app icon is available from API 11 only
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if (NavUtils.getParentActivityName(getActivity()) != null){
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCrime.setTitle(c.toString());
            }

            @Override
            public void afterTextChanged(Editable c) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_date);
        updateDate();
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                FragmentManager fm = getActivity().getSupportFragmentManager();

                //Show DatePickerFragment
                ////DatePickerFragment dialog = new DatePickerFragment();
                //DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                //dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE); //Make CrimeFragment the target fragment of DatePickerFragment
                //dialog.show(fm, DIALOG_DATE);

                //Show TimePickerFragment
                ////TimePickerFragment dialog = new TimePickerFragment();
                //TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                //dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME); //Make CrimeFragment the target fragment of TimePickerFragment
                //dialog.show(fm, DIALOG_TIME);

                //Show ChoiceDialogFragment
                ChoiceDialogFragment dialog = new ChoiceDialogFragment();
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_CHOICE);
                dialog.show(fm, null);

            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }

        if (requestCode == REQUEST_TIME){
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_DATETIME);
            mCrime.setDate(date);
            updateDate();
        }

        if (requestCode == REQUEST_CHOICE){
            int choice = data.getIntExtra(ChoiceDialogFragment.EXTRA_CHOICE, 0);

            if (choice == 0){
                Log.d("ChoiceDialog", "required choice returned nothing");
                return;
            }

            if (choice == ChoiceDialogFragment.CHOICE_TIME){
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME); //Make CrimeFragment the target fragment of TimePickerFragment
                dialog.show(fm, DIALOG_TIME);
            }else if (choice == ChoiceDialogFragment.CHOICE_DATE){
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE); //Make CrimeFragment the target fragment of DatePickerFragment
                dialog.show(fm, DIALOG_DATE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
