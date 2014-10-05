package com.learning.dino.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by dbulj on 02/10/2014.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.learning.dino.criminalintent.date";

    private Date mDate;

    //Private method that creates an intent, puts the data on it as an extra, and calls
    //CrimeFragment.onActivityResult.
    private void sendResult(int resultCode){
        if (getTargetFragment() == null){
            return;
        }

        //When dealing with 2 fragments hosted by the same activity, you can use Fragment.onActivytResult()
        //and call it directly on target fragment to pass data (intent) back.
        //When dealing with activities, you do not call Activity.onActivityResult directly yourself.  That
        //is the job of ActivityManager.
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);

        //Create a calendar to get year, month, and day integers rathere than using date timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                //Translate year, month, day into a Date object using a calendar
                mDate = new GregorianCalendar(year, month, day).getTime();

                //Update argument to preserve selected value in case of device rotation
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                //.setPositiveButton(android.R.string.ok, null)
                //We implement DialogInterface.OnClickListener here to call our private method
                //sendResult() which creates intent and sends it to CrimeFragment.onActivityResult.
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }
}
