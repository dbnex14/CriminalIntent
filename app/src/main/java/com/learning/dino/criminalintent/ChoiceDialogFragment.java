package com.learning.dino.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by dbulj on 04/10/2014.
 */
public class ChoiceDialogFragment extends DialogFragment {
    public static final String EXTRA_CHOICE = "com.learning.dino.criminalintent.choice";
    public static final int CHOICE_DATE = 1;
    public static final int CHOICE_TIME = 2;

    private int mChoice = 0;

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
        i.putExtra(EXTRA_CHOICE, mChoice);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_editDateOrTime)
                .setPositiveButton(R.string.dialog_editDate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mChoice = CHOICE_DATE;
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.dialog_editTime, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        mChoice = CHOICE_TIME;
                        sendResult(Activity.RESULT_OK);
                    }

                });
        return builder.create();
    }
}
