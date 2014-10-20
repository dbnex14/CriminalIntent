package com.learning.dino.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CrimeFragment extends Fragment {

    public static final String TAG = "CrimeFragment";

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

    //constant for the request code to get photo file name
    private static final int REQUEST_PHOTO = 3;

    //constant to id the ImageFragment in the FragmentManager
    private static final String DIALOG_IMAGE = "image";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

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

    private void showPhoto(){
        //(Re)set image button's image based on our photo
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if (p != null){
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);

            //CHALLANGE, Ch.20-Image Orientation
            //db* if (p.getOrientation() == Configuration.ORIENTATION_PORTRAIT){
            if (p.getOrientation() == CrimeCameraActivity.ORIENTATION_PORTRAIT_INVERTED ||
                    p.getOrientation() == CrimeCameraActivity.ORIENTATION_PORTRAIT_NORMAL){ //db*
                b = PictureUtils.getPortraitBitmapDrawable(mPhotoView, b);
            }
        }
        mPhotoView.setImageDrawable(b);
    }

    @Override
    public void onStart(){
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop(){
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
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

        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                //startActivity(i);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        //If camera is not available, disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && android.hardware.Camera.getNumberOfCameras() > 0);
        if (!hasCamera){
            mPhotoButton.setEnabled(false);
        }

        mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
        registerForContextMenu(mPhotoView);
        mPhotoView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Photo p = mCrime.getPhoto();
                if (p == null){
                    return;
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path, p.getOrientation()).show(fm, DIALOG_IMAGE);
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

        if (requestCode == REQUEST_PHOTO){
            //CHALLANGE, Ch.20-Image Orientation, 2nd part to delete photo
            if (mCrime.getPhoto() != null){
                String path = getActivity().getFileStreamPath(mCrime.getPhoto().getFilename()).getAbsolutePath();
                File f = new File(path);
                f.delete();
                mCrime.setPhoto(null);
                Log.i(TAG, "File " + path + " deleted.");
            }

            //Create a new Photo object and attach it to the crime
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            int orientation = data.getIntExtra(CrimeCameraFragment.EXTRA_PHOTO_ORIENTATION, 0); //CHALLANGE Ch.20, Image Orientation
            if (filename != null){
                //Log.i(TAG, "filename: " + filename);
                Photo p = new Photo(filename, orientation);
                //Photo p = new Photo(filename);
                mCrime.setPhoto(p);
                showPhoto();
                //Log.i(TAG, "Crime: " + mCrime.getTitle() + " has a photo");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //challenge portion to delete crime from CrimeFragment
            case R.id.challenge_delete:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.challenge_delete, menu);
    }

    @Override
    public void onPause(){
        super.onPause();

        //if (mTitleField.getText().toString().equalsIgnoreCase("")){
        //    Toast.makeText(getActivity(), "No crime saved since to crime title provided.", Toast.LENGTH_SHORT);
        //} else {
            CrimeLab.get(getActivity()).saveCrimes();  //save crimes to crimes.json file
        //}
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.crime_photo_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_delete_photo:
                if (mCrime.getPhoto() != null){
                    String path = getActivity().getFileStreamPath(mCrime.getPhoto().getFilename()).getAbsolutePath();
                    File f = new File(path);
                    f.delete();
                    mCrime.setPhoto(null);
                    mPhotoView.setImageDrawable(null);
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
