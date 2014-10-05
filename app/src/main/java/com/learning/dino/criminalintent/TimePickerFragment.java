package com.learning.dino.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dbulj on 04/10/2014.
 */
public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_DATETIME = "com.learning.dino.criminalintent.datetime";

    private Date mDate;

    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATETIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        i.putExtra(EXTRA_DATETIME, mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        mDate = (Date)getArguments().getSerializable(EXTRA_DATETIME);

        //create calendar to get integers for year, month, day, hour, minute
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);

        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
            @Override
            public void onTimeChanged(TimePicker view, int hour, int minute){
                Calendar cal = Calendar.getInstance();
                cal.setTime(mDate);
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                mDate = cal.getTime();

                //write date back to fragment's arguments to preserve date in case of device rotation
                //if device is rotated while TimePickerFragment is on screen, the FragmentManager will
                //destroy current instance and create a new one.
                //When the new instance is created, FragmentManager will call onCreateDialog() on it
                //and restored saved date from instance's arguments.
                getArguments().putSerializable(EXTRA_DATETIME, mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                //.setPositiveButton(android.R.string.ok, null)
                //We implement DialogInterface.OnClickListener here to call our private method
                //sendResult() which creates intent and sends it to CrimeFragment.onActivityResult.
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        }
                )
                .create();
    }
}
